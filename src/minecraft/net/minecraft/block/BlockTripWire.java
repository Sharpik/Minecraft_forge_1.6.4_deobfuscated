package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTripWire extends Block
{
    public BlockTripWire(int par1)
    {
        super(par1, Material.circuits);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.15625F, 1.0F);
        this.setTickRandomly(true);
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World par1World)
    {
        return 10;
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
     */
    public int getRenderBlockPass()
    {
        return 1;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 30;
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return Item.silk.itemID;
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        int i1 = par1World.getBlockMetadata(par2, par3, par4);
        boolean flag = (i1 & 2) == 2;
        boolean flag1 = !par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4);

        if (flag != flag1)
        {
            this.dropBlockAsItem(par1World, par2, par3, par4, i1, 0);
            par1World.setBlockToAir(par2, par3, par4);
        }
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        int l = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
        boolean flag = (l & 4) == 4;
        boolean flag1 = (l & 2) == 2;

        if (!flag1)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.09375F, 1.0F);
        }
        else if (!flag)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0625F, 0.0F, 1.0F, 0.15625F, 1.0F);
        }
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        int l = par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) ? 0 : 2;
        par1World.setBlockMetadataWithNotify(par2, par3, par4, l, 3);
        this.func_72149_e(par1World, par2, par3, par4, l);
    }

    /**
     * Called on server worlds only when the block has been replaced by a different block ID, or the same block with a
     * different metadata value, but before the new metadata value is set. Args: World, x, y, z, old block ID, old
     * metadata
     */
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        this.func_72149_e(par1World, par2, par3, par4, par6 | 1);
    }

    /**
     * Called when the block is attempted to be harvested
     */
    public void onBlockHarvested(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer)
    {
        if (!par1World.isRemote)
        {
            if (par6EntityPlayer.getCurrentEquippedItem() != null && par6EntityPlayer.getCurrentEquippedItem().itemID == Item.shears.itemID)
            {
                par1World.setBlockMetadataWithNotify(par2, par3, par4, par5 | 8, 4);
            }
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
     */
    public int idPicked(World par1World, int par2, int par3, int par4)
    {
        return Item.silk.itemID;
    }

    private void func_72149_e(World par1World, int par2, int par3, int par4, int par5)
    {
        int i1 = 0;

        while (i1 < 2)
        {
            int j1 = 1;

            while (true)
            {
                if (j1 < 42)
                {
                    int k1 = par2 + Direction.offsetX[i1] * j1;
                    int l1 = par4 + Direction.offsetZ[i1] * j1;
                    int i2 = par1World.getBlockId(k1, par3, l1);

                    if (i2 == Block.tripWireSource.blockID)
                    {
                        int j2 = par1World.getBlockMetadata(k1, par3, l1) & 3;

                        if (j2 == Direction.rotateOpposite[i1])
                        {
                            Block.tripWireSource.func_72143_a(par1World, k1, par3, l1, i2, par1World.getBlockMetadata(k1, par3, l1), true, j1, par5);
                        }
                    }
                    else if (i2 == Block.tripWire.blockID)
                    {
                        ++j1;
                        continue;
                    }
                }

                ++i1;
                break;
            }
        }
    }

    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
        if (!par1World.isRemote)
        {
            if ((par1World.getBlockMetadata(par2, par3, par4) & 1) != 1)
            {
                this.updateTripWireState(par1World, par2, par3, par4);
            }
        }
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (!par1World.isRemote)
        {
            if ((par1World.getBlockMetadata(par2, par3, par4) & 1) == 1)
            {
                this.updateTripWireState(par1World, par2, par3, par4);
            }
        }
    }

    private void updateTripWireState(World par1World, int par2, int par3, int par4)
    {
        int l = par1World.getBlockMetadata(par2, par3, par4);
        boolean flag = (l & 1) == 1;
        boolean flag1 = false;
        List list = par1World.getEntitiesWithinAABBExcludingEntity((Entity)null, AxisAlignedBB.getAABBPool().getAABB((double)par2 + this.minX, (double)par3 + this.minY, (double)par4 + this.minZ, (double)par2 + this.maxX, (double)par3 + this.maxY, (double)par4 + this.maxZ));

        if (!list.isEmpty())
        {
            Iterator iterator = list.iterator();

            while (iterator.hasNext())
            {
                Entity entity = (Entity)iterator.next();

                if (!entity.doesEntityNotTriggerPressurePlate())
                {
                    flag1 = true;
                    break;
                }
            }
        }

        if (flag1 && !flag)
        {
            l |= 1;
        }

        if (!flag1 && flag)
        {
            l &= -2;
        }

        if (flag1 != flag)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, l, 3);
            this.func_72149_e(par1World, par2, par3, par4, l);
        }

        if (flag1)
        {
            par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World));
        }
    }

    @SideOnly(Side.CLIENT)
    public static boolean func_72148_a(IBlockAccess par0IBlockAccess, int par1, int par2, int par3, int par4, int par5)
    {
        int j1 = par1 + Direction.offsetX[par5];
        int k1 = par3 + Direction.offsetZ[par5];
        int l1 = par0IBlockAccess.getBlockId(j1, par2, k1);
        boolean flag = (par4 & 2) == 2;
        int i2;

        if (l1 == Block.tripWireSource.blockID)
        {
            i2 = par0IBlockAccess.getBlockMetadata(j1, par2, k1);
            int j2 = i2 & 3;
            return j2 == Direction.rotateOpposite[par5];
        }
        else if (l1 == Block.tripWire.blockID)
        {
            i2 = par0IBlockAccess.getBlockMetadata(j1, par2, k1);
            boolean flag1 = (i2 & 2) == 2;
            return flag == flag1;
        }
        else
        {
            return false;
        }
    }
}
