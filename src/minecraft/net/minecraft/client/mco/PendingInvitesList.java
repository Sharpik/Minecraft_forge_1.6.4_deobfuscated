package net.minecraft.client.mco;

import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;
import argo.saj.InvalidSyntaxException;
import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.ValueObject;

@SideOnly(Side.CLIENT)
public class PendingInvitesList extends ValueObject
{
    public List field_130096_a = Lists.newArrayList();

    public static PendingInvitesList func_130095_a(String par0Str)
    {
        PendingInvitesList pendinginviteslist = new PendingInvitesList();

        try
        {
            JsonRootNode jsonrootnode = (new JdomParser()).parse(par0Str);

            if (jsonrootnode.isArrayNode(new Object[] {"invites"}))
            {
                Iterator iterator = jsonrootnode.getArrayNode(new Object[] {"invites"}).iterator();

                while (iterator.hasNext())
                {
                    JsonNode jsonnode = (JsonNode)iterator.next();
                    pendinginviteslist.field_130096_a.add(PendingInvite.func_130091_a(jsonnode));
                }
            }
        }
        catch (InvalidSyntaxException invalidsyntaxexception)
        {
            ;
        }

        return pendinginviteslist;
    }
}
