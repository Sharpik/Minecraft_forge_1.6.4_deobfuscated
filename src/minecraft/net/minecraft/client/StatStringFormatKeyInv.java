package net.minecraft.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.stats.IStatStringFormat;

@SideOnly(Side.CLIENT)
class StatStringFormatKeyInv implements IStatStringFormat
{
    /** Minecraft instance */
    final Minecraft mc;

    StatStringFormatKeyInv(Minecraft par1Minecraft)
    {
        this.mc = par1Minecraft;
    }

    /**
     * Formats the strings based on 'IStatStringFormat' interface.
     */
    public String formatString(String par1Str)
    {
        try
        {
            return String.format(par1Str, new Object[] {GameSettings.getKeyDisplayString(this.mc.gameSettings.keyBindInventory.keyCode)});
        }
        catch (Exception exception)
        {
            return "Error: " + exception.getLocalizedMessage();
        }
    }
}
