package net.minecraft.client.multiplayer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

@SideOnly(Side.CLIENT)
public class ServerList
{
    /** The Minecraft instance. */
    private final Minecraft mc;

    /** List of ServerData instances. */
    private final List servers = new ArrayList();

    public ServerList(Minecraft par1Minecraft)
    {
        this.mc = par1Minecraft;
        this.loadServerList();
    }

    /**
     * Loads a list of servers from servers.dat, by running ServerData.getServerDataFromNBTCompound on each NBT compound
     * found in the "servers" tag list.
     */
    public void loadServerList()
    {
        try
        {
            this.servers.clear();
            NBTTagCompound nbttagcompound = CompressedStreamTools.read(new File(this.mc.mcDataDir, "servers.dat"));

            if (nbttagcompound == null)
            {
                return;
            }

            NBTTagList nbttaglist = nbttagcompound.getTagList("servers");

            for (int i = 0; i < nbttaglist.tagCount(); ++i)
            {
                this.servers.add(ServerData.getServerDataFromNBTCompound((NBTTagCompound)nbttaglist.tagAt(i)));
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    /**
     * Runs getNBTCompound on each ServerData instance, puts everything into a "servers" NBT list and writes it to
     * servers.dat.
     */
    public void saveServerList()
    {
        try
        {
            NBTTagList nbttaglist = new NBTTagList();
            Iterator iterator = this.servers.iterator();

            while (iterator.hasNext())
            {
                ServerData serverdata = (ServerData)iterator.next();
                nbttaglist.appendTag(serverdata.getNBTCompound());
            }

            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setTag("servers", nbttaglist);
            CompressedStreamTools.safeWrite(nbttagcompound, new File(this.mc.mcDataDir, "servers.dat"));
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    /**
     * Gets the ServerData instance stored for the given index in the list.
     */
    public ServerData getServerData(int par1)
    {
        return (ServerData)this.servers.get(par1);
    }

    /**
     * Removes the ServerData instance stored for the given index in the list.
     */
    public void removeServerData(int par1)
    {
        this.servers.remove(par1);
    }

    /**
     * Adds the given ServerData instance to the list.
     */
    public void addServerData(ServerData par1ServerData)
    {
        this.servers.add(par1ServerData);
    }

    /**
     * Counts the number of ServerData instances in the list.
     */
    public int countServers()
    {
        return this.servers.size();
    }

    /**
     * Takes two list indexes, and swaps their order around.
     */
    public void swapServers(int par1, int par2)
    {
        ServerData serverdata = this.getServerData(par1);
        this.servers.set(par1, this.getServerData(par2));
        this.servers.set(par2, serverdata);
        this.saveServerList();
    }
}
