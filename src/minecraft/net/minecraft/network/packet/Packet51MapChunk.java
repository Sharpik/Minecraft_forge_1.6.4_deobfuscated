package net.minecraft.network.packet;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class Packet51MapChunk extends Packet
{
    /** The x-position of the transmitted chunk, in chunk coordinates. */
    public int xCh;

    /** The z-position of the transmitted chunk, in chunk coordinates. */
    public int zCh;

    /**
     * The y-position of the lowest chunk Section in the transmitted chunk, in chunk coordinates.
     */
    public int yChMin;

    /**
     * The y-position of the highest chunk Section in the transmitted chunk, in chunk coordinates.
     */
    public int yChMax;

    /** The transmitted chunk data, decompressed. */
    private byte[] chunkData;

    /** The compressed chunk data */
    private byte[] compressedChunkData;

    /**
     * Whether to initialize the Chunk before applying the effect of the Packet51MapChunk.
     */
    public boolean includeInitialize;

    /** The length of the compressed chunk data byte array. */
    private int tempLength;

    /** A temporary storage for the compressed chunk data byte array. */
    private static byte[] temp = new byte[196864];

    private Semaphore deflateGate;

    public Packet51MapChunk()
    {
        this.isChunkDataPacket = true;
    }

    public Packet51MapChunk(Chunk par1Chunk, boolean par2, int par3)
    {
        this.isChunkDataPacket = true;
        this.xCh = par1Chunk.xPosition;
        this.zCh = par1Chunk.zPosition;
        this.includeInitialize = par2;
        Packet51MapChunkData packet51mapchunkdata = getMapChunkData(par1Chunk, par2, par3);
        this.yChMax = packet51mapchunkdata.chunkHasAddSectionFlag;
        this.yChMin = packet51mapchunkdata.chunkExistFlag;
        this.compressedChunkData = packet51mapchunkdata.compressedData;
        this.deflateGate = new Semaphore(1);
    }

    private void deflate()
    {
        Deflater deflater = new Deflater(-1);
        try
        {
            deflater.setInput(compressedChunkData, 0, compressedChunkData.length);
            deflater.finish();
            byte[] deflated = new byte[compressedChunkData.length];
            this.tempLength = deflater.deflate(deflated);
            this.chunkData = deflated;
        }
        finally
        {
            deflater.end();
        }
    }

    /**
     * Abstract. Reads the raw packet data from the data stream.
     */
    public void readPacketData(DataInput par1DataInput) throws IOException
    {
        this.xCh = par1DataInput.readInt();
        this.zCh = par1DataInput.readInt();
        this.includeInitialize = par1DataInput.readBoolean();
        this.yChMin = par1DataInput.readShort();
        this.yChMax = par1DataInput.readShort();
        this.tempLength = par1DataInput.readInt();

        if (temp.length < this.tempLength)
        {
            temp = new byte[this.tempLength];
        }

        par1DataInput.readFully(temp, 0, this.tempLength);
        int i = 0;
        int j;
        int msb = 0; //BugFix: MC does not read the MSB array from the packet properly, causing issues for servers that use blocks > 256

        for (j = 0; j < 16; ++j)
        {
            i += this.yChMin >> j & 1;
            msb  += this.yChMax >> j & 1;
        }

        j = 12288 * i;
        j += 2048 * msb;

        if (this.includeInitialize)
        {
            j += 256;
        }

        this.compressedChunkData = new byte[j];
        Inflater inflater = new Inflater();
        inflater.setInput(temp, 0, this.tempLength);

        try
        {
            inflater.inflate(this.compressedChunkData);
        }
        catch (DataFormatException dataformatexception)
        {
            throw new IOException("Bad compressed data format");
        }
        finally
        {
            inflater.end();
        }
    }

    /**
     * Abstract. Writes the raw packet data to the data stream.
     */
    public void writePacketData(DataOutput par1DataOutput) throws IOException
    {
        if (chunkData == null)
        {
            deflateGate.acquireUninterruptibly();
            if (chunkData == null)
            {
                deflate();
            }
            deflateGate.release();
        }

        par1DataOutput.writeInt(this.xCh);
        par1DataOutput.writeInt(this.zCh);
        par1DataOutput.writeBoolean(this.includeInitialize);
        par1DataOutput.writeShort((short)(this.yChMin & 65535));
        par1DataOutput.writeShort((short)(this.yChMax & 65535));
        par1DataOutput.writeInt(this.tempLength);
        par1DataOutput.write(this.chunkData, 0, this.tempLength);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleMapChunk(this);
    }

    /**
     * Abstract. Return the size of the packet (not counting the header).
     */
    public int getPacketSize()
    {
        return 17 + this.tempLength;
    }

    public static Packet51MapChunkData getMapChunkData(Chunk par0Chunk, boolean par1, int par2)
    {
        int j = 0;
        ExtendedBlockStorage[] aextendedblockstorage = par0Chunk.getBlockStorageArray();
        int k = 0;
        Packet51MapChunkData packet51mapchunkdata = new Packet51MapChunkData();
        byte[] abyte = temp;

        if (par1)
        {
            par0Chunk.sendUpdates = true;
        }

        int l;

        for (l = 0; l < aextendedblockstorage.length; ++l)
        {
            if (aextendedblockstorage[l] != null && (!par1 || !aextendedblockstorage[l].isEmpty()) && (par2 & 1 << l) != 0)
            {
                packet51mapchunkdata.chunkExistFlag |= 1 << l;

                if (aextendedblockstorage[l].getBlockMSBArray() != null)
                {
                    packet51mapchunkdata.chunkHasAddSectionFlag |= 1 << l;
                    ++k;
                }
            }
        }

        for (l = 0; l < aextendedblockstorage.length; ++l)
        {
            if (aextendedblockstorage[l] != null && (!par1 || !aextendedblockstorage[l].isEmpty()) && (par2 & 1 << l) != 0)
            {
                byte[] abyte1 = aextendedblockstorage[l].getBlockLSBArray();
                System.arraycopy(abyte1, 0, abyte, j, abyte1.length);
                j += abyte1.length;
            }
        }

        NibbleArray nibblearray;

        for (l = 0; l < aextendedblockstorage.length; ++l)
        {
            if (aextendedblockstorage[l] != null && (!par1 || !aextendedblockstorage[l].isEmpty()) && (par2 & 1 << l) != 0)
            {
                nibblearray = aextendedblockstorage[l].getMetadataArray();
                System.arraycopy(nibblearray.data, 0, abyte, j, nibblearray.data.length);
                j += nibblearray.data.length;
            }
        }

        for (l = 0; l < aextendedblockstorage.length; ++l)
        {
            if (aextendedblockstorage[l] != null && (!par1 || !aextendedblockstorage[l].isEmpty()) && (par2 & 1 << l) != 0)
            {
                nibblearray = aextendedblockstorage[l].getBlocklightArray();
                System.arraycopy(nibblearray.data, 0, abyte, j, nibblearray.data.length);
                j += nibblearray.data.length;
            }
        }

        if (!par0Chunk.worldObj.provider.hasNoSky)
        {
            for (l = 0; l < aextendedblockstorage.length; ++l)
            {
                if (aextendedblockstorage[l] != null && (!par1 || !aextendedblockstorage[l].isEmpty()) && (par2 & 1 << l) != 0)
                {
                    nibblearray = aextendedblockstorage[l].getSkylightArray();
                    System.arraycopy(nibblearray.data, 0, abyte, j, nibblearray.data.length);
                    j += nibblearray.data.length;
                }
            }
        }

        if (k > 0)
        {
            for (l = 0; l < aextendedblockstorage.length; ++l)
            {
                if (aextendedblockstorage[l] != null && (!par1 || !aextendedblockstorage[l].isEmpty()) && aextendedblockstorage[l].getBlockMSBArray() != null && (par2 & 1 << l) != 0)
                {
                    nibblearray = aextendedblockstorage[l].getBlockMSBArray();
                    System.arraycopy(nibblearray.data, 0, abyte, j, nibblearray.data.length);
                    j += nibblearray.data.length;
                }
            }
        }

        if (par1)
        {
            byte[] abyte2 = par0Chunk.getBiomeArray();
            System.arraycopy(abyte2, 0, abyte, j, abyte2.length);
            j += abyte2.length;
        }

        packet51mapchunkdata.compressedData = new byte[j];
        System.arraycopy(abyte, 0, packet51mapchunkdata.compressedData, 0, j);
        return packet51mapchunkdata;
    }

    @SideOnly(Side.CLIENT)
    public byte[] getCompressedChunkData()
    {
        return this.compressedChunkData;
    }
}
