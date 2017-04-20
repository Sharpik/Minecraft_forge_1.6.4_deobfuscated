package net.minecraft.command;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.BanEntry;

public class CommandServerBanIp extends CommandBase
{
    public static final Pattern IPv4Pattern = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    public String getCommandName()
    {
        return "ban-ip";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 3;
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return MinecraftServer.getServer().getConfigurationManager().getBannedIPs().isListActive() && super.canCommandSenderUseCommand(par1ICommandSender);
    }

    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return "commands.banip.usage";
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        if (par2ArrayOfStr.length >= 1 && par2ArrayOfStr[0].length() > 1)
        {
            Matcher matcher = IPv4Pattern.matcher(par2ArrayOfStr[0]);
            String s = null;

            if (par2ArrayOfStr.length >= 2)
            {
                s = func_82360_a(par1ICommandSender, par2ArrayOfStr, 1);
            }

            if (matcher.matches())
            {
                this.banIP(par1ICommandSender, par2ArrayOfStr[0], s);
            }
            else
            {
                EntityPlayerMP entityplayermp = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(par2ArrayOfStr[0]);

                if (entityplayermp == null)
                {
                    throw new PlayerNotFoundException("commands.banip.invalid", new Object[0]);
                }

                this.banIP(par1ICommandSender, entityplayermp.getPlayerIP(), s);
            }
        }
        else
        {
            throw new WrongUsageException("commands.banip.usage", new Object[0]);
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        return par2ArrayOfStr.length == 1 ? getListOfStringsMatchingLastWord(par2ArrayOfStr, MinecraftServer.getServer().getAllUsernames()) : null;
    }

    /**
     * Actually does the banning work.
     */
    protected void banIP(ICommandSender par1ICommandSender, String par2Str, String par3Str)
    {
        BanEntry banentry = new BanEntry(par2Str);
        banentry.setBannedBy(par1ICommandSender.getCommandSenderName());

        if (par3Str != null)
        {
            banentry.setBanReason(par3Str);
        }

        MinecraftServer.getServer().getConfigurationManager().getBannedIPs().put(banentry);
        List list = MinecraftServer.getServer().getConfigurationManager().getPlayerList(par2Str);
        String[] astring = new String[list.size()];
        int i = 0;
        EntityPlayerMP entityplayermp;

        for (Iterator iterator = list.iterator(); iterator.hasNext(); astring[i++] = entityplayermp.getEntityName())
        {
            entityplayermp = (EntityPlayerMP)iterator.next();
            entityplayermp.playerNetServerHandler.kickPlayerFromServer("You have been IP banned.");
        }

        if (list.isEmpty())
        {
            notifyAdmins(par1ICommandSender, "commands.banip.success", new Object[] {par2Str});
        }
        else
        {
            notifyAdmins(par1ICommandSender, "commands.banip.success.players", new Object[] {par2Str, joinNiceString(astring)});
        }
    }
}
