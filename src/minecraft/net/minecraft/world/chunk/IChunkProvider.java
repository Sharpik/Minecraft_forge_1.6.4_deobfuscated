package net.minecraft.world.chunk;

import java.util.List;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;

public interface IChunkProvider
{
    /**
     * Checks to see if a chunk exists at x, y
     */
    boolean chunkExists(int i, int j);

    /**
     * Will return back a chunk, if it doesn't exist and its not a MP client it will generates all the blocks for the
     * specified chunk from the map seed and chunk seed
     */
    Chunk provideChunk(int i, int j);

    /**
     * loads or generates the chunk at the chunk location specified
     */
    Chunk loadChunk(int i, int j);

    /**
     * Populates chunk with ores etc etc
     */
    void populate(IChunkProvider ichunkprovider, int i, int j);

    /**
     * Two modes of operation: if passed true, save all Chunks in one go.  If passed false, save up to two chunks.
     * Return true if all chunks have been saved.
     */
    boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate);

    /**
     * Unloads chunks that are marked to be unloaded. This is not guaranteed to unload every such chunk.
     */
    boolean unloadQueuedChunks();

    /**
     * Returns if the IChunkProvider supports saving.
     */
    boolean canSave();

    /**
     * Converts the instance data to a readable string.
     */
    String makeString();

    /**
     * Returns a list of creatures of the specified type that can spawn at the given location.
     */
    List getPossibleCreatures(EnumCreatureType enumcreaturetype, int i, int j, int k);

    /**
     * Returns the location of the closest structure of the specified type. If not found returns null.
     */
    ChunkPosition findClosestStructure(World world, String s, int i, int j, int k);

    int getLoadedChunkCount();

    void recreateStructures(int i, int j);

    /**
     * Save extra data not associated with any Chunk.  Not saved during autosave, only during world unload.  Currently
     * unimplemented.
     */
    void saveExtraData();
}
