package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.client.renderer.StitcherException;

@SideOnly(Side.CLIENT)
public class Stitcher
{
    private final Set setStitchHolders;
    private final List stitchSlots;
    private int currentWidth;
    private int currentHeight;
    private final int maxWidth;
    private final int maxHeight;
    private final boolean forcePowerOf2;

    /** Max size (width or height) of a single tile */
    private final int maxTileDimension;

    public Stitcher(int par1, int par2, boolean par3)
    {
        this(par1, par2, par3, 0);
    }

    public Stitcher(int par1, int par2, boolean par3, int par4)
    {
        this.setStitchHolders = new HashSet(256);
        this.stitchSlots = new ArrayList(256);
        this.maxWidth = par1;
        this.maxHeight = par2;
        this.forcePowerOf2 = par3;
        this.maxTileDimension = par4;
    }

    public int getCurrentWidth()
    {
        return this.currentWidth;
    }

    public int getCurrentHeight()
    {
        return this.currentHeight;
    }

    public void addSprite(TextureAtlasSprite par1TextureAtlasSprite)
    {
        StitchHolder stitchholder = new StitchHolder(par1TextureAtlasSprite);

        if (this.maxTileDimension > 0)
        {
            stitchholder.setNewDimension(this.maxTileDimension);
        }

        this.setStitchHolders.add(stitchholder);
    }

    public void doStitch()
    {
        StitchHolder[] astitchholder = (StitchHolder[])this.setStitchHolders.toArray(new StitchHolder[this.setStitchHolders.size()]);
        Arrays.sort(astitchholder);
        StitchHolder[] astitchholder1 = astitchholder;
        int i = astitchholder.length;

        for (int j = 0; j < i; ++j)
        {
            StitchHolder stitchholder = astitchholder1[j];

            if (!this.allocateSlot(stitchholder))
            {
                String s = String.format("Unable to fit: %s - size: %dx%d - Maybe try a lowerresolution texturepack?", new Object[] {stitchholder.getAtlasSprite().getIconName(), Integer.valueOf(stitchholder.getAtlasSprite().getIconWidth()), Integer.valueOf(stitchholder.getAtlasSprite().getIconHeight())});
                throw new StitcherException(stitchholder, s);
            }
        }

        if (this.forcePowerOf2)
        {
            this.currentWidth = this.getCeilPowerOf2(this.currentWidth);
            this.currentHeight = this.getCeilPowerOf2(this.currentHeight);
        }
    }

    public List getStichSlots()
    {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = this.stitchSlots.iterator();

        while (iterator.hasNext())
        {
            StitchSlot stitchslot = (StitchSlot)iterator.next();
            stitchslot.getAllStitchSlots(arraylist);
        }

        ArrayList arraylist1 = Lists.newArrayList();
        Iterator iterator1 = arraylist.iterator();

        while (iterator1.hasNext())
        {
            StitchSlot stitchslot1 = (StitchSlot)iterator1.next();
            StitchHolder stitchholder = stitchslot1.getStitchHolder();
            TextureAtlasSprite textureatlassprite = stitchholder.getAtlasSprite();
            textureatlassprite.initSprite(this.currentWidth, this.currentHeight, stitchslot1.getOriginX(), stitchslot1.getOriginY(), stitchholder.isRotated());
            arraylist1.add(textureatlassprite);
        }

        return arraylist1;
    }

    /**
     * Returns power of 2 >= the specified value
     */
    private int getCeilPowerOf2(int par1)
    {
        int j = par1 - 1;
        j |= j >> 1;
        j |= j >> 2;
        j |= j >> 4;
        j |= j >> 8;
        j |= j >> 16;
        return j + 1;
    }

    /**
     * Attempts to find space for specified tile
     */
    private boolean allocateSlot(StitchHolder par1StitchHolder)
    {
        for (int i = 0; i < this.stitchSlots.size(); ++i)
        {
            if (((StitchSlot)this.stitchSlots.get(i)).addSlot(par1StitchHolder))
            {
                return true;
            }

            par1StitchHolder.rotate();

            if (((StitchSlot)this.stitchSlots.get(i)).addSlot(par1StitchHolder))
            {
                return true;
            }

            par1StitchHolder.rotate();
        }

        return this.expandAndAllocateSlot(par1StitchHolder);
    }

    /**
     * Expand stitched texture in order to make space for specified tile
     */
    private boolean expandAndAllocateSlot(StitchHolder par1StitchHolder)
    {
        int i = Math.min(par1StitchHolder.getHeight(), par1StitchHolder.getWidth());
        boolean flag = this.currentWidth == 0 && this.currentHeight == 0;
        boolean flag1;

        if (this.forcePowerOf2)
        {
            int j = this.getCeilPowerOf2(this.currentWidth);
            int k = this.getCeilPowerOf2(this.currentHeight);
            int l = this.getCeilPowerOf2(this.currentWidth + i);
            int i1 = this.getCeilPowerOf2(this.currentHeight + i);
            boolean flag2 = l <= this.maxWidth;
            boolean flag3 = i1 <= this.maxHeight;

            if (!flag2 && !flag3)
            {
                return false;
            }

            int j1 = Math.max(par1StitchHolder.getHeight(), par1StitchHolder.getWidth());

            if (flag && !flag2 && this.getCeilPowerOf2(this.currentHeight + j1) > this.maxHeight)
            {
                return false;
            }

            boolean flag4 = j != l;
            boolean flag5 = k != i1;

            if (flag4 ^ flag5)
            {
                flag1 = flag5 && flag3; //Forge: Bug fix: Attempt to fill all downward space before expanding width
            }
            else
            {
                flag1 = flag2 && j <= k;
            }
        }
        else
        {
            boolean flag6 = this.currentWidth + i <= this.maxWidth;
            boolean flag7 = this.currentHeight + i <= this.maxHeight;

            if (!flag6 && !flag7)
            {
                return false;
            }

            flag1 = (flag || this.currentWidth <= this.currentHeight) && flag6;
        }

        StitchSlot stitchslot;

        if (flag1)
        {
            if (par1StitchHolder.getWidth() > par1StitchHolder.getHeight())
            {
                par1StitchHolder.rotate();
            }

            if (this.currentHeight == 0)
            {
                this.currentHeight = par1StitchHolder.getHeight();
            }

            stitchslot = new StitchSlot(this.currentWidth, 0, par1StitchHolder.getWidth(), this.currentHeight);
            this.currentWidth += par1StitchHolder.getWidth();
        }
        else
        {
            stitchslot = new StitchSlot(0, this.currentHeight, this.currentWidth, par1StitchHolder.getHeight());
            this.currentHeight += par1StitchHolder.getHeight();
        }

        stitchslot.addSlot(par1StitchHolder);
        this.stitchSlots.add(stitchslot);
        return true;
    }
}
