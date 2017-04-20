package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.mco.ExceptionMcoService;
import net.minecraft.client.mco.McoClient;
import net.minecraft.client.mco.McoServer;

@SideOnly(Side.CLIENT)
class ThreadOnlineScreen extends Thread
{
    final GuiScreenOnlineServers field_98173_a;

    ThreadOnlineScreen(GuiScreenOnlineServers par1GuiScreenOnlineServers)
    {
        this.field_98173_a = par1GuiScreenOnlineServers;
    }

    public void run()
    {
        try
        {
            McoServer mcoserver = GuiScreenOnlineServers.func_140011_a(this.field_98173_a, GuiScreenOnlineServers.func_140041_a(this.field_98173_a));

            if (mcoserver != null)
            {
                McoClient mcoclient = new McoClient(GuiScreenOnlineServers.func_98075_b(this.field_98173_a).getSession());
                GuiScreenOnlineServers.func_140040_h().func_140058_a(mcoserver);
                GuiScreenOnlineServers.func_140013_c(this.field_98173_a).remove(mcoserver);
                mcoclient.func_140055_c(mcoserver.field_96408_a);
                GuiScreenOnlineServers.func_140040_h().func_140058_a(mcoserver);
                GuiScreenOnlineServers.func_140013_c(this.field_98173_a).remove(mcoserver);
                GuiScreenOnlineServers.func_140017_d(this.field_98173_a);
            }
        }
        catch (ExceptionMcoService exceptionmcoservice)
        {
            GuiScreenOnlineServers.func_98076_f(this.field_98173_a).getLogAgent().logSevere(exceptionmcoservice.toString());
        }
    }
}
