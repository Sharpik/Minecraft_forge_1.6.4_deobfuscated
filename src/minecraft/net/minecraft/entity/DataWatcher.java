package net.minecraft.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.ReportedException;

public class DataWatcher
{
    /** When isBlank is true the DataWatcher is not watching any objects */
    private boolean isBlank = true;
    private static final HashMap dataTypes = new HashMap();
    private final Map watchedObjects = new HashMap();

    /** true if one or more object was changed */
    private boolean objectChanged;
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * adds a new object to dataWatcher to watch, to update an already existing object see updateObject. Arguments: data
     * Value Id, Object to add
     */
    public void addObject(int par1, Object par2Obj)
    {
        Integer integer = (Integer)dataTypes.get(par2Obj.getClass());

        if (integer == null)
        {
            throw new IllegalArgumentException("Unknown data type: " + par2Obj.getClass());
        }
        else if (par1 > 31)
        {
            throw new IllegalArgumentException("Data value id is too big with " + par1 + "! (Max is " + 31 + ")");
        }
        else if (this.watchedObjects.containsKey(Integer.valueOf(par1)))
        {
            throw new IllegalArgumentException("Duplicate id value for " + par1 + "!");
        }
        else
        {
            WatchableObject watchableobject = new WatchableObject(integer.intValue(), par1, par2Obj);
            this.lock.writeLock().lock();
            this.watchedObjects.put(Integer.valueOf(par1), watchableobject);
            this.lock.writeLock().unlock();
            this.isBlank = false;
        }
    }

    /**
     * Add a new object for the DataWatcher to watch, using the specified data type.
     */
    public void addObjectByDataType(int par1, int par2)
    {
        WatchableObject watchableobject = new WatchableObject(par2, par1, (Object)null);
        this.lock.writeLock().lock();
        this.watchedObjects.put(Integer.valueOf(par1), watchableobject);
        this.lock.writeLock().unlock();
        this.isBlank = false;
    }

    /**
     * gets the bytevalue of a watchable object
     */
    public byte getWatchableObjectByte(int par1)
    {
        return ((Byte)this.getWatchedObject(par1).getObject()).byteValue();
    }

    public short getWatchableObjectShort(int par1)
    {
        return ((Short)this.getWatchedObject(par1).getObject()).shortValue();
    }

    /**
     * gets a watchable object and returns it as a Integer
     */
    public int getWatchableObjectInt(int par1)
    {
        return ((Integer)this.getWatchedObject(par1).getObject()).intValue();
    }

    public float getWatchableObjectFloat(int par1)
    {
        return ((Float)this.getWatchedObject(par1).getObject()).floatValue();
    }

    /**
     * gets a watchable object and returns it as a String
     */
    public String getWatchableObjectString(int par1)
    {
        return (String)this.getWatchedObject(par1).getObject();
    }

    /**
     * Get a watchable object as an ItemStack.
     */
    public ItemStack getWatchableObjectItemStack(int par1)
    {
        return (ItemStack)this.getWatchedObject(par1).getObject();
    }

    /**
     * is threadsafe, unless it throws an exception, then
     */
    private WatchableObject getWatchedObject(int par1)
    {
        this.lock.readLock().lock();
        WatchableObject watchableobject;

        try
        {
            watchableobject = (WatchableObject)this.watchedObjects.get(Integer.valueOf(par1));
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting synched entity data");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Synched entity data");
            crashreportcategory.addCrashSection("Data ID", Integer.valueOf(par1));
            throw new ReportedException(crashreport);
        }

        this.lock.readLock().unlock();
        return watchableobject;
    }

    /**
     * updates an already existing object
     */
    public void updateObject(int par1, Object par2Obj)
    {
        WatchableObject watchableobject = this.getWatchedObject(par1);

        if (!par2Obj.equals(watchableobject.getObject()))
        {
            watchableobject.setObject(par2Obj);
            watchableobject.setWatched(true);
            this.objectChanged = true;
        }
    }

    public void setObjectWatched(int par1)
    {
        WatchableObject.setWatchableObjectWatched(this.getWatchedObject(par1), true);
        this.objectChanged = true;
    }

    public boolean hasChanges()
    {
        return this.objectChanged;
    }

    /**
     * writes every object in passed list to dataoutputstream, terminated by 0x7F
     */
    public static void writeObjectsInListToStream(List par0List, DataOutput par1DataOutput) throws IOException
    {
        if (par0List != null)
        {
            Iterator iterator = par0List.iterator();

            while (iterator.hasNext())
            {
                WatchableObject watchableobject = (WatchableObject)iterator.next();
                writeWatchableObject(par1DataOutput, watchableobject);
            }
        }

        par1DataOutput.writeByte(127);
    }

    public List unwatchAndReturnAllWatched()
    {
        ArrayList arraylist = null;

        if (this.objectChanged)
        {
            this.lock.readLock().lock();
            Iterator iterator = this.watchedObjects.values().iterator();

            while (iterator.hasNext())
            {
                WatchableObject watchableobject = (WatchableObject)iterator.next();

                if (watchableobject.isWatched())
                {
                    watchableobject.setWatched(false);

                    if (arraylist == null)
                    {
                        arraylist = new ArrayList();
                    }

                    arraylist.add(watchableobject);
                }
            }

            this.lock.readLock().unlock();
        }

        this.objectChanged = false;
        return arraylist;
    }

    public void writeWatchableObjects(DataOutput par1DataOutput) throws IOException
    {
        this.lock.readLock().lock();
        Iterator iterator = this.watchedObjects.values().iterator();

        while (iterator.hasNext())
        {
            WatchableObject watchableobject = (WatchableObject)iterator.next();
            writeWatchableObject(par1DataOutput, watchableobject);
        }

        this.lock.readLock().unlock();
        par1DataOutput.writeByte(127);
    }

    public List getAllWatched()
    {
        ArrayList arraylist = null;
        this.lock.readLock().lock();
        WatchableObject watchableobject;

        for (Iterator iterator = this.watchedObjects.values().iterator(); iterator.hasNext(); arraylist.add(watchableobject))
        {
            watchableobject = (WatchableObject)iterator.next();

            if (arraylist == null)
            {
                arraylist = new ArrayList();
            }
        }

        this.lock.readLock().unlock();
        return arraylist;
    }

    private static void writeWatchableObject(DataOutput par0DataOutput, WatchableObject par1WatchableObject) throws IOException
    {
        int i = (par1WatchableObject.getObjectType() << 5 | par1WatchableObject.getDataValueId() & 31) & 255;
        par0DataOutput.writeByte(i);

        switch (par1WatchableObject.getObjectType())
        {
            case 0:
                par0DataOutput.writeByte(((Byte)par1WatchableObject.getObject()).byteValue());
                break;
            case 1:
                par0DataOutput.writeShort(((Short)par1WatchableObject.getObject()).shortValue());
                break;
            case 2:
                par0DataOutput.writeInt(((Integer)par1WatchableObject.getObject()).intValue());
                break;
            case 3:
                par0DataOutput.writeFloat(((Float)par1WatchableObject.getObject()).floatValue());
                break;
            case 4:
                Packet.writeString((String)par1WatchableObject.getObject(), par0DataOutput);
                break;
            case 5:
                ItemStack itemstack = (ItemStack)par1WatchableObject.getObject();
                Packet.writeItemStack(itemstack, par0DataOutput);
                break;
            case 6:
                ChunkCoordinates chunkcoordinates = (ChunkCoordinates)par1WatchableObject.getObject();
                par0DataOutput.writeInt(chunkcoordinates.posX);
                par0DataOutput.writeInt(chunkcoordinates.posY);
                par0DataOutput.writeInt(chunkcoordinates.posZ);
        }
    }

    public static List readWatchableObjects(DataInput par0DataInput) throws IOException
    {
        ArrayList arraylist = null;

        for (byte b0 = par0DataInput.readByte(); b0 != 127; b0 = par0DataInput.readByte())
        {
            if (arraylist == null)
            {
                arraylist = new ArrayList();
            }

            int i = (b0 & 224) >> 5;
            int j = b0 & 31;
            WatchableObject watchableobject = null;

            switch (i)
            {
                case 0:
                    watchableobject = new WatchableObject(i, j, Byte.valueOf(par0DataInput.readByte()));
                    break;
                case 1:
                    watchableobject = new WatchableObject(i, j, Short.valueOf(par0DataInput.readShort()));
                    break;
                case 2:
                    watchableobject = new WatchableObject(i, j, Integer.valueOf(par0DataInput.readInt()));
                    break;
                case 3:
                    watchableobject = new WatchableObject(i, j, Float.valueOf(par0DataInput.readFloat()));
                    break;
                case 4:
                    watchableobject = new WatchableObject(i, j, Packet.readString(par0DataInput, 64));
                    break;
                case 5:
                    watchableobject = new WatchableObject(i, j, Packet.readItemStack(par0DataInput));
                    break;
                case 6:
                    int k = par0DataInput.readInt();
                    int l = par0DataInput.readInt();
                    int i1 = par0DataInput.readInt();
                    watchableobject = new WatchableObject(i, j, new ChunkCoordinates(k, l, i1));
            }

            arraylist.add(watchableobject);
        }

        return arraylist;
    }

    @SideOnly(Side.CLIENT)
    public void updateWatchedObjectsFromList(List par1List)
    {
        this.lock.writeLock().lock();
        Iterator iterator = par1List.iterator();

        while (iterator.hasNext())
        {
            WatchableObject watchableobject = (WatchableObject)iterator.next();
            WatchableObject watchableobject1 = (WatchableObject)this.watchedObjects.get(Integer.valueOf(watchableobject.getDataValueId()));

            if (watchableobject1 != null)
            {
                watchableobject1.setObject(watchableobject.getObject());
            }
        }

        this.lock.writeLock().unlock();
        this.objectChanged = true;
    }

    public boolean getIsBlank()
    {
        return this.isBlank;
    }

    public void func_111144_e()
    {
        this.objectChanged = false;
    }

    static
    {
        dataTypes.put(Byte.class, Integer.valueOf(0));
        dataTypes.put(Short.class, Integer.valueOf(1));
        dataTypes.put(Integer.class, Integer.valueOf(2));
        dataTypes.put(Float.class, Integer.valueOf(3));
        dataTypes.put(String.class, Integer.valueOf(4));
        dataTypes.put(ItemStack.class, Integer.valueOf(5));
        dataTypes.put(ChunkCoordinates.class, Integer.valueOf(6));
    }
}
