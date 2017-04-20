package net.minecraft.server;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.NetworkListenThread;

public class ServerListenThread extends Thread
{
    private final List pendingConnections = Collections.synchronizedList(new ArrayList());

    /**
     * This map stores a list of InetAddresses and the last time which they connected at
     */
    private final HashMap recentConnections = new HashMap();
    private int connectionCounter;
    private final ServerSocket myServerSocket;
    private NetworkListenThread myNetworkListenThread;
    private final InetAddress myServerAddress;
    private final int myPort;

    public ServerListenThread(NetworkListenThread par1NetworkListenThread, InetAddress par2InetAddress, int par3) throws IOException
    {
        super("Listen thread");
        this.myNetworkListenThread = par1NetworkListenThread;
        this.myPort = par3;
        this.myServerSocket = new ServerSocket(par3, 0, par2InetAddress);
        this.myServerAddress = par2InetAddress == null ? this.myServerSocket.getInetAddress() : par2InetAddress;
        this.myServerSocket.setPerformancePreferences(0, 2, 1);
    }

    public void processPendingConnections()
    {
        List list = this.pendingConnections;

        synchronized (this.pendingConnections)
        {
            for (int i = 0; i < this.pendingConnections.size(); ++i)
            {
                NetLoginHandler netloginhandler = (NetLoginHandler)this.pendingConnections.get(i);

                try
                {
                    netloginhandler.tryLogin();
                }
                catch (Exception exception)
                {
                    netloginhandler.raiseErrorAndDisconnect("Internal server error");
                    FMLLog.log(Level.SEVERE, exception, "Error handling login related packet - connection from %s refused", netloginhandler.getUsernameAndAddress());
                    this.myNetworkListenThread.getServer().getLogAgent().logWarningException("Failed to handle packet for " + netloginhandler.getUsernameAndAddress() + ": " + exception, exception);
                }

                if (netloginhandler.connectionComplete)
                {
                    this.pendingConnections.remove(i--);
                }

                netloginhandler.myTCPConnection.wakeThreads();
            }
        }
    }

    public void run()
    {
        while (this.myNetworkListenThread.isListening)
        {
            try
            {
                Socket socket = this.myServerSocket.accept();
                NetLoginHandler netloginhandler = new NetLoginHandler(this.myNetworkListenThread.getServer(), socket, "Connection #" + this.connectionCounter++);
                this.addPendingConnection(netloginhandler);
            }
            catch (IOException ioexception)
            {
                ioexception.printStackTrace();
            }
        }

        this.myNetworkListenThread.getServer().getLogAgent().logInfo("Closing listening thread");
    }

    private void addPendingConnection(NetLoginHandler par1NetLoginHandler)
    {
        if (par1NetLoginHandler == null)
        {
            throw new IllegalArgumentException("Got null pendingconnection!");
        }
        else
        {
            List list = this.pendingConnections;

            synchronized (this.pendingConnections)
            {
                this.pendingConnections.add(par1NetLoginHandler);
            }
        }
    }

    public void func_71769_a(InetAddress par1InetAddress)
    {
        if (par1InetAddress != null)
        {
            HashMap hashmap = this.recentConnections;

            synchronized (this.recentConnections)
            {
                this.recentConnections.remove(par1InetAddress);
            }
        }
    }

    public void func_71768_b()
    {
        try
        {
            this.myServerSocket.close();
        }
        catch (Throwable throwable)
        {
            ;
        }
    }

    @SideOnly(Side.CLIENT)
    public int getMyPort()
    {
        return this.myPort;
    }
}
