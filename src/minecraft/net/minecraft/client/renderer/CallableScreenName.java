package net.minecraft.client.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.concurrent.Callable;

@SideOnly(Side.CLIENT)
class CallableScreenName implements Callable
{
    final EntityRenderer entityRender;

    CallableScreenName(EntityRenderer par1EntityRenderer)
    {
        this.entityRender = par1EntityRenderer;
    }

    public String callScreenName()
    {
        return EntityRenderer.getRendererMinecraft(this.entityRender).currentScreen.getClass().getCanonicalName();
    }

    public Object call()
    {
        return this.callScreenName();
    }
}
