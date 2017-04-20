package net.minecraft.client.mco;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.TimerTask;
import net.minecraft.client.Minecraft;

@SideOnly(Side.CLIENT)
class McoServerListUpdateTask extends TimerTask
{
    private McoClient field_140066_b;

    final McoServerList field_140067_a;

    private McoServerListUpdateTask(McoServerList par1McoServerList)
    {
        this.field_140067_a = par1McoServerList;
    }

    public void run()
    {
        if (!McoServerList.func_98249_b(this.field_140067_a))
        {
            this.func_140064_c();
            this.func_140062_a();
            this.func_140063_b();
        }
    }

    private void func_140062_a()
    {
        try
        {
            if (McoServerList.func_100014_a(this.field_140067_a) != null)
            {
                this.field_140066_b = new McoClient(McoServerList.func_100014_a(this.field_140067_a));
                List list = this.field_140066_b.func_96382_a().field_96430_d;

                if (list != null)
                {
                    this.func_140065_a(list);
                    McoServerList.func_98247_a(this.field_140067_a, list);
                }
            }
        }
        catch (ExceptionMcoService exceptionmcoservice)
        {
            Minecraft.getMinecraft().getLogAgent().logSevere(exceptionmcoservice.toString());
        }
        catch (IOException ioexception)
        {
            Minecraft.getMinecraft().getLogAgent().logWarning("Realms: could not parse response from server");
        }
    }

    private void func_140063_b()
    {
        try
        {
            if (McoServerList.func_100014_a(this.field_140067_a) != null)
            {
                int i = this.field_140066_b.func_130106_e();
                McoServerList.func_130122_a(this.field_140067_a, i);
            }
        }
        catch (ExceptionMcoService exceptionmcoservice)
        {
            Minecraft.getMinecraft().getLogAgent().logSevere(exceptionmcoservice.toString());
        }
    }

    private void func_140064_c()
    {
        try
        {
            if (McoServerList.func_100014_a(this.field_140067_a) != null)
            {
                McoClient mcoclient = new McoClient(McoServerList.func_100014_a(this.field_140067_a));
                McoServerList.func_140057_b(this.field_140067_a, mcoclient.func_96379_c());
            }
        }
        catch (ExceptionMcoService exceptionmcoservice)
        {
            Minecraft.getMinecraft().getLogAgent().logSevere(exceptionmcoservice.toString());
            McoServerList.func_140057_b(this.field_140067_a, 0);
        }
    }

    private void func_140065_a(List par1List)
    {
        Collections.sort(par1List, new McoServerListUpdateTaskComparator(this, McoServerList.func_100014_a(this.field_140067_a).getUsername(), (McoServerListEmptyAnon)null));
    }

    McoServerListUpdateTask(McoServerList par1McoServerList, McoServerListEmptyAnon par2McoServerListEmptyAnon)
    {
        this(par1McoServerList);
    }
}
