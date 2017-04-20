package net.minecraft.client.settings;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)

class EnumOptionsHelper
{
    static final int[] enumOptionsMappingHelperArray = new int[EnumOptions.values().length];

    static
    {
        try
        {
            enumOptionsMappingHelperArray[EnumOptions.INVERT_MOUSE.ordinal()] = 1;
        }
        catch (NoSuchFieldError nosuchfielderror)
        {
            ;
        }

        try
        {
            enumOptionsMappingHelperArray[EnumOptions.VIEW_BOBBING.ordinal()] = 2;
        }
        catch (NoSuchFieldError nosuchfielderror1)
        {
            ;
        }

        try
        {
            enumOptionsMappingHelperArray[EnumOptions.ANAGLYPH.ordinal()] = 3;
        }
        catch (NoSuchFieldError nosuchfielderror2)
        {
            ;
        }

        try
        {
            enumOptionsMappingHelperArray[EnumOptions.ADVANCED_OPENGL.ordinal()] = 4;
        }
        catch (NoSuchFieldError nosuchfielderror3)
        {
            ;
        }

        try
        {
            enumOptionsMappingHelperArray[EnumOptions.RENDER_CLOUDS.ordinal()] = 5;
        }
        catch (NoSuchFieldError nosuchfielderror4)
        {
            ;
        }

        try
        {
            enumOptionsMappingHelperArray[EnumOptions.CHAT_COLOR.ordinal()] = 6;
        }
        catch (NoSuchFieldError nosuchfielderror5)
        {
            ;
        }

        try
        {
            enumOptionsMappingHelperArray[EnumOptions.CHAT_LINKS.ordinal()] = 7;
        }
        catch (NoSuchFieldError nosuchfielderror6)
        {
            ;
        }

        try
        {
            enumOptionsMappingHelperArray[EnumOptions.CHAT_LINKS_PROMPT.ordinal()] = 8;
        }
        catch (NoSuchFieldError nosuchfielderror7)
        {
            ;
        }

        try
        {
            enumOptionsMappingHelperArray[EnumOptions.USE_SERVER_TEXTURES.ordinal()] = 9;
        }
        catch (NoSuchFieldError nosuchfielderror8)
        {
            ;
        }

        try
        {
            enumOptionsMappingHelperArray[EnumOptions.SNOOPER_ENABLED.ordinal()] = 10;
        }
        catch (NoSuchFieldError nosuchfielderror9)
        {
            ;
        }

        try
        {
            enumOptionsMappingHelperArray[EnumOptions.USE_FULLSCREEN.ordinal()] = 11;
        }
        catch (NoSuchFieldError nosuchfielderror10)
        {
            ;
        }

        try
        {
            enumOptionsMappingHelperArray[EnumOptions.ENABLE_VSYNC.ordinal()] = 12;
        }
        catch (NoSuchFieldError nosuchfielderror11)
        {
            ;
        }

        try
        {
            enumOptionsMappingHelperArray[EnumOptions.SHOW_CAPE.ordinal()] = 13;
        }
        catch (NoSuchFieldError nosuchfielderror12)
        {
            ;
        }

        try
        {
            enumOptionsMappingHelperArray[EnumOptions.TOUCHSCREEN.ordinal()] = 14;
        }
        catch (NoSuchFieldError nosuchfielderror13)
        {
            ;
        }
    }
}
