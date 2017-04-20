package net.minecraft.client.resources.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.lang.reflect.Type;

@SideOnly(Side.CLIENT)
public class FontMetadataSectionSerializer extends BaseMetadataSectionSerializer
{
    public FontMetadataSection func_110490_a(JsonElement par1JsonElement, Type par2Type, JsonDeserializationContext par3JsonDeserializationContext)
    {
        JsonObject jsonobject = par1JsonElement.getAsJsonObject();
        float[] afloat = new float[256];
        float[] afloat1 = new float[256];
        float[] afloat2 = new float[256];
        float f = 1.0F;
        float f1 = 0.0F;
        float f2 = 0.0F;

        if (jsonobject.has("characters"))
        {
            if (!jsonobject.get("characters").isJsonObject())
            {
                throw new JsonParseException("Invalid font->characters: expected object, was " + jsonobject.get("characters"));
            }

            JsonObject jsonobject1 = jsonobject.getAsJsonObject("characters");

            if (jsonobject1.has("default"))
            {
                if (!jsonobject1.get("default").isJsonObject())
                {
                    throw new JsonParseException("Invalid font->characters->default: expected object, was " + jsonobject1.get("default"));
                }

                JsonObject jsonobject2 = jsonobject1.getAsJsonObject("default");
                f = this.func_110487_a(jsonobject2.get("width"), "characters->default->width", Float.valueOf(f), 0.0F, 2.14748365E9F);
                f1 = this.func_110487_a(jsonobject2.get("spacing"), "characters->default->spacing", Float.valueOf(f1), 0.0F, 2.14748365E9F);
                f2 = this.func_110487_a(jsonobject2.get("left"), "characters->default->left", Float.valueOf(f2), 0.0F, 2.14748365E9F);
            }

            for (int i = 0; i < 256; ++i)
            {
                JsonElement jsonelement1 = jsonobject1.get(Integer.toString(i));
                float f3 = f;
                float f4 = f1;
                float f5 = f2;

                if (jsonelement1 != null)
                {
                    if (!jsonelement1.isJsonObject())
                    {
                        throw new JsonParseException("Invalid font->characters->" + i + ": expected object, was " + jsonelement1);
                    }

                    JsonObject jsonobject3 = jsonelement1.getAsJsonObject();
                    f3 = this.func_110487_a(jsonobject3.get("width"), "characters->" + i + "->width", Float.valueOf(f), 0.0F, 2.14748365E9F);
                    f4 = this.func_110487_a(jsonobject3.get("spacing"), "characters->" + i + "->spacing", Float.valueOf(f1), 0.0F, 2.14748365E9F);
                    f5 = this.func_110487_a(jsonobject3.get("left"), "characters->" + i + "->left", Float.valueOf(f2), 0.0F, 2.14748365E9F);
                }

                afloat[i] = f3;
                afloat1[i] = f4;
                afloat2[i] = f5;
            }
        }

        return new FontMetadataSection(afloat, afloat2, afloat1);
    }

    /**
     * The name of this section type as it appears in JSON.
     */
    public String getSectionName()
    {
        return "font";
    }

    public Object deserialize(JsonElement par1JsonElement, Type par2Type, JsonDeserializationContext par3JsonDeserializationContext)
    {
        return this.func_110490_a(par1JsonElement, par2Type, par3JsonDeserializationContext);
    }
}
