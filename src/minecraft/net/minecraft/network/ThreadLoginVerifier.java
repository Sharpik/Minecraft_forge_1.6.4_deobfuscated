package net.minecraft.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLEncoder;
import net.minecraft.util.CryptManager;

class ThreadLoginVerifier extends Thread
{
    /** The login handler that spawned this thread. */
    final NetLoginHandler loginHandler;

    ThreadLoginVerifier(NetLoginHandler par1NetLoginHandler)
    {
        this.loginHandler = par1NetLoginHandler;
    }

    public void run()
    {
        try
        {
            String s = (new BigInteger(CryptManager.getServerIdHash(NetLoginHandler.getServerId(this.loginHandler), NetLoginHandler.getLoginMinecraftServer(this.loginHandler).getKeyPair().getPublic(), NetLoginHandler.getSharedKey(this.loginHandler)))).toString(16);
            URL url = new URL("http://session.minecraft.net/game/checkserver.jsp?user=" + URLEncoder.encode(NetLoginHandler.getClientUsername(this.loginHandler), "UTF-8") + "&serverId=" + URLEncoder.encode(s, "UTF-8"));
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(url.openConnection(NetLoginHandler.getLoginMinecraftServer(this.loginHandler).getServerProxy()).getInputStream()));
            String s1 = bufferedreader.readLine();
            bufferedreader.close();

            if (!"YES".equals(s1))
            {
                this.loginHandler.raiseErrorAndDisconnect("Failed to verify username!");
                return;
            }

            NetLoginHandler.func_72531_a(this.loginHandler, true);
        }
        catch (Exception exception)
        {
            this.loginHandler.raiseErrorAndDisconnect("Failed to verify username! [internal error " + exception + "]");
            exception.printStackTrace();
        }
    }
}
