package net.minecraft.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;

public class CommandHelp extends CommandBase
{
    public String getCommandName()
    {
        return "help";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return "commands.help.usage";
    }

    public List getCommandAliases()
    {
        return Arrays.asList(new String[] {"?"});
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        List list = this.getSortedPossibleCommands(par1ICommandSender);
        byte b0 = 7;
        int i = (list.size() - 1) / b0;
        boolean flag = false;
        ICommand icommand;
        int j;

        try
        {
            j = par2ArrayOfStr.length == 0 ? 0 : parseIntBounded(par1ICommandSender, par2ArrayOfStr[0], 1, i + 1) - 1;
        }
        catch (NumberInvalidException numberinvalidexception)
        {
            Map map = this.getCommands();
            icommand = (ICommand)map.get(par2ArrayOfStr[0]);

            if (icommand != null)
            {
                throw new WrongUsageException(icommand.getCommandUsage(par1ICommandSender), new Object[0]);
            }

            throw new CommandNotFoundException();
        }

        int k = Math.min((j + 1) * b0, list.size());
        par1ICommandSender.sendChatToPlayer(ChatMessageComponent.createFromTranslationWithSubstitutions("commands.help.header", new Object[] {Integer.valueOf(j + 1), Integer.valueOf(i + 1)}).setColor(EnumChatFormatting.DARK_GREEN));

        for (int l = j * b0; l < k; ++l)
        {
            icommand = (ICommand)list.get(l);
            par1ICommandSender.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey(icommand.getCommandUsage(par1ICommandSender)));
        }

        if (j == 0 && par1ICommandSender instanceof EntityPlayer)
        {
            par1ICommandSender.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("commands.help.footer").setColor(EnumChatFormatting.GREEN));
        }
    }

    /**
     * Returns a sorted list of all possible commands for the given ICommandSender.
     */
    protected List getSortedPossibleCommands(ICommandSender par1ICommandSender)
    {
        List list = MinecraftServer.getServer().getCommandManager().getPossibleCommands(par1ICommandSender);
        Collections.sort(list);
        return list;
    }

    protected Map getCommands()
    {
        return MinecraftServer.getServer().getCommandManager().getCommands();
    }
}
