package net.minecraft.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;

public class ItemFireworkCharge extends Item
{
    @SideOnly(Side.CLIENT)
    private Icon theIcon;

    public ItemFireworkCharge(int par1)
    {
        super(par1);
    }

    @SideOnly(Side.CLIENT)

    /**
     * Gets an icon index based on an item's damage value and the given render pass
     */
    public Icon getIconFromDamageForRenderPass(int par1, int par2)
    {
        return par2 > 0 ? this.theIcon : super.getIconFromDamageForRenderPass(par1, par2);
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
        if (par2 != 1)
        {
            return super.getColorFromItemStack(par1ItemStack, par2);
        }
        else
        {
            NBTBase nbtbase = func_92108_a(par1ItemStack, "Colors");

            if (nbtbase == null)
            {
                return 9079434;
            }
            else
            {
                NBTTagIntArray nbttagintarray = (NBTTagIntArray)nbtbase;

                if (nbttagintarray.intArray.length == 1)
                {
                    return nbttagintarray.intArray[0];
                }
                else
                {
                    int j = 0;
                    int k = 0;
                    int l = 0;
                    int[] aint = nbttagintarray.intArray;
                    int i1 = aint.length;

                    for (int j1 = 0; j1 < i1; ++j1)
                    {
                        int k1 = aint[j1];
                        j += (k1 & 16711680) >> 16;
                        k += (k1 & 65280) >> 8;
                        l += (k1 & 255) >> 0;
                    }

                    j /= nbttagintarray.intArray.length;
                    k /= nbttagintarray.intArray.length;
                    l /= nbttagintarray.intArray.length;
                    return j << 16 | k << 8 | l;
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public static NBTBase func_92108_a(ItemStack par0ItemStack, String par1Str)
    {
        if (par0ItemStack.hasTagCompound())
        {
            NBTTagCompound nbttagcompound = par0ItemStack.getTagCompound().getCompoundTag("Explosion");

            if (nbttagcompound != null)
            {
                return nbttagcompound.getTag(par1Str);
            }
        }

        return null;
    }

    @SideOnly(Side.CLIENT)

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        if (par1ItemStack.hasTagCompound())
        {
            NBTTagCompound nbttagcompound = par1ItemStack.getTagCompound().getCompoundTag("Explosion");

            if (nbttagcompound != null)
            {
                func_92107_a(nbttagcompound, par3List);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static void func_92107_a(NBTTagCompound par0NBTTagCompound, List par1List)
    {
        byte b0 = par0NBTTagCompound.getByte("Type");

        if (b0 >= 0 && b0 <= 4)
        {
            par1List.add(StatCollector.translateToLocal("item.fireworksCharge.type." + b0).trim());
        }
        else
        {
            par1List.add(StatCollector.translateToLocal("item.fireworksCharge.type").trim());
        }

        int[] aint = par0NBTTagCompound.getIntArray("Colors");
        int i;
        int j;

        if (aint.length > 0)
        {
            boolean flag = true;
            String s = "";
            int[] aint1 = aint;
            int k = aint.length;

            for (i = 0; i < k; ++i)
            {
                j = aint1[i];

                if (!flag)
                {
                    s = s + ", ";
                }

                flag = false;
                boolean flag1 = false;

                for (int l = 0; l < 16; ++l)
                {
                    if (j == ItemDye.dyeColors[l])
                    {
                        flag1 = true;
                        s = s + StatCollector.translateToLocal("item.fireworksCharge." + ItemDye.dyeColorNames[l]);
                        break;
                    }
                }

                if (!flag1)
                {
                    s = s + StatCollector.translateToLocal("item.fireworksCharge.customColor");
                }
            }

            par1List.add(s);
        }

        int[] aint2 = par0NBTTagCompound.getIntArray("FadeColors");
        boolean flag2;

        if (aint2.length > 0)
        {
            flag2 = true;
            String s1 = StatCollector.translateToLocal("item.fireworksCharge.fadeTo") + " ";
            int[] aint3 = aint2;
            i = aint2.length;

            for (j = 0; j < i; ++j)
            {
                int i1 = aint3[j];

                if (!flag2)
                {
                    s1 = s1 + ", ";
                }

                flag2 = false;
                boolean flag3 = false;

                for (int j1 = 0; j1 < 16; ++j1)
                {
                    if (i1 == ItemDye.dyeColors[j1])
                    {
                        flag3 = true;
                        s1 = s1 + StatCollector.translateToLocal("item.fireworksCharge." + ItemDye.dyeColorNames[j1]);
                        break;
                    }
                }

                if (!flag3)
                {
                    s1 = s1 + StatCollector.translateToLocal("item.fireworksCharge.customColor");
                }
            }

            par1List.add(s1);
        }

        flag2 = par0NBTTagCompound.getBoolean("Trail");

        if (flag2)
        {
            par1List.add(StatCollector.translateToLocal("item.fireworksCharge.trail"));
        }

        boolean flag4 = par0NBTTagCompound.getBoolean("Flicker");

        if (flag4)
        {
            par1List.add(StatCollector.translateToLocal("item.fireworksCharge.flicker"));
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        super.registerIcons(par1IconRegister);
        this.theIcon = par1IconRegister.registerIcon(this.getIconString() + "_overlay");
    }
}
