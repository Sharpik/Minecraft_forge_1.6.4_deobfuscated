package net.minecraft.util;

class LongHashMapEntry
{
    /**
     * the key as a long (for playerInstances it is the x in the most significant 32 bits and then y)
     */
    final long key;

    /** the value held by the hash at the specified key */
    Object value;

    /** the next hashentry in the table */
    LongHashMapEntry nextEntry;
    final int hash;

    LongHashMapEntry(int par1, long par2, Object par4Obj, LongHashMapEntry par5LongHashMapEntry)
    {
        this.value = par4Obj;
        this.nextEntry = par5LongHashMapEntry;
        this.key = par2;
        this.hash = par1;
    }

    public final long getKey()
    {
        return this.key;
    }

    public final Object getValue()
    {
        return this.value;
    }

    public final boolean equals(Object par1Obj)
    {
        if (!(par1Obj instanceof LongHashMapEntry))
        {
            return false;
        }
        else
        {
            LongHashMapEntry longhashmapentry = (LongHashMapEntry)par1Obj;
            Long olong = Long.valueOf(this.getKey());
            Long olong1 = Long.valueOf(longhashmapentry.getKey());

            if (olong == olong1 || olong != null && olong.equals(olong1))
            {
                Object object1 = this.getValue();
                Object object2 = longhashmapentry.getValue();

                if (object1 == object2 || object1 != null && object1.equals(object2))
                {
                    return true;
                }
            }

            return false;
        }
    }

    public final int hashCode()
    {
        return LongHashMap.getHashCode(this.key);
    }

    public final String toString()
    {
        return this.getKey() + "=" + this.getValue();
    }
}
