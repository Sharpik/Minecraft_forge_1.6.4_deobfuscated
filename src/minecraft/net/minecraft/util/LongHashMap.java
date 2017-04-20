package net.minecraft.util;

public class LongHashMap
{
    /** the array of all elements in the hash */
    private transient LongHashMapEntry[] hashArray = new LongHashMapEntry[16];

    /** the number of elements in the hash array */
    private transient int numHashElements;

    /**
     * the maximum amount of elements in the hash (probably 3/4 the size due to meh hashing function)
     */
    private int capacity = 12;

    /**
     * percent of the hasharray that can be used without hash colliding probably
     */
    private final float percentUseable = 0.75F;

    /** count of times elements have been added/removed */
    private transient volatile int modCount;

    /**
     * returns the hashed key given the original key
     */
    private static int getHashedKey(long par0)
    {
        return hash((int)(par0 ^ par0 >>> 32));
    }

    /**
     * the hash function
     */
    private static int hash(int par0)
    {
        par0 ^= par0 >>> 20 ^ par0 >>> 12;
        return par0 ^ par0 >>> 7 ^ par0 >>> 4;
    }

    /**
     * gets the index in the hash given the array length and the hashed key
     */
    private static int getHashIndex(int par0, int par1)
    {
        return par0 & par1 - 1;
    }

    public int getNumHashElements()
    {
        return this.numHashElements;
    }

    /**
     * get the value from the map given the key
     */
    public Object getValueByKey(long par1)
    {
        int j = getHashedKey(par1);

        for (LongHashMapEntry longhashmapentry = this.hashArray[getHashIndex(j, this.hashArray.length)]; longhashmapentry != null; longhashmapentry = longhashmapentry.nextEntry)
        {
            if (longhashmapentry.key == par1)
            {
                return longhashmapentry.value;
            }
        }

        return null;
    }

    public boolean containsItem(long par1)
    {
        return this.getEntry(par1) != null;
    }

    final LongHashMapEntry getEntry(long par1)
    {
        int j = getHashedKey(par1);

        for (LongHashMapEntry longhashmapentry = this.hashArray[getHashIndex(j, this.hashArray.length)]; longhashmapentry != null; longhashmapentry = longhashmapentry.nextEntry)
        {
            if (longhashmapentry.key == par1)
            {
                return longhashmapentry;
            }
        }

        return null;
    }

    /**
     * Add a key-value pair.
     */
    public void add(long par1, Object par3Obj)
    {
        int j = getHashedKey(par1);
        int k = getHashIndex(j, this.hashArray.length);

        for (LongHashMapEntry longhashmapentry = this.hashArray[k]; longhashmapentry != null; longhashmapentry = longhashmapentry.nextEntry)
        {
            if (longhashmapentry.key == par1)
            {
                longhashmapentry.value = par3Obj;
                return;
            }
        }

        ++this.modCount;
        this.createKey(j, par1, par3Obj, k);
    }

    /**
     * resizes the table
     */
    private void resizeTable(int par1)
    {
        LongHashMapEntry[] alonghashmapentry = this.hashArray;
        int j = alonghashmapentry.length;

        if (j == 1073741824)
        {
            this.capacity = Integer.MAX_VALUE;
        }
        else
        {
            LongHashMapEntry[] alonghashmapentry1 = new LongHashMapEntry[par1];
            this.copyHashTableTo(alonghashmapentry1);
            this.hashArray = alonghashmapentry1;
            this.capacity = (int)((float)par1 * this.percentUseable);
        }
    }

    /**
     * copies the hash table to the specified array
     */
    private void copyHashTableTo(LongHashMapEntry[] par1ArrayOfLongHashMapEntry)
    {
        LongHashMapEntry[] alonghashmapentry1 = this.hashArray;
        int i = par1ArrayOfLongHashMapEntry.length;

        for (int j = 0; j < alonghashmapentry1.length; ++j)
        {
            LongHashMapEntry longhashmapentry = alonghashmapentry1[j];

            if (longhashmapentry != null)
            {
                alonghashmapentry1[j] = null;
                LongHashMapEntry longhashmapentry1;

                do
                {
                    longhashmapentry1 = longhashmapentry.nextEntry;
                    int k = getHashIndex(longhashmapentry.hash, i);
                    longhashmapentry.nextEntry = par1ArrayOfLongHashMapEntry[k];
                    par1ArrayOfLongHashMapEntry[k] = longhashmapentry;
                    longhashmapentry = longhashmapentry1;
                }
                while (longhashmapentry1 != null);
            }
        }
    }

    /**
     * calls the removeKey method and returns removed object
     */
    public Object remove(long par1)
    {
        LongHashMapEntry longhashmapentry = this.removeKey(par1);
        return longhashmapentry == null ? null : longhashmapentry.value;
    }

    /**
     * removes the key from the hash linked list
     */
    final LongHashMapEntry removeKey(long par1)
    {
        int j = getHashedKey(par1);
        int k = getHashIndex(j, this.hashArray.length);
        LongHashMapEntry longhashmapentry = this.hashArray[k];
        LongHashMapEntry longhashmapentry1;
        LongHashMapEntry longhashmapentry2;

        for (longhashmapentry1 = longhashmapentry; longhashmapentry1 != null; longhashmapentry1 = longhashmapentry2)
        {
            longhashmapentry2 = longhashmapentry1.nextEntry;

            if (longhashmapentry1.key == par1)
            {
                ++this.modCount;
                --this.numHashElements;

                if (longhashmapentry == longhashmapentry1)
                {
                    this.hashArray[k] = longhashmapentry2;
                }
                else
                {
                    longhashmapentry.nextEntry = longhashmapentry2;
                }

                return longhashmapentry1;
            }

            longhashmapentry = longhashmapentry1;
        }

        return longhashmapentry1;
    }

    /**
     * creates the key in the hash table
     */
    private void createKey(int par1, long par2, Object par4Obj, int par5)
    {
        LongHashMapEntry longhashmapentry = this.hashArray[par5];
        this.hashArray[par5] = new LongHashMapEntry(par1, par2, par4Obj, longhashmapentry);

        if (this.numHashElements++ >= this.capacity)
        {
            this.resizeTable(2 * this.hashArray.length);
        }
    }

    /**
     * public method to get the hashed key(hashCode)
     */
    static int getHashCode(long par0)
    {
        return getHashedKey(par0);
    }
}
