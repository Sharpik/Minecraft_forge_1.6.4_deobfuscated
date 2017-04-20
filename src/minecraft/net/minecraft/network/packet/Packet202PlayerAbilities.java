package net.minecraft.network.packet;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.entity.player.PlayerCapabilities;

public class Packet202PlayerAbilities extends Packet
{
    /** Disables player damage. */
    private boolean disableDamage;

    /** Indicates whether the player is flying or not. */
    private boolean isFlying;

    /** Whether or not to allow the player to fly when they double jump. */
    private boolean allowFlying;

    /**
     * Used to determine if creative mode is enabled, and therefore if items should be depleted on usage
     */
    private boolean isCreativeMode;
    private float flySpeed;
    private float walkSpeed;

    public Packet202PlayerAbilities() {}

    public Packet202PlayerAbilities(PlayerCapabilities par1PlayerCapabilities)
    {
        this.setDisableDamage(par1PlayerCapabilities.disableDamage);
        this.setFlying(par1PlayerCapabilities.isFlying);
        this.setAllowFlying(par1PlayerCapabilities.allowFlying);
        this.setCreativeMode(par1PlayerCapabilities.isCreativeMode);
        this.setFlySpeed(par1PlayerCapabilities.getFlySpeed());
        this.setWalkSpeed(par1PlayerCapabilities.getWalkSpeed());
    }

    /**
     * Abstract. Reads the raw packet data from the data stream.
     */
    public void readPacketData(DataInput par1DataInput) throws IOException
    {
        byte b0 = par1DataInput.readByte();
        this.setDisableDamage((b0 & 1) > 0);
        this.setFlying((b0 & 2) > 0);
        this.setAllowFlying((b0 & 4) > 0);
        this.setCreativeMode((b0 & 8) > 0);
        this.setFlySpeed(par1DataInput.readFloat());
        this.setWalkSpeed(par1DataInput.readFloat());
    }

    /**
     * Abstract. Writes the raw packet data to the data stream.
     */
    public void writePacketData(DataOutput par1DataOutput) throws IOException
    {
        byte b0 = 0;

        if (this.getDisableDamage())
        {
            b0 = (byte)(b0 | 1);
        }

        if (this.getFlying())
        {
            b0 = (byte)(b0 | 2);
        }

        if (this.getAllowFlying())
        {
            b0 = (byte)(b0 | 4);
        }

        if (this.isCreativeMode())
        {
            b0 = (byte)(b0 | 8);
        }

        par1DataOutput.writeByte(b0);
        par1DataOutput.writeFloat(this.flySpeed);
        par1DataOutput.writeFloat(this.walkSpeed);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handlePlayerAbilities(this);
    }

    /**
     * Abstract. Return the size of the packet (not counting the header).
     */
    public int getPacketSize()
    {
        return 2;
    }

    public boolean getDisableDamage()
    {
        return this.disableDamage;
    }

    /**
     * Sets whether damage is disabled or not.
     */
    public void setDisableDamage(boolean par1)
    {
        this.disableDamage = par1;
    }

    public boolean getFlying()
    {
        return this.isFlying;
    }

    /**
     * Sets whether we're currently flying or not.
     */
    public void setFlying(boolean par1)
    {
        this.isFlying = par1;
    }

    public boolean getAllowFlying()
    {
        return this.allowFlying;
    }

    public void setAllowFlying(boolean par1)
    {
        this.allowFlying = par1;
    }

    public boolean isCreativeMode()
    {
        return this.isCreativeMode;
    }

    public void setCreativeMode(boolean par1)
    {
        this.isCreativeMode = par1;
    }

    @SideOnly(Side.CLIENT)
    public float getFlySpeed()
    {
        return this.flySpeed;
    }

    /**
     * Sets the flying speed.
     */
    public void setFlySpeed(float par1)
    {
        this.flySpeed = par1;
    }

    @SideOnly(Side.CLIENT)
    public float getWalkSpeed()
    {
        return this.walkSpeed;
    }

    /**
     * Sets the walking speed.
     */
    public void setWalkSpeed(float par1)
    {
        this.walkSpeed = par1;
    }

    /**
     * only false for the abstract Packet class, all real packets return true
     */
    public boolean isRealPacket()
    {
        return true;
    }

    /**
     * eg return packet30entity.entityId == entityId; WARNING : will throw if you compare a packet to a different packet
     * class
     */
    public boolean containsSameEntityIDAs(Packet par1Packet)
    {
        return true;
    }
}
