package net.minecraft.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

public class WeightedRandom
{
    /**
     * Returns the total weight of all items in a collection.
     */
    public static int getTotalWeight(Collection par0Collection)
    {
        int i = 0;
        WeightedRandomItem weightedrandomitem;

        for (Iterator iterator = par0Collection.iterator(); iterator.hasNext(); i += weightedrandomitem.itemWeight)
        {
            weightedrandomitem = (WeightedRandomItem)iterator.next();
        }

        return i;
    }

    /**
     * Returns a random choice from the input items, with a total weight value.
     */
    public static WeightedRandomItem getRandomItem(Random par0Random, Collection par1Collection, int par2)
    {
        if (par2 <= 0)
        {
            throw new IllegalArgumentException();
        }
        else
        {
            int j = par0Random.nextInt(par2);
            Iterator iterator = par1Collection.iterator();
            WeightedRandomItem weightedrandomitem;

            do
            {
                if (!iterator.hasNext())
                {
                    return null;
                }

                weightedrandomitem = (WeightedRandomItem)iterator.next();
                j -= weightedrandomitem.itemWeight;
            }
            while (j >= 0);

            return weightedrandomitem;
        }
    }

    /**
     * Returns a random choice from the input items.
     */
    public static WeightedRandomItem getRandomItem(Random par0Random, Collection par1Collection)
    {
        return getRandomItem(par0Random, par1Collection, getTotalWeight(par1Collection));
    }

    /**
     * Returns the total weight of all items in a array.
     */
    public static int getTotalWeight(WeightedRandomItem[] par0ArrayOfWeightedRandomItem)
    {
        int i = 0;
        WeightedRandomItem[] aweightedrandomitem1 = par0ArrayOfWeightedRandomItem;
        int j = par0ArrayOfWeightedRandomItem.length;

        for (int k = 0; k < j; ++k)
        {
            WeightedRandomItem weightedrandomitem = aweightedrandomitem1[k];
            i += weightedrandomitem.itemWeight;
        }

        return i;
    }

    /**
     * Returns a random choice from the input array of items, with a total weight value.
     */
    public static WeightedRandomItem getRandomItem(Random par0Random, WeightedRandomItem[] par1ArrayOfWeightedRandomItem, int par2)
    {
        if (par2 <= 0)
        {
            throw new IllegalArgumentException();
        }
        else
        {
            int j = par0Random.nextInt(par2);
            WeightedRandomItem[] aweightedrandomitem1 = par1ArrayOfWeightedRandomItem;
            int k = par1ArrayOfWeightedRandomItem.length;

            for (int l = 0; l < k; ++l)
            {
                WeightedRandomItem weightedrandomitem = aweightedrandomitem1[l];
                j -= weightedrandomitem.itemWeight;

                if (j < 0)
                {
                    return weightedrandomitem;
                }
            }

            return null;
        }
    }

    /**
     * Returns a random choice from the input items.
     */
    public static WeightedRandomItem getRandomItem(Random par0Random, WeightedRandomItem[] par1ArrayOfWeightedRandomItem)
    {
        return getRandomItem(par0Random, par1ArrayOfWeightedRandomItem, getTotalWeight(par1ArrayOfWeightedRandomItem));
    }
}
