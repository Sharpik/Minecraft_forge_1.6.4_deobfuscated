package net.minecraft.client.audio;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.client.resources.ResourceManager;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class SoundPool
{
    /** The RNG used by SoundPool. */
    private final Random rand = new Random();

    /**
     * Maps a name (can be sound/newsound/streaming/music/newmusic) to a list of SoundPoolEntry's.
     */
    private final Map nameToSoundPoolEntriesMapping = Maps.newHashMap();
    private final ResourceManager soundResourceManager;
    private final String soundType;
    private final boolean isGetRandomSound;

    public SoundPool(ResourceManager par1ResourceManager, String par2Str, boolean par3)
    {
        this.soundResourceManager = par1ResourceManager;
        this.soundType = par2Str;
        this.isGetRandomSound = par3;
    }

    /**
     * Adds a sound to this sound pool.
     */
    public void addSound(String par1Str)
    {
        try
        {
            String s1 = par1Str;
            par1Str = par1Str.substring(0, par1Str.indexOf("."));

            if (this.isGetRandomSound)
            {
                while (Character.isDigit(par1Str.charAt(par1Str.length() - 1)))
                {
                    par1Str = par1Str.substring(0, par1Str.length() - 1);
                }
            }

            par1Str = par1Str.replaceAll("/", ".");
            Object object = (List)this.nameToSoundPoolEntriesMapping.get(par1Str);

            if (object == null)
            {
                object = Lists.newArrayList();
                this.nameToSoundPoolEntriesMapping.put(par1Str, object);
            }

            ((List)object).add(new SoundPoolEntry(s1, this.func_110654_c(s1)));
        }
        catch (MalformedURLException malformedurlexception)
        {
            malformedurlexception.printStackTrace();
            throw new RuntimeException(malformedurlexception);
        }
    }

    private URL func_110654_c(String par1Str) throws MalformedURLException
    {
        ResourceLocation resourcelocation = new ResourceLocation(par1Str);
        String s1 = String.format("%s:%s:%s/%s", new Object[] {"mcsounddomain", resourcelocation.getResourceDomain(), this.soundType, resourcelocation.getResourcePath()});
        SoundPoolProtocolHandler soundpoolprotocolhandler = new SoundPoolProtocolHandler(this);
        return new URL((URL)null, s1, soundpoolprotocolhandler);
    }

    /**
     * gets a random sound from the specified (by name, can be sound/newsound/streaming/music/newmusic) sound pool.
     */
    public SoundPoolEntry getRandomSoundFromSoundPool(String par1Str)
    {
        List list = (List)this.nameToSoundPoolEntriesMapping.get(par1Str);
        return list == null ? null : (SoundPoolEntry)list.get(this.rand.nextInt(list.size()));
    }

    /**
     * Gets a random SoundPoolEntry.
     */
    public SoundPoolEntry getRandomSound()
    {
        if (this.nameToSoundPoolEntriesMapping.isEmpty())
        {
            return null;
        }
        else
        {
            ArrayList arraylist = Lists.newArrayList(this.nameToSoundPoolEntriesMapping.keySet());
            return this.getRandomSoundFromSoundPool((String)arraylist.get(this.rand.nextInt(arraylist.size())));
        }
    }

    static ResourceManager func_110655_a(SoundPool par0SoundPool)
    {
        return par0SoundPool.soundResourceManager;
    }
}
