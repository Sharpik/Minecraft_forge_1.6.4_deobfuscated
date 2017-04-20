package net.minecraft.network;

import java.io.IOException;

class TcpWriterThread extends Thread
{
    final TcpConnection theTcpConnection;

    TcpWriterThread(TcpConnection par1TcpConnection, String par2Str)
    {
        super(par2Str);
        this.theTcpConnection = par1TcpConnection;
    }

    public void run()
    {
        TcpConnection.field_74469_b.getAndIncrement();

        try
        {
            while (TcpConnection.isRunning(this.theTcpConnection))
            {
                boolean flag;

                for (flag = false; TcpConnection.sendNetworkPacket(this.theTcpConnection); flag = true)
                {
                    ;
                }

                try
                {
                    if (flag && TcpConnection.getOutputStream(this.theTcpConnection) != null)
                    {
                        TcpConnection.getOutputStream(this.theTcpConnection).flush();
                    }
                }
                catch (IOException ioexception)
                {
                    if (!TcpConnection.isTerminating(this.theTcpConnection))
                    {
                        TcpConnection.sendError(this.theTcpConnection, ioexception);
                    }

                    ioexception.printStackTrace();
                }

                try
                {
                    sleep(2L);
                }
                catch (InterruptedException interruptedexception)
                {
                    ;
                }
            }
        }
        finally
        {
            TcpConnection.field_74469_b.getAndDecrement();
        }
    }
}
