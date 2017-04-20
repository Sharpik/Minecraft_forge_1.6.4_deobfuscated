package net.minecraft.client.audio;

import net.minecraftforge.client.*;
import net.minecraftforge.client.event.sound.*;
import net.minecraftforge.common.MinecraftForge;
import static net.minecraftforge.client.event.sound.SoundEvent.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.minecraft.client.resources.ResourceManager;
import net.minecraft.client.resources.ResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import org.apache.commons.io.FileUtils;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.codecs.CodecWav;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;

@SideOnly(Side.CLIENT)
public class SoundManager implements ResourceManagerReloadListener
{
    public boolean LOAD_SOUND_SYSTEM = true;
    private static final String[] field_130084_a = new String[] {"ogg"};

    /** A reference to the sound system. */
    public SoundSystem sndSystem;

    /** Set to true when the SoundManager has been initialised. */
    private boolean loaded;

    /** Sound pool containing sounds. */
    public final SoundPool soundPoolSounds;

    /** Sound pool containing streaming audio. */
    public final SoundPool soundPoolStreaming;

    /** Sound pool containing music. */
    public final SoundPool soundPoolMusic;

    /**
     * The last ID used when a sound is played, passed into SoundSystem to give active sounds a unique ID
     */
    private int latestSoundID;

    /** A reference to the game settings. */
    private final GameSettings options;
    private final File fileAssets;

    /** Identifiers of all currently playing sounds. Type: HashSet<String> */
    private final Set playingSounds = new HashSet();
    private final List field_92072_h = new ArrayList();

    /** RNG. */
    private Random rand = new Random();
    private int ticksBeforeMusic;

    public static int MUSIC_INTERVAL = 12000;

    public SoundManager(ResourceManager par1ResourceManager, GameSettings par2GameSettings, File par3File)
    {
        this.ticksBeforeMusic = this.rand.nextInt(MUSIC_INTERVAL);
        this.options = par2GameSettings;
        this.fileAssets = par3File;
        this.soundPoolSounds = new SoundPool(par1ResourceManager, "sound", true);
        this.soundPoolStreaming = new SoundPool(par1ResourceManager, "records", false);
        this.soundPoolMusic = new SoundPool(par1ResourceManager, "music", true);

        try
        {
            SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
            SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
            SoundSystemConfig.setCodec("wav", CodecWav.class);
            MinecraftForge.EVENT_BUS.post(new SoundSetupEvent(this));
        }
        catch (SoundSystemException soundsystemexception)
        {
            soundsystemexception.printStackTrace();
            System.err.println("error linking with the LibraryJavaSound plug-in");
        }

        this.loadSounds();
    }

    public void onResourceManagerReload(ResourceManager par1ResourceManager)
    {
        if (!this.LOAD_SOUND_SYSTEM) return; //Stop the sound system from initializing three times at startup. Causing race conditions on some machines.
        this.stopAllSounds();
        this.cleanup();
        this.tryToSetLibraryAndCodecs();
        MinecraftForge.EVENT_BUS.post(new SoundLoadEvent(this));
    }

    private void loadSounds()
    {
        if (this.fileAssets.isDirectory())
        {
            Collection collection = FileUtils.listFiles(this.fileAssets, field_130084_a, true);
            Iterator iterator = collection.iterator();

            while (iterator.hasNext())
            {
                File file1 = (File)iterator.next();
                this.loadSoundFile(file1);
            }
        }
    }

    private void loadSoundFile(File par1File)
    {
        String s = this.fileAssets.toURI().relativize(par1File.toURI()).getPath();
        int i = s.indexOf("/");

        if (i != -1)
        {
            String s1 = s.substring(0, i);
            s = s.substring(i + 1);

            if ("sound".equalsIgnoreCase(s1))
            {
                this.addSound(s);
            }
            else if ("records".equalsIgnoreCase(s1))
            {
                this.addStreaming(s);
            }
            else if ("music".equalsIgnoreCase(s1))
            {
                this.addMusic(s);
            }
        }
    }

    /**
     * Tries to add the paulscode library and the relevant codecs. If it fails, the volumes (sound and music) will be
     * set to zero in the options file.
     */
    private synchronized void tryToSetLibraryAndCodecs()
    {
        if (!this.loaded)
        {
            float f = this.options.soundVolume;
            float f1 = this.options.musicVolume;
            this.options.soundVolume = 0.0F;
            this.options.musicVolume = 0.0F;
            this.options.saveOptions();

            try
            {
                (new Thread(new SoundManagerINNER1(this))).start();
                this.options.soundVolume = f;
                this.options.musicVolume = f1;
            }
            catch (RuntimeException runtimeexception)
            {
                runtimeexception.printStackTrace();
                System.err.println("error starting SoundSystem turning off sounds & music");
                this.options.soundVolume = 0.0F;
                this.options.musicVolume = 0.0F;
            }

            this.options.saveOptions();
        }
    }

    /**
     * Called when one of the sound level options has changed.
     */
    public void onSoundOptionsChanged()
    {
        if (this.loaded)
        {
            if (this.options.musicVolume == 0.0F)
            {
                this.sndSystem.stop("BgMusic");
                this.sndSystem.stop("streaming");
            }
            else
            {
                this.sndSystem.setVolume("BgMusic", this.options.musicVolume);
                this.sndSystem.setVolume("streaming", this.options.musicVolume);
            }
        }
    }

    /**
     * Cleans up the Sound System
     */
    public void cleanup()
    {
        if (this.loaded)
        {
            this.sndSystem.cleanup();
            this.loaded = false;
        }
    }

    /**
     * Adds a sounds with the name from the file. Args: name, file
     */
    public void addSound(String par1Str)
    {
        this.soundPoolSounds.addSound(par1Str);
    }

    /**
     * Adds an audio file to the streaming SoundPool.
     */
    public void addStreaming(String par1Str)
    {
        this.soundPoolStreaming.addSound(par1Str);
    }

    /**
     * Adds an audio file to the music SoundPool.
     */
    public void addMusic(String par1Str)
    {
        this.soundPoolMusic.addSound(par1Str);
    }

    /**
     * If its time to play new music it starts it up.
     */
    public void playRandomMusicIfReady()
    {
        if (this.loaded && this.options.musicVolume != 0.0F)
        {
            if (!this.sndSystem.playing("BgMusic") && !this.sndSystem.playing("streaming"))
            {
                if (this.ticksBeforeMusic > 0)
                {
                    --this.ticksBeforeMusic;
                }
                else
                {
                    SoundPoolEntry soundpoolentry = this.soundPoolMusic.getRandomSound();
                    soundpoolentry = SoundEvent.getResult(new PlayBackgroundMusicEvent(this, soundpoolentry));

                    if (soundpoolentry != null)
                    {
                        this.ticksBeforeMusic = this.rand.nextInt(MUSIC_INTERVAL) + MUSIC_INTERVAL;
                        this.sndSystem.backgroundMusic("BgMusic", soundpoolentry.getSoundUrl(), soundpoolentry.getSoundName(), false);
                        this.sndSystem.setVolume("BgMusic", this.options.musicVolume);
                        this.sndSystem.play("BgMusic");
                    }
                }
            }
        }
    }

    /**
     * Sets the listener of sounds
     */
    public void setListener(EntityLivingBase par1EntityLivingBase, float par2)
    {
        if (this.loaded && this.options.soundVolume != 0.0F && par1EntityLivingBase != null)
        {
            float f1 = par1EntityLivingBase.prevRotationPitch + (par1EntityLivingBase.rotationPitch - par1EntityLivingBase.prevRotationPitch) * par2;
            float f2 = par1EntityLivingBase.prevRotationYaw + (par1EntityLivingBase.rotationYaw - par1EntityLivingBase.prevRotationYaw) * par2;
            double d0 = par1EntityLivingBase.prevPosX + (par1EntityLivingBase.posX - par1EntityLivingBase.prevPosX) * (double)par2;
            double d1 = par1EntityLivingBase.prevPosY + (par1EntityLivingBase.posY - par1EntityLivingBase.prevPosY) * (double)par2;
            double d2 = par1EntityLivingBase.prevPosZ + (par1EntityLivingBase.posZ - par1EntityLivingBase.prevPosZ) * (double)par2;
            float f3 = MathHelper.cos(-f2 * 0.017453292F - (float)Math.PI);
            float f4 = MathHelper.sin(-f2 * 0.017453292F - (float)Math.PI);
            float f5 = -f4;
            float f6 = -MathHelper.sin(-f1 * 0.017453292F - (float)Math.PI);
            float f7 = -f3;
            float f8 = 0.0F;
            float f9 = 1.0F;
            float f10 = 0.0F;
            this.sndSystem.setListenerPosition((float)d0, (float)d1, (float)d2);
            this.sndSystem.setListenerOrientation(f5, f6, f7, f8, f9, f10);
        }
    }

    /**
     * Stops all currently playing sounds
     */
    public void stopAllSounds()
    {
        if (this.loaded)
        {
            Iterator iterator = this.playingSounds.iterator();

            while (iterator.hasNext())
            {
                String s = (String)iterator.next();
                this.sndSystem.stop(s);
            }

            this.playingSounds.clear();
        }
    }

    public void playStreaming(String par1Str, float par2, float par3, float par4)
    {
        if (this.loaded && (this.options.soundVolume != 0.0F || par1Str == null))
        {
            String s1 = "streaming";

            if (this.sndSystem.playing(s1))
            {
                this.sndSystem.stop(s1);
            }

            if (par1Str != null)
            {
                SoundPoolEntry soundpoolentry = this.soundPoolStreaming.getRandomSoundFromSoundPool(par1Str);
                soundpoolentry = SoundEvent.getResult(new PlayStreamingEvent(this, soundpoolentry, par1Str, par2, par3, par4));

                if (soundpoolentry != null)
                {
                    if (this.sndSystem.playing("BgMusic"))
                    {
                        this.sndSystem.stop("BgMusic");
                    }

                    this.sndSystem.newStreamingSource(true, s1, soundpoolentry.getSoundUrl(), soundpoolentry.getSoundName(), false, par2, par3, par4, 2, 64.0F);
                    this.sndSystem.setVolume(s1, 0.5F * this.options.soundVolume);
                    MinecraftForge.EVENT_BUS.post(new PlayStreamingSourceEvent(this, s1, par2, par3, par4));
                    this.sndSystem.play(s1);
                }
            }
        }
    }

    /**
     * Updates the sound associated with the entity with that entity's position and velocity. Args: the entity
     */
    public void updateSoundLocation(Entity par1Entity)
    {
        this.updateSoundLocation(par1Entity, par1Entity);
    }

    /**
     * Updates the sound associated with soundEntity with the position and velocity of trackEntity. Args: soundEntity,
     * trackEntity
     */
    public void updateSoundLocation(Entity par1Entity, Entity par2Entity)
    {
        String s = "entity_" + par1Entity.entityId;

        if (this.playingSounds.contains(s))
        {
            if (this.sndSystem.playing(s))
            {
                this.sndSystem.setPosition(s, (float)par2Entity.posX, (float)par2Entity.posY, (float)par2Entity.posZ);
                this.sndSystem.setVelocity(s, (float)par2Entity.motionX, (float)par2Entity.motionY, (float)par2Entity.motionZ);
            }
            else
            {
                this.playingSounds.remove(s);
            }
        }
    }

    /**
     * Returns true if a sound is currently associated with the given entity, or false otherwise.
     */
    public boolean isEntitySoundPlaying(Entity par1Entity)
    {
        if (par1Entity != null && this.loaded)
        {
            String s = "entity_" + par1Entity.entityId;
            return this.sndSystem.playing(s);
        }
        else
        {
            return false;
        }
    }

    /**
     * Stops playing the sound associated with the given entity
     */
    public void stopEntitySound(Entity par1Entity)
    {
        if (par1Entity != null && this.loaded)
        {
            String s = "entity_" + par1Entity.entityId;

            if (this.playingSounds.contains(s))
            {
                if (this.sndSystem.playing(s))
                {
                    this.sndSystem.stop(s);
                }

                this.playingSounds.remove(s);
            }
        }
    }

    /**
     * Sets the volume of the sound associated with the given entity, if one is playing. The volume is scaled by the
     * global sound volume. Args: the entity, the volume (from 0 to 1)
     */
    public void setEntitySoundVolume(Entity par1Entity, float par2)
    {
        if (par1Entity != null && this.loaded && this.options.soundVolume != 0.0F)
        {
            String s = "entity_" + par1Entity.entityId;

            if (this.sndSystem.playing(s))
            {
                this.sndSystem.setVolume(s, par2 * this.options.soundVolume);
            }
        }
    }

    /**
     * Sets the pitch of the sound associated with the given entity, if one is playing. Args: the entity, the pitch
     */
    public void setEntitySoundPitch(Entity par1Entity, float par2)
    {
        if (par1Entity != null && this.loaded && this.options.soundVolume != 0.0F)
        {
            String s = "entity_" + par1Entity.entityId;

            if (this.sndSystem.playing(s))
            {
                this.sndSystem.setPitch(s, par2);
            }
        }
    }

    /**
     * If a sound is already playing from the given entity, update the position and velocity of that sound to match the
     * entity. Otherwise, start playing a sound from that entity. Setting the last flag to true will prevent other
     * sounds from overriding this one. Args: The sound name, the entity, the volume, the pitch, priority
     */
    public void playEntitySound(String par1Str, Entity par2Entity, float par3, float par4, boolean par5)
    {
        if (this.loaded && (this.options.soundVolume != 0.0F || par1Str == null) && par2Entity != null)
        {
            String s1 = "entity_" + par2Entity.entityId;

            if (this.playingSounds.contains(s1))
            {
                this.updateSoundLocation(par2Entity);
            }
            else
            {
                if (this.sndSystem.playing(s1))
                {
                    this.sndSystem.stop(s1);
                }

                if (par1Str != null)
                {
                    SoundPoolEntry soundpoolentry = this.soundPoolSounds.getRandomSoundFromSoundPool(par1Str);

                    if (soundpoolentry != null && par3 > 0.0F)
                    {
                        float f2 = 16.0F;

                        if (par3 > 1.0F)
                        {
                            f2 *= par3;
                        }

                        this.sndSystem.newSource(par5, s1, soundpoolentry.getSoundUrl(), soundpoolentry.getSoundName(), false, (float)par2Entity.posX, (float)par2Entity.posY, (float)par2Entity.posZ, 2, f2);
                        this.sndSystem.setLooping(s1, true);
                        this.sndSystem.setPitch(s1, par4);

                        if (par3 > 1.0F)
                        {
                            par3 = 1.0F;
                        }

                        this.sndSystem.setVolume(s1, par3 * this.options.soundVolume);
                        this.sndSystem.setVelocity(s1, (float)par2Entity.motionX, (float)par2Entity.motionY, (float)par2Entity.motionZ);
                        this.sndSystem.play(s1);
                        this.playingSounds.add(s1);
                    }
                }
            }
        }
    }

    /**
     * Plays a sound. Args: soundName, x, y, z, volume, pitch
     */
    public void playSound(String par1Str, float par2, float par3, float par4, float par5, float par6)
    {
        if (this.loaded && this.options.soundVolume != 0.0F)
        {
            SoundPoolEntry soundpoolentry = this.soundPoolSounds.getRandomSoundFromSoundPool(par1Str);
            soundpoolentry = SoundEvent.getResult(new PlaySoundEvent(this, soundpoolentry, par1Str, par2, par3, par4, par5, par6));

            if (soundpoolentry != null && par5 > 0.0F)
            {
                this.latestSoundID = (this.latestSoundID + 1) % 256;
                String s1 = "sound_" + this.latestSoundID;
                float f5 = 16.0F;

                if (par5 > 1.0F)
                {
                    f5 *= par5;
                }

                this.sndSystem.newSource(par5 > 1.0F, s1, soundpoolentry.getSoundUrl(), soundpoolentry.getSoundName(), false, par2, par3, par4, 2, f5);

                if (par5 > 1.0F)
                {
                    par5 = 1.0F;
                }

                this.sndSystem.setPitch(s1, par6);
                this.sndSystem.setVolume(s1, par5 * this.options.soundVolume);
                MinecraftForge.EVENT_BUS.post(new PlaySoundSourceEvent(this, s1, par2, par3, par4));
                this.sndSystem.play(s1);
            }
        }
    }

    /**
     * Plays a sound effect with the volume and pitch of the parameters passed. The sound isn't affected by position of
     * the player (full volume and center balanced)
     */
    public void playSoundFX(String par1Str, float par2, float par3)
    {
        if (this.loaded && this.options.soundVolume != 0.0F)
        {
            SoundPoolEntry soundpoolentry = this.soundPoolSounds.getRandomSoundFromSoundPool(par1Str);
            soundpoolentry = SoundEvent.getResult(new PlaySoundEffectEvent(this, soundpoolentry, par1Str, par2, par3));

            if (soundpoolentry != null && par2 > 0.0F)
            {
                this.latestSoundID = (this.latestSoundID + 1) % 256;
                String s1 = "sound_" + this.latestSoundID;
                this.sndSystem.newSource(false, s1, soundpoolentry.getSoundUrl(), soundpoolentry.getSoundName(), false, 0.0F, 0.0F, 0.0F, 0, 0.0F);

                if (par2 > 1.0F)
                {
                    par2 = 1.0F;
                }

                par2 *= 0.25F;
                this.sndSystem.setPitch(s1, par3);
                this.sndSystem.setVolume(s1, par2 * this.options.soundVolume);
                MinecraftForge.EVENT_BUS.post(new PlaySoundEffectSourceEvent(this, s1));
                this.sndSystem.play(s1);
            }
        }
    }

    /**
     * Pauses all currently playing sounds
     */
    public void pauseAllSounds()
    {
        Iterator iterator = this.playingSounds.iterator();

        while (iterator.hasNext())
        {
            String s = (String)iterator.next();
            this.sndSystem.pause(s);
        }
    }

    /**
     * Resumes playing all currently playing sounds (after pauseAllSounds)
     */
    public void resumeAllSounds()
    {
        Iterator iterator = this.playingSounds.iterator();

        while (iterator.hasNext())
        {
            String s = (String)iterator.next();
            this.sndSystem.play(s);
        }
    }

    public void func_92071_g()
    {
        if (!this.field_92072_h.isEmpty())
        {
            Iterator iterator = this.field_92072_h.iterator();

            while (iterator.hasNext())
            {
                ScheduledSound scheduledsound = (ScheduledSound)iterator.next();
                --scheduledsound.field_92064_g;

                if (scheduledsound.field_92064_g <= 0)
                {
                    this.playSound(scheduledsound.field_92069_a, scheduledsound.field_92067_b, scheduledsound.field_92068_c, scheduledsound.field_92065_d, scheduledsound.field_92066_e, scheduledsound.field_92063_f);
                    iterator.remove();
                }
            }
        }
    }

    public void func_92070_a(String par1Str, float par2, float par3, float par4, float par5, float par6, int par7)
    {
        this.field_92072_h.add(new ScheduledSound(par1Str, par2, par3, par4, par5, par6, par7));
    }

    static SoundSystem func_130080_a(SoundManager par0SoundManager, SoundSystem par1SoundSystem)
    {
        return par0SoundManager.sndSystem = par1SoundSystem;
    }

    static boolean func_130082_a(SoundManager par0SoundManager, boolean par1)
    {
        return par0SoundManager.loaded = par1;
    }
}
