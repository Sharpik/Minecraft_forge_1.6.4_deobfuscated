package net.minecraft.client.multiplayer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@SideOnly(Side.CLIENT)
public class LanServerList
{
    private ArrayList listOfLanServers = new ArrayList();
    boolean wasUpdated;

    public synchronized boolean getWasUpdated()
    {
        return this.wasUpdated;
    }

    public synchronized void setWasNotUpdated()
    {
        this.wasUpdated = false;
    }

    public synchronized List getLanServers()
    {
        return Collections.unmodifiableList(this.listOfLanServers);
    }

    public synchronized void func_77551_a(String par1Str, InetAddress par2InetAddress)
    {
        String s1 = ThreadLanServerPing.getMotdFromPingResponse(par1Str);
        String s2 = ThreadLanServerPing.getAdFromPingResponse(par1Str);

        if (s2 != null)
        {
            s2 = par2InetAddress.getHostAddress() + ":" + s2;
            boolean flag = false;
            Iterator iterator = this.listOfLanServers.iterator();

            while (iterator.hasNext())
            {
                LanServer lanserver = (LanServer)iterator.next();

                if (lanserver.getServerIpPort().equals(s2))
                {
                    lanserver.updateLastSeen();
                    flag = true;
                    break;
                }
            }

            if (!flag)
            {
                this.listOfLanServers.add(new LanServer(s1, s2));
                this.wasUpdated = true;
            }
        }
    }
}
