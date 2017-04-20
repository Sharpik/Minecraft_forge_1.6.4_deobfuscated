package net.minecraft.world;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3Pool;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.ForgeDirection;

public interface IBlockAccess
{
    /**
     * Returns the block ID at coords x,y,z
     */
    int getBlockId(int i, int j, int k);

    /**
     * Returns the TileEntity associated with a given block in X,Y,Z coordinates, or null if no TileEntity exists
     */
    TileEntity getBlockTileEntity(int i, int j, int k);

    @SideOnly(Side.CLIENT)

    /**
     * Any Light rendered on a 1.8 Block goes through here
     */
    int getLightBrightnessForSkyBlocks(int i, int j, int k, int l);

    /**
     * Returns the block metadata at coords x,y,z
     */
    int getBlockMetadata(int i, int j, int k);

    @SideOnly(Side.CLIENT)
    float getBrightness(int i, int j, int k, int l);

    @SideOnly(Side.CLIENT)

    /**
     * Returns how bright the block is shown as which is the block's light value looked up in a lookup table (light
     * values aren't linear for brightness). Args: x, y, z
     */
    float getLightBrightness(int i, int j, int k);

    /**
     * Returns the block's material.
     */
    Material getBlockMaterial(int i, int j, int k);

    @SideOnly(Side.CLIENT)

    /**
     * Returns true if the block at the specified coordinates is an opaque cube. Args: x, y, z
     */
    boolean isBlockOpaqueCube(int i, int j, int k);

    /**
     * Indicate if a material is a normal solid opaque cube.
     */
    boolean isBlockNormalCube(int i, int j, int k);

    /**
     * Returns true if the block at the specified coordinates is empty
     */
    boolean isAirBlock(int i, int j, int k);

    @SideOnly(Side.CLIENT)

    /**
     * Gets the biome for a given set of x/z coordinates
     */
    BiomeGenBase getBiomeGenForCoords(int i, int j);

    @SideOnly(Side.CLIENT)

    /**
     * Returns current world height.
     */
    int getHeight();

    @SideOnly(Side.CLIENT)

    /**
     * set by !chunk.getAreLevelsEmpty
     */
    boolean extendedLevelsInChunkCache();

    @SideOnly(Side.CLIENT)

    /**
     * Returns true if the block at the given coordinate has a solid (buildable) top surface.
     */
    boolean doesBlockHaveSolidTopSurface(int i, int j, int k);

    /**
     * Return the Vec3Pool object for this world.
     */
    Vec3Pool getWorldVec3Pool();

    /**
     * Is this block powering in the specified direction Args: x, y, z, direction
     */
    int isBlockProvidingPowerTo(int i, int j, int k, int l);

    /**
     * FORGE: isBlockSolidOnSide, pulled up from {@link World}
     *
     * @param x X coord
     * @param y Y coord
     * @param z Z coord
     * @param side Side
     * @param _default default return value
     * @return if the block is solid on the side
     */
    boolean isBlockSolidOnSide(int x, int y, int z, ForgeDirection side, boolean _default);
}
