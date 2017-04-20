package net.minecraft.world.storage;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IProgressUpdate;

public class SaveFormatOld implements ISaveFormat
{
    /**
     * Reference to the File object representing the directory for the world saves
     */
    protected final File savesDirectory;

    public SaveFormatOld(File par1File)
    {
        if (!par1File.exists())
        {
            par1File.mkdirs();
        }

        this.savesDirectory = par1File;
    }

    @SideOnly(Side.CLIENT)
    public List getSaveList() throws AnvilConverterException
    {
        ArrayList arraylist = new ArrayList();

        for (int i = 0; i < 5; ++i)
        {
            String s = "World" + (i + 1);
            WorldInfo worldinfo = this.getWorldInfo(s);

            if (worldinfo != null)
            {
                arraylist.add(new SaveFormatComparator(s, "", worldinfo.getLastTimePlayed(), worldinfo.getSizeOnDisk(), worldinfo.getGameType(), false, worldinfo.isHardcoreModeEnabled(), worldinfo.areCommandsAllowed()));
            }
        }

        return arraylist;
    }

    public void flushCache() {}

    /**
     * gets the world info
     */
    public WorldInfo getWorldInfo(String par1Str)
    {
        File file1 = new File(this.savesDirectory, par1Str);

        if (!file1.exists())
        {
            return null;
        }
        else
        {
            File file2 = new File(file1, "level.dat");
            NBTTagCompound nbttagcompound;
            NBTTagCompound nbttagcompound1;

            if (file2.exists())
            {
                try
                {
                    nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(file2));
                    nbttagcompound1 = nbttagcompound.getCompoundTag("Data");
                    return new WorldInfo(nbttagcompound1);
                }
                catch (Exception exception)
                {
                    exception.printStackTrace();
                }
            }

            file2 = new File(file1, "level.dat_old");

            if (file2.exists())
            {
                try
                {
                    nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(file2));
                    nbttagcompound1 = nbttagcompound.getCompoundTag("Data");
                    return new WorldInfo(nbttagcompound1);
                }
                catch (Exception exception1)
                {
                    exception1.printStackTrace();
                }
            }

            return null;
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * @args: Takes two arguments - first the name of the directory containing the world and second the new name for
     * that world. @desc: Renames the world by storing the new name in level.dat. It does *not* rename the directory
     * containing the world data.
     */
    public void renameWorld(String par1Str, String par2Str)
    {
        File file1 = new File(this.savesDirectory, par1Str);

        if (file1.exists())
        {
            File file2 = new File(file1, "level.dat");

            if (file2.exists())
            {
                try
                {
                    NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(file2));
                    NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Data");
                    nbttagcompound1.setString("LevelName", par2Str);
                    CompressedStreamTools.writeCompressed(nbttagcompound, new FileOutputStream(file2));
                }
                catch (Exception exception)
                {
                    exception.printStackTrace();
                }
            }
        }
    }

    /**
     * @args: Takes one argument - the name of the directory of the world to delete. @desc: Delete the world by deleting
     * the associated directory recursively.
     */
    public boolean deleteWorldDirectory(String par1Str)
    {
        File file1 = new File(this.savesDirectory, par1Str);

        if (!file1.exists())
        {
            return true;
        }
        else
        {
            System.out.println("Deleting level " + par1Str);

            for (int i = 1; i <= 5; ++i)
            {
                System.out.println("Attempt " + i + "...");

                if (deleteFiles(file1.listFiles()))
                {
                    break;
                }

                System.out.println("Unsuccessful in deleting contents.");

                if (i < 5)
                {
                    try
                    {
                        Thread.sleep(500L);
                    }
                    catch (InterruptedException interruptedexception)
                    {
                        ;
                    }
                }
            }

            return file1.delete();
        }
    }

    /**
     * @args: Takes one argument - the list of files and directories to delete. @desc: Deletes the files and directory
     * listed in the list recursively.
     */
    protected static boolean deleteFiles(File[] par0ArrayOfFile)
    {
        for (int i = 0; i < par0ArrayOfFile.length; ++i)
        {
            File file1 = par0ArrayOfFile[i];
            System.out.println("Deleting " + file1);

            if (file1.isDirectory() && !deleteFiles(file1.listFiles()))
            {
                System.out.println("Couldn\'t delete directory " + file1);
                return false;
            }

            if (!file1.delete())
            {
                System.out.println("Couldn\'t delete file " + file1);
                return false;
            }
        }

        return true;
    }

    /**
     * Returns back a loader for the specified save directory
     */
    public ISaveHandler getSaveLoader(String par1Str, boolean par2)
    {
        return new SaveHandler(this.savesDirectory, par1Str, par2);
    }

    /**
     * Checks if the save directory uses the old map format
     */
    public boolean isOldMapFormat(String par1Str)
    {
        return false;
    }

    /**
     * Converts the specified map to the new map format. Args: worldName, loadingScreen
     */
    public boolean convertMapFormat(String par1Str, IProgressUpdate par2IProgressUpdate)
    {
        return false;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Return whether the given world can be loaded.
     */
    public boolean canLoadWorld(String par1Str)
    {
        File file1 = new File(this.savesDirectory, par1Str);
        return file1.isDirectory();
    }
}
