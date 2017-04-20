package net.minecraft.client.mco;

import argo.jdom.JsonNode;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ValueObject;

@SideOnly(Side.CLIENT)
public class PendingInvite extends ValueObject
{
    public String field_130094_a;
    public String field_130092_b;
    public String field_130093_c;

    public static PendingInvite func_130091_a(JsonNode par0JsonNode)
    {
        PendingInvite pendinginvite = new PendingInvite();

        try
        {
            pendinginvite.field_130094_a = par0JsonNode.getStringValue(new Object[] {"invitationId"});
            pendinginvite.field_130092_b = par0JsonNode.getStringValue(new Object[] {"worldName"});
            pendinginvite.field_130093_c = par0JsonNode.getStringValue(new Object[] {"worldOwnerName"});
        }
        catch (Exception exception)
        {
            ;
        }

        return pendinginvite;
    }
}
