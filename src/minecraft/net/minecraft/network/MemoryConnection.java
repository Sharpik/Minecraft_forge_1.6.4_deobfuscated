package net.minecraft.network;

import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.google.common.collect.Queues;

import net.minecraft.logging.ILogAgent;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet;

public class MemoryConnection implements INetworkManager
{
    private static final SocketAddress mySocketAddress = new InetSocketAddress("127.0.0.1", 0);
    private final Queue<Packet> readPacketCache = Queues.newConcurrentLinkedQueue();
    private final ILogAgent field_98214_c;
    private MemoryConnection pairedConnection;
    private NetHandler myNetHandler;

    /** set to true by {server,network}Shutdown */
    private boolean shuttingDown;
    private String shutdownReason = "";
    private Object[] field_74439_g;
    @SideOnly(Side.CLIENT)
    private boolean gamePaused;

    @SideOnly(Side.CLIENT)
    public MemoryConnection(ILogAgent par1ILogAgent, NetHandler par2NetHandler)
    {
        this.myNetHandler = par2NetHandler;
        this.field_98214_c = par1ILogAgent;
    }

    /**
     * Sets the NetHandler for this NetworkManager. Server-only.
     */
    public void setNetHandler(NetHandler par1NetHandler)
    {
        this.myNetHandler = par1NetHandler;
    }

    /**
     * Adds the packet to the correct send queue (chunk data packets go to a separate queue).
     */
    public void addToSendQueue(Packet par1Packet)
    {
        if (!this.shuttingDown)
        {
            this.pairedConnection.processOrCachePacket(par1Packet);
        }
    }

    /**
     * Wakes reader and writer threads
     */
    public void wakeThreads() {}

    @SideOnly(Side.CLIENT)
    public void closeConnections()
    {
        this.pairedConnection = null;
        this.myNetHandler = null;
    }

    @SideOnly(Side.CLIENT)
    public boolean isConnectionActive()
    {
        return !this.shuttingDown && this.pairedConnection != null;
    }

    /**
     * Checks timeouts and processes all pending read packets.
     */
    public void processReadPackets()
    {
        int i = 2500;

        while (i-- >= 0 && !this.readPacketCache.isEmpty())
        {
            Packet packet = readPacketCache.poll();
            packet.processPacket(this.myNetHandler);
        }

        if (this.readPacketCache.size() > i)
        {
            this.field_98214_c.logWarning("Memory connection overburdened; after processing 2500 packets, we still have " + this.readPacketCache.size() + " to go!");
        }

        if (this.shuttingDown && this.readPacketCache.isEmpty())
        {
            this.myNetHandler.handleErrorMessage(this.shutdownReason, this.field_74439_g);
            FMLNetworkHandler.onConnectionClosed(this, this.myNetHandler.getPlayer());
        }
    }

    /**
     * Return the InetSocketAddress of the remote endpoint
     */
    public SocketAddress getSocketAddress()
    {
        return mySocketAddress;
    }

    /**
     * Shuts down the server. (Only actually used on the server)
     */
    public void serverShutdown()
    {
        this.shuttingDown = true;
    }

    /**
     * Shuts down the network with the specified reason. Closes all streams and sockets, spawns NetworkMasterThread to
     * stop reading and writing threads.
     */
    public void networkShutdown(String par1Str, Object ... par2ArrayOfObj)
    {
        this.shuttingDown = true;
        this.shutdownReason = par1Str;
        this.field_74439_g = par2ArrayOfObj;
    }

    /**
     * returns 0 for memoryConnections
     */
    public int packetSize()
    {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public void pairWith(MemoryConnection par1MemoryConnection)
    {
        this.pairedConnection = par1MemoryConnection;
        par1MemoryConnection.pairedConnection = this;
    }

    @SideOnly(Side.CLIENT)
    public boolean isGamePaused()
    {
        return this.gamePaused;
    }

    @SideOnly(Side.CLIENT)
    public void setGamePaused(boolean par1)
    {
        this.gamePaused = par1;
    }

    @SideOnly(Side.CLIENT)
    public MemoryConnection getPairedConnection()
    {
        return this.pairedConnection;
    }

    /**
     * acts immiditally if isWritePacket, otherwise adds it to the readCache to be processed next tick
     */
    public void processOrCachePacket(Packet par1Packet)
    {
        if (par1Packet.canProcessAsync() && this.myNetHandler.canProcessPacketsAsync())
        {
            par1Packet.processPacket(this.myNetHandler);
        }
        else
        {
            this.readPacketCache.add(par1Packet);
        }
    }
}
