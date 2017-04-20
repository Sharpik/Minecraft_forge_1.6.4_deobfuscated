package net.minecraft.world.storage;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.util.IProgressUpdate;

public interface ISaveFormat
{
    /**
     * Returns back a loader for the specified save directory
     */
    ISaveHandler getSaveLoader(String s, boolean flag);

    @SideOnly(Side.CLIENT)
    List getSaveList() throws AnvilConverterException;

    void flushCache();

    @SideOnly(Side.CLIENT)

    /**
     * gets the world info
     */
    WorldInfo getWorldInfo(String s);

    /**
     * @args: Takes one argument - the name of the directory of the world to delete. @desc: Delete the world by deleting
     * the associated directory recursively.
     */
    boolean deleteWorldDirectory(String s);

    @SideOnly(Side.CLIENT)

    /**
     * @args: Takes two arguments - first the name of the directory containing the world and second the new name for
     * that world. @desc: Renames the world by storing the new name in level.dat. It does *not* rename the directory
     * containing the world data.
     */
    void renameWorld(String s, String s1);

    /**
     * Checks if the save directory uses the old map format
     */
    boolean isOldMapFormat(String s);

    /**
     * Converts the specified map to the new map format. Args: worldName, loadingScreen
     */
    boolean convertMapFormat(String s, IProgressUpdate iprogressupdate);

    @SideOnly(Side.CLIENT)

    /**
     * Return whether the given world can be loaded.
     */
    boolean canLoadWorld(String s);
}
