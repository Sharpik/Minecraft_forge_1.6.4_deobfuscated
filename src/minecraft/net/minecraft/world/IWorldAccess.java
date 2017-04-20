package net.minecraft.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public interface IWorldAccess
{
    /**
     * On the client, re-renders the block. On the server, sends the block to the client (which will re-render it),
     * including the tile entity description packet if applicable. Args: x, y, z
     */
    void markBlockForUpdate(int i, int j, int k);

    /**
     * On the client, re-renders this block. On the server, does nothing. Used for lighting updates.
     */
    void markBlockForRenderUpdate(int i, int j, int k);

    /**
     * On the client, re-renders all blocks in this range, inclusive. On the server, does nothing. Args: min x, min y,
     * min z, max x, max y, max z
     */
    void markBlockRangeForRenderUpdate(int i, int j, int k, int l, int i1, int j1);

    /**
     * Plays the specified sound. Arg: soundName, x, y, z, volume, pitch
     */
    void playSound(String s, double d0, double d1, double d2, float f, float f1);

    /**
     * Plays sound to all near players except the player reference given
     */
    void playSoundToNearExcept(EntityPlayer entityplayer, String s, double d0, double d1, double d2, float f, float f1);

    /**
     * Spawns a particle. Arg: particleType, x, y, z, velX, velY, velZ
     */
    void spawnParticle(String s, double d0, double d1, double d2, double d3, double d4, double d5);

    /**
     * Called on all IWorldAccesses when an entity is created or loaded. On client worlds, starts downloading any
     * necessary textures. On server worlds, adds the entity to the entity tracker.
     */
    void onEntityCreate(Entity entity);

    /**
     * Called on all IWorldAccesses when an entity is unloaded or destroyed. On client worlds, releases any downloaded
     * textures. On server worlds, removes the entity from the entity tracker.
     */
    void onEntityDestroy(Entity entity);

    /**
     * Plays the specified record. Arg: recordName, x, y, z
     */
    void playRecord(String s, int i, int j, int k);

    void broadcastSound(int i, int j, int k, int l, int i1);

    /**
     * Plays a pre-canned sound effect along with potentially auxiliary data-driven one-shot behaviour (particles, etc).
     */
    void playAuxSFX(EntityPlayer entityplayer, int i, int j, int k, int l, int i1);

    /**
     * Starts (or continues) destroying a block with given ID at the given coordinates for the given partially destroyed
     * value
     */
    void destroyBlockPartially(int i, int j, int k, int l, int i1);
}
