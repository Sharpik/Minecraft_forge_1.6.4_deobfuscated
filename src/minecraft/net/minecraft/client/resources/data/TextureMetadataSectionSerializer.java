package net.minecraft.client.resources.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.lang.reflect.Type;

@SideOnly(Side.CLIENT)
public class TextureMetadataSectionSerializer extends BaseMetadataSectionSerializer
{
    public TextureMetadataSection func_110494_a(JsonElement par1JsonElement, Type par2Type, JsonDeserializationContext par3JsonDeserializationContext)
    {
        JsonObject jsonobject = par1JsonElement.getAsJsonObject();
        boolean flag = this.func_110484_a(jsonobject.get("blur"), "blur", Boolean.valueOf(false));
        boolean flag1 = this.func_110484_a(jsonobject.get("clamp"), "clamp", Boolean.valueOf(false));
        return new TextureMetadataSection(flag, flag1);
    }

    /**
     * The name of this section type as it appears in JSON.
     */
    public String getSectionName()
    {
        return "texture";
    }

    public Object deserialize(JsonElement par1JsonElement, Type par2Type, JsonDeserializationContext par3JsonDeserializationContext)
    {
        return this.func_110494_a(par1JsonElement, par2Type, par3JsonDeserializationContext);
    }
}
