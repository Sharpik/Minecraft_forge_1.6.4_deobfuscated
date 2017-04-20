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

@SideOnly(Side.CLIENT)
public class BackupList
{
    public List field_111223_a;

    public static BackupList func_111222_a(String par0Str)
    {
        BackupList backuplist = new BackupList();
        backuplist.field_111223_a = new ArrayList();

        try
        {
            JsonRootNode jsonrootnode = (new JdomParser()).parse(par0Str);

            if (jsonrootnode.isArrayNode(new Object[] {"backups"}))
            {
                Iterator iterator = jsonrootnode.getArrayNode(new Object[] {"backups"}).iterator();

                while (iterator.hasNext())
                {
                    JsonNode jsonnode = (JsonNode)iterator.next();
                    backuplist.field_111223_a.add(Backup.func_110724_a(jsonnode));
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

        return backuplist;
    }
}
