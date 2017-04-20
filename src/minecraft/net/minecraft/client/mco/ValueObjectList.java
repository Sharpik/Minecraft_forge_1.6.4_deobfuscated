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
public class ValueObjectList extends ValueObject
{
    public List field_96430_d;

    public static ValueObjectList func_98161_a(String par0Str)
    {
        ValueObjectList valueobjectlist = new ValueObjectList();
        valueobjectlist.field_96430_d = new ArrayList();

        try
        {
            JsonRootNode jsonrootnode = (new JdomParser()).parse(par0Str);

            if (jsonrootnode.isArrayNode(new Object[] {"servers"}))
            {
                Iterator iterator = jsonrootnode.getArrayNode(new Object[] {"servers"}).iterator();

                while (iterator.hasNext())
                {
                    JsonNode jsonnode = (JsonNode)iterator.next();
                    valueobjectlist.field_96430_d.add(McoServer.func_98163_a(jsonnode));
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

        return valueobjectlist;
    }
}
