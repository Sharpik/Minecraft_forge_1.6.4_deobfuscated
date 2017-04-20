package net.minecraft.world.gen.structure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class StructureStrongholdPieces
{
    private static final StructureStrongholdPieceWeight[] pieceWeightArray = new StructureStrongholdPieceWeight[] {new StructureStrongholdPieceWeight(ComponentStrongholdStraight.class, 40, 0), new StructureStrongholdPieceWeight(ComponentStrongholdPrison.class, 5, 5), new StructureStrongholdPieceWeight(ComponentStrongholdLeftTurn.class, 20, 0), new StructureStrongholdPieceWeight(ComponentStrongholdRightTurn.class, 20, 0), new StructureStrongholdPieceWeight(ComponentStrongholdRoomCrossing.class, 10, 6), new StructureStrongholdPieceWeight(ComponentStrongholdStairsStraight.class, 5, 5), new StructureStrongholdPieceWeight(ComponentStrongholdStairs.class, 5, 5), new StructureStrongholdPieceWeight(ComponentStrongholdCrossing.class, 5, 4), new StructureStrongholdPieceWeight(ComponentStrongholdChestCorridor.class, 5, 4), new StructureStrongholdPieceWeight2(ComponentStrongholdLibrary.class, 10, 2), new StructureStrongholdPieceWeight3(ComponentStrongholdPortalRoom.class, 20, 1)};
    private static List structurePieceList;
    private static Class strongComponentType;
    static int totalWeight;
    private static final StructureStrongholdStones strongholdStones = new StructureStrongholdStones((StructureStrongholdPieceWeight2)null);

    public static void func_143046_a()
    {
        MapGenStructureIO.func_143031_a(ComponentStrongholdChestCorridor.class, "SHCC");
        MapGenStructureIO.func_143031_a(ComponentStrongholdCorridor.class, "SHFC");
        MapGenStructureIO.func_143031_a(ComponentStrongholdCrossing.class, "SH5C");
        MapGenStructureIO.func_143031_a(ComponentStrongholdLeftTurn.class, "SHLT");
        MapGenStructureIO.func_143031_a(ComponentStrongholdLibrary.class, "SHLi");
        MapGenStructureIO.func_143031_a(ComponentStrongholdPortalRoom.class, "SHPR");
        MapGenStructureIO.func_143031_a(ComponentStrongholdPrison.class, "SHPH");
        MapGenStructureIO.func_143031_a(ComponentStrongholdRightTurn.class, "SHRT");
        MapGenStructureIO.func_143031_a(ComponentStrongholdRoomCrossing.class, "SHRC");
        MapGenStructureIO.func_143031_a(ComponentStrongholdStairs.class, "SHSD");
        MapGenStructureIO.func_143031_a(ComponentStrongholdStairs2.class, "SHStart");
        MapGenStructureIO.func_143031_a(ComponentStrongholdStraight.class, "SHS");
        MapGenStructureIO.func_143031_a(ComponentStrongholdStairsStraight.class, "SHSSD");
    }

    /**
     * sets up Arrays with the Structure pieces and their weights
     */
    public static void prepareStructurePieces()
    {
        structurePieceList = new ArrayList();
        StructureStrongholdPieceWeight[] astructurestrongholdpieceweight = pieceWeightArray;
        int i = astructurestrongholdpieceweight.length;

        for (int j = 0; j < i; ++j)
        {
            StructureStrongholdPieceWeight structurestrongholdpieceweight = astructurestrongholdpieceweight[j];
            structurestrongholdpieceweight.instancesSpawned = 0;
            structurePieceList.add(structurestrongholdpieceweight);
        }

        strongComponentType = null;
    }

    private static boolean canAddStructurePieces()
    {
        boolean flag = false;
        totalWeight = 0;
        StructureStrongholdPieceWeight structurestrongholdpieceweight;

        for (Iterator iterator = structurePieceList.iterator(); iterator.hasNext(); totalWeight += structurestrongholdpieceweight.pieceWeight)
        {
            structurestrongholdpieceweight = (StructureStrongholdPieceWeight)iterator.next();

            if (structurestrongholdpieceweight.instancesLimit > 0 && structurestrongholdpieceweight.instancesSpawned < structurestrongholdpieceweight.instancesLimit)
            {
                flag = true;
            }
        }

        return flag;
    }

    /**
     * translates the PieceWeight class to the Component class
     */
    private static ComponentStronghold getStrongholdComponentFromWeightedPiece(Class par0Class, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        Object object = null;

        if (par0Class == ComponentStrongholdStraight.class)
        {
            object = ComponentStrongholdStraight.findValidPlacement(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (par0Class == ComponentStrongholdPrison.class)
        {
            object = ComponentStrongholdPrison.findValidPlacement(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (par0Class == ComponentStrongholdLeftTurn.class)
        {
            object = ComponentStrongholdLeftTurn.findValidPlacement(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (par0Class == ComponentStrongholdRightTurn.class)
        {
            object = ComponentStrongholdRightTurn.findValidPlacement(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (par0Class == ComponentStrongholdRoomCrossing.class)
        {
            object = ComponentStrongholdRoomCrossing.findValidPlacement(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (par0Class == ComponentStrongholdStairsStraight.class)
        {
            object = ComponentStrongholdStairsStraight.findValidPlacement(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (par0Class == ComponentStrongholdStairs.class)
        {
            object = ComponentStrongholdStairs.getStrongholdStairsComponent(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (par0Class == ComponentStrongholdCrossing.class)
        {
            object = ComponentStrongholdCrossing.findValidPlacement(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (par0Class == ComponentStrongholdChestCorridor.class)
        {
            object = ComponentStrongholdChestCorridor.findValidPlacement(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (par0Class == ComponentStrongholdLibrary.class)
        {
            object = ComponentStrongholdLibrary.findValidPlacement(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (par0Class == ComponentStrongholdPortalRoom.class)
        {
            object = ComponentStrongholdPortalRoom.findValidPlacement(par1List, par2Random, par3, par4, par5, par6, par7);
        }

        return (ComponentStronghold)object;
    }

    private static ComponentStronghold getNextComponent(ComponentStrongholdStairs2 par0ComponentStrongholdStairs2, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        if (!canAddStructurePieces())
        {
            return null;
        }
        else
        {
            if (strongComponentType != null)
            {
                ComponentStronghold componentstronghold = getStrongholdComponentFromWeightedPiece(strongComponentType, par1List, par2Random, par3, par4, par5, par6, par7);
                strongComponentType = null;

                if (componentstronghold != null)
                {
                    return componentstronghold;
                }
            }

            int j1 = 0;

            while (j1 < 5)
            {
                ++j1;
                int k1 = par2Random.nextInt(totalWeight);
                Iterator iterator = structurePieceList.iterator();

                while (iterator.hasNext())
                {
                    StructureStrongholdPieceWeight structurestrongholdpieceweight = (StructureStrongholdPieceWeight)iterator.next();
                    k1 -= structurestrongholdpieceweight.pieceWeight;

                    if (k1 < 0)
                    {
                        if (!structurestrongholdpieceweight.canSpawnMoreStructuresOfType(par7) || structurestrongholdpieceweight == par0ComponentStrongholdStairs2.strongholdPieceWeight)
                        {
                            break;
                        }

                        ComponentStronghold componentstronghold1 = getStrongholdComponentFromWeightedPiece(structurestrongholdpieceweight.pieceClass, par1List, par2Random, par3, par4, par5, par6, par7);

                        if (componentstronghold1 != null)
                        {
                            ++structurestrongholdpieceweight.instancesSpawned;
                            par0ComponentStrongholdStairs2.strongholdPieceWeight = structurestrongholdpieceweight;

                            if (!structurestrongholdpieceweight.canSpawnMoreStructures())
                            {
                                structurePieceList.remove(structurestrongholdpieceweight);
                            }

                            return componentstronghold1;
                        }
                    }
                }
            }

            StructureBoundingBox structureboundingbox = ComponentStrongholdCorridor.func_74992_a(par1List, par2Random, par3, par4, par5, par6);

            if (structureboundingbox != null && structureboundingbox.minY > 1)
            {
                return new ComponentStrongholdCorridor(par7, par2Random, structureboundingbox, par6);
            }
            else
            {
                return null;
            }
        }
    }

    private static StructureComponent getNextValidComponent(ComponentStrongholdStairs2 par0ComponentStrongholdStairs2, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        if (par7 > 50)
        {
            return null;
        }
        else if (Math.abs(par3 - par0ComponentStrongholdStairs2.getBoundingBox().minX) <= 112 && Math.abs(par5 - par0ComponentStrongholdStairs2.getBoundingBox().minZ) <= 112)
        {
            ComponentStronghold componentstronghold = getNextComponent(par0ComponentStrongholdStairs2, par1List, par2Random, par3, par4, par5, par6, par7 + 1);

            if (componentstronghold != null)
            {
                par1List.add(componentstronghold);
                par0ComponentStrongholdStairs2.field_75026_c.add(componentstronghold);
            }

            return componentstronghold;
        }
        else
        {
            return null;
        }
    }

    static StructureComponent getNextValidComponentAccess(ComponentStrongholdStairs2 par0ComponentStrongholdStairs2, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        return getNextValidComponent(par0ComponentStrongholdStairs2, par1List, par2Random, par3, par4, par5, par6, par7);
    }

    static Class setComponentType(Class par0Class)
    {
        strongComponentType = par0Class;
        return par0Class;
    }

    static StructureStrongholdStones getStrongholdStones()
    {
        return strongholdStones;
    }
}
