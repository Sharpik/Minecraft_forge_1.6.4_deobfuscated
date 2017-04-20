package net.minecraft.client.gui.mco;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.mco.ExceptionMcoService;
import net.minecraft.client.mco.McoClient;

@SideOnly(Side.CLIENT)
class GuiScreenPendingInvitationINNER1 extends Thread
{
    final GuiScreenPendingInvitation field_130121_a;

    GuiScreenPendingInvitationINNER1(GuiScreenPendingInvitation par1GuiScreenPendingInvitation)
    {
        this.field_130121_a = par1GuiScreenPendingInvitation;
    }

    public void run()
    {
        McoClient mcoclient = new McoClient(GuiScreenPendingInvitation.func_130048_a(this.field_130121_a).getSession());

        try
        {
            GuiScreenPendingInvitation.func_130043_a(this.field_130121_a, mcoclient.func_130108_f().field_130096_a);
        }
        catch (ExceptionMcoService exceptionmcoservice)
        {
            GuiScreenPendingInvitation.func_130044_b(this.field_130121_a).getLogAgent().logSevere(exceptionmcoservice.toString());
        }
    }
}
