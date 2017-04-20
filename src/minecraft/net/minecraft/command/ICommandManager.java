package net.minecraft.command;

import java.util.List;
import java.util.Map;

public interface ICommandManager
{
    int executeCommand(ICommandSender icommandsender, String s);

    /**
     * Performs a "begins with" string match on each token in par2. Only returns commands that par1 can use.
     */
    List getPossibleCommands(ICommandSender icommandsender, String s);

    /**
     * returns all commands that the commandSender can use
     */
    List getPossibleCommands(ICommandSender icommandsender);

    /**
     * returns a map of string to commads. All commands are returned, not just ones which someone has permission to use.
     */
    Map getCommands();
}
