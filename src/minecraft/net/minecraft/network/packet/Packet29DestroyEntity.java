package net.minecraft.network.packet;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Packet29DestroyEntity extends Packet
{
    /** ID of the entity to be destroyed on the client. */
    public int[] entityId;

    public Packet29DestroyEntity() {}

    public Packet29DestroyEntity(int ... par1ArrayOfInteger)
    {
        this.entityId = par1ArrayOfInteger;
    }

    /**
     * Abstract. Reads the raw packet data from the data stream.
     */
    public void readPacketData(DataInput par1DataInput) throws IOException
    {
        this.entityId = new int[par1DataInput.readByte()];

        for (int i = 0; i < this.entityId.length; ++i)
        {
            this.entityId[i] = par1DataInput.readInt();
        }
    }

    /**
     * Abstract. Writes the raw packet data to the data stream.
     */
    public void writePacketData(DataOutput par1DataOutput) throws IOException
    {
        par1DataOutput.writeByte(this.entityId.length);

        for (int i = 0; i < this.entityId.length; ++i)
        {
            par1DataOutput.writeInt(this.entityId[i]);
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleDestroyEntity(this);
    }

    /**
     * Abstract. Return the size of the packet (not counting the header).
     */
    public int getPacketSize()
    {
        return 1 + this.entityId.length * 4;
    }
}
