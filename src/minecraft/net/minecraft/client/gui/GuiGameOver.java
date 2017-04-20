package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiGameOver extends GuiScreen
{
    /**
     * The cooldown timer for the buttons, increases every tick and enables all buttons when reaching 20.
     */
    private int cooldownTimer;

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.buttonList.clear();

        if (this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled())
        {
            if (this.mc.isIntegratedServerRunning())
            {
                this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.getString("deathScreen.deleteWorld")));
            }
            else
            {
                this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.getString("deathScreen.leaveServer")));
            }
        }
        else
        {
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 72, I18n.getString("deathScreen.respawn")));
            this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 96, I18n.getString("deathScreen.titleScreen")));

            if (this.mc.getSession() == null)
            {
                ((GuiButton)this.buttonList.get(1)).enabled = false;
            }
        }

        GuiButton guibutton;

        for (Iterator iterator = this.buttonList.iterator(); iterator.hasNext(); guibutton.enabled = false)
        {
            guibutton = (GuiButton)iterator.next();
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2) {}

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        switch (par1GuiButton.id)
        {
            case 1:
                this.mc.thePlayer.respawnPlayer();
                this.mc.displayGuiScreen((GuiScreen)null);
                break;
            case 2:
                this.mc.theWorld.sendQuittingDisconnectingPacket();
                this.mc.loadWorld((WorldClient)null);
                this.mc.displayGuiScreen(new GuiMainMenu());
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);
        GL11.glPushMatrix();
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        boolean flag = this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled();
        String s = flag ? I18n.getString("deathScreen.title.hardcore") : I18n.getString("deathScreen.title");
        this.drawCenteredString(this.fontRenderer, s, this.width / 2 / 2, 30, 16777215);
        GL11.glPopMatrix();

        if (flag)
        {
            this.drawCenteredString(this.fontRenderer, I18n.getString("deathScreen.hardcoreInfo"), this.width / 2, 144, 16777215);
        }

        this.drawCenteredString(this.fontRenderer, I18n.getString("deathScreen.score") + ": " + EnumChatFormatting.YELLOW + this.mc.thePlayer.getScore(), this.width / 2, 100, 16777215);
        super.drawScreen(par1, par2, par3);
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        ++this.cooldownTimer;
        GuiButton guibutton;

        if (this.cooldownTimer == 20)
        {
            for (Iterator iterator = this.buttonList.iterator(); iterator.hasNext(); guibutton.enabled = true)
            {
                guibutton = (GuiButton)iterator.next();
            }
        }
    }
}
