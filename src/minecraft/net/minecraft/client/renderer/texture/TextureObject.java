package net.minecraft.client.renderer.texture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.IOException;
import net.minecraft.client.resources.ResourceManager;

@SideOnly(Side.CLIENT)
public interface TextureObject
{
    void loadTexture(ResourceManager resourcemanager) throws IOException;

    int getGlTextureId();
}
