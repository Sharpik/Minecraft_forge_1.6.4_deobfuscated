package net.minecraft.client.renderer.texture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SideOnly(Side.CLIENT)
public class StitchSlot
{
    private final int originX;
    private final int originY;
    private final int width;
    private final int height;
    private List subSlots;
    private StitchHolder holder;

    public StitchSlot(int par1, int par2, int par3, int par4)
    {
        this.originX = par1;
        this.originY = par2;
        this.width = par3;
        this.height = par4;
    }

    public StitchHolder getStitchHolder()
    {
        return this.holder;
    }

    public int getOriginX()
    {
        return this.originX;
    }

    public int getOriginY()
    {
        return this.originY;
    }

    public boolean addSlot(StitchHolder par1StitchHolder)
    {
        if (this.holder != null)
        {
            return false;
        }
        else
        {
            int i = par1StitchHolder.getWidth();
            int j = par1StitchHolder.getHeight();

            if (i <= this.width && j <= this.height)
            {
                if (i == this.width && j == this.height)
                {
                    this.holder = par1StitchHolder;
                    return true;
                }
                else
                {
                    if (this.subSlots == null)
                    {
                        this.subSlots = new ArrayList(1);
                        this.subSlots.add(new StitchSlot(this.originX, this.originY, i, j));
                        int k = this.width - i;
                        int l = this.height - j;

                        if (l > 0 && k > 0)
                        {
                            int i1 = Math.max(this.height, k);
                            int j1 = Math.max(this.width, l);

                            if (i1 >= j1)
                            {
                                this.subSlots.add(new StitchSlot(this.originX, this.originY + j, i, l));
                                this.subSlots.add(new StitchSlot(this.originX + i, this.originY, k, this.height));
                            }
                            else
                            {
                                this.subSlots.add(new StitchSlot(this.originX + i, this.originY, k, j));
                                this.subSlots.add(new StitchSlot(this.originX, this.originY + j, this.width, l));
                            }
                        }
                        else if (k == 0)
                        {
                            this.subSlots.add(new StitchSlot(this.originX, this.originY + j, i, l));
                        }
                        else if (l == 0)
                        {
                            this.subSlots.add(new StitchSlot(this.originX + i, this.originY, k, j));
                        }
                    }

                    Iterator iterator = this.subSlots.iterator();
                    StitchSlot stitchslot;

                    do
                    {
                        if (!iterator.hasNext())
                        {
                            return false;
                        }

                        stitchslot = (StitchSlot)iterator.next();
                    }
                    while (!stitchslot.addSlot(par1StitchHolder));

                    return true;
                }
            }
            else
            {
                return false;
            }
        }
    }

    /**
     * Gets the slot and all its subslots
     */
    public void getAllStitchSlots(List par1List)
    {
        if (this.holder != null)
        {
            par1List.add(this);
        }
        else if (this.subSlots != null)
        {
            Iterator iterator = this.subSlots.iterator();

            while (iterator.hasNext())
            {
                StitchSlot stitchslot = (StitchSlot)iterator.next();
                stitchslot.getAllStitchSlots(par1List);
            }
        }
    }

    public String toString()
    {
        return "Slot{originX=" + this.originX + ", originY=" + this.originY + ", width=" + this.width + ", height=" + this.height + ", texture=" + this.holder + ", subSlots=" + this.subSlots + '}';
    }
}
