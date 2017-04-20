package net.minecraft.command;

import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.GameRules;

public class CommandGameRule extends CommandBase
{
    public String getCommandName()
    {
        return "gamerule";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return "commands.gamerule.usage";
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        String s;

        if (par2ArrayOfStr.length == 2)
        {
            s = par2ArrayOfStr[0];
            String s1 = par2ArrayOfStr[1];
            GameRules gamerules = this.getGameRules();

            if (gamerules.hasRule(s))
            {
                gamerules.setOrCreateGameRule(s, s1);
                notifyAdmins(par1ICommandSender, "commands.gamerule.success", new Object[0]);
            }
            else
            {
                notifyAdmins(par1ICommandSender, "commands.gamerule.norule", new Object[] {s});
            }
        }
        else if (par2ArrayOfStr.length == 1)
        {
            s = par2ArrayOfStr[0];
            GameRules gamerules1 = this.getGameRules();

            if (gamerules1.hasRule(s))
            {
                String s2 = gamerules1.getGameRuleStringValue(s);
                par1ICommandSender.sendChatToPlayer(ChatMessageComponent.createFromText(s).addText(" = ").addText(s2));
            }
            else
            {
                notifyAdmins(par1ICommandSender, "commands.gamerule.norule", new Object[] {s});
            }
        }
        else if (par2ArrayOfStr.length == 0)
        {
            GameRules gamerules2 = this.getGameRules();
            par1ICommandSender.sendChatToPlayer(ChatMessageComponent.createFromText(joinNiceString(gamerules2.getRules())));
        }
        else
        {
            throw new WrongUsageException("commands.gamerule.usage", new Object[0]);
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        return par2ArrayOfStr.length == 1 ? getListOfStringsMatchingLastWord(par2ArrayOfStr, this.getGameRules().getRules()) : (par2ArrayOfStr.length == 2 ? getListOfStringsMatchingLastWord(par2ArrayOfStr, new String[] {"true", "false"}): null);
    }

    /**
     * Return the game rule set this command should be able to manipulate.
     */
    private GameRules getGameRules()
    {
        return MinecraftServer.getServer().worldServerForDimension(0).getGameRules();
    }
}
