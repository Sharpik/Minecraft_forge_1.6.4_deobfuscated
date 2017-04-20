package net.minecraft.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet130UpdateSign;

public class TileEntitySign extends TileEntity
{
    /** An array of four strings storing the lines of text on the sign. */
    public String[] signText = new String[] {"", "", "", ""};

    /**
     * The index of the line currently being edited. Only used on client side, but defined on both. Note this is only
     * really used when the > < are going to be visible.
     */
    public int lineBeingEdited = -1;
    private boolean isEditable = true;
    private EntityPlayer field_142011_d;

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setString("Text1", this.signText[0]);
        par1NBTTagCompound.setString("Text2", this.signText[1]);
        par1NBTTagCompound.setString("Text3", this.signText[2]);
        par1NBTTagCompound.setString("Text4", this.signText[3]);
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        this.isEditable = false;
        super.readFromNBT(par1NBTTagCompound);

        for (int i = 0; i < 4; ++i)
        {
            this.signText[i] = par1NBTTagCompound.getString("Text" + (i + 1));

            if (this.signText[i].length() > 15)
            {
                this.signText[i] = this.signText[i].substring(0, 15);
            }
        }
    }

    /**
     * Overriden in a sign to provide the text.
     */
    public Packet getDescriptionPacket()
    {
        String[] astring = new String[4];
        System.arraycopy(this.signText, 0, astring, 0, 4);
        return new Packet130UpdateSign(this.xCoord, this.yCoord, this.zCoord, astring);
    }

    public boolean isEditable()
    {
        return this.isEditable;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Sets the sign's isEditable flag to the specified parameter.
     */
    public void setEditable(boolean par1)
    {
        this.isEditable = par1;

        if (!par1)
        {
            this.field_142011_d = null;
        }
    }

    public void func_142010_a(EntityPlayer par1EntityPlayer)
    {
        this.field_142011_d = par1EntityPlayer;
    }

    public EntityPlayer func_142009_b()
    {
        return this.field_142011_d;
    }
}
