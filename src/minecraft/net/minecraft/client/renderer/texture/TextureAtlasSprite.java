package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import net.minecraft.client.resources.Resource;
import net.minecraft.client.resources.ResourceManager;
import net.minecraft.client.resources.data.AnimationFrame;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class TextureAtlasSprite implements Icon
{
    private final String iconName;
    protected List framesTextureData = Lists.newArrayList();
    private AnimationMetadataSection animationMetadata;
    protected boolean rotated;
    protected int originX;
    protected int originY;
    protected int width;
    protected int height;
    private float minU;
    private float maxU;
    private float minV;
    private float maxV;
    protected int frameCounter;
    protected int tickCounter;

    protected TextureAtlasSprite(String par1Str)
    {
        this.iconName = par1Str;
    }

    public void initSprite(int par1, int par2, int par3, int par4, boolean par5)
    {
        this.originX = par3;
        this.originY = par4;
        this.rotated = par5;
        float f = (float)(0.009999999776482582D / (double)par1);
        float f1 = (float)(0.009999999776482582D / (double)par2);
        this.minU = (float)par3 / (float)((double)par1) + f;
        this.maxU = (float)(par3 + this.width) / (float)((double)par1) - f;
        this.minV = (float)par4 / (float)par2 + f1;
        this.maxV = (float)(par4 + this.height) / (float)par2 - f1;
    }

    public void copyFrom(TextureAtlasSprite par1TextureAtlasSprite)
    {
        this.originX = par1TextureAtlasSprite.originX;
        this.originY = par1TextureAtlasSprite.originY;
        this.width = par1TextureAtlasSprite.width;
        this.height = par1TextureAtlasSprite.height;
        this.rotated = par1TextureAtlasSprite.rotated;
        this.minU = par1TextureAtlasSprite.minU;
        this.maxU = par1TextureAtlasSprite.maxU;
        this.minV = par1TextureAtlasSprite.minV;
        this.maxV = par1TextureAtlasSprite.maxV;
    }

    /**
     * Returns the X position of this icon on its texture sheet, in pixels.
     */
    public int getOriginX()
    {
        return this.originX;
    }

    /**
     * Returns the Y position of this icon on its texture sheet, in pixels.
     */
    public int getOriginY()
    {
        return this.originY;
    }

    /**
     * Returns the width of the icon, in pixels.
     */
    public int getIconWidth()
    {
        return this.width;
    }

    /**
     * Returns the height of the icon, in pixels.
     */
    public int getIconHeight()
    {
        return this.height;
    }

    /**
     * Returns the minimum U coordinate to use when rendering with this icon.
     */
    public float getMinU()
    {
        return this.minU;
    }

    /**
     * Returns the maximum U coordinate to use when rendering with this icon.
     */
    public float getMaxU()
    {
        return this.maxU;
    }

    /**
     * Gets a U coordinate on the icon. 0 returns uMin and 16 returns uMax. Other arguments return in-between values.
     */
    public float getInterpolatedU(double par1)
    {
        float f = this.maxU - this.minU;
        return this.minU + f * (float)par1 / 16.0F;
    }

    /**
     * Returns the minimum V coordinate to use when rendering with this icon.
     */
    public float getMinV()
    {
        return this.minV;
    }

    /**
     * Returns the maximum V coordinate to use when rendering with this icon.
     */
    public float getMaxV()
    {
        return this.maxV;
    }

    /**
     * Gets a V coordinate on the icon. 0 returns vMin and 16 returns vMax. Other arguments return in-between values.
     */
    public float getInterpolatedV(double par1)
    {
        float f = this.maxV - this.minV;
        return this.minV + f * ((float)par1 / 16.0F);
    }

    public String getIconName()
    {
        return this.iconName;
    }

    public void updateAnimation()
    {
        ++this.tickCounter;

        if (this.tickCounter >= this.animationMetadata.getFrameTimeSingle(this.frameCounter))
        {
            int i = this.animationMetadata.getFrameIndex(this.frameCounter);
            int j = this.animationMetadata.getFrameCount() == 0 ? this.framesTextureData.size() : this.animationMetadata.getFrameCount();
            this.frameCounter = (this.frameCounter + 1) % j;
            this.tickCounter = 0;
            int k = this.animationMetadata.getFrameIndex(this.frameCounter);

            if (i != k && k >= 0 && k < this.framesTextureData.size())
            {
                TextureUtil.uploadTextureSub((int[])this.framesTextureData.get(k), this.width, this.height, this.originX, this.originY, false, false);
            }
        }
    }

    public int[] getFrameTextureData(int par1)
    {
        return (int[])this.framesTextureData.get(par1);
    }

    public int getFrameCount()
    {
        return this.framesTextureData.size();
    }

    public void setIconWidth(int par1)
    {
        this.width = par1;
    }

    public void setIconHeight(int par1)
    {
        this.height = par1;
    }

    public void loadSprite(Resource par1Resource) throws IOException
    {
        this.resetSprite();
        InputStream inputstream = par1Resource.getInputStream();
        AnimationMetadataSection animationmetadatasection = (AnimationMetadataSection)par1Resource.getMetadata("animation");
        BufferedImage bufferedimage = ImageIO.read(inputstream);
        this.height = bufferedimage.getHeight();
        this.width = bufferedimage.getWidth();
        int[] aint = new int[this.height * this.width];
        bufferedimage.getRGB(0, 0, this.width, this.height, aint, 0, this.width);

        if (animationmetadatasection == null)
        {
            if (this.height != this.width)
            {
                throw new RuntimeException("broken aspect ratio and not an animation");
            }

            this.framesTextureData.add(aint);
        }
        else
        {
            int i = this.height / this.width;
            int j = this.width;
            int k = this.width;
            this.height = this.width;
            int l;

            if (animationmetadatasection.getFrameCount() > 0)
            {
                Iterator iterator = animationmetadatasection.getFrameIndexSet().iterator();

                while (iterator.hasNext())
                {
                    l = ((Integer)iterator.next()).intValue();

                    if (l >= i)
                    {
                        throw new RuntimeException("invalid frameindex " + l);
                    }

                    this.allocateFrameTextureData(l);
                    this.framesTextureData.set(l, getFrameTextureData(aint, j, k, l));
                }

                this.animationMetadata = animationmetadatasection;
            }
            else
            {
                ArrayList arraylist = Lists.newArrayList();

                for (l = 0; l < i; ++l)
                {
                    this.framesTextureData.add(getFrameTextureData(aint, j, k, l));
                    arraylist.add(new AnimationFrame(l, -1));
                }

                this.animationMetadata = new AnimationMetadataSection(arraylist, this.width, this.height, animationmetadatasection.getFrameTime());
            }
        }
    }

    private void allocateFrameTextureData(int par1)
    {
        if (this.framesTextureData.size() <= par1)
        {
            for (int j = this.framesTextureData.size(); j <= par1; ++j)
            {
                this.framesTextureData.add((Object)null);
            }
        }
    }

    private static int[] getFrameTextureData(int[] par0ArrayOfInteger, int par1, int par2, int par3)
    {
        int[] aint1 = new int[par1 * par2];
        System.arraycopy(par0ArrayOfInteger, par3 * aint1.length, aint1, 0, aint1.length);
        return aint1;
    }

    public void clearFramesTextureData()
    {
        this.framesTextureData.clear();
    }

    public boolean hasAnimationMetadata()
    {
        return this.animationMetadata != null;
    }

    public void setFramesTextureData(List par1List)
    {
        this.framesTextureData = par1List;
    }

    private void resetSprite()
    {
        this.animationMetadata = null;
        this.setFramesTextureData(Lists.newArrayList());
        this.frameCounter = 0;
        this.tickCounter = 0;
    }

    public String toString()
    {
        return "TextureAtlasSprite{name=\'" + this.iconName + '\'' + ", frameCount=" + this.framesTextureData.size() + ", rotated=" + this.rotated + ", x=" + this.originX + ", y=" + this.originY + ", height=" + this.height + ", width=" + this.width + ", u0=" + this.minU + ", u1=" + this.maxU + ", v0=" + this.minV + ", v1=" + this.maxV + '}';
    }

    /**
     * Load the specified resource as this sprite's data.
     * Returning false from this function will prevent this icon from being stitched onto the master texture.
     * @param manager Main resource manager
     * @param location File resource location
     * @return False to prevent this Icon from being stitched
     * @throws IOException
     */
    public boolean load(ResourceManager manager, ResourceLocation location) throws IOException
    {
        loadSprite(manager.getResource(location));
        return true;
    }
}
