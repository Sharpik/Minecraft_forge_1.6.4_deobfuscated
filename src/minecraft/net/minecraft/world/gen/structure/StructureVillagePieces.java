package net.minecraft.world.gen.structure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.registry.VillagerRegistry;
import net.minecraft.util.MathHelper;

public class StructureVillagePieces
{
    public static void func_143016_a()
    {
        MapGenStructureIO.func_143031_a(ComponentVillageHouse1.class, "ViBH");
        MapGenStructureIO.func_143031_a(ComponentVillageField.class, "ViDF");
        MapGenStructureIO.func_143031_a(ComponentVillageField2.class, "ViF");
        MapGenStructureIO.func_143031_a(ComponentVillageTorch.class, "ViL");
        MapGenStructureIO.func_143031_a(ComponentVillageHall.class, "ViPH");
        MapGenStructureIO.func_143031_a(ComponentVillageHouse4_Garden.class, "ViSH");
        MapGenStructureIO.func_143031_a(ComponentVillageWoodHut.class, "ViSmH");
        MapGenStructureIO.func_143031_a(ComponentVillageChurch.class, "ViST");
        MapGenStructureIO.func_143031_a(ComponentVillageHouse2.class, "ViS");
        MapGenStructureIO.func_143031_a(ComponentVillageStartPiece.class, "ViStart");
        MapGenStructureIO.func_143031_a(ComponentVillagePathGen.class, "ViSR");
        MapGenStructureIO.func_143031_a(ComponentVillageHouse3.class, "ViTRH");
        MapGenStructureIO.func_143031_a(ComponentVillageWell.class, "ViW");
    }

    public static List getStructureVillageWeightedPieceList(Random par0Random, int par1)
    {
        ArrayList arraylist = new ArrayList();
        arraylist.add(new StructureVillagePieceWeight(ComponentVillageHouse4_Garden.class, 4, MathHelper.getRandomIntegerInRange(par0Random, 2 + par1, 4 + par1 * 2)));
        arraylist.add(new StructureVillagePieceWeight(ComponentVillageChurch.class, 20, MathHelper.getRandomIntegerInRange(par0Random, 0 + par1, 1 + par1)));
        arraylist.add(new StructureVillagePieceWeight(ComponentVillageHouse1.class, 20, MathHelper.getRandomIntegerInRange(par0Random, 0 + par1, 2 + par1)));
        arraylist.add(new StructureVillagePieceWeight(ComponentVillageWoodHut.class, 3, MathHelper.getRandomIntegerInRange(par0Random, 2 + par1, 5 + par1 * 3)));
        arraylist.add(new StructureVillagePieceWeight(ComponentVillageHall.class, 15, MathHelper.getRandomIntegerInRange(par0Random, 0 + par1, 2 + par1)));
        arraylist.add(new StructureVillagePieceWeight(ComponentVillageField.class, 3, MathHelper.getRandomIntegerInRange(par0Random, 1 + par1, 4 + par1)));
        arraylist.add(new StructureVillagePieceWeight(ComponentVillageField2.class, 3, MathHelper.getRandomIntegerInRange(par0Random, 2 + par1, 4 + par1 * 2)));
        arraylist.add(new StructureVillagePieceWeight(ComponentVillageHouse2.class, 15, MathHelper.getRandomIntegerInRange(par0Random, 0, 1 + par1)));
        arraylist.add(new StructureVillagePieceWeight(ComponentVillageHouse3.class, 8, MathHelper.getRandomIntegerInRange(par0Random, 0 + par1, 3 + par1 * 2)));
        VillagerRegistry.addExtraVillageComponents(arraylist, par0Random, par1);

        Iterator iterator = arraylist.iterator();

        while (iterator.hasNext())
        {
            if (((StructureVillagePieceWeight)iterator.next()).villagePiecesLimit == 0)
            {
                iterator.remove();
            }
        }

        return arraylist;
    }

    private static int func_75079_a(List par0List)
    {
        boolean flag = false;
        int i = 0;
        StructureVillagePieceWeight structurevillagepieceweight;

        for (Iterator iterator = par0List.iterator(); iterator.hasNext(); i += structurevillagepieceweight.villagePieceWeight)
        {
            structurevillagepieceweight = (StructureVillagePieceWeight)iterator.next();

            if (structurevillagepieceweight.villagePiecesLimit > 0 && structurevillagepieceweight.villagePiecesSpawned < structurevillagepieceweight.villagePiecesLimit)
            {
                flag = true;
            }
        }

        return flag ? i : -1;
    }

    private static ComponentVillage func_75083_a(ComponentVillageStartPiece par0ComponentVillageStartPiece, StructureVillagePieceWeight par1StructureVillagePieceWeight, List par2List, Random par3Random, int par4, int par5, int par6, int par7, int par8)
    {
        Class oclass = par1StructureVillagePieceWeight.villagePieceClass;
        Object object = null;

        if (oclass == ComponentVillageHouse4_Garden.class)
        {
            object = ComponentVillageHouse4_Garden.func_74912_a(par0ComponentVillageStartPiece, par2List, par3Random, par4, par5, par6, par7, par8);
        }
        else if (oclass == ComponentVillageChurch.class)
        {
            object = ComponentVillageChurch.func_74919_a(par0ComponentVillageStartPiece, par2List, par3Random, par4, par5, par6, par7, par8);
        }
        else if (oclass == ComponentVillageHouse1.class)
        {
            object = ComponentVillageHouse1.func_74898_a(par0ComponentVillageStartPiece, par2List, par3Random, par4, par5, par6, par7, par8);
        }
        else if (oclass == ComponentVillageWoodHut.class)
        {
            object = ComponentVillageWoodHut.func_74908_a(par0ComponentVillageStartPiece, par2List, par3Random, par4, par5, par6, par7, par8);
        }
        else if (oclass == ComponentVillageHall.class)
        {
            object = ComponentVillageHall.func_74906_a(par0ComponentVillageStartPiece, par2List, par3Random, par4, par5, par6, par7, par8);
        }
        else if (oclass == ComponentVillageField.class)
        {
            object = ComponentVillageField.func_74900_a(par0ComponentVillageStartPiece, par2List, par3Random, par4, par5, par6, par7, par8);
        }
        else if (oclass == ComponentVillageField2.class)
        {
            object = ComponentVillageField2.func_74902_a(par0ComponentVillageStartPiece, par2List, par3Random, par4, par5, par6, par7, par8);
        }
        else if (oclass == ComponentVillageHouse2.class)
        {
            object = ComponentVillageHouse2.func_74915_a(par0ComponentVillageStartPiece, par2List, par3Random, par4, par5, par6, par7, par8);
        }
        else if (oclass == ComponentVillageHouse3.class)
        {
            object = ComponentVillageHouse3.func_74921_a(par0ComponentVillageStartPiece, par2List, par3Random, par4, par5, par6, par7, par8);
        }
        else
        {
            object = VillagerRegistry.getVillageComponent(par1StructureVillagePieceWeight, par0ComponentVillageStartPiece , par2List, par3Random, par4, par5, par6, par7, par8);
        }

        return (ComponentVillage)object;
    }

    /**
     * attempts to find a next Village Component to be spawned
     */
    private static ComponentVillage getNextVillageComponent(ComponentVillageStartPiece par0ComponentVillageStartPiece, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        int j1 = func_75079_a(par0ComponentVillageStartPiece.structureVillageWeightedPieceList);

        if (j1 <= 0)
        {
            return null;
        }
        else
        {
            int k1 = 0;

            while (k1 < 5)
            {
                ++k1;
                int l1 = par2Random.nextInt(j1);
                Iterator iterator = par0ComponentVillageStartPiece.structureVillageWeightedPieceList.iterator();

                while (iterator.hasNext())
                {
                    StructureVillagePieceWeight structurevillagepieceweight = (StructureVillagePieceWeight)iterator.next();
                    l1 -= structurevillagepieceweight.villagePieceWeight;

                    if (l1 < 0)
                    {
                        if (!structurevillagepieceweight.canSpawnMoreVillagePiecesOfType(par7) || structurevillagepieceweight == par0ComponentVillageStartPiece.structVillagePieceWeight && par0ComponentVillageStartPiece.structureVillageWeightedPieceList.size() > 1)
                        {
                            break;
                        }

                        ComponentVillage componentvillage = func_75083_a(par0ComponentVillageStartPiece, structurevillagepieceweight, par1List, par2Random, par3, par4, par5, par6, par7);

                        if (componentvillage != null)
                        {
                            ++structurevillagepieceweight.villagePiecesSpawned;
                            par0ComponentVillageStartPiece.structVillagePieceWeight = structurevillagepieceweight;

                            if (!structurevillagepieceweight.canSpawnMoreVillagePieces())
                            {
                                par0ComponentVillageStartPiece.structureVillageWeightedPieceList.remove(structurevillagepieceweight);
                            }

                            return componentvillage;
                        }
                    }
                }
            }

            StructureBoundingBox structureboundingbox = ComponentVillageTorch.func_74904_a(par0ComponentVillageStartPiece, par1List, par2Random, par3, par4, par5, par6);

            if (structureboundingbox != null)
            {
                return new ComponentVillageTorch(par0ComponentVillageStartPiece, par7, par2Random, structureboundingbox, par6);
            }
            else
            {
                return null;
            }
        }
    }

    /**
     * attempts to find a next Structure Component to be spawned, private Village function
     */
    private static StructureComponent getNextVillageStructureComponent(ComponentVillageStartPiece par0ComponentVillageStartPiece, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        if (par7 > 50)
        {
            return null;
        }
        else if (Math.abs(par3 - par0ComponentVillageStartPiece.getBoundingBox().minX) <= 112 && Math.abs(par5 - par0ComponentVillageStartPiece.getBoundingBox().minZ) <= 112)
        {
            ComponentVillage componentvillage = getNextVillageComponent(par0ComponentVillageStartPiece, par1List, par2Random, par3, par4, par5, par6, par7 + 1);

            if (componentvillage != null)
            {
                int j1 = (componentvillage.boundingBox.minX + componentvillage.boundingBox.maxX) / 2;
                int k1 = (componentvillage.boundingBox.minZ + componentvillage.boundingBox.maxZ) / 2;
                int l1 = componentvillage.boundingBox.maxX - componentvillage.boundingBox.minX;
                int i2 = componentvillage.boundingBox.maxZ - componentvillage.boundingBox.minZ;
                int j2 = l1 > i2 ? l1 : i2;

                if (par0ComponentVillageStartPiece.getWorldChunkManager().areBiomesViable(j1, k1, j2 / 2 + 4, MapGenVillage.villageSpawnBiomes))
                {
                    par1List.add(componentvillage);
                    par0ComponentVillageStartPiece.field_74932_i.add(componentvillage);
                    return componentvillage;
                }
            }

            return null;
        }
        else
        {
            return null;
        }
    }

    private static StructureComponent getNextComponentVillagePath(ComponentVillageStartPiece par0ComponentVillageStartPiece, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        if (par7 > 3 + par0ComponentVillageStartPiece.terrainType)
        {
            return null;
        }
        else if (Math.abs(par3 - par0ComponentVillageStartPiece.getBoundingBox().minX) <= 112 && Math.abs(par5 - par0ComponentVillageStartPiece.getBoundingBox().minZ) <= 112)
        {
            StructureBoundingBox structureboundingbox = ComponentVillagePathGen.func_74933_a(par0ComponentVillageStartPiece, par1List, par2Random, par3, par4, par5, par6);

            if (structureboundingbox != null && structureboundingbox.minY > 10)
            {
                ComponentVillagePathGen componentvillagepathgen = new ComponentVillagePathGen(par0ComponentVillageStartPiece, par7, par2Random, structureboundingbox, par6);
                int j1 = (componentvillagepathgen.boundingBox.minX + componentvillagepathgen.boundingBox.maxX) / 2;
                int k1 = (componentvillagepathgen.boundingBox.minZ + componentvillagepathgen.boundingBox.maxZ) / 2;
                int l1 = componentvillagepathgen.boundingBox.maxX - componentvillagepathgen.boundingBox.minX;
                int i2 = componentvillagepathgen.boundingBox.maxZ - componentvillagepathgen.boundingBox.minZ;
                int j2 = l1 > i2 ? l1 : i2;

                if (par0ComponentVillageStartPiece.getWorldChunkManager().areBiomesViable(j1, k1, j2 / 2 + 4, MapGenVillage.villageSpawnBiomes))
                {
                    par1List.add(componentvillagepathgen);
                    par0ComponentVillageStartPiece.field_74930_j.add(componentvillagepathgen);
                    return componentvillagepathgen;
                }
            }

            return null;
        }
        else
        {
            return null;
        }
    }

    /**
     * attempts to find a next Structure Component to be spawned
     */
    static StructureComponent getNextStructureComponent(ComponentVillageStartPiece par0ComponentVillageStartPiece, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        return getNextVillageStructureComponent(par0ComponentVillageStartPiece, par1List, par2Random, par3, par4, par5, par6, par7);
    }

    static StructureComponent getNextStructureComponentVillagePath(ComponentVillageStartPiece par0ComponentVillageStartPiece, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        return getNextComponentVillagePath(par0ComponentVillageStartPiece, par1List, par2Random, par3, par4, par5, par6, par7);
    }
}
