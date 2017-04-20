package net.minecraft.world.gen.structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.biome.BiomeGenBase;

public class MapGenStronghold extends MapGenStructure
{
    public static ArrayList<BiomeGenBase> allowedBiomes = new ArrayList<BiomeGenBase>(Arrays.asList(BiomeGenBase.desert, BiomeGenBase.forest, BiomeGenBase.extremeHills, BiomeGenBase.swampland, BiomeGenBase.taiga, BiomeGenBase.icePlains, BiomeGenBase.iceMountains, BiomeGenBase.desertHills, BiomeGenBase.forestHills, BiomeGenBase.extremeHillsEdge, BiomeGenBase.jungle, BiomeGenBase.jungleHills)); 
    private BiomeGenBase[] allowedBiomeGenBases;

    /**
     * is spawned false and set true once the defined BiomeGenBases were compared with the present ones
     */
    private boolean ranBiomeCheck;
    private ChunkCoordIntPair[] structureCoords;
    private double field_82671_h;
    private int field_82672_i;

    public MapGenStronghold()
    {
        this.allowedBiomeGenBases = allowedBiomes.toArray(new BiomeGenBase[0]);
        this.structureCoords = new ChunkCoordIntPair[3];
        this.field_82671_h = 32.0D;
        this.field_82672_i = 3;
    }

    public MapGenStronghold(Map par1Map)
    {
        this.allowedBiomeGenBases = allowedBiomes.toArray(new BiomeGenBase[0]);
        this.structureCoords = new ChunkCoordIntPair[3];
        this.field_82671_h = 32.0D;
        this.field_82672_i = 3;
        Iterator iterator = par1Map.entrySet().iterator();

        while (iterator.hasNext())
        {
            Entry entry = (Entry)iterator.next();

            if (((String)entry.getKey()).equals("distance"))
            {
                this.field_82671_h = MathHelper.func_82713_a((String)entry.getValue(), this.field_82671_h, 1.0D);
            }
            else if (((String)entry.getKey()).equals("count"))
            {
                this.structureCoords = new ChunkCoordIntPair[MathHelper.parseIntWithDefaultAndMax((String)entry.getValue(), this.structureCoords.length, 1)];
            }
            else if (((String)entry.getKey()).equals("spread"))
            {
                this.field_82672_i = MathHelper.parseIntWithDefaultAndMax((String)entry.getValue(), this.field_82672_i, 1);
            }
        }
    }

    public String func_143025_a()
    {
        return "Stronghold";
    }

    protected boolean canSpawnStructureAtCoords(int par1, int par2)
    {
        if (!this.ranBiomeCheck)
        {
            Random random = new Random();
            random.setSeed(this.worldObj.getSeed());
            double d0 = random.nextDouble() * Math.PI * 2.0D;
            int k = 1;

            for (int l = 0; l < this.structureCoords.length; ++l)
            {
                double d1 = (1.25D * (double)k + random.nextDouble()) * this.field_82671_h * (double)k;
                int i1 = (int)Math.round(Math.cos(d0) * d1);
                int j1 = (int)Math.round(Math.sin(d0) * d1);
                ArrayList arraylist = new ArrayList();
                Collections.addAll(arraylist, this.allowedBiomeGenBases);
                ChunkPosition chunkposition = this.worldObj.getWorldChunkManager().findBiomePosition((i1 << 4) + 8, (j1 << 4) + 8, 112, arraylist, random);

                if (chunkposition != null)
                {
                    i1 = chunkposition.x >> 4;
                    j1 = chunkposition.z >> 4;
                }

                this.structureCoords[l] = new ChunkCoordIntPair(i1, j1);
                d0 += (Math.PI * 2D) * (double)k / (double)this.field_82672_i;

                if (l == this.field_82672_i)
                {
                    k += 2 + random.nextInt(5);
                    this.field_82672_i += 1 + random.nextInt(2);
                }
            }

            this.ranBiomeCheck = true;
        }

        ChunkCoordIntPair[] achunkcoordintpair = this.structureCoords;
        int k1 = achunkcoordintpair.length;

        for (int l1 = 0; l1 < k1; ++l1)
        {
            ChunkCoordIntPair chunkcoordintpair = achunkcoordintpair[l1];

            if (par1 == chunkcoordintpair.chunkXPos && par2 == chunkcoordintpair.chunkZPos)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns a list of other locations at which the structure generation has been run, or null if not relevant to this
     * structure generator.
     */
    protected List getCoordList()
    {
        ArrayList arraylist = new ArrayList();
        ChunkCoordIntPair[] achunkcoordintpair = this.structureCoords;
        int i = achunkcoordintpair.length;

        for (int j = 0; j < i; ++j)
        {
            ChunkCoordIntPair chunkcoordintpair = achunkcoordintpair[j];

            if (chunkcoordintpair != null)
            {
                arraylist.add(chunkcoordintpair.getChunkPosition(64));
            }
        }

        return arraylist;
    }

    protected StructureStart getStructureStart(int par1, int par2)
    {
        StructureStrongholdStart structurestrongholdstart;

        for (structurestrongholdstart = new StructureStrongholdStart(this.worldObj, this.rand, par1, par2); structurestrongholdstart.getComponents().isEmpty() || ((ComponentStrongholdStairs2)structurestrongholdstart.getComponents().get(0)).strongholdPortalRoom == null; structurestrongholdstart = new StructureStrongholdStart(this.worldObj, this.rand, par1, par2))
        {
            ;
        }

        return structurestrongholdstart;
    }
}
