package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;

import net.minecraftforge.common.ChestGenHooks;
import static net.minecraftforge.common.ChestGenHooks.*;

public class ComponentStrongholdRoomCrossing extends ComponentStronghold
{
    /**
     * Items that could generate in the chest that is located in Stronghold Room Crossing.
     */
    public static final WeightedRandomChestContent[] strongholdRoomCrossingChestContents = new WeightedRandomChestContent[] {new WeightedRandomChestContent(Item.ingotIron.itemID, 0, 1, 5, 10), new WeightedRandomChestContent(Item.ingotGold.itemID, 0, 1, 3, 5), new WeightedRandomChestContent(Item.redstone.itemID, 0, 4, 9, 5), new WeightedRandomChestContent(Item.coal.itemID, 0, 3, 8, 10), new WeightedRandomChestContent(Item.bread.itemID, 0, 1, 3, 15), new WeightedRandomChestContent(Item.appleRed.itemID, 0, 1, 3, 15), new WeightedRandomChestContent(Item.pickaxeIron.itemID, 0, 1, 1, 1)};
    protected int roomType;

    public ComponentStrongholdRoomCrossing() {}

    public ComponentStrongholdRoomCrossing(int par1, Random par2Random, StructureBoundingBox par3StructureBoundingBox, int par4)
    {
        super(par1);
        this.coordBaseMode = par4;
        this.field_143013_d = this.getRandomDoor(par2Random);
        this.boundingBox = par3StructureBoundingBox;
        this.roomType = par2Random.nextInt(5);
    }

    protected void func_143012_a(NBTTagCompound par1NBTTagCompound)
    {
        super.func_143012_a(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("Type", this.roomType);
    }

    protected void func_143011_b(NBTTagCompound par1NBTTagCompound)
    {
        super.func_143011_b(par1NBTTagCompound);
        this.roomType = par1NBTTagCompound.getInteger("Type");
    }

    /**
     * Initiates construction of the Structure Component picked, at the current Location of StructGen
     */
    public void buildComponent(StructureComponent par1StructureComponent, List par2List, Random par3Random)
    {
        this.getNextComponentNormal((ComponentStrongholdStairs2)par1StructureComponent, par2List, par3Random, 4, 1);
        this.getNextComponentX((ComponentStrongholdStairs2)par1StructureComponent, par2List, par3Random, 1, 4);
        this.getNextComponentZ((ComponentStrongholdStairs2)par1StructureComponent, par2List, par3Random, 1, 4);
    }

    public static ComponentStrongholdRoomCrossing findValidPlacement(List par0List, Random par1Random, int par2, int par3, int par4, int par5, int par6)
    {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(par2, par3, par4, -4, -1, 0, 11, 7, 11, par5);
        return canStrongholdGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(par0List, structureboundingbox) == null ? new ComponentStrongholdRoomCrossing(par6, par1Random, structureboundingbox, par5) : null;
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox)
    {
        if (this.isLiquidInStructureBoundingBox(par1World, par3StructureBoundingBox))
        {
            return false;
        }
        else
        {
            this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 0, 0, 0, 10, 6, 10, true, par2Random, StructureStrongholdPieces.getStrongholdStones());
            this.placeDoor(par1World, par2Random, par3StructureBoundingBox, this.field_143013_d, 4, 1, 0);
            this.fillWithBlocks(par1World, par3StructureBoundingBox, 4, 1, 10, 6, 3, 10, 0, 0, false);
            this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 1, 4, 0, 3, 6, 0, 0, false);
            this.fillWithBlocks(par1World, par3StructureBoundingBox, 10, 1, 4, 10, 3, 6, 0, 0, false);
            int i;

            switch (this.roomType)
            {
                case 0:
                    this.placeBlockAtCurrentPosition(par1World, Block.stoneBrick.blockID, 0, 5, 1, 5, par3StructureBoundingBox);
                    this.placeBlockAtCurrentPosition(par1World, Block.stoneBrick.blockID, 0, 5, 2, 5, par3StructureBoundingBox);
                    this.placeBlockAtCurrentPosition(par1World, Block.stoneBrick.blockID, 0, 5, 3, 5, par3StructureBoundingBox);
                    this.placeBlockAtCurrentPosition(par1World, Block.torchWood.blockID, 0, 4, 3, 5, par3StructureBoundingBox);
                    this.placeBlockAtCurrentPosition(par1World, Block.torchWood.blockID, 0, 6, 3, 5, par3StructureBoundingBox);
                    this.placeBlockAtCurrentPosition(par1World, Block.torchWood.blockID, 0, 5, 3, 4, par3StructureBoundingBox);
                    this.placeBlockAtCurrentPosition(par1World, Block.torchWood.blockID, 0, 5, 3, 6, par3StructureBoundingBox);
                    this.placeBlockAtCurrentPosition(par1World, Block.stoneSingleSlab.blockID, 0, 4, 1, 4, par3StructureBoundingBox);
                    this.placeBlockAtCurrentPosition(par1World, Block.stoneSingleSlab.blockID, 0, 4, 1, 5, par3StructureBoundingBox);
                    this.placeBlockAtCurrentPosition(par1World, Block.stoneSingleSlab.blockID, 0, 4, 1, 6, par3StructureBoundingBox);
                    this.placeBlockAtCurrentPosition(par1World, Block.stoneSingleSlab.blockID, 0, 6, 1, 4, par3StructureBoundingBox);
                    this.placeBlockAtCurrentPosition(par1World, Block.stoneSingleSlab.blockID, 0, 6, 1, 5, par3StructureBoundingBox);
                    this.placeBlockAtCurrentPosition(par1World, Block.stoneSingleSlab.blockID, 0, 6, 1, 6, par3StructureBoundingBox);
                    this.placeBlockAtCurrentPosition(par1World, Block.stoneSingleSlab.blockID, 0, 5, 1, 4, par3StructureBoundingBox);
                    this.placeBlockAtCurrentPosition(par1World, Block.stoneSingleSlab.blockID, 0, 5, 1, 6, par3StructureBoundingBox);
                    break;
                case 1:
                    for (i = 0; i < 5; ++i)
                    {
                        this.placeBlockAtCurrentPosition(par1World, Block.stoneBrick.blockID, 0, 3, 1, 3 + i, par3StructureBoundingBox);
                        this.placeBlockAtCurrentPosition(par1World, Block.stoneBrick.blockID, 0, 7, 1, 3 + i, par3StructureBoundingBox);
                        this.placeBlockAtCurrentPosition(par1World, Block.stoneBrick.blockID, 0, 3 + i, 1, 3, par3StructureBoundingBox);
                        this.placeBlockAtCurrentPosition(par1World, Block.stoneBrick.blockID, 0, 3 + i, 1, 7, par3StructureBoundingBox);
                    }

                    this.placeBlockAtCurrentPosition(par1World, Block.stoneBrick.blockID, 0, 5, 1, 5, par3StructureBoundingBox);
                    this.placeBlockAtCurrentPosition(par1World, Block.stoneBrick.blockID, 0, 5, 2, 5, par3StructureBoundingBox);
                    this.placeBlockAtCurrentPosition(par1World, Block.stoneBrick.blockID, 0, 5, 3, 5, par3StructureBoundingBox);
                    this.placeBlockAtCurrentPosition(par1World, Block.waterMoving.blockID, 0, 5, 4, 5, par3StructureBoundingBox);
                    break;
                case 2:
                    for (i = 1; i <= 9; ++i)
                    {
                        this.placeBlockAtCurrentPosition(par1World, Block.cobblestone.blockID, 0, 1, 3, i, par3StructureBoundingBox);
                        this.placeBlockAtCurrentPosition(par1World, Block.cobblestone.blockID, 0, 9, 3, i, par3StructureBoundingBox);
                    }

                    for (i = 1; i <= 9; ++i)
                    {
                        this.placeBlockAtCurrentPosition(par1World, Block.cobblestone.blockID, 0, i, 3, 1, par3StructureBoundingBox);
                        this.placeBlockAtCurrentPosition(par1World, Block.cobblestone.blockID, 0, i, 3, 9, par3StructureBoundingBox);
                    }

                    this.placeBlockAtCurrentPosition(par1World, Block.cobblestone.blockID, 0, 5, 1, 4, par3StructureBoundingBox);
                    this.placeBlockAtCurrentPosition(par1World, Block.cobblestone.blockID, 0, 5, 1, 6, par3StructureBoundingBox);
                    this.placeBlockAtCurrentPosition(par1World, Block.cobblestone.blockID, 0, 5, 3, 4, par3StructureBoundingBox);
                    this.placeBlockAtCurrentPosition(par1World, Block.cobblestone.blockID, 0, 5, 3, 6, par3StructureBoundingBox);
                    this.placeBlockAtCurrentPosition(par1World, Block.cobblestone.blockID, 0, 4, 1, 5, par3StructureBoundingBox);
                    this.placeBlockAtCurrentPosition(par1World, Block.cobblestone.blockID, 0, 6, 1, 5, par3StructureBoundingBox);
                    this.placeBlockAtCurrentPosition(par1World, Block.cobblestone.blockID, 0, 4, 3, 5, par3StructureBoundingBox);
                    this.placeBlockAtCurrentPosition(par1World, Block.cobblestone.blockID, 0, 6, 3, 5, par3StructureBoundingBox);

                    for (i = 1; i <= 3; ++i)
                    {
                        this.placeBlockAtCurrentPosition(par1World, Block.cobblestone.blockID, 0, 4, i, 4, par3StructureBoundingBox);
                        this.placeBlockAtCurrentPosition(par1World, Block.cobblestone.blockID, 0, 6, i, 4, par3StructureBoundingBox);
                        this.placeBlockAtCurrentPosition(par1World, Block.cobblestone.blockID, 0, 4, i, 6, par3StructureBoundingBox);
                        this.placeBlockAtCurrentPosition(par1World, Block.cobblestone.blockID, 0, 6, i, 6, par3StructureBoundingBox);
                    }

                    this.placeBlockAtCurrentPosition(par1World, Block.torchWood.blockID, 0, 5, 3, 5, par3StructureBoundingBox);

                    for (i = 2; i <= 8; ++i)
                    {
                        this.placeBlockAtCurrentPosition(par1World, Block.planks.blockID, 0, 2, 3, i, par3StructureBoundingBox);
                        this.placeBlockAtCurrentPosition(par1World, Block.planks.blockID, 0, 3, 3, i, par3StructureBoundingBox);

                        if (i <= 3 || i >= 7)
                        {
                            this.placeBlockAtCurrentPosition(par1World, Block.planks.blockID, 0, 4, 3, i, par3StructureBoundingBox);
                            this.placeBlockAtCurrentPosition(par1World, Block.planks.blockID, 0, 5, 3, i, par3StructureBoundingBox);
                            this.placeBlockAtCurrentPosition(par1World, Block.planks.blockID, 0, 6, 3, i, par3StructureBoundingBox);
                        }

                        this.placeBlockAtCurrentPosition(par1World, Block.planks.blockID, 0, 7, 3, i, par3StructureBoundingBox);
                        this.placeBlockAtCurrentPosition(par1World, Block.planks.blockID, 0, 8, 3, i, par3StructureBoundingBox);
                    }

                    this.placeBlockAtCurrentPosition(par1World, Block.ladder.blockID, this.getMetadataWithOffset(Block.ladder.blockID, 4), 9, 1, 3, par3StructureBoundingBox);
                    this.placeBlockAtCurrentPosition(par1World, Block.ladder.blockID, this.getMetadataWithOffset(Block.ladder.blockID, 4), 9, 2, 3, par3StructureBoundingBox);
                    this.placeBlockAtCurrentPosition(par1World, Block.ladder.blockID, this.getMetadataWithOffset(Block.ladder.blockID, 4), 9, 3, 3, par3StructureBoundingBox);
                    this.generateStructureChestContents(par1World, par3StructureBoundingBox, par2Random, 3, 4, 8, ChestGenHooks.getItems(STRONGHOLD_CROSSING, par2Random), ChestGenHooks.getCount(STRONGHOLD_CROSSING, par2Random));
            }

            return true;
        }
    }
}
