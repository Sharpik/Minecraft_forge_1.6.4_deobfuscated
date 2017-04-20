package net.minecraft.command;

import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandClearInventory extends CommandBase
{
    public String getCommandName()
    {
        return "clear";
    }

    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return "commands.clear.usage";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        EntityPlayerMP entityplayermp = par2ArrayOfStr.length == 0 ? getCommandSenderAsPlayer(par1ICommandSender) : getPlayer(par1ICommandSender, par2ArrayOfStr[0]);
        int i = par2ArrayOfStr.length >= 2 ? parseIntWithMin(par1ICommandSender, par2ArrayOfStr[1], 1) : -1;
        int j = par2ArrayOfStr.length >= 3 ? parseIntWithMin(par1ICommandSender, par2ArrayOfStr[2], 0) : -1;
        int k = entityplayermp.inventory.clearInventory(i, j);
        entityplayermp.inventoryContainer.detectAndSendChanges();

        if (!entityplayermp.capabilities.isCreativeMode)
        {
            entityplayermp.updateHeldItem();
        }

        if (k == 0)
        {
            throw new CommandException("commands.clear.failure", new Object[] {entityplayermp.getEntityName()});
        }
        else
        {
            notifyAdmins(par1ICommandSender, "commands.clear.success", new Object[] {entityplayermp.getEntityName(), Integer.valueOf(k)});
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        return par2ArrayOfStr.length == 1 ? getListOfStringsMatchingLastWord(par2ArrayOfStr, this.getAllOnlineUsernames()) : null;
    }

    /**
     * Return all usernames currently connected to the server.
     */
    protected String[] getAllOnlineUsernames()
    {
        return MinecraftServer.getServer().getAllUsernames();
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] par1ArrayOfStr, int par2)
    {
        return par2 == 0;
    }
}
