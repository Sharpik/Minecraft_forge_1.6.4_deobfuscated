package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.EnumOptions;
import net.minecraft.client.settings.GameSettings;

@SideOnly(Side.CLIENT)
public class GuiVideoSettings extends GuiScreen
{
    private GuiScreen parentGuiScreen;

    /** The title string that is displayed in the top-center of the screen. */
    protected String screenTitle = "Video Settings";

    /** GUI game settings */
    private GameSettings guiGameSettings;

    /**
     * True if the system is 64-bit (using a simple indexOf test on a system property)
     */
    private boolean is64bit;

    /** An array of all of EnumOption's video options. */
    private static EnumOptions[] videoOptions = new EnumOptions[] {EnumOptions.GRAPHICS, EnumOptions.RENDER_DISTANCE, EnumOptions.AMBIENT_OCCLUSION, EnumOptions.FRAMERATE_LIMIT, EnumOptions.ANAGLYPH, EnumOptions.VIEW_BOBBING, EnumOptions.GUI_SCALE, EnumOptions.ADVANCED_OPENGL, EnumOptions.GAMMA, EnumOptions.RENDER_CLOUDS, EnumOptions.PARTICLES, EnumOptions.USE_SERVER_TEXTURES, EnumOptions.USE_FULLSCREEN, EnumOptions.ENABLE_VSYNC};

    public GuiVideoSettings(GuiScreen par1GuiScreen, GameSettings par2GameSettings)
    {
        this.parentGuiScreen = par1GuiScreen;
        this.guiGameSettings = par2GameSettings;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.screenTitle = I18n.getString("options.videoTitle");
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, I18n.getString("gui.done")));
        this.is64bit = false;
        String[] astring = new String[] {"sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch"};
        String[] astring1 = astring;
        int i = astring.length;

        for (int j = 0; j < i; ++j)
        {
            String s = astring1[j];
            String s1 = System.getProperty(s);

            if (s1 != null && s1.contains("64"))
            {
                this.is64bit = true;
                break;
            }
        }

        int k = 0;
        i = this.is64bit ? 0 : -15;
        EnumOptions[] aenumoptions = videoOptions;
        int l = aenumoptions.length;

        for (int i1 = 0; i1 < l; ++i1)
        {
            EnumOptions enumoptions = aenumoptions[i1];

            if (enumoptions.getEnumFloat())
            {
                this.buttonList.add(new GuiSlider(enumoptions.returnEnumOrdinal(), this.width / 2 - 155 + k % 2 * 160, this.height / 7 + i + 24 * (k >> 1), enumoptions, this.guiGameSettings.getKeyBinding(enumoptions), this.guiGameSettings.getOptionFloatValue(enumoptions)));
            }
            else
            {
                this.buttonList.add(new GuiSmallButton(enumoptions.returnEnumOrdinal(), this.width / 2 - 155 + k % 2 * 160, this.height / 7 + i + 24 * (k >> 1), enumoptions, this.guiGameSettings.getKeyBinding(enumoptions)));
            }

            ++k;
        }
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.enabled)
        {
            int i = this.guiGameSettings.guiScale;

            if (par1GuiButton.id < 100 && par1GuiButton instanceof GuiSmallButton)
            {
                this.guiGameSettings.setOptionValue(((GuiSmallButton)par1GuiButton).returnEnumOptions(), 1);
                par1GuiButton.displayString = this.guiGameSettings.getKeyBinding(EnumOptions.getEnumOptions(par1GuiButton.id));
            }

            if (par1GuiButton.id == 200)
            {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(this.parentGuiScreen);
            }

            if (this.guiGameSettings.guiScale != i)
            {
                ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
                int j = scaledresolution.getScaledWidth();
                int k = scaledresolution.getScaledHeight();
                this.setWorldAndResolution(this.mc, j, k);
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, this.is64bit ? 20 : 5, 16777215);

        if (!this.is64bit && this.guiGameSettings.renderDistance == 0)
        {
            this.drawCenteredString(this.fontRenderer, I18n.getString("options.farWarning1"), this.width / 2, this.height / 6 + 144 + 1, 11468800);
            this.drawCenteredString(this.fontRenderer, I18n.getString("options.farWarning2"), this.width / 2, this.height / 6 + 144 + 13, 11468800);
        }

        super.drawScreen(par1, par2, par3);
    }
}
