package net.minecraft.world.gen.structure;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingData;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ComponentScatteredFeatureSwampHut extends ComponentScatteredFeature
{
    /** Whether this swamp hut has a witch. */
    private boolean hasWitch;

    public ComponentScatteredFeatureSwampHut() {}

    public ComponentScatteredFeatureSwampHut(Random par1Random, int par2, int par3)
    {
        super(par1Random, par2, 64, par3, 7, 5, 9);
    }

    protected void func_143012_a(NBTTagCompound par1NBTTagCompound)
    {
        super.func_143012_a(par1NBTTagCompound);
        par1NBTTagCompound.setBoolean("Witch", this.hasWitch);
    }

    protected void func_143011_b(NBTTagCompound par1NBTTagCompound)
    {
        super.func_143011_b(par1NBTTagCompound);
        this.hasWitch = par1NBTTagCompound.getBoolean("Witch");
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox)
    {
        if (!this.func_74935_a(par1World, par3StructureBoundingBox, 0))
        {
            return false;
        }
        else
        {
            this.fillWithMetadataBlocks(par1World, par3StructureBoundingBox, 1, 1, 1, 5, 1, 7, Block.planks.blockID, 1, Block.planks.blockID, 1, false);
            this.fillWithMetadataBlocks(par1World, par3StructureBoundingBox, 1, 4, 2, 5, 4, 7, Block.planks.blockID, 1, Block.planks.blockID, 1, false);
            this.fillWithMetadataBlocks(par1World, par3StructureBoundingBox, 2, 1, 0, 4, 1, 0, Block.planks.blockID, 1, Block.planks.blockID, 1, false);
            this.fillWithMetadataBlocks(par1World, par3StructureBoundingBox, 2, 2, 2, 3, 3, 2, Block.planks.blockID, 1, Block.planks.blockID, 1, false);
            this.fillWithMetadataBlocks(par1World, par3StructureBoundingBox, 1, 2, 3, 1, 3, 6, Block.planks.blockID, 1, Block.planks.blockID, 1, false);
            this.fillWithMetadataBlocks(par1World, par3StructureBoundingBox, 5, 2, 3, 5, 3, 6, Block.planks.blockID, 1, Block.planks.blockID, 1, false);
            this.fillWithMetadataBlocks(par1World, par3StructureBoundingBox, 2, 2, 7, 4, 3, 7, Block.planks.blockID, 1, Block.planks.blockID, 1, false);
            this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 2, 1, 3, 2, Block.wood.blockID, Block.wood.blockID, false);
            this.fillWithBlocks(par1World, par3StructureBoundingBox, 5, 0, 2, 5, 3, 2, Block.wood.blockID, Block.wood.blockID, false);
            this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 7, 1, 3, 7, Block.wood.blockID, Block.wood.blockID, false);
            this.fillWithBlocks(par1World, par3StructureBoundingBox, 5, 0, 7, 5, 3, 7, Block.wood.blockID, Block.wood.blockID, false);
            this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 2, 3, 2, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 3, 3, 7, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, 0, 0, 1, 3, 4, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, 0, 0, 5, 3, 4, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, 0, 0, 5, 3, 5, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.flowerPot.blockID, 7, 1, 3, 5, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.workbench.blockID, 0, 3, 2, 6, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.cauldron.blockID, 0, 4, 2, 6, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 1, 2, 1, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 5, 2, 1, par3StructureBoundingBox);
            int i = this.getMetadataWithOffset(Block.stairsWoodOak.blockID, 3);
            int j = this.getMetadataWithOffset(Block.stairsWoodOak.blockID, 1);
            int k = this.getMetadataWithOffset(Block.stairsWoodOak.blockID, 0);
            int l = this.getMetadataWithOffset(Block.stairsWoodOak.blockID, 2);
            this.fillWithMetadataBlocks(par1World, par3StructureBoundingBox, 0, 4, 1, 6, 4, 1, Block.stairsWoodSpruce.blockID, i, Block.stairsWoodSpruce.blockID, i, false);
            this.fillWithMetadataBlocks(par1World, par3StructureBoundingBox, 0, 4, 2, 0, 4, 7, Block.stairsWoodSpruce.blockID, k, Block.stairsWoodSpruce.blockID, k, false);
            this.fillWithMetadataBlocks(par1World, par3StructureBoundingBox, 6, 4, 2, 6, 4, 7, Block.stairsWoodSpruce.blockID, j, Block.stairsWoodSpruce.blockID, j, false);
            this.fillWithMetadataBlocks(par1World, par3StructureBoundingBox, 0, 4, 8, 6, 4, 8, Block.stairsWoodSpruce.blockID, l, Block.stairsWoodSpruce.blockID, l, false);
            int i1;
            int j1;

            for (i1 = 2; i1 <= 7; i1 += 5)
            {
                for (j1 = 1; j1 <= 5; j1 += 4)
                {
                    this.fillCurrentPositionBlocksDownwards(par1World, Block.wood.blockID, 0, j1, -1, i1, par3StructureBoundingBox);
                }
            }

            if (!this.hasWitch)
            {
                i1 = this.getXWithOffset(2, 5);
                j1 = this.getYWithOffset(2);
                int k1 = this.getZWithOffset(2, 5);

                if (par3StructureBoundingBox.isVecInside(i1, j1, k1))
                {
                    this.hasWitch = true;
                    EntityWitch entitywitch = new EntityWitch(par1World);
                    entitywitch.setLocationAndAngles((double)i1 + 0.5D, (double)j1, (double)k1 + 0.5D, 0.0F, 0.0F);
                    entitywitch.onSpawnWithEgg((EntityLivingData)null);
                    par1World.spawnEntityInWorld(entitywitch);
                }
            }

            return true;
        }
    }
}
