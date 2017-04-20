package net.minecraft.command;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldServer;

public class CommandServerSaveAll extends CommandBase
{
    public String getCommandName()
    {
        return "save-all";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 4;
    }

    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return "commands.save.usage";
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        MinecraftServer minecraftserver = MinecraftServer.getServer();
        par1ICommandSender.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("commands.save.start"));

        if (minecraftserver.getConfigurationManager() != null)
        {
            minecraftserver.getConfigurationManager().saveAllPlayerData();
        }

        try
        {
            int i;
            WorldServer worldserver;
            boolean flag;

            for (i = 0; i < minecraftserver.worldServers.length; ++i)
            {
                if (minecraftserver.worldServers[i] != null)
                {
                    worldserver = minecraftserver.worldServers[i];
                    flag = worldserver.canNotSave;
                    worldserver.canNotSave = false;
                    worldserver.saveAllChunks(true, (IProgressUpdate)null);
                    worldserver.canNotSave = flag;
                }
            }

            if (par2ArrayOfStr.length > 0 && "flush".equals(par2ArrayOfStr[0]))
            {
                par1ICommandSender.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("commands.save.flushStart"));

                for (i = 0; i < minecraftserver.worldServers.length; ++i)
                {
                    if (minecraftserver.worldServers[i] != null)
                    {
                        worldserver = minecraftserver.worldServers[i];
                        flag = worldserver.canNotSave;
                        worldserver.canNotSave = false;
                        worldserver.saveChunkData();
                        worldserver.canNotSave = flag;
                    }
                }

                par1ICommandSender.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("commands.save.flushEnd"));
            }
        }
        catch (MinecraftException minecraftexception)
        {
            notifyAdmins(par1ICommandSender, "commands.save.failed", new Object[] {minecraftexception.getMessage()});
            return;
        }

        notifyAdmins(par1ICommandSender, "commands.save.success", new Object[0]);
    }
}
