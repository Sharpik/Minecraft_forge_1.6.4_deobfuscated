package net.minecraft.world.gen.structure;

import java.util.Random;
import net.minecraft.block.Block;

class StructureStrongholdStones extends StructurePieceBlockSelector
{
    private StructureStrongholdStones() {}

    /**
     * picks Block Ids and Metadata (Silverfish)
     */
    public void selectBlocks(Random par1Random, int par2, int par3, int par4, boolean par5)
    {
        if (par5)
        {
            this.selectedBlockId = Block.stoneBrick.blockID;
            float f = par1Random.nextFloat();

            if (f < 0.2F)
            {
                this.selectedBlockMetaData = 2;
            }
            else if (f < 0.5F)
            {
                this.selectedBlockMetaData = 1;
            }
            else if (f < 0.55F)
            {
                this.selectedBlockId = Block.silverfish.blockID;
                this.selectedBlockMetaData = 2;
            }
            else
            {
                this.selectedBlockMetaData = 0;
            }
        }
        else
        {
            this.selectedBlockId = 0;
            this.selectedBlockMetaData = 0;
        }
    }

    StructureStrongholdStones(StructureStrongholdPieceWeight2 par1StructureStrongholdPieceWeight2)
    {
        this();
    }
}
