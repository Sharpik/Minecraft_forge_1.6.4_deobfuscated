package net.minecraft.stats;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.StatCollector;

public class StatList
{
    /** Tracks one-off stats. */
    protected static Map oneShotStats = new HashMap();
    public static List allStats = new ArrayList();
    public static List generalStats = new ArrayList();
    public static List itemStats = new ArrayList();

    /** Tracks the number of times a given block or item has been mined. */
    public static List objectMineStats = new ArrayList();

    /** times the game has been started */
    public static StatBase startGameStat = (new StatBasic(1000, "stat.startGame")).initIndependentStat().registerStat();

    /** times a world has been created */
    public static StatBase createWorldStat = (new StatBasic(1001, "stat.createWorld")).initIndependentStat().registerStat();

    /** the number of times you have loaded a world */
    public static StatBase loadWorldStat = (new StatBasic(1002, "stat.loadWorld")).initIndependentStat().registerStat();

    /** number of times you've joined a multiplayer world */
    public static StatBase joinMultiplayerStat = (new StatBasic(1003, "stat.joinMultiplayer")).initIndependentStat().registerStat();

    /** number of times you've left a game */
    public static StatBase leaveGameStat = (new StatBasic(1004, "stat.leaveGame")).initIndependentStat().registerStat();

    /** number of minutes you have played */
    public static StatBase minutesPlayedStat = (new StatBasic(1100, "stat.playOneMinute", StatBase.timeStatType)).initIndependentStat().registerStat();

    /** distance you've walked */
    public static StatBase distanceWalkedStat = (new StatBasic(2000, "stat.walkOneCm", StatBase.distanceStatType)).initIndependentStat().registerStat();

    /** distance you have swam */
    public static StatBase distanceSwumStat = (new StatBasic(2001, "stat.swimOneCm", StatBase.distanceStatType)).initIndependentStat().registerStat();

    /** the distance you have fallen */
    public static StatBase distanceFallenStat = (new StatBasic(2002, "stat.fallOneCm", StatBase.distanceStatType)).initIndependentStat().registerStat();

    /** the distance you've climbed */
    public static StatBase distanceClimbedStat = (new StatBasic(2003, "stat.climbOneCm", StatBase.distanceStatType)).initIndependentStat().registerStat();

    /** the distance you've flown */
    public static StatBase distanceFlownStat = (new StatBasic(2004, "stat.flyOneCm", StatBase.distanceStatType)).initIndependentStat().registerStat();

    /** the distance you've dived */
    public static StatBase distanceDoveStat = (new StatBasic(2005, "stat.diveOneCm", StatBase.distanceStatType)).initIndependentStat().registerStat();

    /** the distance you've traveled by minecart */
    public static StatBase distanceByMinecartStat = (new StatBasic(2006, "stat.minecartOneCm", StatBase.distanceStatType)).initIndependentStat().registerStat();

    /** the distance you've traveled by boat */
    public static StatBase distanceByBoatStat = (new StatBasic(2007, "stat.boatOneCm", StatBase.distanceStatType)).initIndependentStat().registerStat();

    /** the distance you've traveled by pig */
    public static StatBase distanceByPigStat = (new StatBasic(2008, "stat.pigOneCm", StatBase.distanceStatType)).initIndependentStat().registerStat();

    /** the times you've jumped */
    public static StatBase jumpStat = (new StatBasic(2010, "stat.jump")).initIndependentStat().registerStat();

    /** the distance you've dropped (or times you've fallen?) */
    public static StatBase dropStat = (new StatBasic(2011, "stat.drop")).initIndependentStat().registerStat();

    /** the amount of damage you've dealt */
    public static StatBase damageDealtStat = (new StatBasic(2020, "stat.damageDealt", StatBase.field_111202_k)).registerStat();

    /** the amount of damage you have taken */
    public static StatBase damageTakenStat = (new StatBasic(2021, "stat.damageTaken", StatBase.field_111202_k)).registerStat();

    /** the number of times you have died */
    public static StatBase deathsStat = (new StatBasic(2022, "stat.deaths")).registerStat();

    /** the number of mobs you have killed */
    public static StatBase mobKillsStat = (new StatBasic(2023, "stat.mobKills")).registerStat();

    /** counts the number of times you've killed a player */
    public static StatBase playerKillsStat = (new StatBasic(2024, "stat.playerKills")).registerStat();
    public static StatBase fishCaughtStat = (new StatBasic(2025, "stat.fishCaught")).registerStat();
    public static StatBase[] mineBlockStatArray = initMinableStats("stat.mineBlock", 16777216);

    /** Tracks the number of items a given block or item has been crafted. */
    public static StatBase[] objectCraftStats;

    /** Tracks the number of times a given block or item has been used. */
    public static StatBase[] objectUseStats;

    /** Tracks the number of times a given block or item has been broken. */
    public static StatBase[] objectBreakStats;
    private static boolean blockStatsInitialized;
    private static boolean itemStatsInitialized;

    /**
     * This method simply NOPs. It is presumably used to call the static constructors on server start.
     */
    public static void nopInit() {}

    /**
     * Initializes statistic fields related to breakable items and blocks.
     */
    public static void initBreakableStats()
    {
        objectUseStats = initUsableStats(objectUseStats, "stat.useItem", 16908288, 0, 256);
        objectBreakStats = initBreakStats(objectBreakStats, "stat.breakItem", 16973824, 0, 256);
        blockStatsInitialized = true;
        initCraftableStats();
    }

    public static void initStats()
    {
        objectUseStats = initUsableStats(objectUseStats, "stat.useItem", 16908288, 256, 32000);
        objectBreakStats = initBreakStats(objectBreakStats, "stat.breakItem", 16973824, 256, 32000);
        itemStatsInitialized = true;
        initCraftableStats();
    }

    /**
     * Initializes statistics related to craftable items. Is only called after both block and item stats have been
     * initialized.
     */
    public static void initCraftableStats()
    {
        if (blockStatsInitialized && itemStatsInitialized)
        {
            HashSet hashset = new HashSet();
            Iterator iterator = CraftingManager.getInstance().getRecipeList().iterator();

            while (iterator.hasNext())
            {
                IRecipe irecipe = (IRecipe)iterator.next();

                if (irecipe.getRecipeOutput() != null)
                {
                    hashset.add(Integer.valueOf(irecipe.getRecipeOutput().itemID));
                }
            }

            iterator = FurnaceRecipes.smelting().getSmeltingList().values().iterator();

            while (iterator.hasNext())
            {
                ItemStack itemstack = (ItemStack)iterator.next();
                hashset.add(Integer.valueOf(itemstack.itemID));
            }

            objectCraftStats = new StatBase[32000];
            iterator = hashset.iterator();

            while (iterator.hasNext())
            {
                Integer integer = (Integer)iterator.next();

                if (Item.itemsList[integer.intValue()] != null)
                {
                    String s = StatCollector.translateToLocalFormatted("stat.craftItem", new Object[] {Item.itemsList[integer.intValue()].getStatName()});
                    objectCraftStats[integer.intValue()] = (new StatCrafting(16842752 + integer.intValue(), s, integer.intValue())).registerStat();
                }
            }

            replaceAllSimilarBlocks(objectCraftStats);
        }
    }

    /**
     * Initializes statistic fields related to minable items and blocks.
     */
    private static StatBase[] initMinableStats(String par0Str, int par1)
    {
        StatBase[] astatbase = new StatBase[Block.blocksList.length];

        for (int j = 0; j < Block.blocksList.length; ++j)
        {
            if (Block.blocksList[j] != null && Block.blocksList[j].getEnableStats())
            {
                String s1 = StatCollector.translateToLocalFormatted(par0Str, new Object[] {Block.blocksList[j].getLocalizedName()});
                astatbase[j] = (new StatCrafting(par1 + j, s1, j)).registerStat();
                objectMineStats.add((StatCrafting)astatbase[j]);
            }
        }

        replaceAllSimilarBlocks(astatbase);
        return astatbase;
    }

    /**
     * Initializes statistic fields related to usable items and blocks.
     */
    private static StatBase[] initUsableStats(StatBase[] par0ArrayOfStatBase, String par1Str, int par2, int par3, int par4)
    {
        if (par0ArrayOfStatBase == null)
        {
            par0ArrayOfStatBase = new StatBase[32000];
        }

        for (int l = par3; l < par4; ++l)
        {
            if (Item.itemsList[l] != null)
            {
                String s1 = StatCollector.translateToLocalFormatted(par1Str, new Object[] {Item.itemsList[l].getStatName()});
                par0ArrayOfStatBase[l] = (new StatCrafting(par2 + l, s1, l)).registerStat();

                if (l >= 256)
                {
                    itemStats.add((StatCrafting)par0ArrayOfStatBase[l]);
                }
            }
        }

        replaceAllSimilarBlocks(par0ArrayOfStatBase);
        return par0ArrayOfStatBase;
    }

    private static StatBase[] initBreakStats(StatBase[] par0ArrayOfStatBase, String par1Str, int par2, int par3, int par4)
    {
        if (par0ArrayOfStatBase == null)
        {
            par0ArrayOfStatBase = new StatBase[32000];
        }

        for (int l = par3; l < par4; ++l)
        {
            if (Item.itemsList[l] != null && Item.itemsList[l].isDamageable())
            {
                String s1 = StatCollector.translateToLocalFormatted(par1Str, new Object[] {Item.itemsList[l].getStatName()});
                par0ArrayOfStatBase[l] = (new StatCrafting(par2 + l, s1, l)).registerStat();
            }
        }

        replaceAllSimilarBlocks(par0ArrayOfStatBase);
        return par0ArrayOfStatBase;
    }

    /**
     * Forces all dual blocks to count for each other on the stats list
     */
    private static void replaceAllSimilarBlocks(StatBase[] par0ArrayOfStatBase)
    {
        replaceSimilarBlocks(par0ArrayOfStatBase, Block.waterStill.blockID, Block.waterMoving.blockID);
        replaceSimilarBlocks(par0ArrayOfStatBase, Block.lavaStill.blockID, Block.lavaStill.blockID);
        replaceSimilarBlocks(par0ArrayOfStatBase, Block.pumpkinLantern.blockID, Block.pumpkin.blockID);
        replaceSimilarBlocks(par0ArrayOfStatBase, Block.furnaceBurning.blockID, Block.furnaceIdle.blockID);
        replaceSimilarBlocks(par0ArrayOfStatBase, Block.oreRedstoneGlowing.blockID, Block.oreRedstone.blockID);
        replaceSimilarBlocks(par0ArrayOfStatBase, Block.redstoneRepeaterActive.blockID, Block.redstoneRepeaterIdle.blockID);
        replaceSimilarBlocks(par0ArrayOfStatBase, Block.torchRedstoneActive.blockID, Block.torchRedstoneIdle.blockID);
        replaceSimilarBlocks(par0ArrayOfStatBase, Block.mushroomRed.blockID, Block.mushroomBrown.blockID);
        replaceSimilarBlocks(par0ArrayOfStatBase, Block.stoneDoubleSlab.blockID, Block.stoneSingleSlab.blockID);
        replaceSimilarBlocks(par0ArrayOfStatBase, Block.woodDoubleSlab.blockID, Block.woodSingleSlab.blockID);
        replaceSimilarBlocks(par0ArrayOfStatBase, Block.grass.blockID, Block.dirt.blockID);
        replaceSimilarBlocks(par0ArrayOfStatBase, Block.tilledField.blockID, Block.dirt.blockID);
    }

    /**
     * Forces stats for one block to add to another block, such as idle and active furnaces
     */
    private static void replaceSimilarBlocks(StatBase[] par0ArrayOfStatBase, int par1, int par2)
    {
        if (par0ArrayOfStatBase[par1] != null && par0ArrayOfStatBase[par2] == null)
        {
            par0ArrayOfStatBase[par2] = par0ArrayOfStatBase[par1];
        }
        else
        {
            allStats.remove(par0ArrayOfStatBase[par1]);
            objectMineStats.remove(par0ArrayOfStatBase[par1]);
            generalStats.remove(par0ArrayOfStatBase[par1]);
            par0ArrayOfStatBase[par1] = par0ArrayOfStatBase[par2];
        }
    }

    @SideOnly(Side.CLIENT)
    public static StatBase getOneShotStat(int par0)
    {
        return (StatBase)oneShotStats.get(Integer.valueOf(par0));
    }

    static
    {
        AchievementList.init();
    }
}
