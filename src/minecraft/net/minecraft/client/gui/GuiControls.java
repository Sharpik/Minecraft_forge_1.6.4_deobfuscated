package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;

import net.minecraftforge.client.GuiControlsScrollPanel;

@SideOnly(Side.CLIENT)
public class GuiControls extends GuiScreen
{
    /**
     * A reference to the screen object that created this. Used for navigating between screens.
     */
    private GuiScreen parentScreen;

    /** The title string that is displayed in the top-center of the screen. */
    protected String screenTitle = "Controls";

    /** Reference to the GameSettings object. */
    private GameSettings options;

    /** The ID of the  button that has been pressed. */
    private int buttonId = -1;

    private GuiControlsScrollPanel scrollPane;

    public GuiControls(GuiScreen par1GuiScreen, GameSettings par2GameSettings)
    {
        this.parentScreen = par1GuiScreen;
        this.options = par2GameSettings;
    }

    /**
     * Gets the distance from the left border of the window to left border of the controls screen
     */
    private int getLeftBorder()
    {
        return this.width / 2 - 155;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        scrollPane = new GuiControlsScrollPanel(this, options, mc);
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height - 28, I18n.getString("gui.done")));
        scrollPane.registerScrollButtons(7, 8);
        this.screenTitle = I18n.getString("controls.title");
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.id == 200)
        {
            this.mc.displayGuiScreen(this.parentScreen);
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
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        if (scrollPane.keyTyped(par1, par2))
        {
            super.keyTyped(par1, par2);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground();
        /* Forge Start: Moved all rendering to GuiControlsScrollPanel
        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 16777215);
        int k = this.getLeftBorder();
        int l = 0;

        while (l < this.options.keyBindings.length)
        {
            boolean flag = false;
            int i1 = 0;

            while (true)
            {
                if (i1 < this.options.keyBindings.length)
                {
                    if (i1 == l || this.options.keyBindings[l].keyCode != this.options.keyBindings[i1].keyCode)
                    {
                        ++i1;
                        continue;
                    }

                    flag = true;
                }

                if (this.buttonId == l)
                {
                    ((GuiButton)this.buttonList.get(l)).displayString = "" + EnumChatFormatting.WHITE + "> " + EnumChatFormatting.YELLOW + "??? " + EnumChatFormatting.WHITE + "<";
                }
                else if (flag)
                {
                    ((GuiButton)this.buttonList.get(l)).displayString = EnumChatFormatting.RED + this.options.getOptionDisplayString(l);
                }
                else
                {
                    ((GuiButton)this.buttonList.get(l)).displayString = this.options.getOptionDisplayString(l);
                }

                this.drawString(this.fontRenderer, this.options.getKeyBindingDescription(l), k + l % 2 * 160 + 70 + 6, this.height / 6 + 24 * (l >> 1) + 7, -1);
                ++l;
                break;
            }
        }
        */
        scrollPane.drawScreen(par1, par2, par3);
        drawCenteredString(fontRenderer, screenTitle, width / 2, 4, 0xffffff);
        //Forge End

        super.drawScreen(par1, par2, par3);
    }
}
