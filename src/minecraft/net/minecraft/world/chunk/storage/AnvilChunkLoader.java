package net.minecraft.world.chunk.storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import cpw.mods.fml.common.FMLLog;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.storage.IThreadedFileIO;
import net.minecraft.world.storage.ThreadedFileIOBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkDataEvent;

public class AnvilChunkLoader implements IChunkLoader, IThreadedFileIO
{
    private List chunksToRemove = new ArrayList();
    private Set pendingAnvilChunksCoordinates = new HashSet();
    private Object syncLockObject = new Object();

    /** Save directory for chunks using the Anvil format */
    public final File chunkSaveLocation;

    public AnvilChunkLoader(File par1File)
    {
        this.chunkSaveLocation = par1File;
    }

    /**
     * Loads the specified(XZ) chunk into the specified world.
     */
    public Chunk loadChunk(World par1World, int par2, int par3) throws IOException
    {
        NBTTagCompound nbttagcompound = null;
        ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(par2, par3);
        Object object = this.syncLockObject;

        synchronized (this.syncLockObject)
        {
            if (this.pendingAnvilChunksCoordinates.contains(chunkcoordintpair))
            {
                for (int k = 0; k < this.chunksToRemove.size(); ++k)
                {
                    if (((AnvilChunkLoaderPending)this.chunksToRemove.get(k)).chunkCoordinate.equals(chunkcoordintpair))
                    {
                        nbttagcompound = ((AnvilChunkLoaderPending)this.chunksToRemove.get(k)).nbtTags;
                        break;
                    }
                }
            }
        }

        if (nbttagcompound == null)
        {
            DataInputStream datainputstream = RegionFileCache.getChunkInputStream(this.chunkSaveLocation, par2, par3);

            if (datainputstream == null)
            {
                return null;
            }

            nbttagcompound = CompressedStreamTools.read(datainputstream);
        }

        return this.checkedReadChunkFromNBT(par1World, par2, par3, nbttagcompound);
    }

    /**
     * Wraps readChunkFromNBT. Checks the coordinates and several NBT tags.
     */
    protected Chunk checkedReadChunkFromNBT(World par1World, int par2, int par3, NBTTagCompound par4NBTTagCompound)
    {
        if (!par4NBTTagCompound.hasKey("Level"))
        {
            par1World.getWorldLogAgent().logSevere("Chunk file at " + par2 + "," + par3 + " is missing level data, skipping");
            return null;
        }
        else if (!par4NBTTagCompound.getCompoundTag("Level").hasKey("Sections"))
        {
            par1World.getWorldLogAgent().logSevere("Chunk file at " + par2 + "," + par3 + " is missing block data, skipping");
            return null;
        }
        else
        {
            Chunk chunk = this.readChunkFromNBT(par1World, par4NBTTagCompound.getCompoundTag("Level"));

            if (!chunk.isAtLocation(par2, par3))
            {
                par1World.getWorldLogAgent().logSevere("Chunk file at " + par2 + "," + par3 + " is in the wrong location; relocating. (Expected " + par2 + ", " + par3 + ", got " + chunk.xPosition + ", " + chunk.zPosition + ")");
                par4NBTTagCompound.setInteger("xPos", par2);
                par4NBTTagCompound.setInteger("zPos", par3);
                chunk = this.readChunkFromNBT(par1World, par4NBTTagCompound.getCompoundTag("Level"));
            }

            MinecraftForge.EVENT_BUS.post(new ChunkDataEvent.Load(chunk, par4NBTTagCompound));
            return chunk;
        }
    }

    public void saveChunk(World par1World, Chunk par2Chunk) throws MinecraftException, IOException
    {
        par1World.checkSessionLock();

        try
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound.setTag("Level", nbttagcompound1);
            this.writeChunkToNBT(par2Chunk, par1World, nbttagcompound1);
            MinecraftForge.EVENT_BUS.post(new ChunkDataEvent.Save(par2Chunk, nbttagcompound));
            this.addChunkToPending(par2Chunk.getChunkCoordIntPair(), nbttagcompound);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    protected void addChunkToPending(ChunkCoordIntPair par1ChunkCoordIntPair, NBTTagCompound par2NBTTagCompound)
    {
        Object object = this.syncLockObject;

        synchronized (this.syncLockObject)
        {
            if (this.pendingAnvilChunksCoordinates.contains(par1ChunkCoordIntPair))
            {
                for (int i = 0; i < this.chunksToRemove.size(); ++i)
                {
                    if (((AnvilChunkLoaderPending)this.chunksToRemove.get(i)).chunkCoordinate.equals(par1ChunkCoordIntPair))
                    {
                        this.chunksToRemove.set(i, new AnvilChunkLoaderPending(par1ChunkCoordIntPair, par2NBTTagCompound));
                        return;
                    }
                }
            }

            this.chunksToRemove.add(new AnvilChunkLoaderPending(par1ChunkCoordIntPair, par2NBTTagCompound));
            this.pendingAnvilChunksCoordinates.add(par1ChunkCoordIntPair);
            ThreadedFileIOBase.threadedIOInstance.queueIO(this);
        }
    }

    /**
     * Returns a boolean stating if the write was unsuccessful.
     */
    public boolean writeNextIO()
    {
        AnvilChunkLoaderPending anvilchunkloaderpending = null;
        Object object = this.syncLockObject;

        synchronized (this.syncLockObject)
        {
            if (this.chunksToRemove.isEmpty())
            {
                return false;
            }

            anvilchunkloaderpending = (AnvilChunkLoaderPending)this.chunksToRemove.remove(0);
            this.pendingAnvilChunksCoordinates.remove(anvilchunkloaderpending.chunkCoordinate);
        }

        if (anvilchunkloaderpending != null)
        {
            try
            {
                this.writeChunkNBTTags(anvilchunkloaderpending);
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }
        }

        return true;
    }

    private void writeChunkNBTTags(AnvilChunkLoaderPending par1AnvilChunkLoaderPending) throws IOException
    {
        DataOutputStream dataoutputstream = RegionFileCache.getChunkOutputStream(this.chunkSaveLocation, par1AnvilChunkLoaderPending.chunkCoordinate.chunkXPos, par1AnvilChunkLoaderPending.chunkCoordinate.chunkZPos);
        CompressedStreamTools.write(par1AnvilChunkLoaderPending.nbtTags, dataoutputstream);
        dataoutputstream.close();
    }

    /**
     * Save extra data associated with this Chunk not normally saved during autosave, only during chunk unload.
     * Currently unused.
     */
    public void saveExtraChunkData(World par1World, Chunk par2Chunk) {}

    /**
     * Called every World.tick()
     */
    public void chunkTick() {}

    /**
     * Save extra data not associated with any Chunk.  Not saved during autosave, only during world unload.  Currently
     * unused.
     */
    public void saveExtraData()
    {
        while (this.writeNextIO())
        {
            ;
        }
    }

    /**
     * Writes the Chunk passed as an argument to the NBTTagCompound also passed, using the World argument to retrieve
     * the Chunk's last update time.
     */
    private void writeChunkToNBT(Chunk par1Chunk, World par2World, NBTTagCompound par3NBTTagCompound)
    {
        par3NBTTagCompound.setInteger("xPos", par1Chunk.xPosition);
        par3NBTTagCompound.setInteger("zPos", par1Chunk.zPosition);
        par3NBTTagCompound.setLong("LastUpdate", par2World.getTotalWorldTime());
        par3NBTTagCompound.setIntArray("HeightMap", par1Chunk.heightMap);
        par3NBTTagCompound.setBoolean("TerrainPopulated", par1Chunk.isTerrainPopulated);
        par3NBTTagCompound.setLong("InhabitedTime", par1Chunk.inhabitedTime);
        ExtendedBlockStorage[] aextendedblockstorage = par1Chunk.getBlockStorageArray();
        NBTTagList nbttaglist = new NBTTagList("Sections");
        boolean flag = !par2World.provider.hasNoSky;
        ExtendedBlockStorage[] aextendedblockstorage1 = aextendedblockstorage;
        int i = aextendedblockstorage.length;
        NBTTagCompound nbttagcompound1;

        for (int j = 0; j < i; ++j)
        {
            ExtendedBlockStorage extendedblockstorage = aextendedblockstorage1[j];

            if (extendedblockstorage != null)
            {
                nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Y", (byte)(extendedblockstorage.getYLocation() >> 4 & 255));
                nbttagcompound1.setByteArray("Blocks", extendedblockstorage.getBlockLSBArray());

                if (extendedblockstorage.getBlockMSBArray() != null)
                {
                    nbttagcompound1.setByteArray("Add", extendedblockstorage.getBlockMSBArray().data);
                }

                nbttagcompound1.setByteArray("Data", extendedblockstorage.getMetadataArray().data);
                nbttagcompound1.setByteArray("BlockLight", extendedblockstorage.getBlocklightArray().data);

                if (flag)
                {
                    nbttagcompound1.setByteArray("SkyLight", extendedblockstorage.getSkylightArray().data);
                }
                else
                {
                    nbttagcompound1.setByteArray("SkyLight", new byte[extendedblockstorage.getBlocklightArray().data.length]);
                }

                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        par3NBTTagCompound.setTag("Sections", nbttaglist);
        par3NBTTagCompound.setByteArray("Biomes", par1Chunk.getBiomeArray());
        par1Chunk.hasEntities = false;
        NBTTagList nbttaglist1 = new NBTTagList();
        Iterator iterator;

        for (i = 0; i < par1Chunk.entityLists.length; ++i)
        {
            iterator = par1Chunk.entityLists[i].iterator();

            while (iterator.hasNext())
            {
                Entity entity = (Entity)iterator.next();
                nbttagcompound1 = new NBTTagCompound();

                try
                {
                    if (entity.writeToNBTOptional(nbttagcompound1))
                    {
                        par1Chunk.hasEntities = true;
                        nbttaglist1.appendTag(nbttagcompound1);
                    }
                }
                catch (Exception e)
                {
                    FMLLog.log(Level.SEVERE, e,
                            "An Entity type %s has thrown an exception trying to write state. It will not persist. Report this to the mod author",
                            entity.getClass().getName());
                }
            }
        }

        par3NBTTagCompound.setTag("Entities", nbttaglist1);
        NBTTagList nbttaglist2 = new NBTTagList();
        iterator = par1Chunk.chunkTileEntityMap.values().iterator();

        while (iterator.hasNext())
        {
            TileEntity tileentity = (TileEntity)iterator.next();
            nbttagcompound1 = new NBTTagCompound();
            try
            {
                tileentity.writeToNBT(nbttagcompound1);
                nbttaglist2.appendTag(nbttagcompound1);
            }
            catch (Exception e)
            {
                FMLLog.log(Level.SEVERE, e,
                        "A TileEntity type %s has throw an exception trying to write state. It will not persist. Report this to the mod author",
                        tileentity.getClass().getName());
            }
        }

        par3NBTTagCompound.setTag("TileEntities", nbttaglist2);
        List list = par2World.getPendingBlockUpdates(par1Chunk, false);

        if (list != null)
        {
            long k = par2World.getTotalWorldTime();
            NBTTagList nbttaglist3 = new NBTTagList();
            Iterator iterator1 = list.iterator();

            while (iterator1.hasNext())
            {
                NextTickListEntry nextticklistentry = (NextTickListEntry)iterator1.next();
                NBTTagCompound nbttagcompound2 = new NBTTagCompound();
                nbttagcompound2.setInteger("i", nextticklistentry.blockID);
                nbttagcompound2.setInteger("x", nextticklistentry.xCoord);
                nbttagcompound2.setInteger("y", nextticklistentry.yCoord);
                nbttagcompound2.setInteger("z", nextticklistentry.zCoord);
                nbttagcompound2.setInteger("t", (int)(nextticklistentry.scheduledTime - k));
                nbttagcompound2.setInteger("p", nextticklistentry.priority);
                nbttaglist3.appendTag(nbttagcompound2);
            }

            par3NBTTagCompound.setTag("TileTicks", nbttaglist3);
        }
    }

    /**
     * Reads the data stored in the passed NBTTagCompound and creates a Chunk with that data in the passed World.
     * Returns the created Chunk.
     */
    private Chunk readChunkFromNBT(World par1World, NBTTagCompound par2NBTTagCompound)
    {
        int i = par2NBTTagCompound.getInteger("xPos");
        int j = par2NBTTagCompound.getInteger("zPos");
        Chunk chunk = new Chunk(par1World, i, j);
        chunk.heightMap = par2NBTTagCompound.getIntArray("HeightMap");
        chunk.isTerrainPopulated = par2NBTTagCompound.getBoolean("TerrainPopulated");
        chunk.inhabitedTime = par2NBTTagCompound.getLong("InhabitedTime");
        NBTTagList nbttaglist = par2NBTTagCompound.getTagList("Sections");
        byte b0 = 16;
        ExtendedBlockStorage[] aextendedblockstorage = new ExtendedBlockStorage[b0];
        boolean flag = !par1World.provider.hasNoSky;

        for (int k = 0; k < nbttaglist.tagCount(); ++k)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(k);
            byte b1 = nbttagcompound1.getByte("Y");
            ExtendedBlockStorage extendedblockstorage = new ExtendedBlockStorage(b1 << 4, flag);
            extendedblockstorage.setBlockLSBArray(nbttagcompound1.getByteArray("Blocks"));

            if (nbttagcompound1.hasKey("Add"))
            {
                extendedblockstorage.setBlockMSBArray(new NibbleArray(nbttagcompound1.getByteArray("Add"), 4));
            }

            extendedblockstorage.setBlockMetadataArray(new NibbleArray(nbttagcompound1.getByteArray("Data"), 4));
            extendedblockstorage.setBlocklightArray(new NibbleArray(nbttagcompound1.getByteArray("BlockLight"), 4));

            if (flag)
            {
                extendedblockstorage.setSkylightArray(new NibbleArray(nbttagcompound1.getByteArray("SkyLight"), 4));
            }

            extendedblockstorage.removeInvalidBlocks();
            aextendedblockstorage[b1] = extendedblockstorage;
        }

        chunk.setStorageArrays(aextendedblockstorage);

        if (par2NBTTagCompound.hasKey("Biomes"))
        {
            chunk.setBiomeArray(par2NBTTagCompound.getByteArray("Biomes"));
        }

        NBTTagList nbttaglist1 = par2NBTTagCompound.getTagList("Entities");

        if (nbttaglist1 != null)
        {
            for (int l = 0; l < nbttaglist1.tagCount(); ++l)
            {
                NBTTagCompound nbttagcompound2 = (NBTTagCompound)nbttaglist1.tagAt(l);
                Entity entity = EntityList.createEntityFromNBT(nbttagcompound2, par1World);
                chunk.hasEntities = true;

                if (entity != null)
                {
                    chunk.addEntity(entity);
                    Entity entity1 = entity;

                    for (NBTTagCompound nbttagcompound3 = nbttagcompound2; nbttagcompound3.hasKey("Riding"); nbttagcompound3 = nbttagcompound3.getCompoundTag("Riding"))
                    {
                        Entity entity2 = EntityList.createEntityFromNBT(nbttagcompound3.getCompoundTag("Riding"), par1World);

                        if (entity2 != null)
                        {
                            chunk.addEntity(entity2);
                            entity1.mountEntity(entity2);
                        }

                        entity1 = entity2;
                    }
                }
            }
        }

        NBTTagList nbttaglist2 = par2NBTTagCompound.getTagList("TileEntities");

        if (nbttaglist2 != null)
        {
            for (int i1 = 0; i1 < nbttaglist2.tagCount(); ++i1)
            {
                NBTTagCompound nbttagcompound4 = (NBTTagCompound)nbttaglist2.tagAt(i1);
                TileEntity tileentity = TileEntity.createAndLoadEntity(nbttagcompound4);

                if (tileentity != null)
                {
                    chunk.addTileEntity(tileentity);
                }
            }
        }

        if (par2NBTTagCompound.hasKey("TileTicks"))
        {
            NBTTagList nbttaglist3 = par2NBTTagCompound.getTagList("TileTicks");

            if (nbttaglist3 != null)
            {
                for (int j1 = 0; j1 < nbttaglist3.tagCount(); ++j1)
                {
                    NBTTagCompound nbttagcompound5 = (NBTTagCompound)nbttaglist3.tagAt(j1);
                    par1World.scheduleBlockUpdateFromLoad(nbttagcompound5.getInteger("x"), nbttagcompound5.getInteger("y"), nbttagcompound5.getInteger("z"), nbttagcompound5.getInteger("i"), nbttagcompound5.getInteger("t"), nbttagcompound5.getInteger("p"));
                }
            }
        }

        return chunk;
    }
}
