package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.resources.I18n;
import net.minecraft.stats.StatList;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumGameType;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveFormatComparator;
import net.minecraft.world.storage.WorldInfo;

@SideOnly(Side.CLIENT)
public class GuiSelectWorld extends GuiScreen
{
    /** simple date formater */
    private final DateFormat dateFormatter = new SimpleDateFormat();

    /**
     * A reference to the screen object that created this. Used for navigating between screens.
     */
    protected GuiScreen parentScreen;

    /** The title string that is displayed in the top-center of the screen. */
    protected String screenTitle = "Select world";

    /** True if a world has been selected. */
    private boolean selected;

    /** the currently selected world */
    private int selectedWorld;

    /** The save list for the world selection screen */
    private List saveList;
    private GuiWorldSlot worldSlotContainer;

    /** E.g. World, Welt, Monde, Mundo */
    private String localizedWorldText;
    private String localizedMustConvertText;

    /**
     * The game mode text that is displayed with each world on the world selection list.
     */
    private String[] localizedGameModeText = new String[3];

    /** set to true if you arein the process of deleteing a world/save */
    private boolean deleting;

    /** The delete button in the world selection GUI */
    private GuiButton buttonDelete;

    /** the select button in the world selection gui */
    private GuiButton buttonSelect;

    /** The rename button in the world selection GUI */
    private GuiButton buttonRename;
    private GuiButton buttonRecreate;

    public GuiSelectWorld(GuiScreen par1GuiScreen)
    {
        this.parentScreen = par1GuiScreen;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.screenTitle = I18n.getString("selectWorld.title");

        try
        {
            this.loadSaves();
        }
        catch (AnvilConverterException anvilconverterexception)
        {
            anvilconverterexception.printStackTrace();
            this.mc.displayGuiScreen(new GuiErrorScreen("Unable to load words", anvilconverterexception.getMessage()));
            return;
        }

        this.localizedWorldText = I18n.getString("selectWorld.world");
        this.localizedMustConvertText = I18n.getString("selectWorld.conversion");
        this.localizedGameModeText[EnumGameType.SURVIVAL.getID()] = I18n.getString("gameMode.survival");
        this.localizedGameModeText[EnumGameType.CREATIVE.getID()] = I18n.getString("gameMode.creative");
        this.localizedGameModeText[EnumGameType.ADVENTURE.getID()] = I18n.getString("gameMode.adventure");
        this.worldSlotContainer = new GuiWorldSlot(this);
        this.worldSlotContainer.registerScrollButtons(4, 5);
        this.initButtons();
    }

    /**
     * loads the saves
     */
    private void loadSaves() throws AnvilConverterException
    {
        ISaveFormat isaveformat = this.mc.getSaveLoader();
        this.saveList = isaveformat.getSaveList();
        Collections.sort(this.saveList);
        this.selectedWorld = -1;
    }

    /**
     * returns the file name of the specified save number
     */
    protected String getSaveFileName(int par1)
    {
        return ((SaveFormatComparator)this.saveList.get(par1)).getFileName();
    }

    /**
     * returns the name of the saved game
     */
    protected String getSaveName(int par1)
    {
        String s = ((SaveFormatComparator)this.saveList.get(par1)).getDisplayName();

        if (s == null || MathHelper.stringNullOrLengthZero(s))
        {
            s = I18n.getString("selectWorld.world") + " " + (par1 + 1);
        }

        return s;
    }

    /**
     * intilize the buttons for this GUI
     */
    public void initButtons()
    {
        this.buttonList.add(this.buttonSelect = new GuiButton(1, this.width / 2 - 154, this.height - 52, 150, 20, I18n.getString("selectWorld.select")));
        this.buttonList.add(new GuiButton(3, this.width / 2 + 4, this.height - 52, 150, 20, I18n.getString("selectWorld.create")));
        this.buttonList.add(this.buttonRename = new GuiButton(6, this.width / 2 - 154, this.height - 28, 72, 20, I18n.getString("selectWorld.rename")));
        this.buttonList.add(this.buttonDelete = new GuiButton(2, this.width / 2 - 76, this.height - 28, 72, 20, I18n.getString("selectWorld.delete")));
        this.buttonList.add(this.buttonRecreate = new GuiButton(7, this.width / 2 + 4, this.height - 28, 72, 20, I18n.getString("selectWorld.recreate")));
        this.buttonList.add(new GuiButton(0, this.width / 2 + 82, this.height - 28, 72, 20, I18n.getString("gui.cancel")));
        this.buttonSelect.enabled = false;
        this.buttonDelete.enabled = false;
        this.buttonRename.enabled = false;
        this.buttonRecreate.enabled = false;
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
                String s = this.getSaveName(this.selectedWorld);

                if (s != null)
                {
                    this.deleting = true;
                    GuiYesNo guiyesno = getDeleteWorldScreen(this, s, this.selectedWorld);
                    this.mc.displayGuiScreen(guiyesno);
                }
            }
            else if (par1GuiButton.id == 1)
            {
                this.selectWorld(this.selectedWorld);
            }
            else if (par1GuiButton.id == 3)
            {
                this.mc.displayGuiScreen(new GuiCreateWorld(this));
            }
            else if (par1GuiButton.id == 6)
            {
                this.mc.displayGuiScreen(new GuiRenameWorld(this, this.getSaveFileName(this.selectedWorld)));
            }
            else if (par1GuiButton.id == 0)
            {
                this.mc.displayGuiScreen(this.parentScreen);
            }
            else if (par1GuiButton.id == 7)
            {
                GuiCreateWorld guicreateworld = new GuiCreateWorld(this);
                ISaveHandler isavehandler = this.mc.getSaveLoader().getSaveLoader(this.getSaveFileName(this.selectedWorld), false);
                WorldInfo worldinfo = isavehandler.loadWorldInfo();
                isavehandler.flush();
                guicreateworld.func_82286_a(worldinfo);
                this.mc.displayGuiScreen(guicreateworld);
            }
            else
            {
                this.worldSlotContainer.actionPerformed(par1GuiButton);
            }
        }
    }

    /**
     * Gets the selected world.
     */
    public void selectWorld(int par1)
    {
        this.mc.displayGuiScreen((GuiScreen)null);

        if (!this.selected)
        {
            this.selected = true;
            String s = this.getSaveFileName(par1);

            if (s == null)
            {
                s = "World" + par1;
            }

            String s1 = this.getSaveName(par1);

            if (s1 == null)
            {
                s1 = "World" + par1;
            }

            if (this.mc.getSaveLoader().canLoadWorld(s))
            {
                this.mc.launchIntegratedServer(s, s1, (WorldSettings)null);
                this.mc.statFileWriter.readStat(StatList.loadWorldStat, 1);
            }
        }
    }

    public void confirmClicked(boolean par1, int par2)
    {
        if (this.deleting)
        {
            this.deleting = false;

            if (par1)
            {
                ISaveFormat isaveformat = this.mc.getSaveLoader();
                isaveformat.flushCache();
                isaveformat.deleteWorldDirectory(this.getSaveFileName(par2));

                try
                {
                    this.loadSaves();
                }
                catch (AnvilConverterException anvilconverterexception)
                {
                    anvilconverterexception.printStackTrace();
                }
            }

            this.mc.displayGuiScreen(this);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        this.worldSlotContainer.drawScreen(par1, par2, par3);
        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 16777215);
        super.drawScreen(par1, par2, par3);
    }

    /**
     * Gets a GuiYesNo screen with the warning, buttons, etc.
     */
    public static GuiYesNo getDeleteWorldScreen(GuiScreen par0GuiScreen, String par1Str, int par2)
    {
        String s1 = I18n.getString("selectWorld.deleteQuestion");
        String s2 = "\'" + par1Str + "\' " + I18n.getString("selectWorld.deleteWarning");
        String s3 = I18n.getString("selectWorld.deleteButton");
        String s4 = I18n.getString("gui.cancel");
        GuiYesNo guiyesno = new GuiYesNo(par0GuiScreen, s1, s2, s3, s4, par2);
        return guiyesno;
    }

    static List getSize(GuiSelectWorld par0GuiSelectWorld)
    {
        return par0GuiSelectWorld.saveList;
    }

    /**
     * called whenever an element in this gui is selected
     */
    static int onElementSelected(GuiSelectWorld par0GuiSelectWorld, int par1)
    {
        return par0GuiSelectWorld.selectedWorld = par1;
    }

    /**
     * returns the world currently selected
     */
    static int getSelectedWorld(GuiSelectWorld par0GuiSelectWorld)
    {
        return par0GuiSelectWorld.selectedWorld;
    }

    /**
     * returns the select button
     */
    static GuiButton getSelectButton(GuiSelectWorld par0GuiSelectWorld)
    {
        return par0GuiSelectWorld.buttonSelect;
    }

    /**
     * returns the rename button
     */
    static GuiButton getRenameButton(GuiSelectWorld par0GuiSelectWorld)
    {
        return par0GuiSelectWorld.buttonDelete;
    }

    /**
     * returns the delete button
     */
    static GuiButton getDeleteButton(GuiSelectWorld par0GuiSelectWorld)
    {
        return par0GuiSelectWorld.buttonRename;
    }

    static GuiButton func_82312_f(GuiSelectWorld par0GuiSelectWorld)
    {
        return par0GuiSelectWorld.buttonRecreate;
    }

    static String func_82313_g(GuiSelectWorld par0GuiSelectWorld)
    {
        return par0GuiSelectWorld.localizedWorldText;
    }

    static DateFormat func_82315_h(GuiSelectWorld par0GuiSelectWorld)
    {
        return par0GuiSelectWorld.dateFormatter;
    }

    static String func_82311_i(GuiSelectWorld par0GuiSelectWorld)
    {
        return par0GuiSelectWorld.localizedMustConvertText;
    }

    static String[] func_82314_j(GuiSelectWorld par0GuiSelectWorld)
    {
        return par0GuiSelectWorld.localizedGameModeText;
    }
}
