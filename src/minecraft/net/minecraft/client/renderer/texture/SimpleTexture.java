package net.minecraft.client.renderer.texture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.Resource;
import net.minecraft.client.resources.ResourceManager;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class SimpleTexture extends AbstractTexture
{
    private final ResourceLocation textureLocation;

    public SimpleTexture(ResourceLocation par1ResourceLocation)
    {
        this.textureLocation = par1ResourceLocation;
    }

    public void loadTexture(ResourceManager par1ResourceManager) throws IOException
    {
        InputStream inputstream = null;

        try
        {
            Resource resource = par1ResourceManager.getResource(this.textureLocation);
            inputstream = resource.getInputStream();
            BufferedImage bufferedimage = ImageIO.read(inputstream);
            boolean flag = false;
            boolean flag1 = false;

            if (resource.hasMetadata())
            {
                try
                {
                    TextureMetadataSection texturemetadatasection = (TextureMetadataSection)resource.getMetadata("texture");

                    if (texturemetadatasection != null)
                    {
                        flag = texturemetadatasection.getTextureBlur();
                        flag1 = texturemetadatasection.getTextureClamp();
                    }
                }
                catch (RuntimeException runtimeexception)
                {
                    Minecraft.getMinecraft().getLogAgent().logWarningException("Failed reading metadata of: " + this.textureLocation, runtimeexception);
                }
            }

            TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), bufferedimage, flag, flag1);
        }
        finally
        {
            if (inputstream != null)
            {
                inputstream.close();
            }
        }
    }
}
