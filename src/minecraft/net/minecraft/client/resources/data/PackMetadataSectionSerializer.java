package net.minecraft.client.resources.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.lang.reflect.Type;

@SideOnly(Side.CLIENT)
public class PackMetadataSectionSerializer extends BaseMetadataSectionSerializer implements JsonSerializer
{
    public PackMetadataSection func_110489_a(JsonElement par1JsonElement, Type par2Type, JsonDeserializationContext par3JsonDeserializationContext)
    {
        JsonObject jsonobject = par1JsonElement.getAsJsonObject();
        String s = this.func_110486_a(jsonobject.get("description"), "description", (String)null, 1, Integer.MAX_VALUE);
        int i = this.func_110485_a(jsonobject.get("pack_format"), "pack_format", (Integer)null, 1, Integer.MAX_VALUE);
        return new PackMetadataSection(s, i);
    }

    public JsonElement func_110488_a(PackMetadataSection par1PackMetadataSection, Type par2Type, JsonSerializationContext par3JsonSerializationContext)
    {
        JsonObject jsonobject = new JsonObject();
        jsonobject.addProperty("pack_format", Integer.valueOf(par1PackMetadataSection.getPackFormat()));
        jsonobject.addProperty("description", par1PackMetadataSection.getPackDescription());
        return jsonobject;
    }

    /**
     * The name of this section type as it appears in JSON.
     */
    public String getSectionName()
    {
        return "pack";
    }

    public Object deserialize(JsonElement par1JsonElement, Type par2Type, JsonDeserializationContext par3JsonDeserializationContext)
    {
        return this.func_110489_a(par1JsonElement, par2Type, par3JsonDeserializationContext);
    }

    public JsonElement serialize(Object par1Obj, Type par2Type, JsonSerializationContext par3JsonSerializationContext)
    {
        return this.func_110488_a((PackMetadataSection)par1Obj, par2Type, par3JsonSerializationContext);
    }
}
