package net.minecraft.client.mco;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

@SideOnly(Side.CLIENT)
public class McoServerList
{
    private volatile boolean field_98259_a;
    private McoServerListUpdateTask field_98257_b = new McoServerListUpdateTask(this, (McoServerListEmptyAnon)null);
    private Timer field_98258_c = new Timer();
    private Set field_140060_d = Sets.newHashSet();
    private List field_98255_d = Lists.newArrayList();
    private int field_130130_e;
    private boolean field_140059_g;
    private Session field_98254_f;
    private int field_140061_i;

    public McoServerList()
    {
        this.field_98258_c.schedule(this.field_98257_b, 0L, 10000L);
        this.field_98254_f = Minecraft.getMinecraft().getSession();
    }

    public synchronized void func_130129_a(Session par1Session)
    {
        this.field_98254_f = par1Session;

        if (this.field_98259_a)
        {
            this.field_98259_a = false;
            this.field_98257_b = new McoServerListUpdateTask(this, (McoServerListEmptyAnon)null);
            this.field_98258_c = new Timer();
            this.field_98258_c.schedule(this.field_98257_b, 0L, 10000L);
        }
    }

    public synchronized boolean func_130127_a()
    {
        return this.field_140059_g;
    }

    public synchronized void func_98250_b()
    {
        this.field_140059_g = false;
    }

    public synchronized List func_98252_c()
    {
        return Lists.newArrayList(this.field_98255_d);
    }

    public int func_130124_d()
    {
        return this.field_130130_e;
    }

    public int func_140056_e()
    {
        return this.field_140061_i;
    }

    public synchronized void func_98248_d()
    {
        this.field_98259_a = true;
        this.field_98257_b.cancel();
        this.field_98258_c.cancel();
    }

    private synchronized void func_96426_a(List par1List)
    {
        int i = 0;
        Iterator iterator = this.field_140060_d.iterator();

        while (iterator.hasNext())
        {
            McoServer mcoserver = (McoServer)iterator.next();

            if (par1List.remove(mcoserver))
            {
                ++i;
            }
        }

        if (i == 0)
        {
            this.field_140060_d.clear();
        }

        this.field_98255_d = par1List;
        this.field_140059_g = true;
    }

    public synchronized void func_140058_a(McoServer par1McoServer)
    {
        this.field_98255_d.remove(par1McoServer);
        this.field_140060_d.add(par1McoServer);
    }

    private void func_130123_a(int par1)
    {
        this.field_130130_e = par1;
    }

    static boolean func_98249_b(McoServerList par0McoServerList)
    {
        return par0McoServerList.field_98259_a;
    }

    static Session func_100014_a(McoServerList par0McoServerList)
    {
        return par0McoServerList.field_98254_f;
    }

    static void func_98247_a(McoServerList par0McoServerList, List par1List)
    {
        par0McoServerList.func_96426_a(par1List);
    }

    static void func_130122_a(McoServerList par0McoServerList, int par1)
    {
        par0McoServerList.func_130123_a(par1);
    }

    static int func_140057_b(McoServerList par0McoServerList, int par1)
    {
        return par0McoServerList.field_140061_i = par1;
    }
}
