package net.minecraft.server.integrated;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.net.SocketAddress;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;

@SideOnly(Side.CLIENT)
public class IntegratedPlayerList extends ServerConfigurationManager
{
    /**
     * Holds the NBT data for the host player's save file, so this can be written to level.dat.
     */
    private NBTTagCompound hostPlayerData;

    public IntegratedPlayerList(IntegratedServer par1IntegratedServer)
    {
        super(par1IntegratedServer);
        this.viewDistance = 10;
    }

    /**
     * also stores the NBTTags if this is an intergratedPlayerList
     */
    protected void writePlayerData(EntityPlayerMP par1EntityPlayerMP)
    {
        if (par1EntityPlayerMP.getCommandSenderName().equals(this.getIntegratedServer().getServerOwner()))
        {
            this.hostPlayerData = new NBTTagCompound();
            par1EntityPlayerMP.writeToNBT(this.hostPlayerData);
        }

        super.writePlayerData(par1EntityPlayerMP);
    }

    /**
     * checks ban-lists, then white-lists, then space for the server. Returns null on success, or an error message
     */
    public String allowUserToConnect(SocketAddress par1SocketAddress, String par2Str)
    {
        return par2Str.equalsIgnoreCase(this.getIntegratedServer().getServerOwner()) ? "That name is already taken." : super.allowUserToConnect(par1SocketAddress, par2Str);
    }

    /**
     * get the associated Integrated Server
     */
    public IntegratedServer getIntegratedServer()
    {
        return (IntegratedServer)super.getServerInstance();
    }

    /**
     * On integrated servers, returns the host's player data to be written to level.dat.
     */
    public NBTTagCompound getHostPlayerData()
    {
        return this.hostPlayerData;
    }

    public MinecraftServer getServerInstance()
    {
        return this.getIntegratedServer();
    }
}
