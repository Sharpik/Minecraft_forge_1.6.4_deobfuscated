package net.minecraft.network;

class TcpMasterThread extends Thread
{
    final TcpConnection theTcpConnection;

    TcpMasterThread(TcpConnection par1TcpConnection)
    {
        this.theTcpConnection = par1TcpConnection;
    }

    @SuppressWarnings("deprecation")
    public void run()
    {
        try
        {
            Thread.sleep(5000L);

            if (TcpConnection.getReadThread(this.theTcpConnection).isAlive())
            {
                try
                {
                    TcpConnection.getReadThread(this.theTcpConnection).stop();
                }
                catch (Throwable throwable)
                {
                    ;
                }
            }

            if (TcpConnection.getWriteThread(this.theTcpConnection).isAlive())
            {
                try
                {
                    TcpConnection.getWriteThread(this.theTcpConnection).stop();
                }
                catch (Throwable throwable1)
                {
                    ;
                }
            }
        }
        catch (InterruptedException interruptedexception)
        {
            interruptedexception.printStackTrace();
        }
    }
}
