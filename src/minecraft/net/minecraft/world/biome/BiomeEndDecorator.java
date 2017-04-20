package net.minecraft.world.biome;

import net.minecraft.block.Block;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.world.gen.feature.WorldGenSpikes;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeEndDecorator extends BiomeDecorator
{
    protected WorldGenerator spikeGen;

    public BiomeEndDecorator(BiomeGenBase par1BiomeGenBase)
    {
        super(par1BiomeGenBase);
        this.spikeGen = new WorldGenSpikes(Block.whiteStone.blockID);
    }

    /**
     * The method that does the work of actually decorating chunks
     */
    protected void decorate()
    {
        this.generateOres();

        if (this.randomGenerator.nextInt(5) == 0)
        {
            int i = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
            int j = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
            int k = this.currentWorld.getTopSolidOrLiquidBlock(i, j);
            this.spikeGen.generate(this.currentWorld, this.randomGenerator, i, k, j);
        }

        if (this.chunk_X == 0 && this.chunk_Z == 0)
        {
            EntityDragon entitydragon = new EntityDragon(this.currentWorld);
            entitydragon.setLocationAndAngles(0.0D, 128.0D, 0.0D, this.randomGenerator.nextFloat() * 360.0F, 0.0F);
            this.currentWorld.spawnEntityInWorld(entitydragon);
        }
    }
}
