package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import net.minecraftforge.common.*;
import net.minecraftforge.event.Event.*;
import net.minecraftforge.event.terraingen.*;

public abstract class ComponentVillage extends StructureComponent
{
    protected int field_143015_k = -1;

    /** The number of villagers that have been spawned in this component. */
    private int villagersSpawned;
    private boolean field_143014_b;
    private ComponentVillageStartPiece startPiece;

    public ComponentVillage() {}

    protected ComponentVillage(ComponentVillageStartPiece par1ComponentVillageStartPiece, int par2)
    {
        super(par2);

        if (par1ComponentVillageStartPiece != null)
        {
            this.field_143014_b = par1ComponentVillageStartPiece.inDesert;
            startPiece = par1ComponentVillageStartPiece;
        }
    }

    protected void func_143012_a(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setInteger("HPos", this.field_143015_k);
        par1NBTTagCompound.setInteger("VCount", this.villagersSpawned);
        par1NBTTagCompound.setBoolean("Desert", this.field_143014_b);
    }

    protected void func_143011_b(NBTTagCompound par1NBTTagCompound)
    {
        this.field_143015_k = par1NBTTagCompound.getInteger("HPos");
        this.villagersSpawned = par1NBTTagCompound.getInteger("VCount");
        this.field_143014_b = par1NBTTagCompound.getBoolean("Desert");
    }

    /**
     * Gets the next village component, with the bounding box shifted -1 in the X and Z direction.
     */
    protected StructureComponent getNextComponentNN(ComponentVillageStartPiece par1ComponentVillageStartPiece, List par2List, Random par3Random, int par4, int par5)
    {
        switch (this.coordBaseMode)
        {
            case 0:
                return StructureVillagePieces.getNextStructureComponent(par1ComponentVillageStartPiece, par2List, par3Random, this.boundingBox.minX - 1, this.boundingBox.minY + par4, this.boundingBox.minZ + par5, 1, this.getComponentType());
            case 1:
                return StructureVillagePieces.getNextStructureComponent(par1ComponentVillageStartPiece, par2List, par3Random, this.boundingBox.minX + par5, this.boundingBox.minY + par4, this.boundingBox.minZ - 1, 2, this.getComponentType());
            case 2:
                return StructureVillagePieces.getNextStructureComponent(par1ComponentVillageStartPiece, par2List, par3Random, this.boundingBox.minX - 1, this.boundingBox.minY + par4, this.boundingBox.minZ + par5, 1, this.getComponentType());
            case 3:
                return StructureVillagePieces.getNextStructureComponent(par1ComponentVillageStartPiece, par2List, par3Random, this.boundingBox.minX + par5, this.boundingBox.minY + par4, this.boundingBox.minZ - 1, 2, this.getComponentType());
            default:
                return null;
        }
    }

    /**
     * Gets the next village component, with the bounding box shifted +1 in the X and Z direction.
     */
    protected StructureComponent getNextComponentPP(ComponentVillageStartPiece par1ComponentVillageStartPiece, List par2List, Random par3Random, int par4, int par5)
    {
        switch (this.coordBaseMode)
        {
            case 0:
                return StructureVillagePieces.getNextStructureComponent(par1ComponentVillageStartPiece, par2List, par3Random, this.boundingBox.maxX + 1, this.boundingBox.minY + par4, this.boundingBox.minZ + par5, 3, this.getComponentType());
            case 1:
                return StructureVillagePieces.getNextStructureComponent(par1ComponentVillageStartPiece, par2List, par3Random, this.boundingBox.minX + par5, this.boundingBox.minY + par4, this.boundingBox.maxZ + 1, 0, this.getComponentType());
            case 2:
                return StructureVillagePieces.getNextStructureComponent(par1ComponentVillageStartPiece, par2List, par3Random, this.boundingBox.maxX + 1, this.boundingBox.minY + par4, this.boundingBox.minZ + par5, 3, this.getComponentType());
            case 3:
                return StructureVillagePieces.getNextStructureComponent(par1ComponentVillageStartPiece, par2List, par3Random, this.boundingBox.minX + par5, this.boundingBox.minY + par4, this.boundingBox.maxZ + 1, 0, this.getComponentType());
            default:
                return null;
        }
    }

    /**
     * Discover the y coordinate that will serve as the ground level of the supplied BoundingBox. (A median of all the
     * levels in the BB's horizontal rectangle).
     */
    protected int getAverageGroundLevel(World par1World, StructureBoundingBox par2StructureBoundingBox)
    {
        int i = 0;
        int j = 0;

        for (int k = this.boundingBox.minZ; k <= this.boundingBox.maxZ; ++k)
        {
            for (int l = this.boundingBox.minX; l <= this.boundingBox.maxX; ++l)
            {
                if (par2StructureBoundingBox.isVecInside(l, 64, k))
                {
                    i += Math.max(par1World.getTopSolidOrLiquidBlock(l, k), par1World.provider.getAverageGroundLevel());
                    ++j;
                }
            }
        }

        if (j == 0)
        {
            return -1;
        }
        else
        {
            return i / j;
        }
    }

    protected static boolean canVillageGoDeeper(StructureBoundingBox par0StructureBoundingBox)
    {
        return par0StructureBoundingBox != null && par0StructureBoundingBox.minY > 10;
    }

    /**
     * Spawns a number of villagers in this component. Parameters: world, component bounding box, x offset, y offset, z
     * offset, number of villagers
     */
    protected void spawnVillagers(World par1World, StructureBoundingBox par2StructureBoundingBox, int par3, int par4, int par5, int par6)
    {
        if (this.villagersSpawned < par6)
        {
            for (int i1 = this.villagersSpawned; i1 < par6; ++i1)
            {
                int j1 = this.getXWithOffset(par3 + i1, par5);
                int k1 = this.getYWithOffset(par4);
                int l1 = this.getZWithOffset(par3 + i1, par5);

                if (!par2StructureBoundingBox.isVecInside(j1, k1, l1))
                {
                    break;
                }

                ++this.villagersSpawned;
                EntityVillager entityvillager = new EntityVillager(par1World, this.getVillagerType(i1));
                entityvillager.setLocationAndAngles((double)j1 + 0.5D, (double)k1, (double)l1 + 0.5D, 0.0F, 0.0F);
                par1World.spawnEntityInWorld(entityvillager);
            }
        }
    }

    /**
     * Returns the villager type to spawn in this component, based on the number of villagers already spawned.
     */
    protected int getVillagerType(int par1)
    {
        return 0;
    }

    /**
     * Gets the replacement block for the current biome
     */
    protected int getBiomeSpecificBlock(int par1, int par2)
    {
        BiomeEvent.GetVillageBlockID event = new BiomeEvent.GetVillageBlockID(startPiece == null ? null : startPiece.biome, par1, par2);
        MinecraftForge.TERRAIN_GEN_BUS.post(event);
        if (event.getResult() == Result.DENY) return event.replacement;

        if (this.field_143014_b)
        {
            if (par1 == Block.wood.blockID)
            {
                return Block.sandStone.blockID;
            }

            if (par1 == Block.cobblestone.blockID)
            {
                return Block.sandStone.blockID;
            }

            if (par1 == Block.planks.blockID)
            {
                return Block.sandStone.blockID;
            }

            if (par1 == Block.stairsWoodOak.blockID)
            {
                return Block.stairsSandStone.blockID;
            }

            if (par1 == Block.stairsCobblestone.blockID)
            {
                return Block.stairsSandStone.blockID;
            }

            if (par1 == Block.gravel.blockID)
            {
                return Block.sandStone.blockID;
            }
        }

        return par1;
    }

    /**
     * Gets the replacement block metadata for the current biome
     */
    protected int getBiomeSpecificBlockMetadata(int par1, int par2)
    {
        BiomeEvent.GetVillageBlockMeta event = new BiomeEvent.GetVillageBlockMeta(startPiece == null ? null : startPiece.biome, par1, par2);
        MinecraftForge.TERRAIN_GEN_BUS.post(event);
        if (event.getResult() == Result.DENY) return event.replacement;

        if (this.field_143014_b)
        {
            if (par1 == Block.wood.blockID)
            {
                return 0;
            }

            if (par1 == Block.cobblestone.blockID)
            {
                return 0;
            }

            if (par1 == Block.planks.blockID)
            {
                return 2;
            }
        }

        return par2;
    }

    /**
     * current Position depends on currently set Coordinates mode, is computed here
     */
    protected void placeBlockAtCurrentPosition(World par1World, int par2, int par3, int par4, int par5, int par6, StructureBoundingBox par7StructureBoundingBox)
    {
        int j1 = this.getBiomeSpecificBlock(par2, par3);
        int k1 = this.getBiomeSpecificBlockMetadata(par2, par3);
        super.placeBlockAtCurrentPosition(par1World, j1, k1, par4, par5, par6, par7StructureBoundingBox);
    }

    /**
     * arguments: (World worldObj, StructureBoundingBox structBB, int minX, int minY, int minZ, int maxX, int maxY, int
     * maxZ, int placeBlockId, int replaceBlockId, boolean alwaysreplace)
     */
    protected void fillWithBlocks(World par1World, StructureBoundingBox par2StructureBoundingBox, int par3, int par4, int par5, int par6, int par7, int par8, int par9, int par10, boolean par11)
    {
        int i2 = this.getBiomeSpecificBlock(par9, 0);
        int j2 = this.getBiomeSpecificBlockMetadata(par9, 0);
        int k2 = this.getBiomeSpecificBlock(par10, 0);
        int l2 = this.getBiomeSpecificBlockMetadata(par10, 0);
        super.fillWithMetadataBlocks(par1World, par2StructureBoundingBox, par3, par4, par5, par6, par7, par8, i2, j2, k2, l2, par11);
    }

    /**
     * Overwrites air and liquids from selected position downwards, stops at hitting anything else.
     */
    protected void fillCurrentPositionBlocksDownwards(World par1World, int par2, int par3, int par4, int par5, int par6, StructureBoundingBox par7StructureBoundingBox)
    {
        int j1 = this.getBiomeSpecificBlock(par2, par3);
        int k1 = this.getBiomeSpecificBlockMetadata(par2, par3);
        super.fillCurrentPositionBlocksDownwards(par1World, j1, k1, par4, par5, par6, par7StructureBoundingBox);
    }
}
