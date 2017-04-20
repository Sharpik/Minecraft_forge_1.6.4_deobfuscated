package net.minecraft.util;

import java.util.Random;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.DungeonHooks;
import cpw.mods.fml.common.FMLLog;

public class WeightedRandomChestContent extends WeightedRandomItem
{
    /** The Item/Block ID to generate in the Chest. */
    public ItemStack theItemId;

    /** The minimum chance of item generating. */
    public int theMinimumChanceToGenerateItem;

    /** The maximum chance of item generating. */
    public int theMaximumChanceToGenerateItem;

    public WeightedRandomChestContent(int par1, int par2, int par3, int par4, int par5)
    {
        super(par5);
        this.theItemId = new ItemStack(par1, 1, par2);
        this.theMinimumChanceToGenerateItem = par3;
        this.theMaximumChanceToGenerateItem = par4;
    }

    public WeightedRandomChestContent(ItemStack par1ItemStack, int par2, int par3, int par4)
    {
        super(par4);
        this.theItemId = par1ItemStack;
        this.theMinimumChanceToGenerateItem = par2;
        this.theMaximumChanceToGenerateItem = par3;
    }

    /**
     * Generates the Chest contents.
     */
    public static void generateChestContents(Random par0Random, WeightedRandomChestContent[] par1ArrayOfWeightedRandomChestContent, IInventory par2IInventory, int par3)
    {
        for (int j = 0; j < par3; ++j)
        {
            WeightedRandomChestContent weightedrandomchestcontent = (WeightedRandomChestContent)WeightedRandom.getRandomItem(par0Random, par1ArrayOfWeightedRandomChestContent);
            ItemStack[] stacks = weightedrandomchestcontent.generateChestContent(par0Random, par2IInventory);

            for (ItemStack item : stacks)
            {
                par2IInventory.setInventorySlotContents(par0Random.nextInt(par2IInventory.getSizeInventory()), item);
            }
        }
    }

    /**
     * Generates the Dispenser contents.
     */
    public static void generateDispenserContents(Random par0Random, WeightedRandomChestContent[] par1ArrayOfWeightedRandomChestContent, TileEntityDispenser par2TileEntityDispenser, int par3)
    {
        for (int j = 0; j < par3; ++j)
        {
            WeightedRandomChestContent weightedrandomchestcontent = (WeightedRandomChestContent)WeightedRandom.getRandomItem(par0Random, par1ArrayOfWeightedRandomChestContent);
            ItemStack[] stacks = weightedrandomchestcontent.generateChestContent(par0Random, par2TileEntityDispenser);

            for (ItemStack item : stacks)
            {
                par2TileEntityDispenser.setInventorySlotContents(par0Random.nextInt(par2TileEntityDispenser.getSizeInventory()), item);
            }
        }
    }

    public static WeightedRandomChestContent[] func_92080_a(WeightedRandomChestContent[] par0ArrayOfWeightedRandomChestContent, WeightedRandomChestContent ... par1ArrayOfWeightedRandomChestContent)
    {
        WeightedRandomChestContent[] aweightedrandomchestcontent1 = new WeightedRandomChestContent[par0ArrayOfWeightedRandomChestContent.length + par1ArrayOfWeightedRandomChestContent.length];
        int i = 0;

        for (int j = 0; j < par0ArrayOfWeightedRandomChestContent.length; ++j)
        {
            aweightedrandomchestcontent1[i++] = par0ArrayOfWeightedRandomChestContent[j];
        }

        WeightedRandomChestContent[] aweightedrandomchestcontent2 = par1ArrayOfWeightedRandomChestContent;
        int k = par1ArrayOfWeightedRandomChestContent.length;

        for (int l = 0; l < k; ++l)
        {
            WeightedRandomChestContent weightedrandomchestcontent1 = aweightedrandomchestcontent2[l];
            aweightedrandomchestcontent1[i++] = weightedrandomchestcontent1;
        }

        return aweightedrandomchestcontent1;
    }

    // -- Forge hooks
    /**
     * Allow a mod to submit a custom implementation that can delegate item stack generation beyond simple stack lookup
     *
     * @param random The current random for generation
     * @param newInventory The inventory being generated (do not populate it, but you can refer to it)
     * @return An array of {@link ItemStack} to put into the chest
     */
    protected ItemStack[] generateChestContent(Random random, IInventory newInventory)
    {
        return ChestGenHooks.generateStacks(random, theItemId, theMinimumChanceToGenerateItem, theMaximumChanceToGenerateItem);
    }

}
