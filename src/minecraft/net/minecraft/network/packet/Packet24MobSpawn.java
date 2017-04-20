package net.minecraft.network.packet;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class Packet24MobSpawn extends Packet
{
    /** The entity ID. */
    public int entityId;

    /** The type of mob. */
    public int type;

    /** The X position of the entity. */
    public int xPosition;

    /** The Y position of the entity. */
    public int yPosition;

    /** The Z position of the entity. */
    public int zPosition;
    public int velocityX;
    public int velocityY;
    public int velocityZ;

    /** The yaw of the entity. */
    public byte yaw;

    /** The pitch of the entity. */
    public byte pitch;

    /** The yaw of the entity's head. */
    public byte headYaw;

    /** Indexed metadata for Mob, terminated by 0x7F */
    private DataWatcher metaData;
    private List metadata;

    public Packet24MobSpawn() {}

    public Packet24MobSpawn(EntityLivingBase par1EntityLivingBase)
    {
        this.entityId = par1EntityLivingBase.entityId;
        this.type = (byte)EntityList.getEntityID(par1EntityLivingBase);
        this.xPosition = par1EntityLivingBase.myEntitySize.multiplyBy32AndRound(par1EntityLivingBase.posX);
        this.yPosition = MathHelper.floor_double(par1EntityLivingBase.posY * 32.0D);
        this.zPosition = par1EntityLivingBase.myEntitySize.multiplyBy32AndRound(par1EntityLivingBase.posZ);
        this.yaw = (byte)((int)(par1EntityLivingBase.rotationYaw * 256.0F / 360.0F));
        this.pitch = (byte)((int)(par1EntityLivingBase.rotationPitch * 256.0F / 360.0F));
        this.headYaw = (byte)((int)(par1EntityLivingBase.rotationYawHead * 256.0F / 360.0F));
        double d0 = 3.9D;
        double d1 = par1EntityLivingBase.motionX;
        double d2 = par1EntityLivingBase.motionY;
        double d3 = par1EntityLivingBase.motionZ;

        if (d1 < -d0)
        {
            d1 = -d0;
        }

        if (d2 < -d0)
        {
            d2 = -d0;
        }

        if (d3 < -d0)
        {
            d3 = -d0;
        }

        if (d1 > d0)
        {
            d1 = d0;
        }

        if (d2 > d0)
        {
            d2 = d0;
        }

        if (d3 > d0)
        {
            d3 = d0;
        }

        this.velocityX = (int)(d1 * 8000.0D);
        this.velocityY = (int)(d2 * 8000.0D);
        this.velocityZ = (int)(d3 * 8000.0D);
        this.metaData = par1EntityLivingBase.getDataWatcher();
    }

    /**
     * Abstract. Reads the raw packet data from the data stream.
     */
    public void readPacketData(DataInput par1DataInput) throws IOException
    {
        this.entityId = par1DataInput.readInt();
        this.type = par1DataInput.readByte() & 255;
        this.xPosition = par1DataInput.readInt();
        this.yPosition = par1DataInput.readInt();
        this.zPosition = par1DataInput.readInt();
        this.yaw = par1DataInput.readByte();
        this.pitch = par1DataInput.readByte();
        this.headYaw = par1DataInput.readByte();
        this.velocityX = par1DataInput.readShort();
        this.velocityY = par1DataInput.readShort();
        this.velocityZ = par1DataInput.readShort();
        this.metadata = DataWatcher.readWatchableObjects(par1DataInput);
    }

    /**
     * Abstract. Writes the raw packet data to the data stream.
     */
    public void writePacketData(DataOutput par1DataOutput) throws IOException
    {
        par1DataOutput.writeInt(this.entityId);
        par1DataOutput.writeByte(this.type & 255);
        par1DataOutput.writeInt(this.xPosition);
        par1DataOutput.writeInt(this.yPosition);
        par1DataOutput.writeInt(this.zPosition);
        par1DataOutput.writeByte(this.yaw);
        par1DataOutput.writeByte(this.pitch);
        par1DataOutput.writeByte(this.headYaw);
        par1DataOutput.writeShort(this.velocityX);
        par1DataOutput.writeShort(this.velocityY);
        par1DataOutput.writeShort(this.velocityZ);
        this.metaData.writeWatchableObjects(par1DataOutput);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleMobSpawn(this);
    }

    /**
     * Abstract. Return the size of the packet (not counting the header).
     */
    public int getPacketSize()
    {
        return 26;
    }

    @SideOnly(Side.CLIENT)
    public List getMetadata()
    {
        if (this.metadata == null)
        {
            this.metadata = this.metaData.getAllWatched();
        }

        return this.metadata;
    }
}
