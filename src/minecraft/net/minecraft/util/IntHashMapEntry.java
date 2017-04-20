package net.minecraft.util;

class IntHashMapEntry
{
    /** The hash code of this entry */
    final int hashEntry;

    /** The object stored in this entry */
    Object valueEntry;

    /** The next entry in this slot */
    IntHashMapEntry nextEntry;

    /** The id of the hash slot computed from the hash */
    final int slotHash;

    IntHashMapEntry(int par1, int par2, Object par3Obj, IntHashMapEntry par4IntHashMapEntry)
    {
        this.valueEntry = par3Obj;
        this.nextEntry = par4IntHashMapEntry;
        this.hashEntry = par2;
        this.slotHash = par1;
    }

    /**
     * Returns the hash code for this entry
     */
    public final int getHash()
    {
        return this.hashEntry;
    }

    /**
     * Returns the object stored in this entry
     */
    public final Object getValue()
    {
        return this.valueEntry;
    }

    public final boolean equals(Object par1Obj)
    {
        if (!(par1Obj instanceof IntHashMapEntry))
        {
            return false;
        }
        else
        {
            IntHashMapEntry inthashmapentry = (IntHashMapEntry)par1Obj;
            Integer integer = Integer.valueOf(this.getHash());
            Integer integer1 = Integer.valueOf(inthashmapentry.getHash());

            if (integer == integer1 || integer != null && integer.equals(integer1))
            {
                Object object1 = this.getValue();
                Object object2 = inthashmapentry.getValue();

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
        return IntHashMap.getHash(this.hashEntry);
    }

    public final String toString()
    {
        return this.getHash() + "=" + this.getValue();
    }
}
