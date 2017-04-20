package net.minecraft.server.dedicated;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class DedicatedServerCommandThread extends Thread
{
    final DedicatedServer server;

    DedicatedServerCommandThread(DedicatedServer par1DedicatedServer)
    {
        this.server = par1DedicatedServer;
    }

    public void run()
    {
        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(System.in));
        String s;

        try
        {
            while (!this.server.isServerStopped() && this.server.isServerRunning() && (s = bufferedreader.readLine()) != null)
            {
                this.server.addPendingCommand(s, this.server);
            }
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }
}
