package net.minecraft.world.chunk.storage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.NibbleArray;

public class ChunkLoader
{
    public static AnvilConverterData load(NBTTagCompound par0NBTTagCompound)
    {
        int i = par0NBTTagCompound.getInteger("xPos");
        int j = par0NBTTagCompound.getInteger("zPos");
        AnvilConverterData anvilconverterdata = new AnvilConverterData(i, j);
        anvilconverterdata.blocks = par0NBTTagCompound.getByteArray("Blocks");
        anvilconverterdata.data = new NibbleArrayReader(par0NBTTagCompound.getByteArray("Data"), 7);
        anvilconverterdata.skyLight = new NibbleArrayReader(par0NBTTagCompound.getByteArray("SkyLight"), 7);
        anvilconverterdata.blockLight = new NibbleArrayReader(par0NBTTagCompound.getByteArray("BlockLight"), 7);
        anvilconverterdata.heightmap = par0NBTTagCompound.getByteArray("HeightMap");
        anvilconverterdata.terrainPopulated = par0NBTTagCompound.getBoolean("TerrainPopulated");
        anvilconverterdata.entities = par0NBTTagCompound.getTagList("Entities");
        anvilconverterdata.tileEntities = par0NBTTagCompound.getTagList("TileEntities");
        anvilconverterdata.tileTicks = par0NBTTagCompound.getTagList("TileTicks");

        try
        {
            anvilconverterdata.lastUpdated = par0NBTTagCompound.getLong("LastUpdate");
        }
        catch (ClassCastException classcastexception)
        {
            anvilconverterdata.lastUpdated = (long)par0NBTTagCompound.getInteger("LastUpdate");
        }

        return anvilconverterdata;
    }

    public static void convertToAnvilFormat(AnvilConverterData par0AnvilConverterData, NBTTagCompound par1NBTTagCompound, WorldChunkManager par2WorldChunkManager)
    {
        par1NBTTagCompound.setInteger("xPos", par0AnvilConverterData.x);
        par1NBTTagCompound.setInteger("zPos", par0AnvilConverterData.z);
        par1NBTTagCompound.setLong("LastUpdate", par0AnvilConverterData.lastUpdated);
        int[] aint = new int[par0AnvilConverterData.heightmap.length];

        for (int i = 0; i < par0AnvilConverterData.heightmap.length; ++i)
        {
            aint[i] = par0AnvilConverterData.heightmap[i];
        }

        par1NBTTagCompound.setIntArray("HeightMap", aint);
        par1NBTTagCompound.setBoolean("TerrainPopulated", par0AnvilConverterData.terrainPopulated);
        NBTTagList nbttaglist = new NBTTagList("Sections");
        int j;

        for (int k = 0; k < 8; ++k)
        {
            boolean flag = true;

            for (j = 0; j < 16 && flag; ++j)
            {
                int l = 0;

                while (l < 16 && flag)
                {
                    int i1 = 0;

                    while (true)
                    {
                        if (i1 < 16)
                        {
                            int j1 = j << 11 | i1 << 7 | l + (k << 4);
                            byte b0 = par0AnvilConverterData.blocks[j1];

                            if (b0 == 0)
                            {
                                ++i1;
                                continue;
                            }

                            flag = false;
                        }

                        ++l;
                        break;
                    }
                }
            }

            if (!flag)
            {
                byte[] abyte = new byte[4096];
                NibbleArray nibblearray = new NibbleArray(abyte.length, 4);
                NibbleArray nibblearray1 = new NibbleArray(abyte.length, 4);
                NibbleArray nibblearray2 = new NibbleArray(abyte.length, 4);

                for (int k1 = 0; k1 < 16; ++k1)
                {
                    for (int l1 = 0; l1 < 16; ++l1)
                    {
                        for (int i2 = 0; i2 < 16; ++i2)
                        {
                            int j2 = k1 << 11 | i2 << 7 | l1 + (k << 4);
                            byte b1 = par0AnvilConverterData.blocks[j2];
                            abyte[l1 << 8 | i2 << 4 | k1] = (byte)(b1 & 255);
                            nibblearray.set(k1, l1, i2, par0AnvilConverterData.data.get(k1, l1 + (k << 4), i2));
                            nibblearray1.set(k1, l1, i2, par0AnvilConverterData.skyLight.get(k1, l1 + (k << 4), i2));
                            nibblearray2.set(k1, l1, i2, par0AnvilConverterData.blockLight.get(k1, l1 + (k << 4), i2));
                        }
                    }
                }

                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Y", (byte)(k & 255));
                nbttagcompound1.setByteArray("Blocks", abyte);
                nbttagcompound1.setByteArray("Data", nibblearray.data);
                nbttagcompound1.setByteArray("SkyLight", nibblearray1.data);
                nbttagcompound1.setByteArray("BlockLight", nibblearray2.data);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        par1NBTTagCompound.setTag("Sections", nbttaglist);
        byte[] abyte1 = new byte[256];

        for (int k2 = 0; k2 < 16; ++k2)
        {
            for (j = 0; j < 16; ++j)
            {
                abyte1[j << 4 | k2] = (byte)(par2WorldChunkManager.getBiomeGenAt(par0AnvilConverterData.x << 4 | k2, par0AnvilConverterData.z << 4 | j).biomeID & 255);
            }
        }

        par1NBTTagCompound.setByteArray("Biomes", abyte1);
        par1NBTTagCompound.setTag("Entities", par0AnvilConverterData.entities);
        par1NBTTagCompound.setTag("TileEntities", par0AnvilConverterData.tileEntities);

        if (par0AnvilConverterData.tileTicks != null)
        {
            par1NBTTagCompound.setTag("TileTicks", par0AnvilConverterData.tileTicks);
        }
    }
}
