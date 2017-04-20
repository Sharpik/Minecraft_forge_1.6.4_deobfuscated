package net.minecraft.command;

import java.util.List;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

public class CommandGive extends CommandBase
{
    public String getCommandName()
    {
        return "give";
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
        return "commands.give.usage";
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        if (par2ArrayOfStr.length >= 2)
        {
            EntityPlayerMP entityplayermp = getPlayer(par1ICommandSender, par2ArrayOfStr[0]);
            int i = parseIntWithMin(par1ICommandSender, par2ArrayOfStr[1], 1);
            int j = 1;
            int k = 0;

            if (Item.itemsList[i] == null)
            {
                throw new NumberInvalidException("commands.give.notFound", new Object[] {Integer.valueOf(i)});
            }
            else
            {
                if (par2ArrayOfStr.length >= 3)
                {
                    j = parseIntBounded(par1ICommandSender, par2ArrayOfStr[2], 1, 64);
                }

                if (par2ArrayOfStr.length >= 4)
                {
                    k = parseInt(par1ICommandSender, par2ArrayOfStr[3]);
                }

                ItemStack itemstack = new ItemStack(i, j, k);
                EntityItem entityitem = entityplayermp.dropPlayerItem(itemstack);
                entityitem.delayBeforeCanPickup = 0;
                notifyAdmins(par1ICommandSender, "commands.give.success", new Object[] {Item.itemsList[i].getItemStackDisplayName(itemstack), Integer.valueOf(i), Integer.valueOf(j), entityplayermp.getEntityName()});
            }
        }
        else
        {
            throw new WrongUsageException("commands.give.usage", new Object[0]);
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        return par2ArrayOfStr.length == 1 ? getListOfStringsMatchingLastWord(par2ArrayOfStr, this.getPlayers()) : null;
    }

    protected String[] getPlayers()
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
