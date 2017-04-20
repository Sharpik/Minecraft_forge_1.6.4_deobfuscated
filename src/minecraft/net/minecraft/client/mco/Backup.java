package net.minecraft.client.mco;

import argo.jdom.JsonNode;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Date;
import net.minecraft.util.ValueObject;

@SideOnly(Side.CLIENT)
public class Backup extends ValueObject
{
    public String field_110727_a;
    public Date field_110725_b;
    public long field_110726_c;

    public static Backup func_110724_a(JsonNode par0JsonNode)
    {
        Backup backup = new Backup();

        try
        {
            backup.field_110727_a = par0JsonNode.getStringValue(new Object[] {"backupId"});
            backup.field_110725_b = new Date(Long.parseLong(par0JsonNode.getNumberValue(new Object[] {"lastModifiedDate"})));
            backup.field_110726_c = Long.parseLong(par0JsonNode.getNumberValue(new Object[] {"size"}));
        }
        catch (IllegalArgumentException illegalargumentexception)
        {
            ;
        }

        return backup;
    }
}
