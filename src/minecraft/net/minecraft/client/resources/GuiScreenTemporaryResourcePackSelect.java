package net.minecraft.client.resources;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSmallButton;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.EnumOS;
import net.minecraft.util.Util;
import org.lwjgl.Sys;

@SideOnly(Side.CLIENT)
public class GuiScreenTemporaryResourcePackSelect extends GuiScreen
{
    protected GuiScreen field_110347_a;
    private int refreshTimer = -1;
    private GuiScreenTemporaryResourcePackSelectSelectionList field_110346_c;
    private GameSettings field_96146_n;

    public GuiScreenTemporaryResourcePackSelect(GuiScreen par1GuiScreen, GameSettings par2GameSettings)
    {
        this.field_110347_a = par1GuiScreen;
        this.field_96146_n = par2GameSettings;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.buttonList.add(new GuiSmallButton(5, this.width / 2 - 154, this.height - 48, I18n.getString("resourcePack.openFolder")));
        this.buttonList.add(new GuiSmallButton(6, this.width / 2 + 4, this.height - 48, I18n.getString("gui.done")));
        this.field_110346_c = new GuiScreenTemporaryResourcePackSelectSelectionList(this, this.mc.getResourcePackRepository());
        this.field_110346_c.registerScrollButtons(7, 8);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.enabled)
        {
            if (par1GuiButton.id == 5)
            {
                File file1 = GuiScreenTemporaryResourcePackSelectSelectionList.func_110510_a(this.field_110346_c).getDirResourcepacks();
                String s = file1.getAbsolutePath();

                if (Util.getOSType() == EnumOS.MACOS)
                {
                    try
                    {
                        this.mc.getLogAgent().logInfo(s);
                        Runtime.getRuntime().exec(new String[] {"/usr/bin/open", s});
                        return;
                    }
                    catch (IOException ioexception)
                    {
                        ioexception.printStackTrace();
                    }
                }
                else if (Util.getOSType() == EnumOS.WINDOWS)
                {
                    String s1 = String.format("cmd.exe /C start \"Open file\" \"%s\"", new Object[] {s});

                    try
                    {
                        Runtime.getRuntime().exec(s1);
                        return;
                    }
                    catch (IOException ioexception1)
                    {
                        ioexception1.printStackTrace();
                    }
                }

                boolean flag = false;

                try
                {
                    Class oclass = Class.forName("java.awt.Desktop");
                    Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
                    oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {file1.toURI()});
                }
                catch (Throwable throwable)
                {
                    throwable.printStackTrace();
                    flag = true;
                }

                if (flag)
                {
                    this.mc.getLogAgent().logInfo("Opening via system class!");
                    Sys.openURL("file://" + s);
                }
            }
            else if (par1GuiButton.id == 6)
            {
                this.mc.displayGuiScreen(this.field_110347_a);
            }
            else
            {
                this.field_110346_c.actionPerformed(par1GuiButton);
            }
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
    }

    /**
     * Called when the mouse is moved or a mouse button is released.  Signature: (mouseX, mouseY, which) which==-1 is
     * mouseMove, which==0 or which==1 is mouseUp
     */
    protected void mouseMovedOrUp(int par1, int par2, int par3)
    {
        super.mouseMovedOrUp(par1, par2, par3);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        this.field_110346_c.drawScreen(par1, par2, par3);

        if (this.refreshTimer <= 0)
        {
            GuiScreenTemporaryResourcePackSelectSelectionList.func_110510_a(this.field_110346_c).updateRepositoryEntriesAll();
            this.refreshTimer = 20;
        }

        this.drawCenteredString(this.fontRenderer, I18n.getString("resourcePack.title"), this.width / 2, 16, 16777215);
        this.drawCenteredString(this.fontRenderer, I18n.getString("resourcePack.folderInfo"), this.width / 2 - 77, this.height - 26, 8421504);
        super.drawScreen(par1, par2, par3);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        --this.refreshTimer;
    }

    static Minecraft func_110344_a(GuiScreenTemporaryResourcePackSelect par0GuiScreenTemporaryResourcePackSelect)
    {
        return par0GuiScreenTemporaryResourcePackSelect.mc;
    }

    static Minecraft func_110341_b(GuiScreenTemporaryResourcePackSelect par0GuiScreenTemporaryResourcePackSelect)
    {
        return par0GuiScreenTemporaryResourcePackSelect.mc;
    }

    static Minecraft func_110339_c(GuiScreenTemporaryResourcePackSelect par0GuiScreenTemporaryResourcePackSelect)
    {
        return par0GuiScreenTemporaryResourcePackSelect.mc;
    }

    static Minecraft func_110345_d(GuiScreenTemporaryResourcePackSelect par0GuiScreenTemporaryResourcePackSelect)
    {
        return par0GuiScreenTemporaryResourcePackSelect.mc;
    }

    static Minecraft func_110334_e(GuiScreenTemporaryResourcePackSelect par0GuiScreenTemporaryResourcePackSelect)
    {
        return par0GuiScreenTemporaryResourcePackSelect.mc;
    }

    static Minecraft func_110340_f(GuiScreenTemporaryResourcePackSelect par0GuiScreenTemporaryResourcePackSelect)
    {
        return par0GuiScreenTemporaryResourcePackSelect.mc;
    }

    static FontRenderer func_130017_g(GuiScreenTemporaryResourcePackSelect par0GuiScreenTemporaryResourcePackSelect)
    {
        return par0GuiScreenTemporaryResourcePackSelect.fontRenderer;
    }

    static FontRenderer func_130016_h(GuiScreenTemporaryResourcePackSelect par0GuiScreenTemporaryResourcePackSelect)
    {
        return par0GuiScreenTemporaryResourcePackSelect.fontRenderer;
    }

    static FontRenderer func_110337_i(GuiScreenTemporaryResourcePackSelect par0GuiScreenTemporaryResourcePackSelect)
    {
        return par0GuiScreenTemporaryResourcePackSelect.fontRenderer;
    }

    static FontRenderer func_110335_j(GuiScreenTemporaryResourcePackSelect par0GuiScreenTemporaryResourcePackSelect)
    {
        return par0GuiScreenTemporaryResourcePackSelect.fontRenderer;
    }

    static FontRenderer func_110338_k(GuiScreenTemporaryResourcePackSelect par0GuiScreenTemporaryResourcePackSelect)
    {
        return par0GuiScreenTemporaryResourcePackSelect.fontRenderer;
    }
}
