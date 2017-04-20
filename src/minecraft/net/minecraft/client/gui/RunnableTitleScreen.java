package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.IOException;
import net.minecraft.client.mco.ExceptionMcoService;
import net.minecraft.client.mco.ExceptionRetryCall;
import net.minecraft.client.mco.McoClient;

@SideOnly(Side.CLIENT)
class RunnableTitleScreen extends Thread
{
    final GuiMainMenu theMainMenu;

    RunnableTitleScreen(GuiMainMenu par1GuiMainMenu)
    {
        this.theMainMenu = par1GuiMainMenu;
    }

    public void run()
    {
        McoClient mcoclient = new McoClient(GuiMainMenu.func_110348_a(this.theMainMenu).getSession());
        boolean flag = false;

        for (int i = 0; i < 3; ++i)
        {
            try
            {
                Boolean obool = mcoclient.func_96375_b();

                if (obool.booleanValue())
                {
                    GuiMainMenu.func_130021_b(this.theMainMenu);
                }

                GuiMainMenu.func_110349_a(obool.booleanValue());
            }
            catch (ExceptionRetryCall exceptionretrycall)
            {
                flag = true;
            }
            catch (ExceptionMcoService exceptionmcoservice)
            {
                GuiMainMenu.func_130018_c(this.theMainMenu).getLogAgent().logSevere(exceptionmcoservice.toString());
            }
            catch (IOException ioexception)
            {
                GuiMainMenu.func_130019_d(this.theMainMenu).getLogAgent().logWarning("Realms: could not parse response");
            }

            if (!flag)
            {
                break;
            }

            try
            {
                Thread.sleep(10000L);
            }
            catch (InterruptedException interruptedexception)
            {
                Thread.currentThread().interrupt();
            }
        }
    }
}
