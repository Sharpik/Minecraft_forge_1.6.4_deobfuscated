package net.minecraft.client.gui.mco;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.TaskLongRunning;
import net.minecraft.client.mco.ExceptionMcoService;
import net.minecraft.client.mco.McoClient;
import net.minecraft.client.mco.WorldTemplate;
import net.minecraft.client.resources.I18n;

@SideOnly(Side.CLIENT)
class TaskResetWorld extends TaskLongRunning
{
    private final long field_96591_c;
    private final String field_104066_d;
    private final WorldTemplate field_111252_e;

    final GuiScreenResetWorld field_96592_a;

    public TaskResetWorld(GuiScreenResetWorld par1GuiScreenResetWorld, long par2, String par4Str, WorldTemplate par5WorldTemplate)
    {
        this.field_96592_a = par1GuiScreenResetWorld;
        this.field_96591_c = par2;
        this.field_104066_d = par4Str;
        this.field_111252_e = par5WorldTemplate;
    }

    public void run()
    {
        McoClient mcoclient = new McoClient(this.getMinecraft().getSession());
        String s = I18n.getString("mco.reset.world.resetting.screen.title");
        this.setMessage(s);

        try
        {
            if (this.field_111252_e != null)
            {
                mcoclient.func_111233_e(this.field_96591_c, this.field_111252_e.field_110734_a);
            }
            else
            {
                mcoclient.func_96376_d(this.field_96591_c, this.field_104066_d);
            }

            GuiScreenResetWorld.func_96147_b(this.field_96592_a).displayGuiScreen(GuiScreenResetWorld.func_96148_a(this.field_96592_a));
        }
        catch (ExceptionMcoService exceptionmcoservice)
        {
            GuiScreenResetWorld.func_130025_c(this.field_96592_a).getLogAgent().logSevere(exceptionmcoservice.toString());
            this.setFailedMessage(exceptionmcoservice.toString());
        }
        catch (Exception exception)
        {
            GuiScreenResetWorld.func_130024_d(this.field_96592_a).getLogAgent().logWarning("Realms: ");
            this.setFailedMessage(exception.toString());
        }
    }
}
