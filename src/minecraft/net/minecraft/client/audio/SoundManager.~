package net.minecraft.client.audio;

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
    private static final String[] field_130084_a = new String[] {"ogg"};
    public SoundSystem field_77381_a;
    private boolean field_77376_g;
    public final SoundPool field_77379_b;
    public final SoundPool field_77380_c;
    public final SoundPool field_77377_d;
    private int field_77378_e;
    private final GameSettings field_77375_f;
    private final File field_130085_i;
    private final Set field_82470_g = new HashSet();
    private final List field_92072_h = new ArrayList();
    private Random field_77382_h = new Random();
    private int field_77383_i;

    public SoundManager(ResourceManager p_i1326_1_, GameSettings p_i1326_2_, File p_i1326_3_)
    {
        this.field_77383_i = this.field_77382_h.nextInt(12000);
        this.field_77375_f = p_i1326_2_;
        this.field_130085_i = p_i1326_3_;
        this.field_77379_b = new SoundPool(p_i1326_1_, "sound", true);
        this.field_77380_c = new SoundPool(p_i1326_1_, "records", false);
        this.field_77377_d = new SoundPool(p_i1326_1_, "music", true);

        try
        {
            SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
            SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
            SoundSystemConfig.setCodec("wav", CodecWav.class);
        }
        catch (SoundSystemException soundsystemexception)
        {
            soundsystemexception.printStackTrace();
            System.err.println("error linking with the LibraryJavaSound plug-in");
        }

        this.func_130083_h();
    }

    public void func_110549_a(ResourceManager p_110549_1_)
    {
        this.func_82464_d();
        this.func_77370_b();
        this.func_77363_d();
    }

    private void func_130083_h()
    {
        if (this.field_130085_i.isDirectory())
        {
            Collection collection = FileUtils.listFiles(this.field_130085_i, field_130084_a, true);
            Iterator iterator = collection.iterator();

            while (iterator.hasNext())
            {
                File file1 = (File)iterator.next();
                this.func_130081_a(file1);
            }
        }
    }

    private void func_130081_a(File p_130081_1_)
    {
        String s = this.field_130085_i.toURI().relativize(p_130081_1_.toURI()).getPath();
        int i = s.indexOf("/");

        if (i != -1)
        {
            String s1 = s.substring(0, i);
            s = s.substring(i + 1);

            if ("sound".equalsIgnoreCase(s1))
            {
                this.func_77372_a(s);
            }
            else if ("records".equalsIgnoreCase(s1))
            {
                this.func_77374_b(s);
            }
            else if ("music".equalsIgnoreCase(s1))
            {
                this.func_77365_c(s);
            }
        }
    }

    private synchronized void func_77363_d()
    {
        if (!this.field_77376_g)
        {
            float f = this.field_77375_f.field_74340_b;
            float f1 = this.field_77375_f.field_74342_a;
            this.field_77375_f.field_74340_b = 0.0F;
            this.field_77375_f.field_74342_a = 0.0F;
            this.field_77375_f.func_74303_b();

            try
            {
                (new Thread(new SoundManagerINNER1(this))).start();
                this.field_77375_f.field_74340_b = f;
                this.field_77375_f.field_74342_a = f1;
            }
            catch (RuntimeException runtimeexception)
            {
                runtimeexception.printStackTrace();
                System.err.println("error starting SoundSystem turning off sounds & music");
                this.field_77375_f.field_74340_b = 0.0F;
                this.field_77375_f.field_74342_a = 0.0F;
            }

            this.field_77375_f.func_74303_b();
        }
    }

    public void func_77367_a()
    {
        if (this.field_77376_g)
        {
            if (this.field_77375_f.field_74342_a == 0.0F)
            {
                this.field_77381_a.stop("BgMusic");
                this.field_77381_a.stop("streaming");
            }
            else
            {
                this.field_77381_a.setVolume("BgMusic", this.field_77375_f.field_74342_a);
                this.field_77381_a.setVolume("streaming", this.field_77375_f.field_74342_a);
            }
        }
    }

    public void func_77370_b()
    {
        if (this.field_77376_g)
        {
            this.field_77381_a.cleanup();
            this.field_77376_g = false;
        }
    }

    public void func_77372_a(String p_77372_1_)
    {
        this.field_77379_b.func_77459_a(p_77372_1_);
    }

    public void func_77374_b(String p_77374_1_)
    {
        this.field_77380_c.func_77459_a(p_77374_1_);
    }

    public void func_77365_c(String p_77365_1_)
    {
        this.field_77377_d.func_77459_a(p_77365_1_);
    }

    public void func_77371_c()
    {
        if (this.field_77376_g && this.field_77375_f.field_74342_a != 0.0F)
        {
            if (!this.field_77381_a.playing("BgMusic") && !this.field_77381_a.playing("streaming"))
            {
                if (this.field_77383_i > 0)
                {
                    --this.field_77383_i;
                }
                else
                {
                    SoundPoolEntry soundpoolentry = this.field_77377_d.func_77460_a();

                    if (soundpoolentry != null)
                    {
                        this.field_77383_i = this.field_77382_h.nextInt(12000) + 12000;
                        this.field_77381_a.backgroundMusic("BgMusic", soundpoolentry.func_110457_b(), soundpoolentry.func_110458_a(), false);
                        this.field_77381_a.setVolume("BgMusic", this.field_77375_f.field_74342_a);
                        this.field_77381_a.play("BgMusic");
                    }
                }
            }
        }
    }

    public void func_77369_a(EntityLivingBase p_77369_1_, float p_77369_2_)
    {
        if (this.field_77376_g && this.field_77375_f.field_74340_b != 0.0F && p_77369_1_ != null)
        {
            float f1 = p_77369_1_.field_70127_C + (p_77369_1_.field_70125_A - p_77369_1_.field_70127_C) * p_77369_2_;
            float f2 = p_77369_1_.field_70126_B + (p_77369_1_.field_70177_z - p_77369_1_.field_70126_B) * p_77369_2_;
            double d0 = p_77369_1_.field_70169_q + (p_77369_1_.field_70165_t - p_77369_1_.field_70169_q) * (double)p_77369_2_;
            double d1 = p_77369_1_.field_70167_r + (p_77369_1_.field_70163_u - p_77369_1_.field_70167_r) * (double)p_77369_2_;
            double d2 = p_77369_1_.field_70166_s + (p_77369_1_.field_70161_v - p_77369_1_.field_70166_s) * (double)p_77369_2_;
            float f3 = MathHelper.func_76134_b(-f2 * 0.017453292F - (float)Math.PI);
            float f4 = MathHelper.func_76126_a(-f2 * 0.017453292F - (float)Math.PI);
            float f5 = -f4;
            float f6 = -MathHelper.func_76126_a(-f1 * 0.017453292F - (float)Math.PI);
            float f7 = -f3;
            float f8 = 0.0F;
            float f9 = 1.0F;
            float f10 = 0.0F;
            this.field_77381_a.setListenerPosition((float)d0, (float)d1, (float)d2);
            this.field_77381_a.setListenerOrientation(f5, f6, f7, f8, f9, f10);
        }
    }

    public void func_82464_d()
    {
        if (this.field_77376_g)
        {
            Iterator iterator = this.field_82470_g.iterator();

            while (iterator.hasNext())
            {
                String s = (String)iterator.next();
                this.field_77381_a.stop(s);
            }

            this.field_82470_g.clear();
        }
    }

    public void func_77368_a(String p_77368_1_, float p_77368_2_, float p_77368_3_, float p_77368_4_)
    {
        if (this.field_77376_g && (this.field_77375_f.field_74340_b != 0.0F || p_77368_1_ == null))
        {
            String s1 = "streaming";

            if (this.field_77381_a.playing(s1))
            {
                this.field_77381_a.stop(s1);
            }

            if (p_77368_1_ != null)
            {
                SoundPoolEntry soundpoolentry = this.field_77380_c.func_77458_a(p_77368_1_);

                if (soundpoolentry != null)
                {
                    if (this.field_77381_a.playing("BgMusic"))
                    {
                        this.field_77381_a.stop("BgMusic");
                    }

                    this.field_77381_a.newStreamingSource(true, s1, soundpoolentry.func_110457_b(), soundpoolentry.func_110458_a(), false, p_77368_2_, p_77368_3_, p_77368_4_, 2, 64.0F);
                    this.field_77381_a.setVolume(s1, 0.5F * this.field_77375_f.field_74340_b);
                    this.field_77381_a.play(s1);
                }
            }
        }
    }

    public void func_82460_a(Entity p_82460_1_)
    {
        this.func_82462_a(p_82460_1_, p_82460_1_);
    }

    public void func_82462_a(Entity p_82462_1_, Entity p_82462_2_)
    {
        String s = "entity_" + p_82462_1_.field_70157_k;

        if (this.field_82470_g.contains(s))
        {
            if (this.field_77381_a.playing(s))
            {
                this.field_77381_a.setPosition(s, (float)p_82462_2_.field_70165_t, (float)p_82462_2_.field_70163_u, (float)p_82462_2_.field_70161_v);
                this.field_77381_a.setVelocity(s, (float)p_82462_2_.field_70159_w, (float)p_82462_2_.field_70181_x, (float)p_82462_2_.field_70179_y);
            }
            else
            {
                this.field_82470_g.remove(s);
            }
        }
    }

    public boolean func_82465_b(Entity p_82465_1_)
    {
        if (p_82465_1_ != null && this.field_77376_g)
        {
            String s = "entity_" + p_82465_1_.field_70157_k;
            return this.field_77381_a.playing(s);
        }
        else
        {
            return false;
        }
    }

    public void func_82469_c(Entity p_82469_1_)
    {
        if (p_82469_1_ != null && this.field_77376_g)
        {
            String s = "entity_" + p_82469_1_.field_70157_k;

            if (this.field_82470_g.contains(s))
            {
                if (this.field_77381_a.playing(s))
                {
                    this.field_77381_a.stop(s);
                }

                this.field_82470_g.remove(s);
            }
        }
    }

    public void func_82468_a(Entity p_82468_1_, float p_82468_2_)
    {
        if (p_82468_1_ != null && this.field_77376_g && this.field_77375_f.field_74340_b != 0.0F)
        {
            String s = "entity_" + p_82468_1_.field_70157_k;

            if (this.field_77381_a.playing(s))
            {
                this.field_77381_a.setVolume(s, p_82468_2_ * this.field_77375_f.field_74340_b);
            }
        }
    }

    public void func_82463_b(Entity p_82463_1_, float p_82463_2_)
    {
        if (p_82463_1_ != null && this.field_77376_g && this.field_77375_f.field_74340_b != 0.0F)
        {
            String s = "entity_" + p_82463_1_.field_70157_k;

            if (this.field_77381_a.playing(s))
            {
                this.field_77381_a.setPitch(s, p_82463_2_);
            }
        }
    }

    public void func_82467_a(String p_82467_1_, Entity p_82467_2_, float p_82467_3_, float p_82467_4_, boolean p_82467_5_)
    {
        if (this.field_77376_g && (this.field_77375_f.field_74340_b != 0.0F || p_82467_1_ == null) && p_82467_2_ != null)
        {
            String s1 = "entity_" + p_82467_2_.field_70157_k;

            if (this.field_82470_g.contains(s1))
            {
                this.func_82460_a(p_82467_2_);
            }
            else
            {
                if (this.field_77381_a.playing(s1))
                {
                    this.field_77381_a.stop(s1);
                }

                if (p_82467_1_ != null)
                {
                    SoundPoolEntry soundpoolentry = this.field_77379_b.func_77458_a(p_82467_1_);

                    if (soundpoolentry != null && p_82467_3_ > 0.0F)
                    {
                        float f2 = 16.0F;

                        if (p_82467_3_ > 1.0F)
                        {
                            f2 *= p_82467_3_;
                        }

                        this.field_77381_a.newSource(p_82467_5_, s1, soundpoolentry.func_110457_b(), soundpoolentry.func_110458_a(), false, (float)p_82467_2_.field_70165_t, (float)p_82467_2_.field_70163_u, (float)p_82467_2_.field_70161_v, 2, f2);
                        this.field_77381_a.setLooping(s1, true);
                        this.field_77381_a.setPitch(s1, p_82467_4_);

                        if (p_82467_3_ > 1.0F)
                        {
                            p_82467_3_ = 1.0F;
                        }

                        this.field_77381_a.setVolume(s1, p_82467_3_ * this.field_77375_f.field_74340_b);
                        this.field_77381_a.setVelocity(s1, (float)p_82467_2_.field_70159_w, (float)p_82467_2_.field_70181_x, (float)p_82467_2_.field_70179_y);
                        this.field_77381_a.play(s1);
                        this.field_82470_g.add(s1);
                    }
                }
            }
        }
    }

    public void func_77364_b(String p_77364_1_, float p_77364_2_, float p_77364_3_, float p_77364_4_, float p_77364_5_, float p_77364_6_)
    {
        if (this.field_77376_g && this.field_77375_f.field_74340_b != 0.0F)
        {
            SoundPoolEntry soundpoolentry = this.field_77379_b.func_77458_a(p_77364_1_);

            if (soundpoolentry != null && p_77364_5_ > 0.0F)
            {
                this.field_77378_e = (this.field_77378_e + 1) % 256;
                String s1 = "sound_" + this.field_77378_e;
                float f5 = 16.0F;

                if (p_77364_5_ > 1.0F)
                {
                    f5 *= p_77364_5_;
                }

                this.field_77381_a.newSource(p_77364_5_ > 1.0F, s1, soundpoolentry.func_110457_b(), soundpoolentry.func_110458_a(), false, p_77364_2_, p_77364_3_, p_77364_4_, 2, f5);

                if (p_77364_5_ > 1.0F)
                {
                    p_77364_5_ = 1.0F;
                }

                this.field_77381_a.setPitch(s1, p_77364_6_);
                this.field_77381_a.setVolume(s1, p_77364_5_ * this.field_77375_f.field_74340_b);
                this.field_77381_a.play(s1);
            }
        }
    }

    public void func_77366_a(String p_77366_1_, float p_77366_2_, float p_77366_3_)
    {
        if (this.field_77376_g && this.field_77375_f.field_74340_b != 0.0F)
        {
            SoundPoolEntry soundpoolentry = this.field_77379_b.func_77458_a(p_77366_1_);

            if (soundpoolentry != null && p_77366_2_ > 0.0F)
            {
                this.field_77378_e = (this.field_77378_e + 1) % 256;
                String s1 = "sound_" + this.field_77378_e;
                this.field_77381_a.newSource(false, s1, soundpoolentry.func_110457_b(), soundpoolentry.func_110458_a(), false, 0.0F, 0.0F, 0.0F, 0, 0.0F);

                if (p_77366_2_ > 1.0F)
                {
                    p_77366_2_ = 1.0F;
                }

                p_77366_2_ *= 0.25F;
                this.field_77381_a.setPitch(s1, p_77366_3_);
                this.field_77381_a.setVolume(s1, p_77366_2_ * this.field_77375_f.field_74340_b);
                this.field_77381_a.play(s1);
            }
        }
    }

    public void func_82466_e()
    {
        Iterator iterator = this.field_82470_g.iterator();

        while (iterator.hasNext())
        {
            String s = (String)iterator.next();
            this.field_77381_a.pause(s);
        }
    }

    public void func_82461_f()
    {
        Iterator iterator = this.field_82470_g.iterator();

        while (iterator.hasNext())
        {
            String s = (String)iterator.next();
            this.field_77381_a.play(s);
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
                    this.func_77364_b(scheduledsound.field_92069_a, scheduledsound.field_92067_b, scheduledsound.field_92068_c, scheduledsound.field_92065_d, scheduledsound.field_92066_e, scheduledsound.field_92063_f);
                    iterator.remove();
                }
            }
        }
    }

    public void func_92070_a(String p_92070_1_, float p_92070_2_, float p_92070_3_, float p_92070_4_, float p_92070_5_, float p_92070_6_, int p_92070_7_)
    {
        this.field_92072_h.add(new ScheduledSound(p_92070_1_, p_92070_2_, p_92070_3_, p_92070_4_, p_92070_5_, p_92070_6_, p_92070_7_));
    }

    static SoundSystem func_130080_a(SoundManager p_130080_0_, SoundSystem p_130080_1_)
    {
        return p_130080_0_.field_77381_a = p_130080_1_;
    }

    static boolean func_130082_a(SoundManager p_130082_0_, boolean p_130082_1_)
    {
        return p_130082_0_.field_77376_g = p_130082_1_;
    }
}
