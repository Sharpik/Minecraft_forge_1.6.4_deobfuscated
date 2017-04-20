package net.minecraft.world.gen.structure;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.item.ItemDoor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.Direction;
import net.minecraft.util.Facing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;

public abstract class StructureComponent
{
    protected StructureBoundingBox boundingBox;

    /** switches the Coordinate System base off the Bounding Box */
    protected int coordBaseMode;

    /** The type ID of this component. */
    protected int componentType;

    public StructureComponent() {}

    protected StructureComponent(int par1)
    {
        this.componentType = par1;
        this.coordBaseMode = -1;
    }

    public NBTTagCompound func_143010_b()
    {
        if (MapGenStructureIO.func_143036_a(this) == null)
        {
            throw new RuntimeException("StructureComponent \"" + this.getClass().getName() + "\" missing ID Mapping, Modder see MapGenStructureIO");
        }

        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setString("id", MapGenStructureIO.func_143036_a(this));
        nbttagcompound.setTag("BB", this.boundingBox.func_143047_a("BB"));
        nbttagcompound.setInteger("O", this.coordBaseMode);
        nbttagcompound.setInteger("GD", this.componentType);
        this.func_143012_a(nbttagcompound);
        return nbttagcompound;
    }

    protected abstract void func_143012_a(NBTTagCompound nbttagcompound);

    public void func_143009_a(World par1World, NBTTagCompound par2NBTTagCompound)
    {
        if (par2NBTTagCompound.hasKey("BB"))
        {
            this.boundingBox = new StructureBoundingBox(par2NBTTagCompound.getIntArray("BB"));
        }

        this.coordBaseMode = par2NBTTagCompound.getInteger("O");
        this.componentType = par2NBTTagCompound.getInteger("GD");
        this.func_143011_b(par2NBTTagCompound);
    }

    protected abstract void func_143011_b(NBTTagCompound nbttagcompound);

    /**
     * Initiates construction of the Structure Component picked, at the current Location of StructGen
     */
    public void buildComponent(StructureComponent par1StructureComponent, List par2List, Random par3Random) {}

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    public abstract boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox);

    public StructureBoundingBox getBoundingBox()
    {
        return this.boundingBox;
    }

    /**
     * Returns the component type ID of this component.
     */
    public int getComponentType()
    {
        return this.componentType;
    }

    /**
     * Discover if bounding box can fit within the current bounding box object.
     */
    public static StructureComponent findIntersecting(List par0List, StructureBoundingBox par1StructureBoundingBox)
    {
        Iterator iterator = par0List.iterator();
        StructureComponent structurecomponent;

        do
        {
            if (!iterator.hasNext())
            {
                return null;
            }

            structurecomponent = (StructureComponent)iterator.next();
        }
        while (structurecomponent.getBoundingBox() == null || !structurecomponent.getBoundingBox().intersectsWith(par1StructureBoundingBox));

        return structurecomponent;
    }

    public ChunkPosition getCenter()
    {
        return new ChunkPosition(this.boundingBox.getCenterX(), this.boundingBox.getCenterY(), this.boundingBox.getCenterZ());
    }

    /**
     * checks the entire StructureBoundingBox for Liquids
     */
    protected boolean isLiquidInStructureBoundingBox(World par1World, StructureBoundingBox par2StructureBoundingBox)
    {
        int i = Math.max(this.boundingBox.minX - 1, par2StructureBoundingBox.minX);
        int j = Math.max(this.boundingBox.minY - 1, par2StructureBoundingBox.minY);
        int k = Math.max(this.boundingBox.minZ - 1, par2StructureBoundingBox.minZ);
        int l = Math.min(this.boundingBox.maxX + 1, par2StructureBoundingBox.maxX);
        int i1 = Math.min(this.boundingBox.maxY + 1, par2StructureBoundingBox.maxY);
        int j1 = Math.min(this.boundingBox.maxZ + 1, par2StructureBoundingBox.maxZ);
        int k1;
        int l1;
        int i2;

        for (k1 = i; k1 <= l; ++k1)
        {
            for (l1 = k; l1 <= j1; ++l1)
            {
                i2 = par1World.getBlockId(k1, j, l1);

                if (i2 > 0 && Block.blocksList[i2].blockMaterial.isLiquid())
                {
                    return true;
                }

                i2 = par1World.getBlockId(k1, i1, l1);

                if (i2 > 0 && Block.blocksList[i2].blockMaterial.isLiquid())
                {
                    return true;
                }
            }
        }

        for (k1 = i; k1 <= l; ++k1)
        {
            for (l1 = j; l1 <= i1; ++l1)
            {
                i2 = par1World.getBlockId(k1, l1, k);

                if (i2 > 0 && Block.blocksList[i2].blockMaterial.isLiquid())
                {
                    return true;
                }

                i2 = par1World.getBlockId(k1, l1, j1);

                if (i2 > 0 && Block.blocksList[i2].blockMaterial.isLiquid())
                {
                    return true;
                }
            }
        }

        for (k1 = k; k1 <= j1; ++k1)
        {
            for (l1 = j; l1 <= i1; ++l1)
            {
                i2 = par1World.getBlockId(i, l1, k1);

                if (i2 > 0 && Block.blocksList[i2].blockMaterial.isLiquid())
                {
                    return true;
                }

                i2 = par1World.getBlockId(l, l1, k1);

                if (i2 > 0 && Block.blocksList[i2].blockMaterial.isLiquid())
                {
                    return true;
                }
            }
        }

        return false;
    }

    protected int getXWithOffset(int par1, int par2)
    {
        switch (this.coordBaseMode)
        {
            case 0:
            case 2:
                return this.boundingBox.minX + par1;
            case 1:
                return this.boundingBox.maxX - par2;
            case 3:
                return this.boundingBox.minX + par2;
            default:
                return par1;
        }
    }

    protected int getYWithOffset(int par1)
    {
        return this.coordBaseMode == -1 ? par1 : par1 + this.boundingBox.minY;
    }

    protected int getZWithOffset(int par1, int par2)
    {
        switch (this.coordBaseMode)
        {
            case 0:
                return this.boundingBox.minZ + par2;
            case 1:
            case 3:
                return this.boundingBox.minZ + par1;
            case 2:
                return this.boundingBox.maxZ - par2;
            default:
                return par2;
        }
    }

    /**
     * Returns the direction-shifted metadata for blocks that require orientation, e.g. doors, stairs, ladders.
     * Parameters: block ID, original metadata
     */
    protected int getMetadataWithOffset(int par1, int par2)
    {
        if (par1 == Block.rail.blockID)
        {
            if (this.coordBaseMode == 1 || this.coordBaseMode == 3)
            {
                if (par2 == 1)
                {
                    return 0;
                }

                return 1;
            }
        }
        else if (par1 != Block.doorWood.blockID && par1 != Block.doorIron.blockID)
        {
            if (par1 != Block.stairsCobblestone.blockID && par1 != Block.stairsWoodOak.blockID && par1 != Block.stairsNetherBrick.blockID && par1 != Block.stairsStoneBrick.blockID && par1 != Block.stairsSandStone.blockID)
            {
                if (par1 == Block.ladder.blockID)
                {
                    if (this.coordBaseMode == 0)
                    {
                        if (par2 == 2)
                        {
                            return 3;
                        }

                        if (par2 == 3)
                        {
                            return 2;
                        }
                    }
                    else if (this.coordBaseMode == 1)
                    {
                        if (par2 == 2)
                        {
                            return 4;
                        }

                        if (par2 == 3)
                        {
                            return 5;
                        }

                        if (par2 == 4)
                        {
                            return 2;
                        }

                        if (par2 == 5)
                        {
                            return 3;
                        }
                    }
                    else if (this.coordBaseMode == 3)
                    {
                        if (par2 == 2)
                        {
                            return 5;
                        }

                        if (par2 == 3)
                        {
                            return 4;
                        }

                        if (par2 == 4)
                        {
                            return 2;
                        }

                        if (par2 == 5)
                        {
                            return 3;
                        }
                    }
                }
                else if (par1 == Block.stoneButton.blockID)
                {
                    if (this.coordBaseMode == 0)
                    {
                        if (par2 == 3)
                        {
                            return 4;
                        }

                        if (par2 == 4)
                        {
                            return 3;
                        }
                    }
                    else if (this.coordBaseMode == 1)
                    {
                        if (par2 == 3)
                        {
                            return 1;
                        }

                        if (par2 == 4)
                        {
                            return 2;
                        }

                        if (par2 == 2)
                        {
                            return 3;
                        }

                        if (par2 == 1)
                        {
                            return 4;
                        }
                    }
                    else if (this.coordBaseMode == 3)
                    {
                        if (par2 == 3)
                        {
                            return 2;
                        }

                        if (par2 == 4)
                        {
                            return 1;
                        }

                        if (par2 == 2)
                        {
                            return 3;
                        }

                        if (par2 == 1)
                        {
                            return 4;
                        }
                    }
                }
                else if (par1 != Block.tripWireSource.blockID && (Block.blocksList[par1] == null || !(Block.blocksList[par1] instanceof BlockDirectional)))
                {
                    if (par1 == Block.pistonBase.blockID || par1 == Block.pistonStickyBase.blockID || par1 == Block.lever.blockID || par1 == Block.dispenser.blockID)
                    {
                        if (this.coordBaseMode == 0)
                        {
                            if (par2 == 2 || par2 == 3)
                            {
                                return Facing.oppositeSide[par2];
                            }
                        }
                        else if (this.coordBaseMode == 1)
                        {
                            if (par2 == 2)
                            {
                                return 4;
                            }

                            if (par2 == 3)
                            {
                                return 5;
                            }

                            if (par2 == 4)
                            {
                                return 2;
                            }

                            if (par2 == 5)
                            {
                                return 3;
                            }
                        }
                        else if (this.coordBaseMode == 3)
                        {
                            if (par2 == 2)
                            {
                                return 5;
                            }

                            if (par2 == 3)
                            {
                                return 4;
                            }

                            if (par2 == 4)
                            {
                                return 2;
                            }

                            if (par2 == 5)
                            {
                                return 3;
                            }
                        }
                    }
                }
                else if (this.coordBaseMode == 0)
                {
                    if (par2 == 0 || par2 == 2)
                    {
                        return Direction.rotateOpposite[par2];
                    }
                }
                else if (this.coordBaseMode == 1)
                {
                    if (par2 == 2)
                    {
                        return 1;
                    }

                    if (par2 == 0)
                    {
                        return 3;
                    }

                    if (par2 == 1)
                    {
                        return 2;
                    }

                    if (par2 == 3)
                    {
                        return 0;
                    }
                }
                else if (this.coordBaseMode == 3)
                {
                    if (par2 == 2)
                    {
                        return 3;
                    }

                    if (par2 == 0)
                    {
                        return 1;
                    }

                    if (par2 == 1)
                    {
                        return 2;
                    }

                    if (par2 == 3)
                    {
                        return 0;
                    }
                }
            }
            else if (this.coordBaseMode == 0)
            {
                if (par2 == 2)
                {
                    return 3;
                }

                if (par2 == 3)
                {
                    return 2;
                }
            }
            else if (this.coordBaseMode == 1)
            {
                if (par2 == 0)
                {
                    return 2;
                }

                if (par2 == 1)
                {
                    return 3;
                }

                if (par2 == 2)
                {
                    return 0;
                }

                if (par2 == 3)
                {
                    return 1;
                }
            }
            else if (this.coordBaseMode == 3)
            {
                if (par2 == 0)
                {
                    return 2;
                }

                if (par2 == 1)
                {
                    return 3;
                }

                if (par2 == 2)
                {
                    return 1;
                }

                if (par2 == 3)
                {
                    return 0;
                }
            }
        }
        else if (this.coordBaseMode == 0)
        {
            if (par2 == 0)
            {
                return 2;
            }

            if (par2 == 2)
            {
                return 0;
            }
        }
        else
        {
            if (this.coordBaseMode == 1)
            {
                return par2 + 1 & 3;
            }

            if (this.coordBaseMode == 3)
            {
                return par2 + 3 & 3;
            }
        }

        return par2;
    }

    /**
     * current Position depends on currently set Coordinates mode, is computed here
     */
    protected void placeBlockAtCurrentPosition(World par1World, int par2, int par3, int par4, int par5, int par6, StructureBoundingBox par7StructureBoundingBox)
    {
        int j1 = this.getXWithOffset(par4, par6);
        int k1 = this.getYWithOffset(par5);
        int l1 = this.getZWithOffset(par4, par6);

        if (par7StructureBoundingBox.isVecInside(j1, k1, l1))
        {
            par1World.setBlock(j1, k1, l1, par2, par3, 2);
        }
    }

    protected int getBlockIdAtCurrentPosition(World par1World, int par2, int par3, int par4, StructureBoundingBox par5StructureBoundingBox)
    {
        int l = this.getXWithOffset(par2, par4);
        int i1 = this.getYWithOffset(par3);
        int j1 = this.getZWithOffset(par2, par4);
        return !par5StructureBoundingBox.isVecInside(l, i1, j1) ? 0 : par1World.getBlockId(l, i1, j1);
    }

    /**
     * arguments: (World worldObj, StructureBoundingBox structBB, int minX, int minY, int minZ, int maxX, int maxY, int
     * maxZ)
     */
    protected void fillWithAir(World par1World, StructureBoundingBox par2StructureBoundingBox, int par3, int par4, int par5, int par6, int par7, int par8)
    {
        for (int k1 = par4; k1 <= par7; ++k1)
        {
            for (int l1 = par3; l1 <= par6; ++l1)
            {
                for (int i2 = par5; i2 <= par8; ++i2)
                {
                    this.placeBlockAtCurrentPosition(par1World, 0, 0, l1, k1, i2, par2StructureBoundingBox);
                }
            }
        }
    }

    /**
     * arguments: (World worldObj, StructureBoundingBox structBB, int minX, int minY, int minZ, int maxX, int maxY, int
     * maxZ, int placeBlockId, int replaceBlockId, boolean alwaysreplace)
     */
    protected void fillWithBlocks(World par1World, StructureBoundingBox par2StructureBoundingBox, int par3, int par4, int par5, int par6, int par7, int par8, int par9, int par10, boolean par11)
    {
        for (int i2 = par4; i2 <= par7; ++i2)
        {
            for (int j2 = par3; j2 <= par6; ++j2)
            {
                for (int k2 = par5; k2 <= par8; ++k2)
                {
                    if (!par11 || this.getBlockIdAtCurrentPosition(par1World, j2, i2, k2, par2StructureBoundingBox) != 0)
                    {
                        if (i2 != par4 && i2 != par7 && j2 != par3 && j2 != par6 && k2 != par5 && k2 != par8)
                        {
                            this.placeBlockAtCurrentPosition(par1World, par10, 0, j2, i2, k2, par2StructureBoundingBox);
                        }
                        else
                        {
                            this.placeBlockAtCurrentPosition(par1World, par9, 0, j2, i2, k2, par2StructureBoundingBox);
                        }
                    }
                }
            }
        }
    }

    /**
     * arguments: (World worldObj, StructureBoundingBox structBB, int minX, int minY, int minZ, int maxX, int maxY, int
     * maxZ, int placeBlockId, int placeBlockMetadata, int replaceBlockId, int replaceBlockMetadata, boolean
     * alwaysreplace)
     */
    protected void fillWithMetadataBlocks(World par1World, StructureBoundingBox par2StructureBoundingBox, int par3, int par4, int par5, int par6, int par7, int par8, int par9, int par10, int par11, int par12, boolean par13)
    {
        for (int k2 = par4; k2 <= par7; ++k2)
        {
            for (int l2 = par3; l2 <= par6; ++l2)
            {
                for (int i3 = par5; i3 <= par8; ++i3)
                {
                    if (!par13 || this.getBlockIdAtCurrentPosition(par1World, l2, k2, i3, par2StructureBoundingBox) != 0)
                    {
                        if (k2 != par4 && k2 != par7 && l2 != par3 && l2 != par6 && i3 != par5 && i3 != par8)
                        {
                            this.placeBlockAtCurrentPosition(par1World, par11, par12, l2, k2, i3, par2StructureBoundingBox);
                        }
                        else
                        {
                            this.placeBlockAtCurrentPosition(par1World, par9, par10, l2, k2, i3, par2StructureBoundingBox);
                        }
                    }
                }
            }
        }
    }

    /**
     * arguments: World worldObj, StructureBoundingBox structBB, int minX, int minY, int minZ, int maxX, int maxY, int
     * maxZ, boolean alwaysreplace, Random rand, StructurePieceBlockSelector blockselector
     */
    protected void fillWithRandomizedBlocks(World par1World, StructureBoundingBox par2StructureBoundingBox, int par3, int par4, int par5, int par6, int par7, int par8, boolean par9, Random par10Random, StructurePieceBlockSelector par11StructurePieceBlockSelector)
    {
        for (int k1 = par4; k1 <= par7; ++k1)
        {
            for (int l1 = par3; l1 <= par6; ++l1)
            {
                for (int i2 = par5; i2 <= par8; ++i2)
                {
                    if (!par9 || this.getBlockIdAtCurrentPosition(par1World, l1, k1, i2, par2StructureBoundingBox) != 0)
                    {
                        par11StructurePieceBlockSelector.selectBlocks(par10Random, l1, k1, i2, k1 == par4 || k1 == par7 || l1 == par3 || l1 == par6 || i2 == par5 || i2 == par8);
                        this.placeBlockAtCurrentPosition(par1World, par11StructurePieceBlockSelector.getSelectedBlockId(), par11StructurePieceBlockSelector.getSelectedBlockMetaData(), l1, k1, i2, par2StructureBoundingBox);
                    }
                }
            }
        }
    }

    /**
     * arguments: World worldObj, StructureBoundingBox structBB, Random rand, float randLimit, int minX, int minY, int
     * minZ, int maxX, int maxY, int maxZ, int olaceBlockId, int replaceBlockId, boolean alwaysreplace
     */
    protected void randomlyFillWithBlocks(World par1World, StructureBoundingBox par2StructureBoundingBox, Random par3Random, float par4, int par5, int par6, int par7, int par8, int par9, int par10, int par11, int par12, boolean par13)
    {
        for (int i2 = par6; i2 <= par9; ++i2)
        {
            for (int j2 = par5; j2 <= par8; ++j2)
            {
                for (int k2 = par7; k2 <= par10; ++k2)
                {
                    if (par3Random.nextFloat() <= par4 && (!par13 || this.getBlockIdAtCurrentPosition(par1World, j2, i2, k2, par2StructureBoundingBox) != 0))
                    {
                        if (i2 != par6 && i2 != par9 && j2 != par5 && j2 != par8 && k2 != par7 && k2 != par10)
                        {
                            this.placeBlockAtCurrentPosition(par1World, par12, 0, j2, i2, k2, par2StructureBoundingBox);
                        }
                        else
                        {
                            this.placeBlockAtCurrentPosition(par1World, par11, 0, j2, i2, k2, par2StructureBoundingBox);
                        }
                    }
                }
            }
        }
    }

    /**
     * Randomly decides if placing or not. Used for Decoration such as Torches and Spiderwebs
     */
    protected void randomlyPlaceBlock(World par1World, StructureBoundingBox par2StructureBoundingBox, Random par3Random, float par4, int par5, int par6, int par7, int par8, int par9)
    {
        if (par3Random.nextFloat() < par4)
        {
            this.placeBlockAtCurrentPosition(par1World, par8, par9, par5, par6, par7, par2StructureBoundingBox);
        }
    }

    /**
     * arguments: World worldObj, StructureBoundingBox structBB, int minX, int minY, int minZ, int maxX, int maxY, int
     * maxZ, int placeBlockId, boolean alwaysreplace
     */
    protected void randomlyRareFillWithBlocks(World par1World, StructureBoundingBox par2StructureBoundingBox, int par3, int par4, int par5, int par6, int par7, int par8, int par9, boolean par10)
    {
        float f = (float)(par6 - par3 + 1);
        float f1 = (float)(par7 - par4 + 1);
        float f2 = (float)(par8 - par5 + 1);
        float f3 = (float)par3 + f / 2.0F;
        float f4 = (float)par5 + f2 / 2.0F;

        for (int l1 = par4; l1 <= par7; ++l1)
        {
            float f5 = (float)(l1 - par4) / f1;

            for (int i2 = par3; i2 <= par6; ++i2)
            {
                float f6 = ((float)i2 - f3) / (f * 0.5F);

                for (int j2 = par5; j2 <= par8; ++j2)
                {
                    float f7 = ((float)j2 - f4) / (f2 * 0.5F);

                    if (!par10 || this.getBlockIdAtCurrentPosition(par1World, i2, l1, j2, par2StructureBoundingBox) != 0)
                    {
                        float f8 = f6 * f6 + f5 * f5 + f7 * f7;

                        if (f8 <= 1.05F)
                        {
                            this.placeBlockAtCurrentPosition(par1World, par9, 0, i2, l1, j2, par2StructureBoundingBox);
                        }
                    }
                }
            }
        }
    }

    /**
     * Deletes all continuous blocks from selected position upwards. Stops at hitting air.
     */
    protected void clearCurrentPositionBlocksUpwards(World par1World, int par2, int par3, int par4, StructureBoundingBox par5StructureBoundingBox)
    {
        int l = this.getXWithOffset(par2, par4);
        int i1 = this.getYWithOffset(par3);
        int j1 = this.getZWithOffset(par2, par4);

        if (par5StructureBoundingBox.isVecInside(l, i1, j1))
        {
            while (!par1World.isAirBlock(l, i1, j1) && i1 < 255)
            {
                par1World.setBlock(l, i1, j1, 0, 0, 2);
                ++i1;
            }
        }
    }

    /**
     * Overwrites air and liquids from selected position downwards, stops at hitting anything else.
     */
    protected void fillCurrentPositionBlocksDownwards(World par1World, int par2, int par3, int par4, int par5, int par6, StructureBoundingBox par7StructureBoundingBox)
    {
        int j1 = this.getXWithOffset(par4, par6);
        int k1 = this.getYWithOffset(par5);
        int l1 = this.getZWithOffset(par4, par6);

        if (par7StructureBoundingBox.isVecInside(j1, k1, l1))
        {
            while ((par1World.isAirBlock(j1, k1, l1) || par1World.getBlockMaterial(j1, k1, l1).isLiquid()) && k1 > 1)
            {
                par1World.setBlock(j1, k1, l1, par2, par3, 2);
                --k1;
            }
        }
    }

    /**
     * Used to generate chests with items in it. ex: Temple Chests, Village Blacksmith Chests, Mineshaft Chests.
     */
    protected boolean generateStructureChestContents(World par1World, StructureBoundingBox par2StructureBoundingBox, Random par3Random, int par4, int par5, int par6, WeightedRandomChestContent[] par7ArrayOfWeightedRandomChestContent, int par8)
    {
        int i1 = this.getXWithOffset(par4, par6);
        int j1 = this.getYWithOffset(par5);
        int k1 = this.getZWithOffset(par4, par6);

        if (par2StructureBoundingBox.isVecInside(i1, j1, k1) && par1World.getBlockId(i1, j1, k1) != Block.chest.blockID)
        {
            par1World.setBlock(i1, j1, k1, Block.chest.blockID, 0, 2);
            TileEntityChest tileentitychest = (TileEntityChest)par1World.getBlockTileEntity(i1, j1, k1);

            if (tileentitychest != null)
            {
                WeightedRandomChestContent.generateChestContents(par3Random, par7ArrayOfWeightedRandomChestContent, tileentitychest, par8);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Used to generate dispenser contents for structures. ex: Jungle Temples.
     */
    protected boolean generateStructureDispenserContents(World par1World, StructureBoundingBox par2StructureBoundingBox, Random par3Random, int par4, int par5, int par6, int par7, WeightedRandomChestContent[] par8ArrayOfWeightedRandomChestContent, int par9)
    {
        int j1 = this.getXWithOffset(par4, par6);
        int k1 = this.getYWithOffset(par5);
        int l1 = this.getZWithOffset(par4, par6);

        if (par2StructureBoundingBox.isVecInside(j1, k1, l1) && par1World.getBlockId(j1, k1, l1) != Block.dispenser.blockID)
        {
            par1World.setBlock(j1, k1, l1, Block.dispenser.blockID, this.getMetadataWithOffset(Block.dispenser.blockID, par7), 2);
            TileEntityDispenser tileentitydispenser = (TileEntityDispenser)par1World.getBlockTileEntity(j1, k1, l1);

            if (tileentitydispenser != null)
            {
                WeightedRandomChestContent.generateDispenserContents(par3Random, par8ArrayOfWeightedRandomChestContent, tileentitydispenser, par9);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    protected void placeDoorAtCurrentPosition(World par1World, StructureBoundingBox par2StructureBoundingBox, Random par3Random, int par4, int par5, int par6, int par7)
    {
        int i1 = this.getXWithOffset(par4, par6);
        int j1 = this.getYWithOffset(par5);
        int k1 = this.getZWithOffset(par4, par6);

        if (par2StructureBoundingBox.isVecInside(i1, j1, k1))
        {
            ItemDoor.placeDoorBlock(par1World, i1, j1, k1, par7, Block.doorWood);
        }
    }
}
