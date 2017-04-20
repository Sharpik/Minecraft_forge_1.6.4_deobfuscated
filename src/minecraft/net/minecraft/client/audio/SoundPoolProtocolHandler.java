package net.minecraft.client.audio;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

@SideOnly(Side.CLIENT)
class SoundPoolProtocolHandler extends URLStreamHandler
{
    final SoundPool theSoundPool;

    SoundPoolProtocolHandler(SoundPool par1SoundPool)
    {
        this.theSoundPool = par1SoundPool;
    }

    protected URLConnection openConnection(URL par1URL)
    {
        return new SoundPoolURLConnection(this.theSoundPool, par1URL, (SoundPoolProtocolHandler)null);
    }
}
