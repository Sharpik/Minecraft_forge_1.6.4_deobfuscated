package net.minecraft.world.gen.structure;

import java.util.Random;

public abstract class StructurePieceBlockSelector
{
    protected int selectedBlockId;
    protected int selectedBlockMetaData;

    /**
     * picks Block Ids and Metadata (Silverfish)
     */
    public abstract void selectBlocks(Random random, int i, int j, int k, boolean flag);

    public int getSelectedBlockId()
    {
        return this.selectedBlockId;
    }

    public int getSelectedBlockMetaData()
    {
        return this.selectedBlockMetaData;
    }
}
