package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.EnumOptions;
import net.minecraft.client.settings.GameSettings;

@SideOnly(Side.CLIENT)
public class GuiSnooper extends GuiScreen
{
    /** Instance of GuiScreen. */
    private final GuiScreen snooperGuiScreen;

    /** Instance of GameSettings. */
    private final GameSettings snooperGameSettings;
    private final List field_74098_c = new ArrayList();
    private final List field_74096_d = new ArrayList();

    /** The Snooper title. */
    private String snooperTitle;
    private String[] field_74101_n;
    private GuiSnooperList snooperList;
    private GuiButton buttonAllowSnooping;

    public GuiSnooper(GuiScreen par1GuiScreen, GameSettings par2GameSettings)
    {
        this.snooperGuiScreen = par1GuiScreen;
        this.snooperGameSettings = par2GameSettings;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.snooperTitle = I18n.getString("options.snooper.title");
        String s = I18n.getString("options.snooper.desc");
        ArrayList arraylist = new ArrayList();
        Iterator iterator = this.fontRenderer.listFormattedStringToWidth(s, this.width - 30).iterator();

        while (iterator.hasNext())
        {
            String s1 = (String)iterator.next();
            arraylist.add(s1);
        }

        this.field_74101_n = (String[])arraylist.toArray(new String[0]);
        this.field_74098_c.clear();
        this.field_74096_d.clear();
        this.buttonList.add(this.buttonAllowSnooping = new GuiButton(1, this.width / 2 - 152, this.height - 30, 150, 20, this.snooperGameSettings.getKeyBinding(EnumOptions.SNOOPER_ENABLED)));
        this.buttonList.add(new GuiButton(2, this.width / 2 + 2, this.height - 30, 150, 20, I18n.getString("gui.done")));
        boolean flag = this.mc.getIntegratedServer() != null && this.mc.getIntegratedServer().getPlayerUsageSnooper() != null;
        Iterator iterator1 = (new TreeMap(this.mc.getPlayerUsageSnooper().getCurrentStats())).entrySet().iterator();
        Entry entry;

        while (iterator1.hasNext())
        {
            entry = (Entry)iterator1.next();
            this.field_74098_c.add((flag ? "C " : "") + (String)entry.getKey());
            this.field_74096_d.add(this.fontRenderer.trimStringToWidth((String)entry.getValue(), this.width - 220));
        }

        if (flag)
        {
            iterator1 = (new TreeMap(this.mc.getIntegratedServer().getPlayerUsageSnooper().getCurrentStats())).entrySet().iterator();

            while (iterator1.hasNext())
            {
                entry = (Entry)iterator1.next();
                this.field_74098_c.add("S " + (String)entry.getKey());
                this.field_74096_d.add(this.fontRenderer.trimStringToWidth((String)entry.getValue(), this.width - 220));
            }
        }

        this.snooperList = new GuiSnooperList(this);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.enabled)
        {
            if (par1GuiButton.id == 2)
            {
                this.snooperGameSettings.saveOptions();
                this.snooperGameSettings.saveOptions();
                this.mc.displayGuiScreen(this.snooperGuiScreen);
            }

            if (par1GuiButton.id == 1)
            {
                this.snooperGameSettings.setOptionValue(EnumOptions.SNOOPER_ENABLED, 1);
                this.buttonAllowSnooping.displayString = this.snooperGameSettings.getKeyBinding(EnumOptions.SNOOPER_ENABLED);
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground();
        this.snooperList.drawScreen(par1, par2, par3);
        this.drawCenteredString(this.fontRenderer, this.snooperTitle, this.width / 2, 8, 16777215);
        int k = 22;
        String[] astring = this.field_74101_n;
        int l = astring.length;

        for (int i1 = 0; i1 < l; ++i1)
        {
            String s = astring[i1];
            this.drawCenteredString(this.fontRenderer, s, this.width / 2, k, 8421504);
            k += this.fontRenderer.FONT_HEIGHT;
        }

        super.drawScreen(par1, par2, par3);
    }

    static List func_74095_a(GuiSnooper par0GuiSnooper)
    {
        return par0GuiSnooper.field_74098_c;
    }

    static List func_74094_b(GuiSnooper par0GuiSnooper)
    {
        return par0GuiSnooper.field_74096_d;
    }
}
