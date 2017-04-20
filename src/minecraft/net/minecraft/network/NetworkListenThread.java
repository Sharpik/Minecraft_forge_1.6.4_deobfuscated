package net.minecraft.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ReportedException;

public abstract class NetworkListenThread
{
    /** Reference to the MinecraftServer object. */
    private final MinecraftServer mcServer;
    private final List connections = Collections.synchronizedList(new ArrayList());

    /** Whether the network listener object is listening. */
    public volatile boolean isListening;

    public NetworkListenThread(MinecraftServer par1MinecraftServer) throws IOException
    {
        this.mcServer = par1MinecraftServer;
        this.isListening = true;
    }

    /**
     * adds this connection to the list of currently connected players
     */
    public void addPlayer(NetServerHandler par1NetServerHandler)
    {
        this.connections.add(par1NetServerHandler);
    }

    public void stopListening()
    {
        this.isListening = false;
    }

    /**
     * processes packets and pending connections
     */
    public void networkTick()
    {
        for (int i = 0; i < this.connections.size(); ++i)
        {
            NetServerHandler netserverhandler = (NetServerHandler)this.connections.get(i);

            try
            {
                netserverhandler.networkTick();
            }
            catch (Exception exception)
            {
                if (netserverhandler.netManager instanceof MemoryConnection)
                {
                    CrashReport crashreport = CrashReport.makeCrashReport(exception, "Ticking memory connection");
                    CrashReportCategory crashreportcategory = crashreport.makeCategory("Ticking connection");
                    crashreportcategory.addCrashSectionCallable("Connection", new CallableConnectionName(this, netserverhandler));
                    throw new ReportedException(crashreport);
                }

                FMLLog.log(Level.SEVERE, exception, "A critical server error occured handling a packet, kicking %s", netserverhandler.getPlayer().entityId);
                this.mcServer.getLogAgent().logWarningException("Failed to handle packet for " + netserverhandler.playerEntity.getEntityName() + "/" + netserverhandler.playerEntity.getPlayerIP() + ": " + exception, exception);
                netserverhandler.kickPlayerFromServer("Internal server error");
            }

            if (netserverhandler.connectionClosed)
            {
                this.connections.remove(i--);
            }

            netserverhandler.netManager.wakeThreads();
        }
    }

    public MinecraftServer getServer()
    {
        return this.mcServer;
    }
}
