package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;

import net.minecraftforge.common.ChestGenHooks;
import static net.minecraftforge.common.ChestGenHooks.*;

public class ComponentMineshaftCorridor extends StructureComponent
{
    private boolean hasRails;
    private boolean hasSpiders;
    private boolean spawnerPlaced;

    /**
     * A count of the different sections of this mine. The space between ceiling supports.
     */
    private int sectionCount;

    public ComponentMineshaftCorridor() {}

    protected void func_143012_a(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setBoolean("hr", this.hasRails);
        par1NBTTagCompound.setBoolean("sc", this.hasSpiders);
        par1NBTTagCompound.setBoolean("hps", this.spawnerPlaced);
        par1NBTTagCompound.setInteger("Num", this.sectionCount);
    }

    protected void func_143011_b(NBTTagCompound par1NBTTagCompound)
    {
        this.hasRails = par1NBTTagCompound.getBoolean("hr");
        this.hasSpiders = par1NBTTagCompound.getBoolean("sc");
        this.spawnerPlaced = par1NBTTagCompound.getBoolean("hps");
        this.sectionCount = par1NBTTagCompound.getInteger("Num");
    }

    public ComponentMineshaftCorridor(int par1, Random par2Random, StructureBoundingBox par3StructureBoundingBox, int par4)
    {
        super(par1);
        this.coordBaseMode = par4;
        this.boundingBox = par3StructureBoundingBox;
        this.hasRails = par2Random.nextInt(3) == 0;
        this.hasSpiders = !this.hasRails && par2Random.nextInt(23) == 0;

        if (this.coordBaseMode != 2 && this.coordBaseMode != 0)
        {
            this.sectionCount = par3StructureBoundingBox.getXSize() / 5;
        }
        else
        {
            this.sectionCount = par3StructureBoundingBox.getZSize() / 5;
        }
    }

    public static StructureBoundingBox findValidPlacement(List par0List, Random par1Random, int par2, int par3, int par4, int par5)
    {
        StructureBoundingBox structureboundingbox = new StructureBoundingBox(par2, par3, par4, par2, par3 + 2, par4);
        int i1;

        for (i1 = par1Random.nextInt(3) + 2; i1 > 0; --i1)
        {
            int j1 = i1 * 5;

            switch (par5)
            {
                case 0:
                    structureboundingbox.maxX = par2 + 2;
                    structureboundingbox.maxZ = par4 + (j1 - 1);
                    break;
                case 1:
                    structureboundingbox.minX = par2 - (j1 - 1);
                    structureboundingbox.maxZ = par4 + 2;
                    break;
                case 2:
                    structureboundingbox.maxX = par2 + 2;
                    structureboundingbox.minZ = par4 - (j1 - 1);
                    break;
                case 3:
                    structureboundingbox.maxX = par2 + (j1 - 1);
                    structureboundingbox.maxZ = par4 + 2;
            }

            if (StructureComponent.findIntersecting(par0List, structureboundingbox) == null)
            {
                break;
            }
        }

        return i1 > 0 ? structureboundingbox : null;
    }

    /**
     * Initiates construction of the Structure Component picked, at the current Location of StructGen
     */
    public void buildComponent(StructureComponent par1StructureComponent, List par2List, Random par3Random)
    {
        int i = this.getComponentType();
        int j = par3Random.nextInt(4);

        switch (this.coordBaseMode)
        {
            case 0:
                if (j <= 1)
                {
                    StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, this.boundingBox.minX, this.boundingBox.minY - 1 + par3Random.nextInt(3), this.boundingBox.maxZ + 1, this.coordBaseMode, i);
                }
                else if (j == 2)
                {
                    StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + par3Random.nextInt(3), this.boundingBox.maxZ - 3, 1, i);
                }
                else
                {
                    StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + par3Random.nextInt(3), this.boundingBox.maxZ - 3, 3, i);
                }

                break;
            case 1:
                if (j <= 1)
                {
                    StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + par3Random.nextInt(3), this.boundingBox.minZ, this.coordBaseMode, i);
                }
                else if (j == 2)
                {
                    StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, this.boundingBox.minX, this.boundingBox.minY - 1 + par3Random.nextInt(3), this.boundingBox.minZ - 1, 2, i);
                }
                else
                {
                    StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, this.boundingBox.minX, this.boundingBox.minY - 1 + par3Random.nextInt(3), this.boundingBox.maxZ + 1, 0, i);
                }

                break;
            case 2:
                if (j <= 1)
                {
                    StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, this.boundingBox.minX, this.boundingBox.minY - 1 + par3Random.nextInt(3), this.boundingBox.minZ - 1, this.coordBaseMode, i);
                }
                else if (j == 2)
                {
                    StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + par3Random.nextInt(3), this.boundingBox.minZ, 1, i);
                }
                else
                {
                    StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + par3Random.nextInt(3), this.boundingBox.minZ, 3, i);
                }

                break;
            case 3:
                if (j <= 1)
                {
                    StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + par3Random.nextInt(3), this.boundingBox.minZ, this.coordBaseMode, i);
                }
                else if (j == 2)
                {
                    StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, this.boundingBox.maxX - 3, this.boundingBox.minY - 1 + par3Random.nextInt(3), this.boundingBox.minZ - 1, 2, i);
                }
                else
                {
                    StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, this.boundingBox.maxX - 3, this.boundingBox.minY - 1 + par3Random.nextInt(3), this.boundingBox.maxZ + 1, 0, i);
                }
        }

        if (i < 8)
        {
            int k;
            int l;

            if (this.coordBaseMode != 2 && this.coordBaseMode != 0)
            {
                for (k = this.boundingBox.minX + 3; k + 3 <= this.boundingBox.maxX; k += 5)
                {
                    l = par3Random.nextInt(5);

                    if (l == 0)
                    {
                        StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, k, this.boundingBox.minY, this.boundingBox.minZ - 1, 2, i + 1);
                    }
                    else if (l == 1)
                    {
                        StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, k, this.boundingBox.minY, this.boundingBox.maxZ + 1, 0, i + 1);
                    }
                }
            }
            else
            {
                for (k = this.boundingBox.minZ + 3; k + 3 <= this.boundingBox.maxZ; k += 5)
                {
                    l = par3Random.nextInt(5);

                    if (l == 0)
                    {
                        StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, this.boundingBox.minX - 1, this.boundingBox.minY, k, 1, i + 1);
                    }
                    else if (l == 1)
                    {
                        StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, this.boundingBox.maxX + 1, this.boundingBox.minY, k, 3, i + 1);
                    }
                }
            }
        }
    }

    /**
     * Used to generate chests with items in it. ex: Temple Chests, Village Blacksmith Chests, Mineshaft Chests.
     */
    protected boolean generateStructureChestContents(World par1World, StructureBoundingBox par2StructureBoundingBox, Random par3Random, int par4, int par5, int par6, WeightedRandomChestContent[] par7ArrayOfWeightedRandomChestContent, int par8)
    {
        int i1 = this.getXWithOffset(par4, par6);
        int j1 = this.getYWithOffset(par5);
        int k1 = this.getZWithOffset(par4, par6);

        if (par2StructureBoundingBox.isVecInside(i1, j1, k1) && par1World.getBlockId(i1, j1, k1) == 0)
        {
            par1World.setBlock(i1, j1, k1, Block.rail.blockID, this.getMetadataWithOffset(Block.rail.blockID, par3Random.nextBoolean() ? 1 : 0), 2);
            EntityMinecartChest entityminecartchest = new EntityMinecartChest(par1World, (double)((float)i1 + 0.5F), (double)((float)j1 + 0.5F), (double)((float)k1 + 0.5F));
            WeightedRandomChestContent.generateChestContents(par3Random, par7ArrayOfWeightedRandomChestContent, entityminecartchest, par8);
            par1World.spawnEntityInWorld(entityminecartchest);
            return true;
        }
        else
        {
            return false;
        }
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
            boolean flag = false;
            boolean flag1 = true;
            boolean flag2 = false;
            boolean flag3 = true;
            int i = this.sectionCount * 5 - 1;
            this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 0, 0, 2, 1, i, 0, 0, false);
            this.randomlyFillWithBlocks(par1World, par3StructureBoundingBox, par2Random, 0.8F, 0, 2, 0, 2, 2, i, 0, 0, false);

            if (this.hasSpiders)
            {
                this.randomlyFillWithBlocks(par1World, par3StructureBoundingBox, par2Random, 0.6F, 0, 0, 0, 2, 1, i, Block.web.blockID, 0, false);
            }

            int j;
            int k;
            int l;

            for (j = 0; j < this.sectionCount; ++j)
            {
                k = 2 + j * 5;
                this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 0, k, 0, 1, k, Block.fence.blockID, 0, false);
                this.fillWithBlocks(par1World, par3StructureBoundingBox, 2, 0, k, 2, 1, k, Block.fence.blockID, 0, false);

                if (par2Random.nextInt(4) == 0)
                {
                    this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 2, k, 0, 2, k, Block.planks.blockID, 0, false);
                    this.fillWithBlocks(par1World, par3StructureBoundingBox, 2, 2, k, 2, 2, k, Block.planks.blockID, 0, false);
                }
                else
                {
                    this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 2, k, 2, 2, k, Block.planks.blockID, 0, false);
                }

                this.randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 0.1F, 0, 2, k - 1, Block.web.blockID, 0);
                this.randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 0.1F, 2, 2, k - 1, Block.web.blockID, 0);
                this.randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 0.1F, 0, 2, k + 1, Block.web.blockID, 0);
                this.randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 0.1F, 2, 2, k + 1, Block.web.blockID, 0);
                this.randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 0.05F, 0, 2, k - 2, Block.web.blockID, 0);
                this.randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 0.05F, 2, 2, k - 2, Block.web.blockID, 0);
                this.randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 0.05F, 0, 2, k + 2, Block.web.blockID, 0);
                this.randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 0.05F, 2, 2, k + 2, Block.web.blockID, 0);
                this.randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 0.05F, 1, 2, k - 1, Block.torchWood.blockID, 0);
                this.randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 0.05F, 1, 2, k + 1, Block.torchWood.blockID, 0);

                ChestGenHooks info = ChestGenHooks.getInfo(MINESHAFT_CORRIDOR);

                if (par2Random.nextInt(100) == 0)
                {
                    this.generateStructureChestContents(par1World, par3StructureBoundingBox, par2Random, 2, 0, k - 1, info.getItems(par2Random), info.getCount(par2Random));
                }

                if (par2Random.nextInt(100) == 0)
                {
                    this.generateStructureChestContents(par1World, par3StructureBoundingBox, par2Random, 0, 0, k + 1, info.getItems(par2Random), info.getCount(par2Random));
                }

                if (this.hasSpiders && !this.spawnerPlaced)
                {
                    l = this.getYWithOffset(0);
                    int i1 = k - 1 + par2Random.nextInt(3);
                    int j1 = this.getXWithOffset(1, i1);
                    i1 = this.getZWithOffset(1, i1);

                    if (par3StructureBoundingBox.isVecInside(j1, l, i1))
                    {
                        this.spawnerPlaced = true;
                        par1World.setBlock(j1, l, i1, Block.mobSpawner.blockID, 0, 2);
                        TileEntityMobSpawner tileentitymobspawner = (TileEntityMobSpawner)par1World.getBlockTileEntity(j1, l, i1);

                        if (tileentitymobspawner != null)
                        {
                            tileentitymobspawner.getSpawnerLogic().setMobID("CaveSpider");
                        }
                    }
                }
            }

            for (j = 0; j <= 2; ++j)
            {
                for (k = 0; k <= i; ++k)
                {
                    l = this.getBlockIdAtCurrentPosition(par1World, j, -1, k, par3StructureBoundingBox);

                    if (l == 0)
                    {
                        this.placeBlockAtCurrentPosition(par1World, Block.planks.blockID, 0, j, -1, k, par3StructureBoundingBox);
                    }
                }
            }

            if (this.hasRails)
            {
                for (j = 0; j <= i; ++j)
                {
                    k = this.getBlockIdAtCurrentPosition(par1World, 1, -1, j, par3StructureBoundingBox);

                    if (k > 0 && Block.opaqueCubeLookup[k])
                    {
                        this.randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 0.7F, 1, 0, j, Block.rail.blockID, this.getMetadataWithOffset(Block.rail.blockID, 0));
                    }
                }
            }

            return true;
        }
    }
}
