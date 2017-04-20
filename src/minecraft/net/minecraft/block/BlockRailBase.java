package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockRailBase extends Block
{
    /** Power related rails have this field at true. */
    protected final boolean isPowered;

    /**
     * Returns true if the block at the coordinates of world passed is a valid rail block (current is rail, powered or
     * detector).
     */
    public static final boolean isRailBlockAt(World par0World, int par1, int par2, int par3)
    {
        return isRailBlock(par0World.getBlockId(par1, par2, par3));
    }

    /**
     * Return true if the parameter is a blockID for a valid rail block (current is rail, powered or detector).
     */
    public static final boolean isRailBlock(int par0)
    {
        return Block.blocksList[par0] instanceof BlockRailBase;
    }

    protected BlockRailBase(int par1, boolean par2)
    {
        super(par1, Material.circuits);
        this.isPowered = par2;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        this.setCreativeTab(CreativeTabs.tabTransport);
    }

    /**
     * Returns true if the block is power related rail.
     */
    public boolean isPowered()
    {
        return this.isPowered;
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
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit. Args: world,
     * x, y, z, startVec, endVec
     */
    public MovingObjectPosition collisionRayTrace(World par1World, int par2, int par3, int par4, Vec3 par5Vec3, Vec3 par6Vec3)
    {
        this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
        return super.collisionRayTrace(par1World, par2, par3, par4, par5Vec3, par6Vec3);
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        int l = par1IBlockAccess.getBlockMetadata(par2, par3, par4);

        if (l >= 2 && l <= 5)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        }
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return renderType;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random par1Random)
    {
        return 1;
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4);
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        if (!par1World.isRemote)
        {
            this.refreshTrackShape(par1World, par2, par3, par4, true);

            if (this.isPowered)
            {
                this.onNeighborBlockChange(par1World, par2, par3, par4, this.blockID);
            }
        }
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        if (!par1World.isRemote)
        {
            int i1 = par1World.getBlockMetadata(par2, par3, par4);
            int j1 = i1;

            if (this.isPowered)
            {
                j1 = i1 & 7;
            }

            boolean flag = false;

            if (!par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4))
            {
                flag = true;
            }

            if (j1 == 2 && !par1World.doesBlockHaveSolidTopSurface(par2 + 1, par3, par4))
            {
                flag = true;
            }

            if (j1 == 3 && !par1World.doesBlockHaveSolidTopSurface(par2 - 1, par3, par4))
            {
                flag = true;
            }

            if (j1 == 4 && !par1World.doesBlockHaveSolidTopSurface(par2, par3, par4 - 1))
            {
                flag = true;
            }

            if (j1 == 5 && !par1World.doesBlockHaveSolidTopSurface(par2, par3, par4 + 1))
            {
                flag = true;
            }

            if (flag)
            {
                this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
                par1World.setBlockToAir(par2, par3, par4);
            }
            else
            {
                this.func_94358_a(par1World, par2, par3, par4, i1, j1, par5);
            }
        }
    }

    protected void func_94358_a(World par1World, int par2, int par3, int par4, int par5, int par6, int par7) {}

    /**
     * Completely recalculates the track shape based on neighboring tracks
     */
    protected void refreshTrackShape(World par1World, int par2, int par3, int par4, boolean par5)
    {
        if (!par1World.isRemote)
        {
            (new BlockBaseRailLogic(this, par1World, par2, par3, par4)).func_94511_a(par1World.isBlockIndirectlyGettingPowered(par2, par3, par4), par5);
        }
    }

    /**
     * Returns the mobility information of the block, 0 = free, 1 = can't push but can move over, 2 = total immobility
     * and stop pistons
     */
    public int getMobilityFlag()
    {
        return 0;
    }

    /**
     * Called on server worlds only when the block has been replaced by a different block ID, or the same block with a
     * different metadata value, but before the new metadata value is set. Args: World, x, y, z, old block ID, old
     * metadata
     */
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        int j1 = par6;

        if (this.isPowered)
        {
            j1 = par6 & 7;
        }

        super.breakBlock(par1World, par2, par3, par4, par5, par6);

        if (j1 == 2 || j1 == 3 || j1 == 4 || j1 == 5)
        {
            par1World.notifyBlocksOfNeighborChange(par2, par3 + 1, par4, par5);
        }

        if (this.isPowered)
        {
            par1World.notifyBlocksOfNeighborChange(par2, par3, par4, par5);
            par1World.notifyBlocksOfNeighborChange(par2, par3 - 1, par4, par5);
        }
    }
    
        /**
     * Return true if the rail can make corners.
     * Used by placement logic.
     * @param world The world.
     * @param x The rail X coordinate.
     * @param y The rail Y coordinate.
     * @param z The rail Z coordinate.
     * @return True if the rail can make corners.
     */
    public boolean isFlexibleRail(World world, int y, int x, int z)
    {
        return !isPowered;
    }

    /**
     * Returns true if the rail can make up and down slopes.
     * Used by placement logic.
     * @param world The world.
     * @param x The rail X coordinate.
     * @param y The rail Y coordinate.
     * @param z The rail Z coordinate.
     * @return True if the rail can make slopes.
     */
    public boolean canMakeSlopes(World world, int x, int y, int z)
    {
        return true;
    }

    /**
     * Return the rail's metadata (without the power bit if the rail uses one).
     * Can be used to make the cart think the rail something other than it is,
     * for example when making diamond junctions or switches.
     * The cart parameter will often be null unless it it called from EntityMinecart.
     * 
     * Valid rail metadata is defined as follows:
     * 0x0: flat track going North-South
     * 0x1: flat track going West-East
     * 0x2: track ascending to the East
     * 0x3: track ascending to the West
     * 0x4: track ascending to the North
     * 0x5: track ascending to the South
     * 0x6: WestNorth corner (connecting East and South)
     * 0x7: EastNorth corner (connecting West and South)
     * 0x8: EastSouth corner (connecting West and North)
     * 0x9: WestSouth corner (connecting East and North)
     * 
     * @param world The world.
     * @param cart The cart asking for the metadata, null if it is not called by EntityMinecart.
     * @param y The rail X coordinate.
     * @param x The rail Y coordinate.
     * @param z The rail Z coordinate.
     * @return The metadata.
     */
    public int getBasicRailMetadata(IBlockAccess world, EntityMinecart cart, int x, int y, int z)
    {
        int meta = world.getBlockMetadata(x, y, z);
        if(isPowered)
        {
            meta = meta & 7;
        }
        return meta;
    }

    /**
     * Returns the max speed of the rail at the specified position.
     * @param world The world.
     * @param cart The cart on the rail, may be null.
     * @param x The rail X coordinate.
     * @param y The rail Y coordinate.
     * @param z The rail Z coordinate.
     * @return The max speed of the current rail.
     */
    public float getRailMaxSpeed(World world, EntityMinecart cart, int y, int x, int z)
    {
        return 0.4f;
    }

    /**
     * This function is called by any minecart that passes over this rail.
     * It is called once per update tick that the minecart is on the rail.
     * @param world The world.
     * @param cart The cart on the rail.
     * @param y The rail X coordinate.
     * @param x The rail Y coordinate.
     * @param z The rail Z coordinate.
     */
    public void onMinecartPass(World world, EntityMinecart cart, int y, int x, int z)
    {
    }    
    
    /**
     * Forge: Moved render type to a field and a setter.
     * This allows for a mod to change the render type
     * for vanilla rails, and any mod rails that extend
     * this class.
     */
    private int renderType = 9;
    
    public void setRenderType(int value)
    {
        renderType = value;
    }
}
