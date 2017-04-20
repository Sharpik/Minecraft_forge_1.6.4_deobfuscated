package net.minecraft.network.packet;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.security.PublicKey;
import net.minecraft.util.CryptManager;

public class Packet253ServerAuthData extends Packet
{
    private String serverId;
    private PublicKey publicKey;
    private byte[] verifyToken = new byte[0];

    public Packet253ServerAuthData() {}

    public Packet253ServerAuthData(String par1Str, PublicKey par2PublicKey, byte[] par3ArrayOfByte)
    {
        this.serverId = par1Str;
        this.publicKey = par2PublicKey;
        this.verifyToken = par3ArrayOfByte;
    }

    /**
     * Abstract. Reads the raw packet data from the data stream.
     */
    public void readPacketData(DataInput par1DataInput) throws IOException
    {
        this.serverId = readString(par1DataInput, 20);
        this.publicKey = CryptManager.decodePublicKey(readBytesFromStream(par1DataInput));
        this.verifyToken = readBytesFromStream(par1DataInput);
    }

    /**
     * Abstract. Writes the raw packet data to the data stream.
     */
    public void writePacketData(DataOutput par1DataOutput) throws IOException
    {
        writeString(this.serverId, par1DataOutput);
        writeByteArray(par1DataOutput, this.publicKey.getEncoded());
        writeByteArray(par1DataOutput, this.verifyToken);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleServerAuthData(this);
    }

    /**
     * Abstract. Return the size of the packet (not counting the header).
     */
    public int getPacketSize()
    {
        return 2 + this.serverId.length() * 2 + 2 + this.publicKey.getEncoded().length + 2 + this.verifyToken.length;
    }

    @SideOnly(Side.CLIENT)
    public String getServerId()
    {
        return this.serverId;
    }

    @SideOnly(Side.CLIENT)
    public PublicKey getPublicKey()
    {
        return this.publicKey;
    }

    @SideOnly(Side.CLIENT)
    public byte[] getVerifyToken()
    {
        return this.verifyToken;
    }
}
