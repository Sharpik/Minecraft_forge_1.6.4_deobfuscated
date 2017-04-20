package net.minecraft.network.packet;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkPosition;

public class Packet60Explosion extends Packet
{
    public double explosionX;
    public double explosionY;
    public double explosionZ;
    public float explosionSize;
    public List chunkPositionRecords;

    /** X velocity of the player being pushed by the explosion */
    private float playerVelocityX;

    /** Y velocity of the player being pushed by the explosion */
    private float playerVelocityY;

    /** Z velocity of the player being pushed by the explosion */
    private float playerVelocityZ;

    public Packet60Explosion() {}

    public Packet60Explosion(double par1, double par3, double par5, float par7, List par8List, Vec3 par9Vec3)
    {
        this.explosionX = par1;
        this.explosionY = par3;
        this.explosionZ = par5;
        this.explosionSize = par7;
        this.chunkPositionRecords = new ArrayList(par8List);

        if (par9Vec3 != null)
        {
            this.playerVelocityX = (float)par9Vec3.xCoord;
            this.playerVelocityY = (float)par9Vec3.yCoord;
            this.playerVelocityZ = (float)par9Vec3.zCoord;
        }
    }

    /**
     * Abstract. Reads the raw packet data from the data stream.
     */
    public void readPacketData(DataInput par1DataInput) throws IOException
    {
        this.explosionX = par1DataInput.readDouble();
        this.explosionY = par1DataInput.readDouble();
        this.explosionZ = par1DataInput.readDouble();
        this.explosionSize = par1DataInput.readFloat();
        int i = par1DataInput.readInt();
        this.chunkPositionRecords = new ArrayList(i);
        int j = (int)this.explosionX;
        int k = (int)this.explosionY;
        int l = (int)this.explosionZ;

        for (int i1 = 0; i1 < i; ++i1)
        {
            int j1 = par1DataInput.readByte() + j;
            int k1 = par1DataInput.readByte() + k;
            int l1 = par1DataInput.readByte() + l;
            this.chunkPositionRecords.add(new ChunkPosition(j1, k1, l1));
        }

        this.playerVelocityX = par1DataInput.readFloat();
        this.playerVelocityY = par1DataInput.readFloat();
        this.playerVelocityZ = par1DataInput.readFloat();
    }

    /**
     * Abstract. Writes the raw packet data to the data stream.
     */
    public void writePacketData(DataOutput par1DataOutput) throws IOException
    {
        par1DataOutput.writeDouble(this.explosionX);
        par1DataOutput.writeDouble(this.explosionY);
        par1DataOutput.writeDouble(this.explosionZ);
        par1DataOutput.writeFloat(this.explosionSize);
        par1DataOutput.writeInt(this.chunkPositionRecords.size());
        int i = (int)this.explosionX;
        int j = (int)this.explosionY;
        int k = (int)this.explosionZ;
        Iterator iterator = this.chunkPositionRecords.iterator();

        while (iterator.hasNext())
        {
            ChunkPosition chunkposition = (ChunkPosition)iterator.next();
            int l = chunkposition.x - i;
            int i1 = chunkposition.y - j;
            int j1 = chunkposition.z - k;
            par1DataOutput.writeByte(l);
            par1DataOutput.writeByte(i1);
            par1DataOutput.writeByte(j1);
        }

        par1DataOutput.writeFloat(this.playerVelocityX);
        par1DataOutput.writeFloat(this.playerVelocityY);
        par1DataOutput.writeFloat(this.playerVelocityZ);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleExplosion(this);
    }

    /**
     * Abstract. Return the size of the packet (not counting the header).
     */
    public int getPacketSize()
    {
        return 32 + this.chunkPositionRecords.size() * 3 + 3;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Gets the X velocity of the player being pushed by the explosion.
     */
    public float getPlayerVelocityX()
    {
        return this.playerVelocityX;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Gets the Y velocity of the player being pushed by the explosion.
     */
    public float getPlayerVelocityY()
    {
        return this.playerVelocityY;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Gets the Z velocity of the player being pushed by the explosion.
     */
    public float getPlayerVelocityZ()
    {
        return this.playerVelocityZ;
    }
}
