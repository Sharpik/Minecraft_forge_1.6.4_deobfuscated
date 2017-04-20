package net.minecraft.client.gui.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
class GuiBeaconButton extends GuiButton
{
    /** Texture for this button. */
    private final ResourceLocation buttonTexture;
    private final int field_82257_l;
    private final int field_82258_m;
    private boolean field_82256_n;

    protected GuiBeaconButton(int par1, int par2, int par3, ResourceLocation par4ResourceLocation, int par5, int par6)
    {
        super(par1, par2, par3, 22, 22, "");
        this.buttonTexture = par4ResourceLocation;
        this.field_82257_l = par5;
        this.field_82258_m = par6;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft par1Minecraft, int par2, int par3)
    {
        if (this.drawButton)
        {
            par1Minecraft.getTextureManager().bindTexture(GuiBeacon.getBeaconGuiTextures());
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.field_82253_i = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
            short short1 = 219;
            int k = 0;

            if (!this.enabled)
            {
                k += this.width * 2;
            }
            else if (this.field_82256_n)
            {
                k += this.width * 1;
            }
            else if (this.field_82253_i)
            {
                k += this.width * 3;
            }

            this.drawTexturedModalRect(this.xPosition, this.yPosition, k, short1, this.width, this.height);

            if (!GuiBeacon.getBeaconGuiTextures().equals(this.buttonTexture))
            {
                par1Minecraft.getTextureManager().bindTexture(this.buttonTexture);
            }

            this.drawTexturedModalRect(this.xPosition + 2, this.yPosition + 2, this.field_82257_l, this.field_82258_m, 18, 18);
        }
    }

    public boolean func_82255_b()
    {
        return this.field_82256_n;
    }

    public void func_82254_b(boolean par1)
    {
        this.field_82256_n = par1;
    }
}
