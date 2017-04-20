package net.minecraft.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.ServerSocket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.logging.ILogAgent;
import net.minecraft.server.MinecraftServer;

public class HttpUtil
{
    /**
     * Builds an encoded HTTP POST content string from a string map
     */
    public static String buildPostString(Map par0Map)
    {
        StringBuilder stringbuilder = new StringBuilder();
        Iterator iterator = par0Map.entrySet().iterator();

        while (iterator.hasNext())
        {
            Entry entry = (Entry)iterator.next();

            if (stringbuilder.length() > 0)
            {
                stringbuilder.append('&');
            }

            try
            {
                stringbuilder.append(URLEncoder.encode((String)entry.getKey(), "UTF-8"));
            }
            catch (UnsupportedEncodingException unsupportedencodingexception)
            {
                unsupportedencodingexception.printStackTrace();
            }

            if (entry.getValue() != null)
            {
                stringbuilder.append('=');

                try
                {
                    stringbuilder.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                }
                catch (UnsupportedEncodingException unsupportedencodingexception1)
                {
                    unsupportedencodingexception1.printStackTrace();
                }
            }
        }

        return stringbuilder.toString();
    }

    /**
     * Sends a HTTP POST request to the given URL with data from a map
     */
    public static String sendPost(ILogAgent par0ILogAgent, URL par1URL, Map par2Map, boolean par3)
    {
        return sendPost(par0ILogAgent, par1URL, buildPostString(par2Map), par3);
    }

    /**
     * Sends a HTTP POST request to the given URL with data from a string
     */
    private static String sendPost(ILogAgent par0ILogAgent, URL par1URL, String par2Str, boolean par3)
    {
        try
        {
            Proxy proxy = MinecraftServer.getServer() == null ? null : MinecraftServer.getServer().getServerProxy();

            if (proxy == null)
            {
                proxy = Proxy.NO_PROXY;
            }

            HttpURLConnection httpurlconnection = (HttpURLConnection)par1URL.openConnection(proxy);
            httpurlconnection.setRequestMethod("POST");
            httpurlconnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpurlconnection.setRequestProperty("Content-Length", "" + par2Str.getBytes().length);
            httpurlconnection.setRequestProperty("Content-Language", "en-US");
            httpurlconnection.setUseCaches(false);
            httpurlconnection.setDoInput(true);
            httpurlconnection.setDoOutput(true);
            DataOutputStream dataoutputstream = new DataOutputStream(httpurlconnection.getOutputStream());
            dataoutputstream.writeBytes(par2Str);
            dataoutputstream.flush();
            dataoutputstream.close();
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream()));
            StringBuffer stringbuffer = new StringBuffer();
            String s1;

            while ((s1 = bufferedreader.readLine()) != null)
            {
                stringbuffer.append(s1);
                stringbuffer.append('\r');
            }

            bufferedreader.close();
            return stringbuffer.toString();
        }
        catch (Exception exception)
        {
            if (!par3)
            {
                if (par0ILogAgent != null)
                {
                    par0ILogAgent.logSevereException("Could not post to " + par1URL, exception);
                }
                else
                {
                    Logger.getAnonymousLogger().log(Level.SEVERE, "Could not post to " + par1URL, exception);
                }
            }

            return "";
        }
    }

    @SideOnly(Side.CLIENT)
    public static int func_76181_a() throws IOException
    {
        ServerSocket serversocket = null;
        boolean flag = true;
        int i;

        try
        {
            serversocket = new ServerSocket(0);
            i = serversocket.getLocalPort();
        }
        finally
        {
            try
            {
                if (serversocket != null)
                {
                    serversocket.close();
                }
            }
            catch (IOException ioexception)
            {
                ;
            }
        }

        return i;
    }
}
