package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockRailPowered extends BlockRailBase
{
    @SideOnly(Side.CLIENT)
    protected Icon theIcon;

    protected BlockRailPowered(int par1)
    {
        super(par1, true);
    }

    @SideOnly(Side.CLIENT)

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int par1, int par2)
    {
        return (par2 & 8) == 0 ? this.blockIcon : this.theIcon;
    }

    @SideOnly(Side.CLIENT)

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister)
    {
        super.registerIcons(par1IconRegister);
        this.theIcon = par1IconRegister.registerIcon(this.getTextureName() + "_powered");
    }

    protected boolean func_94360_a(World par1World, int par2, int par3, int par4, int par5, boolean par6, int par7)
    {
        if (par7 >= 8)
        {
            return false;
        }
        else
        {
            int j1 = par5 & 7;
            boolean flag1 = true;

            switch (j1)
            {
                case 0:
                    if (par6)
                    {
                        ++par4;
                    }
                    else
                    {
                        --par4;
                    }

                    break;
                case 1:
                    if (par6)
                    {
                        --par2;
                    }
                    else
                    {
                        ++par2;
                    }

                    break;
                case 2:
                    if (par6)
                    {
                        --par2;
                    }
                    else
                    {
                        ++par2;
                        ++par3;
                        flag1 = false;
                    }

                    j1 = 1;
                    break;
                case 3:
                    if (par6)
                    {
                        --par2;
                        ++par3;
                        flag1 = false;
                    }
                    else
                    {
                        ++par2;
                    }

                    j1 = 1;
                    break;
                case 4:
                    if (par6)
                    {
                        ++par4;
                    }
                    else
                    {
                        --par4;
                        ++par3;
                        flag1 = false;
                    }

                    j1 = 0;
                    break;
                case 5:
                    if (par6)
                    {
                        ++par4;
                        ++par3;
                        flag1 = false;
                    }
                    else
                    {
                        --par4;
                    }

                    j1 = 0;
            }

            return this.func_94361_a(par1World, par2, par3, par4, par6, par7, j1) ? true : flag1 && this.func_94361_a(par1World, par2, par3 - 1, par4, par6, par7, j1);
        }
    }

    protected boolean func_94361_a(World par1World, int par2, int par3, int par4, boolean par5, int par6, int par7)
    {
        int j1 = par1World.getBlockId(par2, par3, par4);

        if (j1 == this.blockID)
        {
            int k1 = par1World.getBlockMetadata(par2, par3, par4);
            int l1 = k1 & 7;

            if (par7 == 1 && (l1 == 0 || l1 == 4 || l1 == 5))
            {
                return false;
            }

            if (par7 == 0 && (l1 == 1 || l1 == 2 || l1 == 3))
            {
                return false;
            }

            if ((k1 & 8) != 0)
            {
                if (par1World.isBlockIndirectlyGettingPowered(par2, par3, par4))
                {
                    return true;
                }

                return this.func_94360_a(par1World, par2, par3, par4, k1, par5, par6 + 1);
            }
        }

        return false;
    }

    protected void func_94358_a(World par1World, int par2, int par3, int par4, int par5, int par6, int par7)
    {
        boolean flag = par1World.isBlockIndirectlyGettingPowered(par2, par3, par4);
        flag = flag || this.func_94360_a(par1World, par2, par3, par4, par5, true, 0) || this.func_94360_a(par1World, par2, par3, par4, par5, false, 0);
        boolean flag1 = false;

        if (flag && (par5 & 8) == 0)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, par6 | 8, 3);
            flag1 = true;
        }
        else if (!flag && (par5 & 8) != 0)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, par6, 3);
            flag1 = true;
        }

        if (flag1)
        {
            par1World.notifyBlocksOfNeighborChange(par2, par3 - 1, par4, this.blockID);

            if (par6 == 2 || par6 == 3 || par6 == 4 || par6 == 5)
            {
                par1World.notifyBlocksOfNeighborChange(par2, par3 + 1, par4, this.blockID);
            }
        }
    }
}
