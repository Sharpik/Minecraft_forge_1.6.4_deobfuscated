package net.minecraft.world.gen.structure;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLever;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;

import net.minecraftforge.common.ChestGenHooks;
import static net.minecraftforge.common.ChestGenHooks.*;

public class ComponentScatteredFeatureJunglePyramid extends ComponentScatteredFeature
{
    private boolean field_74947_h;
    private boolean field_74948_i;
    private boolean field_74945_j;
    private boolean field_74946_k;

    /** List of Chest contents to be generated in the Jungle Pyramid chests. */
    public static final WeightedRandomChestContent[] junglePyramidsChestContents = new WeightedRandomChestContent[] {new WeightedRandomChestContent(Item.diamond.itemID, 0, 1, 3, 3), new WeightedRandomChestContent(Item.ingotIron.itemID, 0, 1, 5, 10), new WeightedRandomChestContent(Item.ingotGold.itemID, 0, 2, 7, 15), new WeightedRandomChestContent(Item.emerald.itemID, 0, 1, 3, 2), new WeightedRandomChestContent(Item.bone.itemID, 0, 4, 6, 20), new WeightedRandomChestContent(Item.rottenFlesh.itemID, 0, 3, 7, 16), new WeightedRandomChestContent(Item.saddle.itemID, 0, 1, 1, 3), new WeightedRandomChestContent(Item.horseArmorIron.itemID, 0, 1, 1, 1), new WeightedRandomChestContent(Item.horseArmorGold.itemID, 0, 1, 1, 1), new WeightedRandomChestContent(Item.horseArmorDiamond.itemID, 0, 1, 1, 1)};

    /**
     * List of Dispenser contents to be generated in the Jungle Pyramid dispensers.
     */
    public static final WeightedRandomChestContent[] junglePyramidsDispenserContents = new WeightedRandomChestContent[] {new WeightedRandomChestContent(Item.arrow.itemID, 0, 2, 7, 30)};

    /** List of random stones to be generated in the Jungle Pyramid. */
    private static StructureScatteredFeatureStones junglePyramidsRandomScatteredStones = new StructureScatteredFeatureStones((ComponentScatteredFeaturePieces2)null);

    public ComponentScatteredFeatureJunglePyramid() {}

    public ComponentScatteredFeatureJunglePyramid(Random par1Random, int par2, int par3)
    {
        super(par1Random, par2, 64, par3, 12, 10, 15);
    }

    protected void func_143012_a(NBTTagCompound par1NBTTagCompound)
    {
        super.func_143012_a(par1NBTTagCompound);
        par1NBTTagCompound.setBoolean("placedMainChest", this.field_74947_h);
        par1NBTTagCompound.setBoolean("placedHiddenChest", this.field_74948_i);
        par1NBTTagCompound.setBoolean("placedTrap1", this.field_74945_j);
        par1NBTTagCompound.setBoolean("placedTrap2", this.field_74946_k);
    }

    protected void func_143011_b(NBTTagCompound par1NBTTagCompound)
    {
        super.func_143011_b(par1NBTTagCompound);
        this.field_74947_h = par1NBTTagCompound.getBoolean("placedMainChest");
        this.field_74948_i = par1NBTTagCompound.getBoolean("placedHiddenChest");
        this.field_74945_j = par1NBTTagCompound.getBoolean("placedTrap1");
        this.field_74946_k = par1NBTTagCompound.getBoolean("placedTrap2");
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox)
    {
        if (!this.func_74935_a(par1World, par3StructureBoundingBox, 0))
        {
            return false;
        }
        else
        {
            int i = this.getMetadataWithOffset(Block.stairsCobblestone.blockID, 3);
            int j = this.getMetadataWithOffset(Block.stairsCobblestone.blockID, 2);
            int k = this.getMetadataWithOffset(Block.stairsCobblestone.blockID, 0);
            int l = this.getMetadataWithOffset(Block.stairsCobblestone.blockID, 1);
            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 0, -4, 0, this.scatteredFeatureSizeX - 1, 0, this.scatteredFeatureSizeZ - 1, false, par2Random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 2, 1, 2, 9, 2, 2, false, par2Random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 2, 1, 12, 9, 2, 12, false, par2Random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 2, 1, 3, 2, 2, 11, false, par2Random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 9, 1, 3, 9, 2, 11, false, par2Random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 1, 3, 1, 10, 6, 1, false, par2Random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 1, 3, 13, 10, 6, 13, false, par2Random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 1, 3, 2, 1, 6, 12, false, par2Random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 10, 3, 2, 10, 6, 12, false, par2Random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 2, 3, 2, 9, 3, 12, false, par2Random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 2, 6, 2, 9, 6, 12, false, par2Random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 3, 7, 3, 8, 7, 11, false, par2Random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 4, 8, 4, 7, 8, 10, false, par2Random, junglePyramidsRandomScatteredStones);
            this.fillWithAir(par1World, par3StructureBoundingBox, 3, 1, 3, 8, 2, 11);
            this.fillWithAir(par1World, par3StructureBoundingBox, 4, 3, 6, 7, 3, 9);
            this.fillWithAir(par1World, par3StructureBoundingBox, 2, 4, 2, 9, 5, 12);
            this.fillWithAir(par1World, par3StructureBoundingBox, 4, 6, 5, 7, 6, 9);
            this.fillWithAir(par1World, par3StructureBoundingBox, 5, 7, 6, 6, 7, 8);
            this.fillWithAir(par1World, par3StructureBoundingBox, 5, 1, 2, 6, 2, 2);
            this.fillWithAir(par1World, par3StructureBoundingBox, 5, 2, 12, 6, 2, 12);
            this.fillWithAir(par1World, par3StructureBoundingBox, 5, 5, 1, 6, 5, 1);
            this.fillWithAir(par1World, par3StructureBoundingBox, 5, 5, 13, 6, 5, 13);
            this.placeBlockAtCurrentPosition(par1World, 0, 0, 1, 5, 5, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, 0, 0, 10, 5, 5, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, 0, 0, 1, 5, 9, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, 0, 0, 10, 5, 9, par3StructureBoundingBox);
            int i1;

            for (i1 = 0; i1 <= 14; i1 += 14)
            {
                this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 2, 4, i1, 2, 5, i1, false, par2Random, junglePyramidsRandomScatteredStones);
                this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 4, 4, i1, 4, 5, i1, false, par2Random, junglePyramidsRandomScatteredStones);
                this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 7, 4, i1, 7, 5, i1, false, par2Random, junglePyramidsRandomScatteredStones);
                this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 9, 4, i1, 9, 5, i1, false, par2Random, junglePyramidsRandomScatteredStones);
            }

            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 5, 6, 0, 6, 6, 0, false, par2Random, junglePyramidsRandomScatteredStones);

            for (i1 = 0; i1 <= 11; i1 += 11)
            {
                for (int j1 = 2; j1 <= 12; j1 += 2)
                {
                    this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, i1, 4, j1, i1, 5, j1, false, par2Random, junglePyramidsRandomScatteredStones);
                }

                this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, i1, 6, 5, i1, 6, 5, false, par2Random, junglePyramidsRandomScatteredStones);
                this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, i1, 6, 9, i1, 6, 9, false, par2Random, junglePyramidsRandomScatteredStones);
            }

            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 2, 7, 2, 2, 9, 2, false, par2Random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 9, 7, 2, 9, 9, 2, false, par2Random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 2, 7, 12, 2, 9, 12, false, par2Random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 9, 7, 12, 9, 9, 12, false, par2Random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 4, 9, 4, 4, 9, 4, false, par2Random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 7, 9, 4, 7, 9, 4, false, par2Random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 4, 9, 10, 4, 9, 10, false, par2Random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 7, 9, 10, 7, 9, 10, false, par2Random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 5, 9, 7, 6, 9, 7, false, par2Random, junglePyramidsRandomScatteredStones);
            this.placeBlockAtCurrentPosition(par1World, Block.stairsCobblestone.blockID, i, 5, 9, 6, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.stairsCobblestone.blockID, i, 6, 9, 6, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.stairsCobblestone.blockID, j, 5, 9, 8, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.stairsCobblestone.blockID, j, 6, 9, 8, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.stairsCobblestone.blockID, i, 4, 0, 0, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.stairsCobblestone.blockID, i, 5, 0, 0, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.stairsCobblestone.blockID, i, 6, 0, 0, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.stairsCobblestone.blockID, i, 7, 0, 0, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.stairsCobblestone.blockID, i, 4, 1, 8, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.stairsCobblestone.blockID, i, 4, 2, 9, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.stairsCobblestone.blockID, i, 4, 3, 10, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.stairsCobblestone.blockID, i, 7, 1, 8, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.stairsCobblestone.blockID, i, 7, 2, 9, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.stairsCobblestone.blockID, i, 7, 3, 10, par3StructureBoundingBox);
            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 4, 1, 9, 4, 1, 9, false, par2Random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 7, 1, 9, 7, 1, 9, false, par2Random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 4, 1, 10, 7, 2, 10, false, par2Random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 5, 4, 5, 6, 4, 5, false, par2Random, junglePyramidsRandomScatteredStones);
            this.placeBlockAtCurrentPosition(par1World, Block.stairsCobblestone.blockID, k, 4, 4, 5, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.stairsCobblestone.blockID, l, 7, 4, 5, par3StructureBoundingBox);

            for (i1 = 0; i1 < 4; ++i1)
            {
                this.placeBlockAtCurrentPosition(par1World, Block.stairsCobblestone.blockID, j, 5, 0 - i1, 6 + i1, par3StructureBoundingBox);
                this.placeBlockAtCurrentPosition(par1World, Block.stairsCobblestone.blockID, j, 6, 0 - i1, 6 + i1, par3StructureBoundingBox);
                this.fillWithAir(par1World, par3StructureBoundingBox, 5, 0 - i1, 7 + i1, 6, 0 - i1, 9 + i1);
            }

            this.fillWithAir(par1World, par3StructureBoundingBox, 1, -3, 12, 10, -1, 13);
            this.fillWithAir(par1World, par3StructureBoundingBox, 1, -3, 1, 3, -1, 13);
            this.fillWithAir(par1World, par3StructureBoundingBox, 1, -3, 1, 9, -1, 5);

            for (i1 = 1; i1 <= 13; i1 += 2)
            {
                this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 1, -3, i1, 1, -2, i1, false, par2Random, junglePyramidsRandomScatteredStones);
            }

            for (i1 = 2; i1 <= 12; i1 += 2)
            {
                this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 1, -1, i1, 3, -1, i1, false, par2Random, junglePyramidsRandomScatteredStones);
            }

            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 2, -2, 1, 5, -2, 1, false, par2Random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 7, -2, 1, 9, -2, 1, false, par2Random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 6, -3, 1, 6, -3, 1, false, par2Random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 6, -1, 1, 6, -1, 1, false, par2Random, junglePyramidsRandomScatteredStones);
            this.placeBlockAtCurrentPosition(par1World, Block.tripWireSource.blockID, this.getMetadataWithOffset(Block.tripWireSource.blockID, 3) | 4, 1, -3, 8, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.tripWireSource.blockID, this.getMetadataWithOffset(Block.tripWireSource.blockID, 1) | 4, 4, -3, 8, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.tripWire.blockID, 4, 2, -3, 8, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.tripWire.blockID, 4, 3, -3, 8, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.redstoneWire.blockID, 0, 5, -3, 7, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.redstoneWire.blockID, 0, 5, -3, 6, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.redstoneWire.blockID, 0, 5, -3, 5, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.redstoneWire.blockID, 0, 5, -3, 4, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.redstoneWire.blockID, 0, 5, -3, 3, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.redstoneWire.blockID, 0, 5, -3, 2, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.redstoneWire.blockID, 0, 5, -3, 1, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.redstoneWire.blockID, 0, 4, -3, 1, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.cobblestoneMossy.blockID, 0, 3, -3, 1, par3StructureBoundingBox);

            ChestGenHooks dispenser = ChestGenHooks.getInfo(PYRAMID_JUNGLE_DISPENSER);
            ChestGenHooks chest = ChestGenHooks.getInfo(PYRAMID_JUNGLE_CHEST);

            if (!this.field_74945_j)
            {
                this.field_74945_j = this.generateStructureDispenserContents(par1World, par3StructureBoundingBox, par2Random, 3, -2, 1, 2, dispenser.getItems(par2Random), dispenser.getCount(par2Random));
            }

            this.placeBlockAtCurrentPosition(par1World, Block.vine.blockID, 15, 3, -2, 2, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.tripWireSource.blockID, this.getMetadataWithOffset(Block.tripWireSource.blockID, 2) | 4, 7, -3, 1, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.tripWireSource.blockID, this.getMetadataWithOffset(Block.tripWireSource.blockID, 0) | 4, 7, -3, 5, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.tripWire.blockID, 4, 7, -3, 2, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.tripWire.blockID, 4, 7, -3, 3, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.tripWire.blockID, 4, 7, -3, 4, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.redstoneWire.blockID, 0, 8, -3, 6, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.redstoneWire.blockID, 0, 9, -3, 6, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.redstoneWire.blockID, 0, 9, -3, 5, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.cobblestoneMossy.blockID, 0, 9, -3, 4, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.redstoneWire.blockID, 0, 9, -2, 4, par3StructureBoundingBox);

            if (!this.field_74946_k)
            {
                this.field_74946_k = this.generateStructureDispenserContents(par1World, par3StructureBoundingBox, par2Random, 9, -2, 3, 4, dispenser.getItems(par2Random), dispenser.getCount(par2Random));
            }

            this.placeBlockAtCurrentPosition(par1World, Block.vine.blockID, 15, 8, -1, 3, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.vine.blockID, 15, 8, -2, 3, par3StructureBoundingBox);

            if (!this.field_74947_h)
            {
                this.field_74947_h = this.generateStructureChestContents(par1World, par3StructureBoundingBox, par2Random, 8, -3, 3, chest.getItems(par2Random), chest.getCount(par2Random));
            }

            this.placeBlockAtCurrentPosition(par1World, Block.cobblestoneMossy.blockID, 0, 9, -3, 2, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.cobblestoneMossy.blockID, 0, 8, -3, 1, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.cobblestoneMossy.blockID, 0, 4, -3, 5, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.cobblestoneMossy.blockID, 0, 5, -2, 5, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.cobblestoneMossy.blockID, 0, 5, -1, 5, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.cobblestoneMossy.blockID, 0, 6, -3, 5, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.cobblestoneMossy.blockID, 0, 7, -2, 5, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.cobblestoneMossy.blockID, 0, 7, -1, 5, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.cobblestoneMossy.blockID, 0, 8, -3, 5, par3StructureBoundingBox);
            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 9, -1, 1, 9, -1, 5, false, par2Random, junglePyramidsRandomScatteredStones);
            this.fillWithAir(par1World, par3StructureBoundingBox, 8, -3, 8, 10, -1, 10);
            this.placeBlockAtCurrentPosition(par1World, Block.stoneBrick.blockID, 3, 8, -2, 11, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.stoneBrick.blockID, 3, 9, -2, 11, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.stoneBrick.blockID, 3, 10, -2, 11, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.lever.blockID, BlockLever.invertMetadata(this.getMetadataWithOffset(Block.lever.blockID, 2)), 8, -2, 12, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.lever.blockID, BlockLever.invertMetadata(this.getMetadataWithOffset(Block.lever.blockID, 2)), 9, -2, 12, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.lever.blockID, BlockLever.invertMetadata(this.getMetadataWithOffset(Block.lever.blockID, 2)), 10, -2, 12, par3StructureBoundingBox);
            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 8, -3, 8, 8, -3, 10, false, par2Random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 10, -3, 8, 10, -3, 10, false, par2Random, junglePyramidsRandomScatteredStones);
            this.placeBlockAtCurrentPosition(par1World, Block.cobblestoneMossy.blockID, 0, 10, -2, 9, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.redstoneWire.blockID, 0, 8, -2, 9, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.redstoneWire.blockID, 0, 8, -2, 10, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.redstoneWire.blockID, 0, 10, -1, 9, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.pistonStickyBase.blockID, 1, 9, -2, 8, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.pistonStickyBase.blockID, this.getMetadataWithOffset(Block.pistonStickyBase.blockID, 4), 10, -2, 8, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.pistonStickyBase.blockID, this.getMetadataWithOffset(Block.pistonStickyBase.blockID, 4), 10, -1, 8, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.redstoneRepeaterIdle.blockID, this.getMetadataWithOffset(Block.redstoneRepeaterIdle.blockID, 2), 10, -2, 10, par3StructureBoundingBox);

            if (!this.field_74948_i)
            {
                this.field_74948_i = this.generateStructureChestContents(par1World, par3StructureBoundingBox, par2Random, 9, -3, 10, chest.getItems(par2Random), chest.getCount(par2Random));
            }

            return true;
        }
    }
}
