package net.minecraft.command;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.EnumGameType;

public class CommandServerPublishLocal extends CommandBase
{
    public String getCommandName()
    {
        return "publish";
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
        return "commands.publish.usage";
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        String s = MinecraftServer.getServer().shareToLAN(EnumGameType.SURVIVAL, false);

        if (s != null)
        {
            notifyAdmins(par1ICommandSender, "commands.publish.started", new Object[] {s});
        }
        else
        {
            notifyAdmins(par1ICommandSender, "commands.publish.failed", new Object[0]);
        }
    }
}
