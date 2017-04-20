package net.minecraft.client.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.concurrent.Callable;
import net.minecraft.client.gui.ScaledResolution;

@SideOnly(Side.CLIENT)
class CallableScreenSize implements Callable
{
    final ScaledResolution theScaledResolution;

    final EntityRenderer theEntityRenderer;

    CallableScreenSize(EntityRenderer par1EntityRenderer, ScaledResolution par2ScaledResolution)
    {
        this.theEntityRenderer = par1EntityRenderer;
        this.theScaledResolution = par2ScaledResolution;
    }

    public String callScreenSize()
    {
        return String.format("Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d", new Object[] {Integer.valueOf(this.theScaledResolution.getScaledWidth()), Integer.valueOf(this.theScaledResolution.getScaledHeight()), Integer.valueOf(EntityRenderer.getRendererMinecraft(this.theEntityRenderer).displayWidth), Integer.valueOf(EntityRenderer.getRendererMinecraft(this.theEntityRenderer).displayHeight), Integer.valueOf(this.theScaledResolution.getScaleFactor())});
    }

    public Object call()
    {
        return this.callScreenSize();
    }
}
