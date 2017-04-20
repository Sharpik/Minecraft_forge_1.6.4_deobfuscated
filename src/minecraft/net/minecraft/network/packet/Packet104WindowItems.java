package net.minecraft.network.packet;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;
import net.minecraft.item.ItemStack;

public class Packet104WindowItems extends Packet
{
    /**
     * The id of window which items are being sent for. 0 for player inventory.
     */
    public int windowId;

    /** Stack of items */
    public ItemStack[] itemStack;

    public Packet104WindowItems() {}

    public Packet104WindowItems(int par1, List par2List)
    {
        this.windowId = par1;
        this.itemStack = new ItemStack[par2List.size()];

        for (int j = 0; j < this.itemStack.length; ++j)
        {
            ItemStack itemstack = (ItemStack)par2List.get(j);
            this.itemStack[j] = itemstack == null ? null : itemstack.copy();
        }
    }

    /**
     * Abstract. Reads the raw packet data from the data stream.
     */
    public void readPacketData(DataInput par1DataInput) throws IOException
    {
        this.windowId = par1DataInput.readByte();
        short short1 = par1DataInput.readShort();
        this.itemStack = new ItemStack[short1];

        for (int i = 0; i < short1; ++i)
        {
            this.itemStack[i] = readItemStack(par1DataInput);
        }
    }

    /**
     * Abstract. Writes the raw packet data to the data stream.
     */
    public void writePacketData(DataOutput par1DataOutput) throws IOException
    {
        par1DataOutput.writeByte(this.windowId);
        par1DataOutput.writeShort(this.itemStack.length);

        for (int i = 0; i < this.itemStack.length; ++i)
        {
            writeItemStack(this.itemStack[i], par1DataOutput);
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleWindowItems(this);
    }

    /**
     * Abstract. Return the size of the packet (not counting the header).
     */
    public int getPacketSize()
    {
        return 3 + this.itemStack.length * 5;
    }
}
