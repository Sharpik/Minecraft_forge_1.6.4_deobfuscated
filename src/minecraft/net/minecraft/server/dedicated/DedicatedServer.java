package net.minecraft.server.dedicated;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.ServerCommand;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.logging.ILogAgent;
import net.minecraft.logging.LogAgent;
import net.minecraft.network.NetworkListenThread;
import net.minecraft.network.rcon.IServer;
import net.minecraft.network.rcon.RConThreadMain;
import net.minecraft.network.rcon.RConThreadQuery;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.gui.MinecraftServerGui;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.CryptManager;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumGameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;

public class DedicatedServer extends MinecraftServer implements IServer
{
    private final List pendingCommandList = Collections.synchronizedList(new ArrayList());
    private final ILogAgent field_98131_l;
    private RConThreadQuery theRConThreadQuery;
    private RConThreadMain theRConThreadMain;
    private PropertyManager settings;
    private boolean canSpawnStructures;
    private EnumGameType gameType;
    private NetworkListenThread networkThread;
    private boolean guiIsEnabled;

    public DedicatedServer(File par1File)
    {
        super(par1File);
        this.field_98131_l = new LogAgent("Minecraft-Server", (String)null, (new File(par1File, "server.log")).getAbsolutePath());
        new DedicatedServerSleepThread(this);
    }

    /**
     * Initialises the server and starts it.
     */
    protected boolean startServer() throws IOException
    {
        DedicatedServerCommandThread dedicatedservercommandthread = new DedicatedServerCommandThread(this);
        dedicatedservercommandthread.setDaemon(true);
        dedicatedservercommandthread.start();
        this.getLogAgent().logInfo("Starting minecraft server version 1.6.4");

        if (Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L)
        {
            this.getLogAgent().logWarning("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
        }

        FMLCommonHandler.instance().onServerStart(this);

        this.getLogAgent().logInfo("Loading properties");
        this.settings = new PropertyManager(new File("server.properties"), this.getLogAgent());

        if (this.isSinglePlayer())
        {
            this.setHostname("127.0.0.1");
        }
        else
        {
            this.setOnlineMode(this.settings.getBooleanProperty("online-mode", true));
            this.setHostname(this.settings.getProperty("server-ip", ""));
        }

        this.setCanSpawnAnimals(this.settings.getBooleanProperty("spawn-animals", true));
        this.setCanSpawnNPCs(this.settings.getBooleanProperty("spawn-npcs", true));
        this.setAllowPvp(this.settings.getBooleanProperty("pvp", true));
        this.setAllowFlight(this.settings.getBooleanProperty("allow-flight", false));
        this.setTexturePack(this.settings.getProperty("texture-pack", ""));
        this.setMOTD(this.settings.getProperty("motd", "A Minecraft Server"));
        this.setForceGamemode(this.settings.getBooleanProperty("force-gamemode", false));
        this.func_143006_e(this.settings.getIntProperty("player-idle-timeout", 0));

        if (this.settings.getIntProperty("difficulty", 1) < 0)
        {
            this.settings.setProperty("difficulty", Integer.valueOf(0));
        }
        else if (this.settings.getIntProperty("difficulty", 1) > 3)
        {
            this.settings.setProperty("difficulty", Integer.valueOf(3));
        }

        this.canSpawnStructures = this.settings.getBooleanProperty("generate-structures", true);
        int i = this.settings.getIntProperty("gamemode", EnumGameType.SURVIVAL.getID());
        this.gameType = WorldSettings.getGameTypeById(i);
        this.getLogAgent().logInfo("Default game type: " + this.gameType);
        InetAddress inetaddress = null;

        if (this.getServerHostname().length() > 0)
        {
            inetaddress = InetAddress.getByName(this.getServerHostname());
        }

        if (this.getServerPort() < 0)
        {
            this.setServerPort(this.settings.getIntProperty("server-port", 25565));
        }

        this.getLogAgent().logInfo("Generating keypair");
        this.setKeyPair(CryptManager.createNewKeyPair());
        this.getLogAgent().logInfo("Starting Minecraft server on " + (this.getServerHostname().length() == 0 ? "*" : this.getServerHostname()) + ":" + this.getServerPort());

        try
        {
            this.networkThread = new DedicatedServerListenThread(this, inetaddress, this.getServerPort());
        }
        catch (IOException ioexception)
        {
            this.getLogAgent().logWarning("**** FAILED TO BIND TO PORT!");
            this.getLogAgent().logWarningFormatted("The exception was: {0}", new Object[] {ioexception.toString()});
            this.getLogAgent().logWarning("Perhaps a server is already running on that port?");
            return false;
        }

        if (!this.isServerInOnlineMode())
        {
            this.getLogAgent().logWarning("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
            this.getLogAgent().logWarning("The server will make no attempt to authenticate usernames. Beware.");
            this.getLogAgent().logWarning("While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
            this.getLogAgent().logWarning("To change this, set \"online-mode\" to \"true\" in the server.properties file.");
        }

        FMLCommonHandler.instance().onServerStarted();

        this.setConfigurationManager(new DedicatedPlayerList(this));
        long j = System.nanoTime();

        if (this.getFolderName() == null)
        {
            this.setFolderName(this.settings.getProperty("level-name", "world"));
        }

        String s = this.settings.getProperty("level-seed", "");
        String s1 = this.settings.getProperty("level-type", "DEFAULT");
        String s2 = this.settings.getProperty("generator-settings", "");
        long k = (new Random()).nextLong();

        if (s.length() > 0)
        {
            try
            {
                long l = Long.parseLong(s);

                if (l != 0L)
                {
                    k = l;
                }
            }
            catch (NumberFormatException numberformatexception)
            {
                k = (long)s.hashCode();
            }
        }

        WorldType worldtype = WorldType.parseWorldType(s1);

        if (worldtype == null)
        {
            worldtype = WorldType.DEFAULT;
        }

        this.setBuildLimit(this.settings.getIntProperty("max-build-height", 256));
        this.setBuildLimit((this.getBuildLimit() + 8) / 16 * 16);
        this.setBuildLimit(MathHelper.clamp_int(this.getBuildLimit(), 64, 256));
        this.settings.setProperty("max-build-height", Integer.valueOf(this.getBuildLimit()));
        if (!FMLCommonHandler.instance().handleServerAboutToStart(this)) { return false; }
        this.getLogAgent().logInfo("Preparing level \"" + this.getFolderName() + "\"");
        this.loadAllWorlds(this.getFolderName(), this.getFolderName(), k, worldtype, s2);
        long i1 = System.nanoTime() - j;
        String s3 = String.format("%.3fs", new Object[] {Double.valueOf((double)i1 / 1.0E9D)});
        this.getLogAgent().logInfo("Done (" + s3 + ")! For help, type \"help\" or \"?\"");

        if (this.settings.getBooleanProperty("enable-query", false))
        {
            this.getLogAgent().logInfo("Starting GS4 status listener");
            this.theRConThreadQuery = new RConThreadQuery(this);
            this.theRConThreadQuery.startThread();
        }

        if (this.settings.getBooleanProperty("enable-rcon", false))
        {
            this.getLogAgent().logInfo("Starting remote control listener");
            this.theRConThreadMain = new RConThreadMain(this);
            this.theRConThreadMain.startThread();
        }

        return FMLCommonHandler.instance().handleServerStarting(this);
    }

    public boolean canStructuresSpawn()
    {
        return this.canSpawnStructures;
    }

    public EnumGameType getGameType()
    {
        return this.gameType;
    }

    /**
     * Defaults to "1" (Easy) for the dedicated server, defaults to "2" (Normal) on the client.
     */
    public int getDifficulty()
    {
        return this.settings.getIntProperty("difficulty", 1);
    }

    /**
     * Defaults to false.
     */
    public boolean isHardcore()
    {
        return this.settings.getBooleanProperty("hardcore", false);
    }

    /**
     * Called on exit from the main run() loop.
     */
    protected void finalTick(CrashReport par1CrashReport)
    {
        while (this.isServerRunning())
        {
            this.executePendingCommands();

            try
            {
                Thread.sleep(10L);
            }
            catch (InterruptedException interruptedexception)
            {
                interruptedexception.printStackTrace();
            }
        }
    }

    /**
     * Adds the server info, including from theWorldServer, to the crash report.
     */
    public CrashReport addServerInfoToCrashReport(CrashReport par1CrashReport)
    {
        par1CrashReport = super.addServerInfoToCrashReport(par1CrashReport);
        par1CrashReport.getCategory().addCrashSectionCallable("Is Modded", new CallableType(this));
        par1CrashReport.getCategory().addCrashSectionCallable("Type", new CallableServerType(this));
        return par1CrashReport;
    }

    /**
     * Directly calls System.exit(0), instantly killing the program.
     */
    protected void systemExitNow()
    {
        System.exit(0);
    }

    public void updateTimeLightAndEntities()
    {
        super.updateTimeLightAndEntities();
        this.executePendingCommands();
    }

    public boolean getAllowNether()
    {
        return this.settings.getBooleanProperty("allow-nether", true);
    }

    public boolean allowSpawnMonsters()
    {
        return this.settings.getBooleanProperty("spawn-monsters", true);
    }

    public void addServerStatsToSnooper(PlayerUsageSnooper par1PlayerUsageSnooper)
    {
        par1PlayerUsageSnooper.addData("whitelist_enabled", Boolean.valueOf(this.getDedicatedPlayerList().isWhiteListEnabled()));
        par1PlayerUsageSnooper.addData("whitelist_count", Integer.valueOf(this.getDedicatedPlayerList().getWhiteListedPlayers().size()));
        super.addServerStatsToSnooper(par1PlayerUsageSnooper);
    }

    /**
     * Returns whether snooping is enabled or not.
     */
    public boolean isSnooperEnabled()
    {
        return this.settings.getBooleanProperty("snooper-enabled", true);
    }

    public void addPendingCommand(String par1Str, ICommandSender par2ICommandSender)
    {
        this.pendingCommandList.add(new ServerCommand(par1Str, par2ICommandSender));
    }

    public void executePendingCommands()
    {
        while (!this.pendingCommandList.isEmpty())
        {
            ServerCommand servercommand = (ServerCommand)this.pendingCommandList.remove(0);
            this.getCommandManager().executeCommand(servercommand.sender, servercommand.command);
        }
    }

    public boolean isDedicatedServer()
    {
        return true;
    }

    public DedicatedPlayerList getDedicatedPlayerList()
    {
        return (DedicatedPlayerList)super.getConfigurationManager();
    }

    public NetworkListenThread getNetworkThread()
    {
        return this.networkThread;
    }

    /**
     * Gets an integer property. If it does not exist, set it to the specified value.
     */
    public int getIntProperty(String par1Str, int par2)
    {
        return this.settings.getIntProperty(par1Str, par2);
    }

    /**
     * Gets a string property. If it does not exist, set it to the specified value.
     */
    public String getStringProperty(String par1Str, String par2Str)
    {
        return this.settings.getProperty(par1Str, par2Str);
    }

    /**
     * Gets a boolean property. If it does not exist, set it to the specified value.
     */
    public boolean getBooleanProperty(String par1Str, boolean par2)
    {
        return this.settings.getBooleanProperty(par1Str, par2);
    }

    /**
     * Saves an Object with the given property name.
     */
    public void setProperty(String par1Str, Object par2Obj)
    {
        this.settings.setProperty(par1Str, par2Obj);
    }

    /**
     * Saves all of the server properties to the properties file.
     */
    public void saveProperties()
    {
        this.settings.saveProperties();
    }

    /**
     * Returns the filename where server properties are stored
     */
    public String getSettingsFilename()
    {
        File file1 = this.settings.getPropertiesFile();
        return file1 != null ? file1.getAbsolutePath() : "No settings file";
    }

    public boolean getGuiEnabled()
    {
        return this.guiIsEnabled;
    }

    /**
     * On dedicated does nothing. On integrated, sets commandsAllowedForAll, gameType and allows external connections.
     */
    public String shareToLAN(EnumGameType par1EnumGameType, boolean par2)
    {
        return "";
    }

    /**
     * Return whether command blocks are enabled.
     */
    public boolean isCommandBlockEnabled()
    {
        return this.settings.getBooleanProperty("enable-command-block", false);
    }

    /**
     * Return the spawn protection area's size.
     */
    public int getSpawnProtectionSize()
    {
        return this.settings.getIntProperty("spawn-protection", super.getSpawnProtectionSize());
    }

    /**
     * Returns true if a player does not have permission to edit the block at the given coordinates.
     */
    public boolean isBlockProtected(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer)
    {
        if (par1World.provider.dimensionId != 0)
        {
            return false;
        }
        else if (this.getDedicatedPlayerList().getOps().isEmpty())
        {
            return false;
        }
        else if (this.getDedicatedPlayerList().isPlayerOpped(par5EntityPlayer.getCommandSenderName()))
        {
            return false;
        }
        else if (this.getSpawnProtectionSize() <= 0)
        {
            return false;
        }
        else
        {
            ChunkCoordinates chunkcoordinates = par1World.getSpawnPoint();
            int l = MathHelper.abs_int(par2 - chunkcoordinates.posX);
            int i1 = MathHelper.abs_int(par4 - chunkcoordinates.posZ);
            int j1 = Math.max(l, i1);
            return j1 <= this.getSpawnProtectionSize();
        }
    }

    public ILogAgent getLogAgent()
    {
        return this.field_98131_l;
    }

    public int func_110455_j()
    {
        return this.settings.getIntProperty("op-permission-level", 4);
    }

    public void func_143006_e(int par1)
    {
        super.func_143006_e(par1);
        this.settings.setProperty("player-idle-timeout", Integer.valueOf(par1));
        this.saveProperties();
    }

    public ServerConfigurationManager getConfigurationManager()
    {
        return this.getDedicatedPlayerList();
    }

    @SideOnly(Side.SERVER)
    public void func_120011_ar()
    {
        MinecraftServerGui.func_120016_a(this);
        this.guiIsEnabled = true;
    }
}
