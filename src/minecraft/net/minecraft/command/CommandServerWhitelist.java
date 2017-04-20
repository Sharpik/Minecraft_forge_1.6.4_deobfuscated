package net.minecraft.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;

public class CommandServerWhitelist extends CommandBase
{
    public String getCommandName()
    {
        return "whitelist";
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
        return "commands.whitelist.usage";
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        if (par2ArrayOfStr.length >= 1)
        {
            if (par2ArrayOfStr[0].equals("on"))
            {
                MinecraftServer.getServer().getConfigurationManager().setWhiteListEnabled(true);
                notifyAdmins(par1ICommandSender, "commands.whitelist.enabled", new Object[0]);
                return;
            }

            if (par2ArrayOfStr[0].equals("off"))
            {
                MinecraftServer.getServer().getConfigurationManager().setWhiteListEnabled(false);
                notifyAdmins(par1ICommandSender, "commands.whitelist.disabled", new Object[0]);
                return;
            }

            if (par2ArrayOfStr[0].equals("list"))
            {
                par1ICommandSender.sendChatToPlayer(ChatMessageComponent.createFromTranslationWithSubstitutions("commands.whitelist.list", new Object[] {Integer.valueOf(MinecraftServer.getServer().getConfigurationManager().getWhiteListedPlayers().size()), Integer.valueOf(MinecraftServer.getServer().getConfigurationManager().getAvailablePlayerDat().length)}));
                Set set = MinecraftServer.getServer().getConfigurationManager().getWhiteListedPlayers();
                par1ICommandSender.sendChatToPlayer(ChatMessageComponent.createFromText(joinNiceString(set.toArray(new String[set.size()]))));
                return;
            }

            if (par2ArrayOfStr[0].equals("add"))
            {
                if (par2ArrayOfStr.length < 2)
                {
                    throw new WrongUsageException("commands.whitelist.add.usage", new Object[0]);
                }

                MinecraftServer.getServer().getConfigurationManager().addToWhiteList(par2ArrayOfStr[1]);
                notifyAdmins(par1ICommandSender, "commands.whitelist.add.success", new Object[] {par2ArrayOfStr[1]});
                return;
            }

            if (par2ArrayOfStr[0].equals("remove"))
            {
                if (par2ArrayOfStr.length < 2)
                {
                    throw new WrongUsageException("commands.whitelist.remove.usage", new Object[0]);
                }

                MinecraftServer.getServer().getConfigurationManager().removeFromWhitelist(par2ArrayOfStr[1]);
                notifyAdmins(par1ICommandSender, "commands.whitelist.remove.success", new Object[] {par2ArrayOfStr[1]});
                return;
            }

            if (par2ArrayOfStr[0].equals("reload"))
            {
                MinecraftServer.getServer().getConfigurationManager().loadWhiteList();
                notifyAdmins(par1ICommandSender, "commands.whitelist.reloaded", new Object[0]);
                return;
            }
        }

        throw new WrongUsageException("commands.whitelist.usage", new Object[0]);
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        if (par2ArrayOfStr.length == 1)
        {
            return getListOfStringsMatchingLastWord(par2ArrayOfStr, new String[] {"on", "off", "list", "add", "remove", "reload"});
        }
        else
        {
            if (par2ArrayOfStr.length == 2)
            {
                if (par2ArrayOfStr[0].equals("add"))
                {
                    String[] astring1 = MinecraftServer.getServer().getConfigurationManager().getAvailablePlayerDat();
                    ArrayList arraylist = new ArrayList();
                    String s = par2ArrayOfStr[par2ArrayOfStr.length - 1];
                    String[] astring2 = astring1;
                    int i = astring1.length;

                    for (int j = 0; j < i; ++j)
                    {
                        String s1 = astring2[j];

                        if (doesStringStartWith(s, s1) && !MinecraftServer.getServer().getConfigurationManager().getWhiteListedPlayers().contains(s1))
                        {
                            arraylist.add(s1);
                        }
                    }

                    return arraylist;
                }

                if (par2ArrayOfStr[0].equals("remove"))
                {
                    return getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr, MinecraftServer.getServer().getConfigurationManager().getWhiteListedPlayers());
                }
            }

            return null;
        }
    }
}
