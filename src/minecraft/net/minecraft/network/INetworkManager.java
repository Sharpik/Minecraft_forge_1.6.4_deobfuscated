package net.minecraft.network;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.net.SocketAddress;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet;

public interface INetworkManager
{
    /**
     * Sets the NetHandler for this NetworkManager. Server-only.
     */
    void setNetHandler(NetHandler nethandler);

    /**
     * Adds the packet to the correct send queue (chunk data packets go to a separate queue).
     */
    void addToSendQueue(Packet packet);

    /**
     * Wakes reader and writer threads
     */
    void wakeThreads();

    /**
     * Checks timeouts and processes all pending read packets.
     */
    void processReadPackets();

    /**
     * Return the InetSocketAddress of the remote endpoint
     */
    SocketAddress getSocketAddress();

    /**
     * Shuts down the server. (Only actually used on the server)
     */
    void serverShutdown();

    /**
     * returns 0 for memoryConnections
     */
    int packetSize();

    /**
     * Shuts down the network with the specified reason. Closes all streams and sockets, spawns NetworkMasterThread to
     * stop reading and writing threads.
     */
    void networkShutdown(String s, Object ... var2);

    @SideOnly(Side.CLIENT)
    void closeConnections();
}
