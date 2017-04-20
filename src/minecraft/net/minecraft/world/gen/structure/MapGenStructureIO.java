package net.minecraft.world.gen.structure;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class MapGenStructureIO
{
    private static Map field_143040_a = new HashMap();
    private static Map field_143038_b = new HashMap();
    private static Map field_143039_c = new HashMap();
    private static Map field_143037_d = new HashMap();

    public static void func_143034_b(Class par0Class, String par1Str)
    {
        field_143040_a.put(par1Str, par0Class);
        field_143038_b.put(par0Class, par1Str);
    }

    public static void func_143031_a(Class par0Class, String par1Str)
    {
        field_143039_c.put(par1Str, par0Class);
        field_143037_d.put(par0Class, par1Str);
    }

    public static String func_143033_a(StructureStart par0StructureStart)
    {
        return (String)field_143038_b.get(par0StructureStart.getClass());
    }

    public static String func_143036_a(StructureComponent par0StructureComponent)
    {
        return (String)field_143037_d.get(par0StructureComponent.getClass());
    }

    public static StructureStart func_143035_a(NBTTagCompound par0NBTTagCompound, World par1World)
    {
        StructureStart structurestart = null;

        try
        {
            Class oclass = (Class)field_143040_a.get(par0NBTTagCompound.getString("id"));

            if (oclass != null)
            {
                structurestart = (StructureStart)oclass.newInstance();
            }
        }
        catch (Exception exception)
        {
            par1World.getWorldLogAgent().logWarning("Failed Start with id " + par0NBTTagCompound.getString("id"));
            exception.printStackTrace();
        }

        if (structurestart != null)
        {
            structurestart.func_143020_a(par1World, par0NBTTagCompound);
        }
        else
        {
            par1World.getWorldLogAgent().logWarning("Skipping Structure with id " + par0NBTTagCompound.getString("id"));
        }

        return structurestart;
    }

    public static StructureComponent func_143032_b(NBTTagCompound par0NBTTagCompound, World par1World)
    {
        StructureComponent structurecomponent = null;

        try
        {
            Class oclass = (Class)field_143039_c.get(par0NBTTagCompound.getString("id"));

            if (oclass != null)
            {
                structurecomponent = (StructureComponent)oclass.newInstance();
            }
        }
        catch (Exception exception)
        {
            par1World.getWorldLogAgent().logWarning("Failed Piece with id " + par0NBTTagCompound.getString("id"));
            exception.printStackTrace();
        }

        if (structurecomponent != null)
        {
            structurecomponent.func_143009_a(par1World, par0NBTTagCompound);
        }
        else
        {
            par1World.getWorldLogAgent().logWarning("Skipping Piece with id " + par0NBTTagCompound.getString("id"));
        }

        return structurecomponent;
    }

    static
    {
        func_143034_b(StructureMineshaftStart.class, "Mineshaft");
        func_143034_b(StructureVillageStart.class, "Village");
        func_143034_b(StructureNetherBridgeStart.class, "Fortress");
        func_143034_b(StructureStrongholdStart.class, "Stronghold");
        func_143034_b(StructureScatteredFeatureStart.class, "Temple");
        StructureMineshaftPieces.func_143048_a();
        StructureVillagePieces.func_143016_a();
        StructureNetherBridgePieces.func_143049_a();
        StructureStrongholdPieces.func_143046_a();
        ComponentScatteredFeaturePieces.func_143045_a();
    }
}
