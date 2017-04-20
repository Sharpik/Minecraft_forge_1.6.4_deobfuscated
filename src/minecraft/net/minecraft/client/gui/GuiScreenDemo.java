package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.net.URI;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiScreenDemo extends GuiScreen
{
    private static final ResourceLocation field_110407_a = new ResourceLocation("textures/gui/demo_background.png");

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.buttonList.clear();
        byte b0 = -16;
        this.buttonList.add(new GuiButton(1, this.width / 2 - 116, this.height / 2 + 62 + b0, 114, 20, I18n.getString("demo.help.buy")));
        this.buttonList.add(new GuiButton(2, this.width / 2 + 2, this.height / 2 + 62 + b0, 114, 20, I18n.getString("demo.help.later")));
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        switch (par1GuiButton.id)
        {
            case 1:
                par1GuiButton.enabled = false;

                try
                {
                    Class oclass = Class.forName("java.awt.Desktop");
                    Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
                    oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {new URI("http://www.minecraft.net/store?source=demo")});
                }
                catch (Throwable throwable)
                {
                    throwable.printStackTrace();
                }

                break;
            case 2:
                this.mc.displayGuiScreen((GuiScreen)null);
                this.mc.setIngameFocus();
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
    }

    /**
     * Draws either a gradient over the background screen (when it exists) or a flat gradient over background.png
     */
    public void drawDefaultBackground()
    {
        super.drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(field_110407_a);
        int i = (this.width - 248) / 2;
        int j = (this.height - 166) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, 248, 166);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground();
        int k = (this.width - 248) / 2 + 10;
        int l = (this.height - 166) / 2 + 8;
        this.fontRenderer.drawString(I18n.getString("demo.help.title"), k, l, 2039583);
        l += 12;
        GameSettings gamesettings = this.mc.gameSettings;
        this.fontRenderer.drawString(I18n.getStringParams("demo.help.movementShort", new Object[] {GameSettings.getKeyDisplayString(gamesettings.keyBindForward.keyCode), GameSettings.getKeyDisplayString(gamesettings.keyBindLeft.keyCode), GameSettings.getKeyDisplayString(gamesettings.keyBindBack.keyCode), GameSettings.getKeyDisplayString(gamesettings.keyBindRight.keyCode)}), k, l, 5197647);
        this.fontRenderer.drawString(I18n.getString("demo.help.movementMouse"), k, l + 12, 5197647);
        this.fontRenderer.drawString(I18n.getStringParams("demo.help.jump", new Object[] {GameSettings.getKeyDisplayString(gamesettings.keyBindJump.keyCode)}), k, l + 24, 5197647);
        this.fontRenderer.drawString(I18n.getStringParams("demo.help.inventory", new Object[] {GameSettings.getKeyDisplayString(gamesettings.keyBindInventory.keyCode)}), k, l + 36, 5197647);
        this.fontRenderer.drawSplitString(I18n.getString("demo.help.fullWrapped"), k, l + 68, 218, 2039583);
        super.drawScreen(par1, par2, par3);
    }
}
