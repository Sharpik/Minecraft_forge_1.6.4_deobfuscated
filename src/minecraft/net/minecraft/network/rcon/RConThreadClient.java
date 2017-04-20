package net.minecraft.network.rcon;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class RConThreadClient extends RConThreadBase
{
    /**
     * True if the client has succefssfully logged into the RCon, otherwise false
     */
    private boolean loggedIn;

    /** The client's Socket connection */
    private Socket clientSocket;

    /** A buffer for incoming Socket data */
    private byte[] buffer = new byte[1460];

    /** The RCon password */
    private String rconPassword;

    RConThreadClient(IServer par1IServer, Socket par2Socket)
    {
        super(par1IServer);
        this.clientSocket = par2Socket;

        try
        {
            this.clientSocket.setSoTimeout(0);
        }
        catch (Exception var4)
        {
            this.running = false;
        }

        this.rconPassword = par1IServer.getStringProperty("rcon.password", "");
        this.logInfo("Rcon connection from: " + par2Socket.getInetAddress());
    }

    public void run()
    {
        try
        {
            while (true)
            {
                if (!this.running)
                {
                    break;
                }

                BufferedInputStream bufferedinputstream = new BufferedInputStream(this.clientSocket.getInputStream());
                int i = bufferedinputstream.read(this.buffer, 0, 1460);

                if (10 > i)
                {
                    return;
                }

                byte b0 = 0;
                int j = RConUtils.getBytesAsLEInt(this.buffer, 0, i);

                if (j == i - 4)
                {
                    int k = b0 + 4;
                    int l = RConUtils.getBytesAsLEInt(this.buffer, k, i);
                    k += 4;
                    int i1 = RConUtils.getRemainingBytesAsLEInt(this.buffer, k);
                    k += 4;

                    switch (i1)
                    {
                        case 2:
                            if (this.loggedIn)
                            {
                                String s = RConUtils.getBytesAsString(this.buffer, k, i);

                                try
                                {
                                    this.sendMultipacketResponse(l, this.server.executeCommand(s));
                                }
                                catch (Exception exception)
                                {
                                    this.sendMultipacketResponse(l, "Error executing: " + s + " (" + exception.getMessage() + ")");
                                }

                                continue;
                            }

                            this.sendLoginFailedResponse();
                            continue;
                        case 3:
                            String s1 = RConUtils.getBytesAsString(this.buffer, k, i);
                            int j1 = k + s1.length();

                            if (0 != s1.length() && s1.equals(this.rconPassword))
                            {
                                this.loggedIn = true;
                                this.sendResponse(l, 2, "");
                                continue;
                            }

                            this.loggedIn = false;
                            this.sendLoginFailedResponse();
                            continue;
                        default:
                            this.sendMultipacketResponse(l, String.format("Unknown request %s", new Object[] {Integer.toHexString(i1)}));
                            continue;
                    }
                }
            }
        }
        catch (SocketTimeoutException sockettimeoutexception)
        {
        }
        catch (IOException ioexception)
        {
        }
        catch (Exception exception1)
        {
            System.out.println(exception1);
        }
        finally
        {
            this.closeSocket();
        }
    }

    /**
     * Sends the given response message to the client
     */
    private void sendResponse(int par1, int par2, String par3Str) throws IOException
    {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream(1248);
        DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);
        byte[] abyte = par3Str.getBytes("UTF-8");
        dataoutputstream.writeInt(Integer.reverseBytes(abyte.length + 10));
        dataoutputstream.writeInt(Integer.reverseBytes(par1));
        dataoutputstream.writeInt(Integer.reverseBytes(par2));
        dataoutputstream.write(abyte);
        dataoutputstream.write(0);
        dataoutputstream.write(0);
        this.clientSocket.getOutputStream().write(bytearrayoutputstream.toByteArray());
    }

    /**
     * Sends the standard RCon 'authorization failed' response packet
     */
    private void sendLoginFailedResponse() throws IOException
    {
        this.sendResponse(-1, 2, "");
    }

    /**
     * Splits the response message into individual packets and sends each one
     */
    private void sendMultipacketResponse(int par1, String par2Str) throws IOException
    {
        int j = par2Str.length();

        do
        {
            int k = 4096 <= j ? 4096 : j;
            this.sendResponse(par1, 0, par2Str.substring(0, k));
            par2Str = par2Str.substring(k);
            j = par2Str.length();
        }
        while (0 != j);
    }

    /**
     * Closes the client socket
     */
    private void closeSocket()
    {
        if (null != this.clientSocket)
        {
            try
            {
                this.clientSocket.close();
            }
            catch (IOException ioexception)
            {
                this.logWarning("IO: " + ioexception.getMessage());
            }

            this.clientSocket = null;
        }
    }
}
