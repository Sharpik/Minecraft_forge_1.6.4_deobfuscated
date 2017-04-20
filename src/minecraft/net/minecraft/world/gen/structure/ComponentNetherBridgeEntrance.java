package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class ComponentNetherBridgeEntrance extends ComponentNetherBridgePiece
{
    public ComponentNetherBridgeEntrance() {}

    public ComponentNetherBridgeEntrance(int par1, Random par2Random, StructureBoundingBox par3StructureBoundingBox, int par4)
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
        this.getNextComponentNormal((ComponentNetherBridgeStartPiece)par1StructureComponent, par2List, par3Random, 5, 3, true);
    }

    /**
     * Creates and returns a new component piece. Or null if it could not find enough room to place it.
     */
    public static ComponentNetherBridgeEntrance createValidComponent(List par0List, Random par1Random, int par2, int par3, int par4, int par5, int par6)
    {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(par2, par3, par4, -5, -3, 0, 13, 14, 13, par5);
        return isAboveGround(structureboundingbox) && StructureComponent.findIntersecting(par0List, structureboundingbox) == null ? new ComponentNetherBridgeEntrance(par6, par1Random, structureboundingbox, par5) : null;
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox)
    {
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 3, 0, 12, 4, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 5, 0, 12, 13, 12, 0, 0, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 5, 0, 1, 12, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 11, 5, 0, 12, 12, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 2, 5, 11, 4, 12, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 8, 5, 11, 10, 12, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 5, 9, 11, 7, 12, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 2, 5, 0, 4, 12, 1, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 8, 5, 0, 10, 12, 1, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 5, 9, 0, 7, 12, 1, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 2, 11, 2, 10, 12, 10, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 5, 8, 0, 7, 8, 0, Block.netherFence.blockID, Block.netherFence.blockID, false);
        int i;

        for (i = 1; i <= 11; i += 2)
        {
            this.fillWithBlocks(par1World, par3StructureBoundingBox, i, 10, 0, i, 11, 0, Block.netherFence.blockID, Block.netherFence.blockID, false);
            this.fillWithBlocks(par1World, par3StructureBoundingBox, i, 10, 12, i, 11, 12, Block.netherFence.blockID, Block.netherFence.blockID, false);
            this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 10, i, 0, 11, i, Block.netherFence.blockID, Block.netherFence.blockID, false);
            this.fillWithBlocks(par1World, par3StructureBoundingBox, 12, 10, i, 12, 11, i, Block.netherFence.blockID, Block.netherFence.blockID, false);
            this.placeBlockAtCurrentPosition(par1World, Block.netherBrick.blockID, 0, i, 13, 0, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.netherBrick.blockID, 0, i, 13, 12, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.netherBrick.blockID, 0, 0, 13, i, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.netherBrick.blockID, 0, 12, 13, i, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.netherFence.blockID, 0, i + 1, 13, 0, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.netherFence.blockID, 0, i + 1, 13, 12, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.netherFence.blockID, 0, 0, 13, i + 1, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.netherFence.blockID, 0, 12, 13, i + 1, par3StructureBoundingBox);
        }

        this.placeBlockAtCurrentPosition(par1World, Block.netherFence.blockID, 0, 0, 13, 0, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.netherFence.blockID, 0, 0, 13, 12, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.netherFence.blockID, 0, 0, 13, 0, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.netherFence.blockID, 0, 12, 13, 0, par3StructureBoundingBox);

        for (i = 3; i <= 9; i += 2)
        {
            this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 7, i, 1, 8, i, Block.netherFence.blockID, Block.netherFence.blockID, false);
            this.fillWithBlocks(par1World, par3StructureBoundingBox, 11, 7, i, 11, 8, i, Block.netherFence.blockID, Block.netherFence.blockID, false);
        }

        this.fillWithBlocks(par1World, par3StructureBoundingBox, 4, 2, 0, 8, 2, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 2, 4, 12, 2, 8, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 4, 0, 0, 8, 1, 3, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 4, 0, 9, 8, 1, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 0, 4, 3, 1, 8, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 9, 0, 4, 12, 1, 8, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        int j;

        for (i = 4; i <= 8; ++i)
        {
            for (j = 0; j <= 2; ++j)
            {
                this.fillCurrentPositionBlocksDownwards(par1World, Block.netherBrick.blockID, 0, i, -1, j, par3StructureBoundingBox);
                this.fillCurrentPositionBlocksDownwards(par1World, Block.netherBrick.blockID, 0, i, -1, 12 - j, par3StructureBoundingBox);
            }
        }

        for (i = 0; i <= 2; ++i)
        {
            for (j = 4; j <= 8; ++j)
            {
                this.fillCurrentPositionBlocksDownwards(par1World, Block.netherBrick.blockID, 0, i, -1, j, par3StructureBoundingBox);
                this.fillCurrentPositionBlocksDownwards(par1World, Block.netherBrick.blockID, 0, 12 - i, -1, j, par3StructureBoundingBox);
            }
        }

        this.fillWithBlocks(par1World, par3StructureBoundingBox, 5, 5, 5, 7, 5, 7, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 6, 1, 6, 6, 4, 6, 0, 0, false);
        this.placeBlockAtCurrentPosition(par1World, Block.netherBrick.blockID, 0, 6, 0, 6, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.lavaMoving.blockID, 0, 6, 5, 6, par3StructureBoundingBox);
        i = this.getXWithOffset(6, 6);
        j = this.getYWithOffset(5);
        int k = this.getZWithOffset(6, 6);

        if (par3StructureBoundingBox.isVecInside(i, j, k))
        {
            par1World.scheduledUpdatesAreImmediate = true;
            Block.blocksList[Block.lavaMoving.blockID].updateTick(par1World, i, j, k, par2Random);
            par1World.scheduledUpdatesAreImmediate = false;
        }

        return true;
    }
}
