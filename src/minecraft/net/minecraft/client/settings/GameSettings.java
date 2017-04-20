package net.minecraft.client.settings;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.packet.Packet204ClientInfo;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

@SideOnly(Side.CLIENT)
public class GameSettings
{
    private static final String[] RENDER_DISTANCES = new String[] {"options.renderDistance.far", "options.renderDistance.normal", "options.renderDistance.short", "options.renderDistance.tiny"};
    private static final String[] DIFFICULTIES = new String[] {"options.difficulty.peaceful", "options.difficulty.easy", "options.difficulty.normal", "options.difficulty.hard"};

    /** GUI scale values */
    private static final String[] GUISCALES = new String[] {"options.guiScale.auto", "options.guiScale.small", "options.guiScale.normal", "options.guiScale.large"};
    private static final String[] CHAT_VISIBILITIES = new String[] {"options.chat.visibility.full", "options.chat.visibility.system", "options.chat.visibility.hidden"};
    private static final String[] PARTICLES = new String[] {"options.particles.all", "options.particles.decreased", "options.particles.minimal"};

    /** Limit framerate labels */
    private static final String[] LIMIT_FRAMERATES = new String[] {"performance.max", "performance.balanced", "performance.powersaver"};
    private static final String[] AMBIENT_OCCLUSIONS = new String[] {"options.ao.off", "options.ao.min", "options.ao.max"};
    public float musicVolume = 1.0F;
    public float soundVolume = 1.0F;
    public float mouseSensitivity = 0.5F;
    public boolean invertMouse;
    public int renderDistance;
    public boolean viewBobbing = true;
    public boolean anaglyph;

    /** Advanced OpenGL */
    public boolean advancedOpengl;
    public int limitFramerate = 1;
    public boolean fancyGraphics = true;

    /** Smooth Lighting */
    public int ambientOcclusion = 2;

    /** Clouds flag */
    public boolean clouds = true;

    /** The name of the selected texture pack. */
    public String skin = "Default";
    public int chatVisibility;
    public boolean chatColours = true;
    public boolean chatLinks = true;
    public boolean chatLinksPrompt = true;
    public float chatOpacity = 1.0F;
    public boolean serverTextures = true;
    public boolean snooperEnabled = true;
    public boolean fullScreen;
    public boolean enableVsync = true;
    public boolean hideServerAddress;

    /**
     * Whether to show advanced information on item tooltips, toggled by F3+H
     */
    public boolean advancedItemTooltips;

    /** Whether to pause when the game loses focus, toggled by F3+P */
    public boolean pauseOnLostFocus = true;

    /** Whether to show your cape */
    public boolean showCape = true;
    public boolean touchscreen;
    public int overrideWidth;
    public int overrideHeight;
    public boolean heldItemTooltips = true;
    public float chatScale = 1.0F;
    public float chatWidth = 1.0F;
    public float chatHeightUnfocused = 0.44366196F;
    public float chatHeightFocused = 1.0F;
    public KeyBinding keyBindForward = new KeyBinding("key.forward", 17);
    public KeyBinding keyBindLeft = new KeyBinding("key.left", 30);
    public KeyBinding keyBindBack = new KeyBinding("key.back", 31);
    public KeyBinding keyBindRight = new KeyBinding("key.right", 32);
    public KeyBinding keyBindJump = new KeyBinding("key.jump", 57);
    public KeyBinding keyBindInventory = new KeyBinding("key.inventory", 18);
    public KeyBinding keyBindDrop = new KeyBinding("key.drop", 16);
    public KeyBinding keyBindChat = new KeyBinding("key.chat", 20);
    public KeyBinding keyBindSneak = new KeyBinding("key.sneak", 42);
    public KeyBinding keyBindAttack = new KeyBinding("key.attack", -100);
    public KeyBinding keyBindUseItem = new KeyBinding("key.use", -99);
    public KeyBinding keyBindPlayerList = new KeyBinding("key.playerlist", 15);
    public KeyBinding keyBindPickBlock = new KeyBinding("key.pickItem", -98);
    public KeyBinding keyBindCommand = new KeyBinding("key.command", 53);
    public KeyBinding[] keyBindings;
    protected Minecraft mc;
    private File optionsFile;
    public int difficulty;
    public boolean hideGUI;
    public int thirdPersonView;

    /** true if debug info should be displayed instead of version */
    public boolean showDebugInfo;
    public boolean showDebugProfilerChart;

    /** The lastServer string. */
    public String lastServer;

    /** No clipping for singleplayer */
    public boolean noclip;

    /** Smooth Camera Toggle */
    public boolean smoothCamera;
    public boolean debugCamEnable;

    /** No clipping movement rate */
    public float noclipRate;

    /** Change rate for debug camera */
    public float debugCamRate;
    public float fovSetting;
    public float gammaSetting;

    /** GUI scale */
    public int guiScale;

    /** Determines amount of particles. 0 = All, 1 = Decreased, 2 = Minimal */
    public int particleSetting;

    /** Game settings language */
    public String language;

    public GameSettings(Minecraft par1Minecraft, File par2File)
    {
        this.keyBindings = new KeyBinding[] {this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand};
        this.difficulty = 2;
        this.lastServer = "";
        this.noclipRate = 1.0F;
        this.debugCamRate = 1.0F;
        this.language = "en_US";
        this.mc = par1Minecraft;
        this.optionsFile = new File(par2File, "options.txt");
        this.loadOptions();
    }

    public GameSettings()
    {
        this.keyBindings = new KeyBinding[] {this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand};
        this.difficulty = 2;
        this.lastServer = "";
        this.noclipRate = 1.0F;
        this.debugCamRate = 1.0F;
        this.language = "en_US";
    }

    public String getKeyBindingDescription(int par1)
    {
        return I18n.getString(this.keyBindings[par1].keyDescription);
    }

    /**
     * The string that appears inside the button/slider in the options menu.
     */
    public String getOptionDisplayString(int par1)
    {
        int j = this.keyBindings[par1].keyCode;
        return getKeyDisplayString(j);
    }

    /**
     * Represents a key or mouse button as a string. Args: key
     */
    public static String getKeyDisplayString(int par0)
    {
        return par0 < 0 ? I18n.getStringParams("key.mouseButton", new Object[] {Integer.valueOf(par0 + 101)}): Keyboard.getKeyName(par0);
    }

    /**
     * Returns whether the specified key binding is currently being pressed.
     */
    public static boolean isKeyDown(KeyBinding par0KeyBinding)
    {
        return par0KeyBinding.keyCode < 0 ? Mouse.isButtonDown(par0KeyBinding.keyCode + 100) : Keyboard.isKeyDown(par0KeyBinding.keyCode);
    }

    /**
     * Sets a key binding.
     */
    public void setKeyBinding(int par1, int par2)
    {
        this.keyBindings[par1].keyCode = par2;
        this.saveOptions();
    }

    /**
     * If the specified option is controlled by a slider (float value), this will set the float value.
     */
    public void setOptionFloatValue(EnumOptions par1EnumOptions, float par2)
    {
        if (par1EnumOptions == EnumOptions.MUSIC)
        {
            this.musicVolume = par2;
            this.mc.sndManager.onSoundOptionsChanged();
        }

        if (par1EnumOptions == EnumOptions.SOUND)
        {
            this.soundVolume = par2;
            this.mc.sndManager.onSoundOptionsChanged();
        }

        if (par1EnumOptions == EnumOptions.SENSITIVITY)
        {
            this.mouseSensitivity = par2;
        }

        if (par1EnumOptions == EnumOptions.FOV)
        {
            this.fovSetting = par2;
        }

        if (par1EnumOptions == EnumOptions.GAMMA)
        {
            this.gammaSetting = par2;
        }

        if (par1EnumOptions == EnumOptions.CHAT_OPACITY)
        {
            this.chatOpacity = par2;
            this.mc.ingameGUI.getChatGUI().func_96132_b();
        }

        if (par1EnumOptions == EnumOptions.CHAT_HEIGHT_FOCUSED)
        {
            this.chatHeightFocused = par2;
            this.mc.ingameGUI.getChatGUI().func_96132_b();
        }

        if (par1EnumOptions == EnumOptions.CHAT_HEIGHT_UNFOCUSED)
        {
            this.chatHeightUnfocused = par2;
            this.mc.ingameGUI.getChatGUI().func_96132_b();
        }

        if (par1EnumOptions == EnumOptions.CHAT_WIDTH)
        {
            this.chatWidth = par2;
            this.mc.ingameGUI.getChatGUI().func_96132_b();
        }

        if (par1EnumOptions == EnumOptions.CHAT_SCALE)
        {
            this.chatScale = par2;
            this.mc.ingameGUI.getChatGUI().func_96132_b();
        }
    }

    /**
     * For non-float options. Toggles the option on/off, or cycles through the list i.e. render distances.
     */
    public void setOptionValue(EnumOptions par1EnumOptions, int par2)
    {
        if (par1EnumOptions == EnumOptions.INVERT_MOUSE)
        {
            this.invertMouse = !this.invertMouse;
        }

        if (par1EnumOptions == EnumOptions.RENDER_DISTANCE)
        {
            this.renderDistance = this.renderDistance + par2 & 3;
        }

        if (par1EnumOptions == EnumOptions.GUI_SCALE)
        {
            this.guiScale = this.guiScale + par2 & 3;
        }

        if (par1EnumOptions == EnumOptions.PARTICLES)
        {
            this.particleSetting = (this.particleSetting + par2) % 3;
        }

        if (par1EnumOptions == EnumOptions.VIEW_BOBBING)
        {
            this.viewBobbing = !this.viewBobbing;
        }

        if (par1EnumOptions == EnumOptions.RENDER_CLOUDS)
        {
            this.clouds = !this.clouds;
        }

        if (par1EnumOptions == EnumOptions.ADVANCED_OPENGL)
        {
            this.advancedOpengl = !this.advancedOpengl;
            this.mc.renderGlobal.loadRenderers();
        }

        if (par1EnumOptions == EnumOptions.ANAGLYPH)
        {
            this.anaglyph = !this.anaglyph;
            this.mc.refreshResources();
        }

        if (par1EnumOptions == EnumOptions.FRAMERATE_LIMIT)
        {
            this.limitFramerate = (this.limitFramerate + par2 + 3) % 3;
        }

        if (par1EnumOptions == EnumOptions.DIFFICULTY)
        {
            this.difficulty = this.difficulty + par2 & 3;
        }

        if (par1EnumOptions == EnumOptions.GRAPHICS)
        {
            this.fancyGraphics = !this.fancyGraphics;
            this.mc.renderGlobal.loadRenderers();
        }

        if (par1EnumOptions == EnumOptions.AMBIENT_OCCLUSION)
        {
            this.ambientOcclusion = (this.ambientOcclusion + par2) % 3;
            this.mc.renderGlobal.loadRenderers();
        }

        if (par1EnumOptions == EnumOptions.CHAT_VISIBILITY)
        {
            this.chatVisibility = (this.chatVisibility + par2) % 3;
        }

        if (par1EnumOptions == EnumOptions.CHAT_COLOR)
        {
            this.chatColours = !this.chatColours;
        }

        if (par1EnumOptions == EnumOptions.CHAT_LINKS)
        {
            this.chatLinks = !this.chatLinks;
        }

        if (par1EnumOptions == EnumOptions.CHAT_LINKS_PROMPT)
        {
            this.chatLinksPrompt = !this.chatLinksPrompt;
        }

        if (par1EnumOptions == EnumOptions.USE_SERVER_TEXTURES)
        {
            this.serverTextures = !this.serverTextures;
        }

        if (par1EnumOptions == EnumOptions.SNOOPER_ENABLED)
        {
            this.snooperEnabled = !this.snooperEnabled;
        }

        if (par1EnumOptions == EnumOptions.SHOW_CAPE)
        {
            this.showCape = !this.showCape;
        }

        if (par1EnumOptions == EnumOptions.TOUCHSCREEN)
        {
            this.touchscreen = !this.touchscreen;
        }

        if (par1EnumOptions == EnumOptions.USE_FULLSCREEN)
        {
            this.fullScreen = !this.fullScreen;

            if (this.mc.isFullScreen() != this.fullScreen)
            {
                this.mc.toggleFullscreen();
            }
        }

        if (par1EnumOptions == EnumOptions.ENABLE_VSYNC)
        {
            this.enableVsync = !this.enableVsync;
            Display.setVSyncEnabled(this.enableVsync);
        }

        this.saveOptions();
    }

    public float getOptionFloatValue(EnumOptions par1EnumOptions)
    {
        return par1EnumOptions == EnumOptions.FOV ? this.fovSetting : (par1EnumOptions == EnumOptions.GAMMA ? this.gammaSetting : (par1EnumOptions == EnumOptions.MUSIC ? this.musicVolume : (par1EnumOptions == EnumOptions.SOUND ? this.soundVolume : (par1EnumOptions == EnumOptions.SENSITIVITY ? this.mouseSensitivity : (par1EnumOptions == EnumOptions.CHAT_OPACITY ? this.chatOpacity : (par1EnumOptions == EnumOptions.CHAT_HEIGHT_FOCUSED ? this.chatHeightFocused : (par1EnumOptions == EnumOptions.CHAT_HEIGHT_UNFOCUSED ? this.chatHeightUnfocused : (par1EnumOptions == EnumOptions.CHAT_SCALE ? this.chatScale : (par1EnumOptions == EnumOptions.CHAT_WIDTH ? this.chatWidth : 0.0F)))))))));
    }

    public boolean getOptionOrdinalValue(EnumOptions par1EnumOptions)
    {
        switch (EnumOptionsHelper.enumOptionsMappingHelperArray[par1EnumOptions.ordinal()])
        {
            case 1:
                return this.invertMouse;
            case 2:
                return this.viewBobbing;
            case 3:
                return this.anaglyph;
            case 4:
                return this.advancedOpengl;
            case 5:
                return this.clouds;
            case 6:
                return this.chatColours;
            case 7:
                return this.chatLinks;
            case 8:
                return this.chatLinksPrompt;
            case 9:
                return this.serverTextures;
            case 10:
                return this.snooperEnabled;
            case 11:
                return this.fullScreen;
            case 12:
                return this.enableVsync;
            case 13:
                return this.showCape;
            case 14:
                return this.touchscreen;
            default:
                return false;
        }
    }

    /**
     * Returns the translation of the given index in the given String array. If the index is smaller than 0 or greater
     * than/equal to the length of the String array, it is changed to 0.
     */
    private static String getTranslation(String[] par0ArrayOfStr, int par1)
    {
        if (par1 < 0 || par1 >= par0ArrayOfStr.length)
        {
            par1 = 0;
        }

        return I18n.getString(par0ArrayOfStr[par1]);
    }

    /**
     * Gets a key binding.
     */
    public String getKeyBinding(EnumOptions par1EnumOptions)
    {
        String s = I18n.getString(par1EnumOptions.getEnumString()) + ": ";

        if (par1EnumOptions.getEnumFloat())
        {
            float f = this.getOptionFloatValue(par1EnumOptions);
            return par1EnumOptions == EnumOptions.SENSITIVITY ? (f == 0.0F ? s + I18n.getString("options.sensitivity.min") : (f == 1.0F ? s + I18n.getString("options.sensitivity.max") : s + (int)(f * 200.0F) + "%")) : (par1EnumOptions == EnumOptions.FOV ? (f == 0.0F ? s + I18n.getString("options.fov.min") : (f == 1.0F ? s + I18n.getString("options.fov.max") : s + (int)(70.0F + f * 40.0F))) : (par1EnumOptions == EnumOptions.GAMMA ? (f == 0.0F ? s + I18n.getString("options.gamma.min") : (f == 1.0F ? s + I18n.getString("options.gamma.max") : s + "+" + (int)(f * 100.0F) + "%")) : (par1EnumOptions == EnumOptions.CHAT_OPACITY ? s + (int)(f * 90.0F + 10.0F) + "%" : (par1EnumOptions == EnumOptions.CHAT_HEIGHT_UNFOCUSED ? s + GuiNewChat.func_96130_b(f) + "px" : (par1EnumOptions == EnumOptions.CHAT_HEIGHT_FOCUSED ? s + GuiNewChat.func_96130_b(f) + "px" : (par1EnumOptions == EnumOptions.CHAT_WIDTH ? s + GuiNewChat.func_96128_a(f) + "px" : (f == 0.0F ? s + I18n.getString("options.off") : s + (int)(f * 100.0F) + "%")))))));
        }
        else if (par1EnumOptions.getEnumBoolean())
        {
            boolean flag = this.getOptionOrdinalValue(par1EnumOptions);
            return flag ? s + I18n.getString("options.on") : s + I18n.getString("options.off");
        }
        else if (par1EnumOptions == EnumOptions.RENDER_DISTANCE)
        {
            return s + getTranslation(RENDER_DISTANCES, this.renderDistance);
        }
        else if (par1EnumOptions == EnumOptions.DIFFICULTY)
        {
            return s + getTranslation(DIFFICULTIES, this.difficulty);
        }
        else if (par1EnumOptions == EnumOptions.GUI_SCALE)
        {
            return s + getTranslation(GUISCALES, this.guiScale);
        }
        else if (par1EnumOptions == EnumOptions.CHAT_VISIBILITY)
        {
            return s + getTranslation(CHAT_VISIBILITIES, this.chatVisibility);
        }
        else if (par1EnumOptions == EnumOptions.PARTICLES)
        {
            return s + getTranslation(PARTICLES, this.particleSetting);
        }
        else if (par1EnumOptions == EnumOptions.FRAMERATE_LIMIT)
        {
            return s + getTranslation(LIMIT_FRAMERATES, this.limitFramerate);
        }
        else if (par1EnumOptions == EnumOptions.AMBIENT_OCCLUSION)
        {
            return s + getTranslation(AMBIENT_OCCLUSIONS, this.ambientOcclusion);
        }
        else if (par1EnumOptions == EnumOptions.GRAPHICS)
        {
            if (this.fancyGraphics)
            {
                return s + I18n.getString("options.graphics.fancy");
            }
            else
            {
                String s1 = "options.graphics.fast";
                return s + I18n.getString("options.graphics.fast");
            }
        }
        else
        {
            return s;
        }
    }

    /**
     * Loads the options from the options file. It appears that this has replaced the previous 'loadOptions'
     */
    public void loadOptions()
    {
        try
        {
            if (!this.optionsFile.exists())
            {
                return;
            }

            BufferedReader bufferedreader = new BufferedReader(new FileReader(this.optionsFile));
            String s = "";

            while ((s = bufferedreader.readLine()) != null)
            {
                try
                {
                    String[] astring = s.split(":");

                    if (astring[0].equals("music"))
                    {
                        this.musicVolume = this.parseFloat(astring[1]);
                    }

                    if (astring[0].equals("sound"))
                    {
                        this.soundVolume = this.parseFloat(astring[1]);
                    }

                    if (astring[0].equals("mouseSensitivity"))
                    {
                        this.mouseSensitivity = this.parseFloat(astring[1]);
                    }

                    if (astring[0].equals("fov"))
                    {
                        this.fovSetting = this.parseFloat(astring[1]);
                    }

                    if (astring[0].equals("gamma"))
                    {
                        this.gammaSetting = this.parseFloat(astring[1]);
                    }

                    if (astring[0].equals("invertYMouse"))
                    {
                        this.invertMouse = astring[1].equals("true");
                    }

                    if (astring[0].equals("viewDistance"))
                    {
                        this.renderDistance = Integer.parseInt(astring[1]);
                    }

                    if (astring[0].equals("guiScale"))
                    {
                        this.guiScale = Integer.parseInt(astring[1]);
                    }

                    if (astring[0].equals("particles"))
                    {
                        this.particleSetting = Integer.parseInt(astring[1]);
                    }

                    if (astring[0].equals("bobView"))
                    {
                        this.viewBobbing = astring[1].equals("true");
                    }

                    if (astring[0].equals("anaglyph3d"))
                    {
                        this.anaglyph = astring[1].equals("true");
                    }

                    if (astring[0].equals("advancedOpengl"))
                    {
                        this.advancedOpengl = astring[1].equals("true");
                    }

                    if (astring[0].equals("fpsLimit"))
                    {
                        this.limitFramerate = Integer.parseInt(astring[1]);
                    }

                    if (astring[0].equals("difficulty"))
                    {
                        this.difficulty = Integer.parseInt(astring[1]);
                    }

                    if (astring[0].equals("fancyGraphics"))
                    {
                        this.fancyGraphics = astring[1].equals("true");
                    }

                    if (astring[0].equals("ao"))
                    {
                        if (astring[1].equals("true"))
                        {
                            this.ambientOcclusion = 2;
                        }
                        else if (astring[1].equals("false"))
                        {
                            this.ambientOcclusion = 0;
                        }
                        else
                        {
                            this.ambientOcclusion = Integer.parseInt(astring[1]);
                        }
                    }

                    if (astring[0].equals("clouds"))
                    {
                        this.clouds = astring[1].equals("true");
                    }

                    if (astring[0].equals("skin"))
                    {
                        this.skin = astring[1];
                    }

                    if (astring[0].equals("lastServer") && astring.length >= 2)
                    {
                        this.lastServer = s.substring(s.indexOf(58) + 1);
                    }

                    if (astring[0].equals("lang") && astring.length >= 2)
                    {
                        this.language = astring[1];
                    }

                    if (astring[0].equals("chatVisibility"))
                    {
                        this.chatVisibility = Integer.parseInt(astring[1]);
                    }

                    if (astring[0].equals("chatColors"))
                    {
                        this.chatColours = astring[1].equals("true");
                    }

                    if (astring[0].equals("chatLinks"))
                    {
                        this.chatLinks = astring[1].equals("true");
                    }

                    if (astring[0].equals("chatLinksPrompt"))
                    {
                        this.chatLinksPrompt = astring[1].equals("true");
                    }

                    if (astring[0].equals("chatOpacity"))
                    {
                        this.chatOpacity = this.parseFloat(astring[1]);
                    }

                    if (astring[0].equals("serverTextures"))
                    {
                        this.serverTextures = astring[1].equals("true");
                    }

                    if (astring[0].equals("snooperEnabled"))
                    {
                        this.snooperEnabled = astring[1].equals("true");
                    }

                    if (astring[0].equals("fullscreen"))
                    {
                        this.fullScreen = astring[1].equals("true");
                    }

                    if (astring[0].equals("enableVsync"))
                    {
                        this.enableVsync = astring[1].equals("true");
                    }

                    if (astring[0].equals("hideServerAddress"))
                    {
                        this.hideServerAddress = astring[1].equals("true");
                    }

                    if (astring[0].equals("advancedItemTooltips"))
                    {
                        this.advancedItemTooltips = astring[1].equals("true");
                    }

                    if (astring[0].equals("pauseOnLostFocus"))
                    {
                        this.pauseOnLostFocus = astring[1].equals("true");
                    }

                    if (astring[0].equals("showCape"))
                    {
                        this.showCape = astring[1].equals("true");
                    }

                    if (astring[0].equals("touchscreen"))
                    {
                        this.touchscreen = astring[1].equals("true");
                    }

                    if (astring[0].equals("overrideHeight"))
                    {
                        this.overrideHeight = Integer.parseInt(astring[1]);
                    }

                    if (astring[0].equals("overrideWidth"))
                    {
                        this.overrideWidth = Integer.parseInt(astring[1]);
                    }

                    if (astring[0].equals("heldItemTooltips"))
                    {
                        this.heldItemTooltips = astring[1].equals("true");
                    }

                    if (astring[0].equals("chatHeightFocused"))
                    {
                        this.chatHeightFocused = this.parseFloat(astring[1]);
                    }

                    if (astring[0].equals("chatHeightUnfocused"))
                    {
                        this.chatHeightUnfocused = this.parseFloat(astring[1]);
                    }

                    if (astring[0].equals("chatScale"))
                    {
                        this.chatScale = this.parseFloat(astring[1]);
                    }

                    if (astring[0].equals("chatWidth"))
                    {
                        this.chatWidth = this.parseFloat(astring[1]);
                    }

                    for (int i = 0; i < this.keyBindings.length; ++i)
                    {
                        if (astring[0].equals("key_" + this.keyBindings[i].keyDescription))
                        {
                            this.keyBindings[i].keyCode = Integer.parseInt(astring[1]);
                        }
                    }
                }
                catch (Exception exception)
                {
                    this.mc.getLogAgent().logWarning("Skipping bad option: " + s);
                }
            }

            KeyBinding.resetKeyBindingArrayAndHash();
            bufferedreader.close();
        }
        catch (Exception exception1)
        {
            this.mc.getLogAgent().logWarning("Failed to load options");
            exception1.printStackTrace();
        }
    }

    /**
     * Parses a string into a float.
     */
    private float parseFloat(String par1Str)
    {
        return par1Str.equals("true") ? 1.0F : (par1Str.equals("false") ? 0.0F : Float.parseFloat(par1Str));
    }

    /**
     * Saves the options to the options file.
     */
    public void saveOptions()
    {
        if (FMLClientHandler.instance().isLoading()) return;
        try
        {
            PrintWriter printwriter = new PrintWriter(new FileWriter(this.optionsFile));
            printwriter.println("music:" + this.musicVolume);
            printwriter.println("sound:" + this.soundVolume);
            printwriter.println("invertYMouse:" + this.invertMouse);
            printwriter.println("mouseSensitivity:" + this.mouseSensitivity);
            printwriter.println("fov:" + this.fovSetting);
            printwriter.println("gamma:" + this.gammaSetting);
            printwriter.println("viewDistance:" + this.renderDistance);
            printwriter.println("guiScale:" + this.guiScale);
            printwriter.println("particles:" + this.particleSetting);
            printwriter.println("bobView:" + this.viewBobbing);
            printwriter.println("anaglyph3d:" + this.anaglyph);
            printwriter.println("advancedOpengl:" + this.advancedOpengl);
            printwriter.println("fpsLimit:" + this.limitFramerate);
            printwriter.println("difficulty:" + this.difficulty);
            printwriter.println("fancyGraphics:" + this.fancyGraphics);
            printwriter.println("ao:" + this.ambientOcclusion);
            printwriter.println("clouds:" + this.clouds);
            printwriter.println("skin:" + this.skin);
            printwriter.println("lastServer:" + this.lastServer);
            printwriter.println("lang:" + this.language);
            printwriter.println("chatVisibility:" + this.chatVisibility);
            printwriter.println("chatColors:" + this.chatColours);
            printwriter.println("chatLinks:" + this.chatLinks);
            printwriter.println("chatLinksPrompt:" + this.chatLinksPrompt);
            printwriter.println("chatOpacity:" + this.chatOpacity);
            printwriter.println("serverTextures:" + this.serverTextures);
            printwriter.println("snooperEnabled:" + this.snooperEnabled);
            printwriter.println("fullscreen:" + this.fullScreen);
            printwriter.println("enableVsync:" + this.enableVsync);
            printwriter.println("hideServerAddress:" + this.hideServerAddress);
            printwriter.println("advancedItemTooltips:" + this.advancedItemTooltips);
            printwriter.println("pauseOnLostFocus:" + this.pauseOnLostFocus);
            printwriter.println("showCape:" + this.showCape);
            printwriter.println("touchscreen:" + this.touchscreen);
            printwriter.println("overrideWidth:" + this.overrideWidth);
            printwriter.println("overrideHeight:" + this.overrideHeight);
            printwriter.println("heldItemTooltips:" + this.heldItemTooltips);
            printwriter.println("chatHeightFocused:" + this.chatHeightFocused);
            printwriter.println("chatHeightUnfocused:" + this.chatHeightUnfocused);
            printwriter.println("chatScale:" + this.chatScale);
            printwriter.println("chatWidth:" + this.chatWidth);

            for (int i = 0; i < this.keyBindings.length; ++i)
            {
                printwriter.println("key_" + this.keyBindings[i].keyDescription + ":" + this.keyBindings[i].keyCode);
            }

            printwriter.close();
        }
        catch (Exception exception)
        {
            this.mc.getLogAgent().logWarning("Failed to save options");
            exception.printStackTrace();
        }

        this.sendSettingsToServer();
    }

    /**
     * Send a client info packet with settings information to the server
     */
    public void sendSettingsToServer()
    {
        if (this.mc.thePlayer != null)
        {
            this.mc.thePlayer.sendQueue.addToSendQueue(new Packet204ClientInfo(this.language, this.renderDistance, this.chatVisibility, this.chatColours, this.difficulty, this.showCape));
        }
    }

    /**
     * Should render clouds
     */
    public boolean shouldRenderClouds()
    {
        return this.renderDistance < 2 && this.clouds;
    }
}
