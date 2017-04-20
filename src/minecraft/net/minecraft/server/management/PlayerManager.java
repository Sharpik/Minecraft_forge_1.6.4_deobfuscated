package net.minecraft.server.management;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.LongHashMap;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;

public class PlayerManager
{
    private final WorldServer theWorldServer;

    /** players in the current instance */
    private final List players = new ArrayList();

    /**
     * A map of chunk position (two ints concatenated into a long) to PlayerInstance
     */
    private final LongHashMap playerInstances = new LongHashMap();

    /**
     * contains a PlayerInstance for every chunk they can see. the "player instance" cotains a list of all players who
     * can also that chunk
     */
    private final List chunkWatcherWithPlayers = new ArrayList();

    /** This field is using when chunk should be processed (every 8000 ticks) */
    private final List playerInstanceList = new ArrayList();

    /**
     * Number of chunks the server sends to the client. Valid 3<=x<=15. In server.properties.
     */
    private final int playerViewRadius;

    /** time what is using to check if InhabitedTime should be calculated */
    private long previousTotalWorldTime;

    /** x, z direction vectors: east, south, west, north */
    private final int[][] xzDirectionsConst = new int[][] {{1, 0}, {0, 1}, { -1, 0}, {0, -1}};

    public PlayerManager(WorldServer par1WorldServer, int par2)
    {
        if (par2 > 15)
        {
            throw new IllegalArgumentException("Too big view radius!");
        }
        else if (par2 < 3)
        {
            throw new IllegalArgumentException("Too small view radius!");
        }
        else
        {
            this.playerViewRadius = par2;
            this.theWorldServer = par1WorldServer;
        }
    }

    public WorldServer getWorldServer()
    {
        return this.theWorldServer;
    }

    /**
     * updates all the player instances that need to be updated
     */
    public void updatePlayerInstances()
    {
        long i = this.theWorldServer.getTotalWorldTime();
        int j;
        PlayerInstance playerinstance;

        if (i - this.previousTotalWorldTime > 8000L)
        {
            this.previousTotalWorldTime = i;

            for (j = 0; j < this.playerInstanceList.size(); ++j)
            {
                playerinstance = (PlayerInstance)this.playerInstanceList.get(j);
                playerinstance.sendChunkUpdate();
                playerinstance.processChunk();
            }
        }
        else
        {
            for (j = 0; j < this.chunkWatcherWithPlayers.size(); ++j)
            {
                playerinstance = (PlayerInstance)this.chunkWatcherWithPlayers.get(j);
                playerinstance.sendChunkUpdate();
            }
        }

        this.chunkWatcherWithPlayers.clear();

        if (this.players.isEmpty())
        {
            WorldProvider worldprovider = this.theWorldServer.provider;

            if (!worldprovider.canRespawnHere())
            {
                this.theWorldServer.theChunkProviderServer.unloadAllChunks();
            }
        }
    }

    public PlayerInstance getOrCreateChunkWatcher(int par1, int par2, boolean par3)
    {
        long k = (long)par1 + 2147483647L | (long)par2 + 2147483647L << 32;
        PlayerInstance playerinstance = (PlayerInstance)this.playerInstances.getValueByKey(k);

        if (playerinstance == null && par3)
        {
            playerinstance = new PlayerInstance(this, par1, par2);
            this.playerInstances.add(k, playerinstance);
            this.playerInstanceList.add(playerinstance);
        }

        return playerinstance;
    }

    /**
     * Called by WorldManager.markBlockForUpdate; marks a block to be resent to clients.
     */
    public void markBlockForUpdate(int par1, int par2, int par3)
    {
        int l = par1 >> 4;
        int i1 = par3 >> 4;
        PlayerInstance playerinstance = this.getOrCreateChunkWatcher(l, i1, false);

        if (playerinstance != null)
        {
            playerinstance.flagChunkForUpdate(par1 & 15, par2, par3 & 15);
        }
    }

    /**
     * Adds an EntityPlayerMP to the PlayerManager.
     */
    public void addPlayer(EntityPlayerMP par1EntityPlayerMP)
    {
        int i = (int)par1EntityPlayerMP.posX >> 4;
        int j = (int)par1EntityPlayerMP.posZ >> 4;
        par1EntityPlayerMP.managedPosX = par1EntityPlayerMP.posX;
        par1EntityPlayerMP.managedPosZ = par1EntityPlayerMP.posZ;

        for (int k = i - this.playerViewRadius; k <= i + this.playerViewRadius; ++k)
        {
            for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; ++l)
            {
                this.getOrCreateChunkWatcher(k, l, true).addPlayer(par1EntityPlayerMP);
            }
        }

        this.players.add(par1EntityPlayerMP);
        this.filterChunkLoadQueue(par1EntityPlayerMP);
    }

    /**
     * Removes all chunks from the given player's chunk load queue that are not in viewing range of the player.
     */
    public void filterChunkLoadQueue(EntityPlayerMP par1EntityPlayerMP)
    {
        ArrayList arraylist = new ArrayList(par1EntityPlayerMP.loadedChunks);
        int i = 0;
        int j = this.playerViewRadius;
        int k = (int)par1EntityPlayerMP.posX >> 4;
        int l = (int)par1EntityPlayerMP.posZ >> 4;
        int i1 = 0;
        int j1 = 0;
        ChunkCoordIntPair chunkcoordintpair = PlayerInstance.getChunkLocation(this.getOrCreateChunkWatcher(k, l, true));
        par1EntityPlayerMP.loadedChunks.clear();

        if (arraylist.contains(chunkcoordintpair))
        {
            par1EntityPlayerMP.loadedChunks.add(chunkcoordintpair);
        }

        int k1;

        for (k1 = 1; k1 <= j * 2; ++k1)
        {
            for (int l1 = 0; l1 < 2; ++l1)
            {
                int[] aint = this.xzDirectionsConst[i++ % 4];

                for (int i2 = 0; i2 < k1; ++i2)
                {
                    i1 += aint[0];
                    j1 += aint[1];
                    chunkcoordintpair = PlayerInstance.getChunkLocation(this.getOrCreateChunkWatcher(k + i1, l + j1, true));

                    if (arraylist.contains(chunkcoordintpair))
                    {
                        par1EntityPlayerMP.loadedChunks.add(chunkcoordintpair);
                    }
                }
            }
        }

        i %= 4;

        for (k1 = 0; k1 < j * 2; ++k1)
        {
            i1 += this.xzDirectionsConst[i][0];
            j1 += this.xzDirectionsConst[i][1];
            chunkcoordintpair = PlayerInstance.getChunkLocation(this.getOrCreateChunkWatcher(k + i1, l + j1, true));

            if (arraylist.contains(chunkcoordintpair))
            {
                par1EntityPlayerMP.loadedChunks.add(chunkcoordintpair);
            }
        }
    }

    /**
     * Removes an EntityPlayerMP from the PlayerManager.
     */
    public void removePlayer(EntityPlayerMP par1EntityPlayerMP)
    {
        int i = (int)par1EntityPlayerMP.managedPosX >> 4;
        int j = (int)par1EntityPlayerMP.managedPosZ >> 4;

        for (int k = i - this.playerViewRadius; k <= i + this.playerViewRadius; ++k)
        {
            for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; ++l)
            {
                PlayerInstance playerinstance = this.getOrCreateChunkWatcher(k, l, false);

                if (playerinstance != null)
                {
                    playerinstance.removePlayer(par1EntityPlayerMP);
                }
            }
        }

        this.players.remove(par1EntityPlayerMP);
    }

    /**
     * Determine if two rectangles centered at the given points overlap for the provided radius. Arguments: x1, z1, x2,
     * z2, radius.
     */
    private boolean overlaps(int par1, int par2, int par3, int par4, int par5)
    {
        int j1 = par1 - par3;
        int k1 = par2 - par4;
        return j1 >= -par5 && j1 <= par5 ? k1 >= -par5 && k1 <= par5 : false;
    }

    /**
     * update chunks around a player being moved by server logic (e.g. cart, boat)
     */
    public void updateMountedMovingPlayer(EntityPlayerMP par1EntityPlayerMP)
    {
        int i = (int)par1EntityPlayerMP.posX >> 4;
        int j = (int)par1EntityPlayerMP.posZ >> 4;
        double d0 = par1EntityPlayerMP.managedPosX - par1EntityPlayerMP.posX;
        double d1 = par1EntityPlayerMP.managedPosZ - par1EntityPlayerMP.posZ;
        double d2 = d0 * d0 + d1 * d1;

        if (d2 >= 64.0D)
        {
            int k = (int)par1EntityPlayerMP.managedPosX >> 4;
            int l = (int)par1EntityPlayerMP.managedPosZ >> 4;
            int i1 = this.playerViewRadius;
            int j1 = i - k;
            int k1 = j - l;

            if (j1 != 0 || k1 != 0)
            {
                for (int l1 = i - i1; l1 <= i + i1; ++l1)
                {
                    for (int i2 = j - i1; i2 <= j + i1; ++i2)
                    {
                        if (!this.overlaps(l1, i2, k, l, i1))
                        {
                            this.getOrCreateChunkWatcher(l1, i2, true).addPlayer(par1EntityPlayerMP);
                        }

                        if (!this.overlaps(l1 - j1, i2 - k1, i, j, i1))
                        {
                            PlayerInstance playerinstance = this.getOrCreateChunkWatcher(l1 - j1, i2 - k1, false);

                            if (playerinstance != null)
                            {
                                playerinstance.removePlayer(par1EntityPlayerMP);
                            }
                        }
                    }
                }

                this.filterChunkLoadQueue(par1EntityPlayerMP);
                par1EntityPlayerMP.managedPosX = par1EntityPlayerMP.posX;
                par1EntityPlayerMP.managedPosZ = par1EntityPlayerMP.posZ;
            }
        }
    }

    public boolean isPlayerWatchingChunk(EntityPlayerMP par1EntityPlayerMP, int par2, int par3)
    {
        PlayerInstance playerinstance = this.getOrCreateChunkWatcher(par2, par3, false);
        return playerinstance == null ? false : PlayerInstance.getPlayersInChunk(playerinstance).contains(par1EntityPlayerMP) && !par1EntityPlayerMP.loadedChunks.contains(PlayerInstance.getChunkLocation(playerinstance));
    }

    /**
     * Get the furthest viewable block given player's view distance
     */
    public static int getFurthestViewableBlock(int par0)
    {
        return par0 * 16 - 16;
    }

    static WorldServer getWorldServer(PlayerManager par0PlayerManager)
    {
        return par0PlayerManager.theWorldServer;
    }

    static LongHashMap getChunkWatchers(PlayerManager par0PlayerManager)
    {
        return par0PlayerManager.playerInstances;
    }

    /**
     * Returns player instances as list
     */
    static List getChunkWatcherList(PlayerManager par0PlayerManager)
    {
        return par0PlayerManager.playerInstanceList;
    }

    static List getChunkWatchersWithPlayers(PlayerManager par0PlayerManager)
    {
        return par0PlayerManager.chunkWatcherWithPlayers;
    }
}
