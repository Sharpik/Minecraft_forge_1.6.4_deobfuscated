package net.minecraft.client.gui.mco;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.text.DateFormat;
import java.util.Date;
import net.minecraft.client.gui.GuiScreenSelectLocation;
import net.minecraft.client.mco.Backup;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.server.MinecraftServer;

@SideOnly(Side.CLIENT)
class GuiScreenBackupSelectionList extends GuiScreenSelectLocation
{
    final GuiScreenBackup field_111249_a;

    public GuiScreenBackupSelectionList(GuiScreenBackup par1GuiScreenBackup)
    {
        super(GuiScreenBackup.func_130036_f(par1GuiScreenBackup), par1GuiScreenBackup.width, par1GuiScreenBackup.height, 32, par1GuiScreenBackup.height - 64, 36);
        this.field_111249_a = par1GuiScreenBackup;
    }

    /**
     * Gets the size of the current slot list.
     */
    protected int getSize()
    {
        return GuiScreenBackup.func_110370_e(this.field_111249_a).size() + 1;
    }

    /**
     * the element in the slot that was clicked, boolean for wether it was double clicked or not
     */
    protected void elementClicked(int par1, boolean par2)
    {
        if (par1 < GuiScreenBackup.func_110370_e(this.field_111249_a).size())
        {
            GuiScreenBackup.func_130029_a(this.field_111249_a, par1);
        }
    }

    /**
     * returns true if the element passed in is currently selected
     */
    protected boolean isSelected(int par1)
    {
        return par1 == GuiScreenBackup.func_130034_h(this.field_111249_a);
    }

    protected boolean func_104086_b(int par1)
    {
        return false;
    }

    protected int func_130003_b()
    {
        return this.getSize() * 36;
    }

    protected void func_130004_c()
    {
        this.field_111249_a.drawDefaultBackground();
    }

    protected void drawSlot(int par1, int par2, int par3, int par4, Tessellator par5Tessellator)
    {
        if (par1 < GuiScreenBackup.func_110370_e(this.field_111249_a).size())
        {
            this.func_111246_b(par1, par2, par3, par4, par5Tessellator);
        }
    }

    private void func_111246_b(int par1, int par2, int par3, int par4, Tessellator par5Tessellator)
    {
        Backup backup = (Backup)GuiScreenBackup.func_110370_e(this.field_111249_a).get(par1);
        this.field_111249_a.drawString(GuiScreenBackup.func_130032_i(this.field_111249_a), "Backup (" + this.func_111248_a(Long.valueOf(MinecraftServer.getSystemTimeMillis() - backup.field_110725_b.getTime())) + ")", par2 + 2, par3 + 1, 16777215);
        this.field_111249_a.drawString(GuiScreenBackup.func_130033_j(this.field_111249_a), this.func_111247_a(backup.field_110725_b), par2 + 2, par3 + 12, 7105644);
    }

    private String func_111247_a(Date par1Date)
    {
        return DateFormat.getDateTimeInstance(3, 3).format(par1Date);
    }

    private String func_111248_a(Long par1)
    {
        if (par1.longValue() < 0L)
        {
            return "right now";
        }
        else
        {
            long i = par1.longValue() / 1000L;

            if (i < 60L)
            {
                return (i == 1L ? "1 second" : i + " seconds") + " ago";
            }
            else
            {
                long j;

                if (i < 3600L)
                {
                    j = i / 60L;
                    return (j == 1L ? "1 minute" : j + " minutes") + " ago";
                }
                else if (i < 86400L)
                {
                    j = i / 3600L;
                    return (j == 1L ? "1 hour" : j + " hours") + " ago";
                }
                else
                {
                    j = i / 86400L;
                    return (j == 1L ? "1 day" : j + " days") + " ago";
                }
            }
        }
    }
}
