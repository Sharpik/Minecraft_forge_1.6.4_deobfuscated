package net.minecraft.command;

import java.util.List;

public interface ICommand extends Comparable
{
    String getCommandName();

    String getCommandUsage(ICommandSender icommandsender);

    List getCommandAliases();

    void processCommand(ICommandSender icommandsender, String[] astring);

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    boolean canCommandSenderUseCommand(ICommandSender icommandsender);

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    List addTabCompletionOptions(ICommandSender icommandsender, String[] astring);

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    boolean isUsernameIndex(String[] astring, int i);
}
