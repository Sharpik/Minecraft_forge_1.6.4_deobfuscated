package net.minecraft.client.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.image.BufferedImage;
import java.io.IOException;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.ResourceManager;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class ThreadDownloadImageData extends AbstractTexture
{
    private final String imageUrl;
    private final IImageBuffer imageBuffer;
    private BufferedImage bufferedImage;
    private Thread imageThread;
    private SimpleTexture imageLocation;
    private boolean textureUploaded;

    public ThreadDownloadImageData(String par1Str, ResourceLocation par2ResourceLocation, IImageBuffer par3IImageBuffer)
    {
        this.imageUrl = par1Str;
        this.imageBuffer = par3IImageBuffer;
        this.imageLocation = par2ResourceLocation != null ? new SimpleTexture(par2ResourceLocation) : null;
    }

    public int getGlTextureId()
    {
        int i = super.getGlTextureId();

        if (!this.textureUploaded && this.bufferedImage != null)
        {
            TextureUtil.uploadTextureImage(i, this.bufferedImage);
            this.textureUploaded = true;
        }

        return i;
    }

    public void getBufferedImage(BufferedImage par1BufferedImage)
    {
        this.bufferedImage = par1BufferedImage;
    }

    public void loadTexture(ResourceManager par1ResourceManager) throws IOException
    {
        if (this.bufferedImage == null)
        {
            if (this.imageLocation != null)
            {
                this.imageLocation.loadTexture(par1ResourceManager);
                this.glTextureId = this.imageLocation.getGlTextureId();
            }
        }
        else
        {
            TextureUtil.uploadTextureImage(this.getGlTextureId(), this.bufferedImage);
        }

        if (this.imageThread == null)
        {
            this.imageThread = new ThreadDownloadImageDataINNER1(this);
            this.imageThread.setDaemon(true);
            this.imageThread.setName("Skin downloader: " + this.imageUrl);
            this.imageThread.start();
        }
    }

    public boolean isTextureUploaded()
    {
        this.getGlTextureId();
        return this.textureUploaded;
    }

    static String getImageUrl(ThreadDownloadImageData par0ThreadDownloadImageData)
    {
        return par0ThreadDownloadImageData.imageUrl;
    }

    static IImageBuffer getImageBuffer(ThreadDownloadImageData par0ThreadDownloadImageData)
    {
        return par0ThreadDownloadImageData.imageBuffer;
    }
}
