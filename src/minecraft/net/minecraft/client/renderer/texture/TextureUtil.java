package net.minecraft.client.renderer.texture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.resources.ResourceManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class TextureUtil
{
    private static final IntBuffer dataBuffer = GLAllocation.createDirectIntBuffer(4194304);
    public static final DynamicTexture missingTexture = new DynamicTexture(16, 16);
    public static final int[] missingTextureData = missingTexture.getTextureData();

    public static int glGenTextures()
    {
        return GL11.glGenTextures();
    }

    public static int uploadTextureImage(int par0, BufferedImage par1BufferedImage)
    {
        return uploadTextureImageAllocate(par0, par1BufferedImage, false, false);
    }

    public static void uploadTexture(int par0, int[] par1ArrayOfInteger, int par2, int par3)
    {
        bindTexture(par0);
        uploadTextureSub(par1ArrayOfInteger, par2, par3, 0, 0, false, false);
    }

    public static void uploadTextureSub(int[] par0ArrayOfInteger, int par1, int par2, int par3, int par4, boolean par5, boolean par6)
    {
        int i1 = 4194304 / par1;
        setTextureBlurred(par5);
        setTextureClamped(par6);
        int j1;

        for (int k1 = 0; k1 < par1 * par2; k1 += par1 * j1)
        {
            int l1 = k1 / par1;
            j1 = Math.min(i1, par2 - l1);
            int i2 = par1 * j1;
            copyToBufferPos(par0ArrayOfInteger, k1, i2);
            GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, par3, par4 + l1, par1, j1, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, dataBuffer);
        }
    }

    public static int uploadTextureImageAllocate(int par0, BufferedImage par1BufferedImage, boolean par2, boolean par3)
    {
        allocateTexture(par0, par1BufferedImage.getWidth(), par1BufferedImage.getHeight());
        return uploadTextureImageSub(par0, par1BufferedImage, 0, 0, par2, par3);
    }

    public static void allocateTexture(int par0, int par1, int par2)
    {
        bindTexture(par0);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, par1, par2, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, (IntBuffer)null);
    }

    public static int uploadTextureImageSub(int par0, BufferedImage par1BufferedImage, int par2, int par3, boolean par4, boolean par5)
    {
        bindTexture(par0);
        uploadTextureImageSubImpl(par1BufferedImage, par2, par3, par4, par5);
        return par0;
    }

    private static void uploadTextureImageSubImpl(BufferedImage par0BufferedImage, int par1, int par2, boolean par3, boolean par4)
    {
        int k = par0BufferedImage.getWidth();
        int l = par0BufferedImage.getHeight();
        int i1 = 4194304 / k;
        int[] aint = new int[i1 * k];
        setTextureBlurred(par3);
        setTextureClamped(par4);

        for (int j1 = 0; j1 < k * l; j1 += k * i1)
        {
            int k1 = j1 / k;
            int l1 = Math.min(i1, l - k1);
            int i2 = k * l1;
            par0BufferedImage.getRGB(0, k1, k, l1, aint, 0, k);
            copyToBuffer(aint, i2);
            GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, par1, par2 + k1, k, l1, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, dataBuffer);
        }
    }

    private static void setTextureClamped(boolean par0)
    {
        if (par0)
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
        }
        else
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        }
    }

    private static void setTextureBlurred(boolean par0)
    {
        if (par0)
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        }
        else
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        }
    }

    private static void copyToBuffer(int[] par0ArrayOfInteger, int par1)
    {
        copyToBufferPos(par0ArrayOfInteger, 0, par1);
    }

    private static void copyToBufferPos(int[] par0ArrayOfInteger, int par1, int par2)
    {
        int[] aint1 = par0ArrayOfInteger;

        if (Minecraft.getMinecraft().gameSettings.anaglyph)
        {
            aint1 = updateAnaglyph(par0ArrayOfInteger);
        }

        dataBuffer.clear();
        dataBuffer.put(aint1, par1, par2);
        dataBuffer.position(0).limit(par2);
    }

    static void bindTexture(int par0)
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, par0);
    }

    public static int[] readImageData(ResourceManager par0ResourceManager, ResourceLocation par1ResourceLocation) throws IOException
    {
        BufferedImage bufferedimage = ImageIO.read(par0ResourceManager.getResource(par1ResourceLocation).getInputStream());
        int i = bufferedimage.getWidth();
        int j = bufferedimage.getHeight();
        int[] aint = new int[i * j];
        bufferedimage.getRGB(0, 0, i, j, aint, 0, i);
        return aint;
    }

    public static int[] updateAnaglyph(int[] par0ArrayOfInteger)
    {
        int[] aint1 = new int[par0ArrayOfInteger.length];

        for (int i = 0; i < par0ArrayOfInteger.length; ++i)
        {
            int j = par0ArrayOfInteger[i] >> 24 & 255;
            int k = par0ArrayOfInteger[i] >> 16 & 255;
            int l = par0ArrayOfInteger[i] >> 8 & 255;
            int i1 = par0ArrayOfInteger[i] & 255;
            int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
            int k1 = (k * 30 + l * 70) / 100;
            int l1 = (k * 30 + i1 * 70) / 100;
            aint1[i] = j << 24 | j1 << 16 | k1 << 8 | l1;
        }

        return aint1;
    }

    static
    {
        int i = -16777216;
        int j = -524040;
        int[] aint = new int[] { -524040, -524040, -524040, -524040, -524040, -524040, -524040, -524040};
        int[] aint1 = new int[] { -16777216, -16777216, -16777216, -16777216, -16777216, -16777216, -16777216, -16777216};
        int k = aint.length;

        for (int l = 0; l < 16; ++l)
        {
            System.arraycopy(l < k ? aint : aint1, 0, missingTextureData, 16 * l, k);
            System.arraycopy(l < k ? aint1 : aint, 0, missingTextureData, 16 * l + k, k);
        }

        missingTexture.updateDynamicTexture();
    }
}
