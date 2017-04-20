package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;

public class ComponentStrongholdPortalRoom extends ComponentStronghold
{
    private boolean hasSpawner;

    public ComponentStrongholdPortalRoom() {}

    public ComponentStrongholdPortalRoom(int par1, Random par2Random, StructureBoundingBox par3StructureBoundingBox, int par4)
    {
        super(par1);
        this.coordBaseMode = par4;
        this.boundingBox = par3StructureBoundingBox;
    }

    protected void func_143012_a(NBTTagCompound par1NBTTagCompound)
    {
        super.func_143012_a(par1NBTTagCompound);
        par1NBTTagCompound.setBoolean("Mob", this.hasSpawner);
    }

    protected void func_143011_b(NBTTagCompound par1NBTTagCompound)
    {
        super.func_143011_b(par1NBTTagCompound);
        this.hasSpawner = par1NBTTagCompound.getBoolean("Mob");
    }

    /**
     * Initiates construction of the Structure Component picked, at the current Location of StructGen
     */
    public void buildComponent(StructureComponent par1StructureComponent, List par2List, Random par3Random)
    {
        if (par1StructureComponent != null)
        {
            ((ComponentStrongholdStairs2)par1StructureComponent).strongholdPortalRoom = this;
        }
    }

    public static ComponentStrongholdPortalRoom findValidPlacement(List par0List, Random par1Random, int par2, int par3, int par4, int par5, int par6)
    {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(par2, par3, par4, -4, -1, 0, 11, 8, 16, par5);
        return canStrongholdGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(par0List, structureboundingbox) == null ? new ComponentStrongholdPortalRoom(par6, par1Random, structureboundingbox, par5) : null;
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox)
    {
        this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 0, 0, 0, 10, 7, 15, false, par2Random, StructureStrongholdPieces.getStrongholdStones());
        this.placeDoor(par1World, par2Random, par3StructureBoundingBox, EnumDoor.GRATES, 4, 1, 0);
        byte b0 = 6;
        this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 1, b0, 1, 1, b0, 14, false, par2Random, StructureStrongholdPieces.getStrongholdStones());
        this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 9, b0, 1, 9, b0, 14, false, par2Random, StructureStrongholdPieces.getStrongholdStones());
        this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 2, b0, 1, 8, b0, 2, false, par2Random, StructureStrongholdPieces.getStrongholdStones());
        this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 2, b0, 14, 8, b0, 14, false, par2Random, StructureStrongholdPieces.getStrongholdStones());
        this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 1, 1, 1, 2, 1, 4, false, par2Random, StructureStrongholdPieces.getStrongholdStones());
        this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 8, 1, 1, 9, 1, 4, false, par2Random, StructureStrongholdPieces.getStrongholdStones());
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 1, 1, 1, 1, 3, Block.lavaMoving.blockID, Block.lavaMoving.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 9, 1, 1, 9, 1, 3, Block.lavaMoving.blockID, Block.lavaMoving.blockID, false);
        this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 3, 1, 8, 7, 1, 12, false, par2Random, StructureStrongholdPieces.getStrongholdStones());
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 4, 1, 9, 6, 1, 11, Block.lavaMoving.blockID, Block.lavaMoving.blockID, false);
        int i;

        for (i = 3; i < 14; i += 2)
        {
            this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 3, i, 0, 4, i, Block.fenceIron.blockID, Block.fenceIron.blockID, false);
            this.fillWithBlocks(par1World, par3StructureBoundingBox, 10, 3, i, 10, 4, i, Block.fenceIron.blockID, Block.fenceIron.blockID, false);
        }

        for (i = 2; i < 9; i += 2)
        {
            this.fillWithBlocks(par1World, par3StructureBoundingBox, i, 3, 15, i, 4, 15, Block.fenceIron.blockID, Block.fenceIron.blockID, false);
        }

        i = this.getMetadataWithOffset(Block.stairsStoneBrick.blockID, 3);
        this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 4, 1, 5, 6, 1, 7, false, par2Random, StructureStrongholdPieces.getStrongholdStones());
        this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 4, 2, 6, 6, 2, 7, false, par2Random, StructureStrongholdPieces.getStrongholdStones());
        this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 4, 3, 7, 6, 3, 7, false, par2Random, StructureStrongholdPieces.getStrongholdStones());

        for (int j = 4; j <= 6; ++j)
        {
            this.placeBlockAtCurrentPosition(par1World, Block.stairsStoneBrick.blockID, i, j, 1, 4, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.stairsStoneBrick.blockID, i, j, 2, 5, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.stairsStoneBrick.blockID, i, j, 3, 6, par3StructureBoundingBox);
        }

        byte b1 = 2;
        byte b2 = 0;
        byte b3 = 3;
        byte b4 = 1;

        switch (this.coordBaseMode)
        {
            case 0:
                b1 = 0;
                b2 = 2;
                break;
            case 1:
                b1 = 1;
                b2 = 3;
                b3 = 0;
                b4 = 2;
            case 2:
            default:
                break;
            case 3:
                b1 = 3;
                b2 = 1;
                b3 = 0;
                b4 = 2;
        }

        this.placeBlockAtCurrentPosition(par1World, Block.endPortalFrame.blockID, b1 + (par2Random.nextFloat() > 0.9F ? 4 : 0), 4, 3, 8, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.endPortalFrame.blockID, b1 + (par2Random.nextFloat() > 0.9F ? 4 : 0), 5, 3, 8, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.endPortalFrame.blockID, b1 + (par2Random.nextFloat() > 0.9F ? 4 : 0), 6, 3, 8, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.endPortalFrame.blockID, b2 + (par2Random.nextFloat() > 0.9F ? 4 : 0), 4, 3, 12, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.endPortalFrame.blockID, b2 + (par2Random.nextFloat() > 0.9F ? 4 : 0), 5, 3, 12, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.endPortalFrame.blockID, b2 + (par2Random.nextFloat() > 0.9F ? 4 : 0), 6, 3, 12, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.endPortalFrame.blockID, b3 + (par2Random.nextFloat() > 0.9F ? 4 : 0), 3, 3, 9, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.endPortalFrame.blockID, b3 + (par2Random.nextFloat() > 0.9F ? 4 : 0), 3, 3, 10, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.endPortalFrame.blockID, b3 + (par2Random.nextFloat() > 0.9F ? 4 : 0), 3, 3, 11, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.endPortalFrame.blockID, b4 + (par2Random.nextFloat() > 0.9F ? 4 : 0), 7, 3, 9, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.endPortalFrame.blockID, b4 + (par2Random.nextFloat() > 0.9F ? 4 : 0), 7, 3, 10, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.endPortalFrame.blockID, b4 + (par2Random.nextFloat() > 0.9F ? 4 : 0), 7, 3, 11, par3StructureBoundingBox);

        if (!this.hasSpawner)
        {
            int k = this.getYWithOffset(3);
            int l = this.getXWithOffset(5, 6);
            int i1 = this.getZWithOffset(5, 6);

            if (par3StructureBoundingBox.isVecInside(l, k, i1))
            {
                this.hasSpawner = true;
                par1World.setBlock(l, k, i1, Block.mobSpawner.blockID, 0, 2);
                TileEntityMobSpawner tileentitymobspawner = (TileEntityMobSpawner)par1World.getBlockTileEntity(l, k, i1);

                if (tileentitymobspawner != null)
                {
                    tileentitymobspawner.getSpawnerLogic().setMobID("Silverfish");
                }
            }
        }

        return true;
    }
}
