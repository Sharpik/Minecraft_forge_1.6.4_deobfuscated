package net.minecraft.command;

import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.BanEntry;

public class CommandServerBan extends CommandBase
{
    public String getCommandName()
    {
        return "ban";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 3;
    }

    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return "commands.ban.usage";
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().isListActive() && super.canCommandSenderUseCommand(par1ICommandSender);
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        if (par2ArrayOfStr.length >= 1 && par2ArrayOfStr[0].length() > 0)
        {
            EntityPlayerMP entityplayermp = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(par2ArrayOfStr[0]);
            BanEntry banentry = new BanEntry(par2ArrayOfStr[0]);
            banentry.setBannedBy(par1ICommandSender.getCommandSenderName());

            if (par2ArrayOfStr.length >= 2)
            {
                banentry.setBanReason(func_82360_a(par1ICommandSender, par2ArrayOfStr, 1));
            }

            MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().put(banentry);

            if (entityplayermp != null)
            {
                entityplayermp.playerNetServerHandler.kickPlayerFromServer("You are banned from this server.");
            }

            notifyAdmins(par1ICommandSender, "commands.ban.success", new Object[] {par2ArrayOfStr[0]});
        }
        else
        {
            throw new WrongUsageException("commands.ban.usage", new Object[0]);
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        return par2ArrayOfStr.length >= 1 ? getListOfStringsMatchingLastWord(par2ArrayOfStr, MinecraftServer.getServer().getAllUsernames()) : null;
    }
}
