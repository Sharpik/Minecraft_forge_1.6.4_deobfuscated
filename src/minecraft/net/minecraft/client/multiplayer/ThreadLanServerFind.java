package net.minecraft.client.multiplayer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import net.minecraft.client.Minecraft;

@SideOnly(Side.CLIENT)
public class ThreadLanServerFind extends Thread
{
    /** The LanServerList */
    private final LanServerList localServerList;

    /** InetAddress for 224.0.2.60 */
    private final InetAddress broadcastAddress;

    /** The socket we're using to receive packets on. */
    private final MulticastSocket socket;

    public ThreadLanServerFind(LanServerList par1LanServerList) throws IOException
    {
        super("LanServerDetector");
        this.localServerList = par1LanServerList;
        this.setDaemon(true);
        this.socket = new MulticastSocket(4445);
        this.broadcastAddress = InetAddress.getByName("224.0.2.60");
        this.socket.setSoTimeout(5000);
        this.socket.joinGroup(this.broadcastAddress);
    }

    public void run()
    {
        byte[] abyte = new byte[1024];

        while (!this.isInterrupted())
        {
            DatagramPacket datagrampacket = new DatagramPacket(abyte, abyte.length);

            try
            {
                this.socket.receive(datagrampacket);
            }
            catch (SocketTimeoutException sockettimeoutexception)
            {
                continue;
            }
            catch (IOException ioexception)
            {
                ioexception.printStackTrace();
                break;
            }

            String s = new String(datagrampacket.getData(), datagrampacket.getOffset(), datagrampacket.getLength());
            Minecraft.getMinecraft().getLogAgent().logFine(datagrampacket.getAddress() + ": " + s);
            this.localServerList.func_77551_a(s, datagrampacket.getAddress());
        }

        try
        {
            this.socket.leaveGroup(this.broadcastAddress);
        }
        catch (IOException ioexception1)
        {
            ;
        }

        this.socket.close();
    }
}
