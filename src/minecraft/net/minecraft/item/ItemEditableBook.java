package net.minecraft.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemEditableBook extends Item
{
    public ItemEditableBook(int par1)
    {
        super(par1);
        this.setMaxStackSize(1);
    }

    public static boolean validBookTagContents(NBTTagCompound par0NBTTagCompound)
    {
        if (!ItemWritableBook.validBookTagPages(par0NBTTagCompound))
        {
            return false;
        }
        else if (!par0NBTTagCompound.hasKey("title"))
        {
            return false;
        }
        else
        {
            String s = par0NBTTagCompound.getString("title");
            return s != null && s.length() <= 16 ? par0NBTTagCompound.hasKey("author") : false;
        }
    }

    public String getItemDisplayName(ItemStack par1ItemStack)
    {
        if (par1ItemStack.hasTagCompound())
        {
            NBTTagCompound nbttagcompound = par1ItemStack.getTagCompound();
            NBTTagString nbttagstring = (NBTTagString)nbttagcompound.getTag("title");

            if (nbttagstring != null)
            {
                return nbttagstring.toString();
            }
        }

        return super.getItemDisplayName(par1ItemStack);
    }

    @SideOnly(Side.CLIENT)

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        if (par1ItemStack.hasTagCompound())
        {
            NBTTagCompound nbttagcompound = par1ItemStack.getTagCompound();
            NBTTagString nbttagstring = (NBTTagString)nbttagcompound.getTag("author");

            if (nbttagstring != null)
            {
                par3List.add(EnumChatFormatting.GRAY + String.format(StatCollector.translateToLocalFormatted("book.byAuthor", new Object[] {nbttagstring.data}), new Object[0]));
            }
        }
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        par3EntityPlayer.displayGUIBook(par1ItemStack);
        return par1ItemStack;
    }

    /**
     * If this function returns true (or the item is damageable), the ItemStack's NBT tag will be sent to the client.
     */
    public boolean getShareTag()
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack par1ItemStack)
    {
        return true;
    }
}
