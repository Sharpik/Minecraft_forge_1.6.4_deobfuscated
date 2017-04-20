package net.minecraft.client.mco;

import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;
import argo.saj.InvalidSyntaxException;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.ValueObject;

@SideOnly(Side.CLIENT)
public class WorldTemplateList extends ValueObject
{
    public List field_110736_a;

    public static WorldTemplateList func_110735_a(String par0Str)
    {
        WorldTemplateList worldtemplatelist = new WorldTemplateList();
        worldtemplatelist.field_110736_a = new ArrayList();

        try
        {
            JsonRootNode jsonrootnode = (new JdomParser()).parse(par0Str);

            if (jsonrootnode.isArrayNode(new Object[] {"templates"}))
            {
                Iterator iterator = jsonrootnode.getArrayNode(new Object[] {"templates"}).iterator();

                while (iterator.hasNext())
                {
                    JsonNode jsonnode = (JsonNode)iterator.next();
                    worldtemplatelist.field_110736_a.add(WorldTemplate.func_110730_a(jsonnode));
                }
            }
        }
        catch (InvalidSyntaxException invalidsyntaxexception)
        {
            ;
        }
        catch (IllegalArgumentException illegalargumentexception)
        {
            ;
        }

        return worldtemplatelist;
    }
}
