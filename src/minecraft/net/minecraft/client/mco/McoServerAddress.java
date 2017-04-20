package net.minecraft.client.mco;

import argo.jdom.JdomParser;
import argo.jdom.JsonRootNode;
import argo.saj.InvalidSyntaxException;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ValueObject;

@SideOnly(Side.CLIENT)
public class McoServerAddress extends ValueObject
{
    public String field_96417_a;

    public static McoServerAddress func_98162_a(String par0Str)
    {
        McoServerAddress mcoserveraddress = new McoServerAddress();

        try
        {
            JsonRootNode jsonrootnode = (new JdomParser()).parse(par0Str);
            mcoserveraddress.field_96417_a = jsonrootnode.getStringValue(new Object[] {"address"});
        }
        catch (InvalidSyntaxException invalidsyntaxexception)
        {
            ;
        }
        catch (IllegalArgumentException illegalargumentexception)
        {
            ;
        }

        return mcoserveraddress;
    }
}
