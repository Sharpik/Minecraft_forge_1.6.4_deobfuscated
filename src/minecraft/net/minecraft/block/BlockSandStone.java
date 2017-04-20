package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class BlockSandStone extends Block
{
    public static final String[] SAND_STONE_TYPES = new String[] {"default", "chiseled", "smooth"};
    private static final String[] field_94405_b = new String[] {"normal", "carved", "smooth"};
    @SideOnly(Side.CLIENT)
    private Icon[] field_94406_c;
    @SideOnly(Side.CLIENT)
    private Icon field_94403_cO;
    @SideOnly(Side.CLIENT)
    private Icon field_94404_cP;

    public BlockSandStone(int par1)
    {
        super(par1, Material.rock);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @SideOnly(Side.CLIENT)

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int par1, int par2)
    {
        if (par1 != 1 && (par1 != 0 || par2 != 1 && par2 != 2))
        {
            if (par1 == 0)
            {
                return this.field_94404_cP;
            }
            else
            {
                if (par2 < 0 || par2 >= this.field_94406_c.length)
                {
                    par2 = 0;
                }

                return this.field_94406_c[par2];
            }
        }
        else
        {
            return this.field_94403_cO;
        }
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int par1)
    {
        return par1;
    }

    @SideOnly(Side.CLIENT)

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
        par3List.add(new ItemStack(par1, 1, 2));
    }

    @SideOnly(Side.CLIENT)

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.field_94406_c = new Icon[field_94405_b.length];

        for (int i = 0; i < this.field_94406_c.length; ++i)
        {
            this.field_94406_c[i] = par1IconRegister.registerIcon(this.getTextureName() + "_" + field_94405_b[i]);
        }

        this.field_94403_cO = par1IconRegister.registerIcon(this.getTextureName() + "_top");
        this.field_94404_cP = par1IconRegister.registerIcon(this.getTextureName() + "_bottom");
    }
}
