package net.minecraft.world.gen.structure;

import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class StructureScatteredFeatureStart extends StructureStart
{
    public StructureScatteredFeatureStart() {}

    public StructureScatteredFeatureStart(World par1World, Random par2Random, int par3, int par4)
    {
        super(par3, par4);
        BiomeGenBase biomegenbase = par1World.getBiomeGenForCoords(par3 * 16 + 8, par4 * 16 + 8);

        if (biomegenbase != BiomeGenBase.jungle && biomegenbase != BiomeGenBase.jungleHills)
        {
            if (biomegenbase == BiomeGenBase.swampland)
            {
                ComponentScatteredFeatureSwampHut componentscatteredfeatureswamphut = new ComponentScatteredFeatureSwampHut(par2Random, par3 * 16, par4 * 16);
                this.components.add(componentscatteredfeatureswamphut);
            }
            else
            {
                ComponentScatteredFeatureDesertPyramid componentscatteredfeaturedesertpyramid = new ComponentScatteredFeatureDesertPyramid(par2Random, par3 * 16, par4 * 16);
                this.components.add(componentscatteredfeaturedesertpyramid);
            }
        }
        else
        {
            ComponentScatteredFeatureJunglePyramid componentscatteredfeaturejunglepyramid = new ComponentScatteredFeatureJunglePyramid(par2Random, par3 * 16, par4 * 16);
            this.components.add(componentscatteredfeaturejunglepyramid);
        }

        this.updateBoundingBox();
    }
}
