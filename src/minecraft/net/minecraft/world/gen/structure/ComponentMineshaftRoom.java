package net.minecraft.world.gen.structure;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class ComponentMineshaftRoom extends StructureComponent
{
    /** List of other Mineshaft components linked to this room. */
    private List roomsLinkedToTheRoom = new LinkedList();

    public ComponentMineshaftRoom() {}

    public ComponentMineshaftRoom(int par1, Random par2Random, int par3, int par4)
    {
        super(par1);
        this.boundingBox = new StructureBoundingBox(par3, 50, par4, par3 + 7 + par2Random.nextInt(6), 54 + par2Random.nextInt(6), par4 + 7 + par2Random.nextInt(6));
    }

    /**
     * Initiates construction of the Structure Component picked, at the current Location of StructGen
     */
    public void buildComponent(StructureComponent par1StructureComponent, List par2List, Random par3Random)
    {
        int i = this.getComponentType();
        int j = this.boundingBox.getYSize() - 3 - 1;

        if (j <= 0)
        {
            j = 1;
        }

        int k;
        StructureComponent structurecomponent1;
        StructureBoundingBox structureboundingbox;

        for (k = 0; k < this.boundingBox.getXSize(); k += 4)
        {
            k += par3Random.nextInt(this.boundingBox.getXSize());

            if (k + 3 > this.boundingBox.getXSize())
            {
                break;
            }

            structurecomponent1 = StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, this.boundingBox.minX + k, this.boundingBox.minY + par3Random.nextInt(j) + 1, this.boundingBox.minZ - 1, 2, i);

            if (structurecomponent1 != null)
            {
                structureboundingbox = structurecomponent1.getBoundingBox();
                this.roomsLinkedToTheRoom.add(new StructureBoundingBox(structureboundingbox.minX, structureboundingbox.minY, this.boundingBox.minZ, structureboundingbox.maxX, structureboundingbox.maxY, this.boundingBox.minZ + 1));
            }
        }

        for (k = 0; k < this.boundingBox.getXSize(); k += 4)
        {
            k += par3Random.nextInt(this.boundingBox.getXSize());

            if (k + 3 > this.boundingBox.getXSize())
            {
                break;
            }

            structurecomponent1 = StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, this.boundingBox.minX + k, this.boundingBox.minY + par3Random.nextInt(j) + 1, this.boundingBox.maxZ + 1, 0, i);

            if (structurecomponent1 != null)
            {
                structureboundingbox = structurecomponent1.getBoundingBox();
                this.roomsLinkedToTheRoom.add(new StructureBoundingBox(structureboundingbox.minX, structureboundingbox.minY, this.boundingBox.maxZ - 1, structureboundingbox.maxX, structureboundingbox.maxY, this.boundingBox.maxZ));
            }
        }

        for (k = 0; k < this.boundingBox.getZSize(); k += 4)
        {
            k += par3Random.nextInt(this.boundingBox.getZSize());

            if (k + 3 > this.boundingBox.getZSize())
            {
                break;
            }

            structurecomponent1 = StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, this.boundingBox.minX - 1, this.boundingBox.minY + par3Random.nextInt(j) + 1, this.boundingBox.minZ + k, 1, i);

            if (structurecomponent1 != null)
            {
                structureboundingbox = structurecomponent1.getBoundingBox();
                this.roomsLinkedToTheRoom.add(new StructureBoundingBox(this.boundingBox.minX, structureboundingbox.minY, structureboundingbox.minZ, this.boundingBox.minX + 1, structureboundingbox.maxY, structureboundingbox.maxZ));
            }
        }

        for (k = 0; k < this.boundingBox.getZSize(); k += 4)
        {
            k += par3Random.nextInt(this.boundingBox.getZSize());

            if (k + 3 > this.boundingBox.getZSize())
            {
                break;
            }

            structurecomponent1 = StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, this.boundingBox.maxX + 1, this.boundingBox.minY + par3Random.nextInt(j) + 1, this.boundingBox.minZ + k, 3, i);

            if (structurecomponent1 != null)
            {
                structureboundingbox = structurecomponent1.getBoundingBox();
                this.roomsLinkedToTheRoom.add(new StructureBoundingBox(this.boundingBox.maxX - 1, structureboundingbox.minY, structureboundingbox.minZ, this.boundingBox.maxX, structureboundingbox.maxY, structureboundingbox.maxZ));
            }
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
            this.fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.minY, this.boundingBox.maxZ, Block.dirt.blockID, 0, true);
            this.fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX, this.boundingBox.minY + 1, this.boundingBox.minZ, this.boundingBox.maxX, Math.min(this.boundingBox.minY + 3, this.boundingBox.maxY), this.boundingBox.maxZ, 0, 0, false);
            Iterator iterator = this.roomsLinkedToTheRoom.iterator();

            while (iterator.hasNext())
            {
                StructureBoundingBox structureboundingbox1 = (StructureBoundingBox)iterator.next();
                this.fillWithBlocks(par1World, par3StructureBoundingBox, structureboundingbox1.minX, structureboundingbox1.maxY - 2, structureboundingbox1.minZ, structureboundingbox1.maxX, structureboundingbox1.maxY, structureboundingbox1.maxZ, 0, 0, false);
            }

            this.randomlyRareFillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX, this.boundingBox.minY + 4, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ, 0, false);
            return true;
        }
    }

    protected void func_143012_a(NBTTagCompound par1NBTTagCompound)
    {
        NBTTagList nbttaglist = new NBTTagList("Entrances");
        Iterator iterator = this.roomsLinkedToTheRoom.iterator();

        while (iterator.hasNext())
        {
            StructureBoundingBox structureboundingbox = (StructureBoundingBox)iterator.next();
            nbttaglist.appendTag(structureboundingbox.func_143047_a(""));
        }

        par1NBTTagCompound.setTag("Entrances", nbttaglist);
    }

    protected void func_143011_b(NBTTagCompound par1NBTTagCompound)
    {
        NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Entrances");

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            this.roomsLinkedToTheRoom.add(new StructureBoundingBox(((NBTTagIntArray)nbttaglist.tagAt(i)).intArray));
        }
    }
}
