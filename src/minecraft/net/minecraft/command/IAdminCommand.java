package net.minecraft.command;

public interface IAdminCommand
{
    /**
     * Sends a message to the admins of the server from a given CommandSender with the given resource string and given
     * extra srings. If the int par2 is even or zero, the original sender is also notified.
     */
    void notifyAdmins(ICommandSender icommandsender, int i, String s, Object ... var4);
}
