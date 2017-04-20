package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class ComponentNetherBridgeCorridor3 extends ComponentNetherBridgePiece
{
    public ComponentNetherBridgeCorridor3() {}

    public ComponentNetherBridgeCorridor3(int par1, Random par2Random, StructureBoundingBox par3StructureBoundingBox, int par4)
    {
        super(par1);
        this.coordBaseMode = par4;
        this.boundingBox = par3StructureBoundingBox;
    }

    /**
     * Initiates construction of the Structure Component picked, at the current Location of StructGen
     */
    public void buildComponent(StructureComponent par1StructureComponent, List par2List, Random par3Random)
    {
        this.getNextComponentNormal((ComponentNetherBridgeStartPiece)par1StructureComponent, par2List, par3Random, 1, 0, true);
    }

    /**
     * Creates and returns a new component piece. Or null if it could not find enough room to place it.
     */
    public static ComponentNetherBridgeCorridor3 createValidComponent(List par0List, Random par1Random, int par2, int par3, int par4, int par5, int par6)
    {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(par2, par3, par4, -1, -7, 0, 5, 14, 10, par5);
        return isAboveGround(structureboundingbox) && StructureComponent.findIntersecting(par0List, structureboundingbox) == null ? new ComponentNetherBridgeCorridor3(par6, par1Random, structureboundingbox, par5) : null;
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox)
    {
        int i = this.getMetadataWithOffset(Block.stairsNetherBrick.blockID, 2);

        for (int j = 0; j <= 9; ++j)
        {
            int k = Math.max(1, 7 - j);
            int l = Math.min(Math.max(k + 5, 14 - j), 13);
            int i1 = j;
            this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 0, j, 4, k, j, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
            this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, k + 1, j, 3, l - 1, j, 0, 0, false);

            if (j <= 6)
            {
                this.placeBlockAtCurrentPosition(par1World, Block.stairsNetherBrick.blockID, i, 1, k + 1, j, par3StructureBoundingBox);
                this.placeBlockAtCurrentPosition(par1World, Block.stairsNetherBrick.blockID, i, 2, k + 1, j, par3StructureBoundingBox);
                this.placeBlockAtCurrentPosition(par1World, Block.stairsNetherBrick.blockID, i, 3, k + 1, j, par3StructureBoundingBox);
            }

            this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, l, j, 4, l, j, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
            this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, k + 1, j, 0, l - 1, j, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
            this.fillWithBlocks(par1World, par3StructureBoundingBox, 4, k + 1, j, 4, l - 1, j, Block.netherBrick.blockID, Block.netherBrick.blockID, false);

            if ((j & 1) == 0)
            {
                this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, k + 2, j, 0, k + 3, j, Block.netherFence.blockID, Block.netherFence.blockID, false);
                this.fillWithBlocks(par1World, par3StructureBoundingBox, 4, k + 2, j, 4, k + 3, j, Block.netherFence.blockID, Block.netherFence.blockID, false);
            }

            for (int j1 = 0; j1 <= 4; ++j1)
            {
                this.fillCurrentPositionBlocksDownwards(par1World, Block.netherBrick.blockID, 0, j1, -1, i1, par3StructureBoundingBox);
            }
        }

        return true;
    }
}
