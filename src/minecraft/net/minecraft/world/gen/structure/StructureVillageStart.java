package net.minecraft.world.gen.structure;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class StructureVillageStart extends StructureStart
{
    /** well ... thats what it does */
    private boolean hasMoreThanTwoComponents;

    public StructureVillageStart() {}

    public StructureVillageStart(World par1World, Random par2Random, int par3, int par4, int par5)
    {
        super(par3, par4);
        List list = StructureVillagePieces.getStructureVillageWeightedPieceList(par2Random, par5);
        ComponentVillageStartPiece componentvillagestartpiece = new ComponentVillageStartPiece(par1World.getWorldChunkManager(), 0, par2Random, (par3 << 4) + 2, (par4 << 4) + 2, list, par5);
        this.components.add(componentvillagestartpiece);
        componentvillagestartpiece.buildComponent(componentvillagestartpiece, this.components, par2Random);
        List list1 = componentvillagestartpiece.field_74930_j;
        List list2 = componentvillagestartpiece.field_74932_i;
        int l;

        while (!list1.isEmpty() || !list2.isEmpty())
        {
            StructureComponent structurecomponent;

            if (list1.isEmpty())
            {
                l = par2Random.nextInt(list2.size());
                structurecomponent = (StructureComponent)list2.remove(l);
                structurecomponent.buildComponent(componentvillagestartpiece, this.components, par2Random);
            }
            else
            {
                l = par2Random.nextInt(list1.size());
                structurecomponent = (StructureComponent)list1.remove(l);
                structurecomponent.buildComponent(componentvillagestartpiece, this.components, par2Random);
            }
        }

        this.updateBoundingBox();
        l = 0;
        Iterator iterator = this.components.iterator();

        while (iterator.hasNext())
        {
            StructureComponent structurecomponent1 = (StructureComponent)iterator.next();

            if (!(structurecomponent1 instanceof ComponentVillageRoadPiece))
            {
                ++l;
            }
        }

        this.hasMoreThanTwoComponents = l > 2;
    }

    /**
     * currently only defined for Villages, returns true if Village has more than 2 non-road components
     */
    public boolean isSizeableStructure()
    {
        return this.hasMoreThanTwoComponents;
    }

    public void func_143022_a(NBTTagCompound par1NBTTagCompound)
    {
        super.func_143022_a(par1NBTTagCompound);
        par1NBTTagCompound.setBoolean("Valid", this.hasMoreThanTwoComponents);
    }

    public void func_143017_b(NBTTagCompound nbttagcompound)
    {
        super.func_143017_b(nbttagcompound);
        this.hasMoreThanTwoComponents = nbttagcompound.getBoolean("Valid");
    }
}
