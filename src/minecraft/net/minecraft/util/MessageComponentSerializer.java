package net.minecraft.util;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

public class MessageComponentSerializer implements JsonDeserializer, JsonSerializer
{
    public ChatMessageComponent deserializeComponent(JsonElement par1JsonElement, Type par2Type, JsonDeserializationContext par3JsonDeserializationContext)
    {
        ChatMessageComponent chatmessagecomponent = new ChatMessageComponent();
        JsonObject jsonobject = (JsonObject)par1JsonElement;
        JsonElement jsonelement1 = jsonobject.get("text");
        JsonElement jsonelement2 = jsonobject.get("translate");
        JsonElement jsonelement3 = jsonobject.get("color");
        JsonElement jsonelement4 = jsonobject.get("bold");
        JsonElement jsonelement5 = jsonobject.get("italic");
        JsonElement jsonelement6 = jsonobject.get("underlined");
        JsonElement jsonelement7 = jsonobject.get("obfuscated");

        if (jsonelement3 != null && jsonelement3.isJsonPrimitive())
        {
            EnumChatFormatting enumchatformatting = EnumChatFormatting.func_96300_b(jsonelement3.getAsString());

            if (enumchatformatting == null || !enumchatformatting.isColor())
            {
                throw new JsonParseException("Given color (" + jsonelement3.getAsString() + ") is not a valid selection");
            }

            chatmessagecomponent.setColor(enumchatformatting);
        }

        if (jsonelement4 != null && jsonelement4.isJsonPrimitive())
        {
            chatmessagecomponent.setBold(Boolean.valueOf(jsonelement4.getAsBoolean()));
        }

        if (jsonelement5 != null && jsonelement5.isJsonPrimitive())
        {
            chatmessagecomponent.setItalic(Boolean.valueOf(jsonelement5.getAsBoolean()));
        }

        if (jsonelement6 != null && jsonelement6.isJsonPrimitive())
        {
            chatmessagecomponent.setUnderline(Boolean.valueOf(jsonelement6.getAsBoolean()));
        }

        if (jsonelement7 != null && jsonelement7.isJsonPrimitive())
        {
            chatmessagecomponent.setObfuscated(Boolean.valueOf(jsonelement7.getAsBoolean()));
        }

        if (jsonelement1 != null)
        {
            if (jsonelement1.isJsonArray())
            {
                JsonArray jsonarray = jsonelement1.getAsJsonArray();
                Iterator iterator = jsonarray.iterator();

                while (iterator.hasNext())
                {
                    JsonElement jsonelement8 = (JsonElement)iterator.next();

                    if (jsonelement8.isJsonPrimitive())
                    {
                        chatmessagecomponent.addText(jsonelement8.getAsString());
                    }
                    else if (jsonelement8.isJsonObject())
                    {
                        chatmessagecomponent.appendComponent(this.deserializeComponent(jsonelement8, par2Type, par3JsonDeserializationContext));
                    }
                }
            }
            else if (jsonelement1.isJsonPrimitive())
            {
                chatmessagecomponent.addText(jsonelement1.getAsString());
            }
        }
        else if (jsonelement2 != null && jsonelement2.isJsonPrimitive())
        {
            JsonElement jsonelement9 = jsonobject.get("using");

            if (jsonelement9 != null)
            {
                if (jsonelement9.isJsonArray())
                {
                    ArrayList arraylist = Lists.newArrayList();
                    Iterator iterator1 = jsonelement9.getAsJsonArray().iterator();

                    while (iterator1.hasNext())
                    {
                        JsonElement jsonelement10 = (JsonElement)iterator1.next();

                        if (jsonelement10.isJsonPrimitive())
                        {
                            arraylist.add(jsonelement10.getAsString());
                        }
                        else if (jsonelement10.isJsonObject())
                        {
                            arraylist.add(this.deserializeComponent(jsonelement10, par2Type, par3JsonDeserializationContext));
                        }
                    }

                    chatmessagecomponent.addFormatted(jsonelement2.getAsString(), arraylist.toArray());
                }
                else if (jsonelement9.isJsonPrimitive())
                {
                    chatmessagecomponent.addFormatted(jsonelement2.getAsString(), new Object[] {jsonelement9.getAsString()});
                }
            }
            else
            {
                chatmessagecomponent.addKey(jsonelement2.getAsString());
            }
        }

        return chatmessagecomponent;
    }

    public JsonElement serializeComponent(ChatMessageComponent par1ChatMessageComponent, Type par2Type, JsonSerializationContext par3JsonSerializationContext)
    {
        JsonObject jsonobject = new JsonObject();

        if (par1ChatMessageComponent.getColor() != null)
        {
            jsonobject.addProperty("color", par1ChatMessageComponent.getColor().func_96297_d());
        }

        if (par1ChatMessageComponent.isBold() != null)
        {
            jsonobject.addProperty("bold", par1ChatMessageComponent.isBold());
        }

        if (par1ChatMessageComponent.isItalic() != null)
        {
            jsonobject.addProperty("italic", par1ChatMessageComponent.isItalic());
        }

        if (par1ChatMessageComponent.isUnderline() != null)
        {
            jsonobject.addProperty("underlined", par1ChatMessageComponent.isUnderline());
        }

        if (par1ChatMessageComponent.isObfuscated() != null)
        {
            jsonobject.addProperty("obfuscated", par1ChatMessageComponent.isObfuscated());
        }

        if (par1ChatMessageComponent.getText() != null)
        {
            jsonobject.addProperty("text", par1ChatMessageComponent.getText());
        }
        else if (par1ChatMessageComponent.getTranslationKey() != null)
        {
            jsonobject.addProperty("translate", par1ChatMessageComponent.getTranslationKey());

            if (par1ChatMessageComponent.getSubComponents() != null && !par1ChatMessageComponent.getSubComponents().isEmpty())
            {
                jsonobject.add("using", this.serializeComponentChildren(par1ChatMessageComponent, par2Type, par3JsonSerializationContext));
            }
        }
        else if (par1ChatMessageComponent.getSubComponents() != null && !par1ChatMessageComponent.getSubComponents().isEmpty())
        {
            jsonobject.add("text", this.serializeComponentChildren(par1ChatMessageComponent, par2Type, par3JsonSerializationContext));
        }

        return jsonobject;
    }

    private JsonArray serializeComponentChildren(ChatMessageComponent par1ChatMessageComponent, Type par2Type, JsonSerializationContext par3JsonSerializationContext)
    {
        JsonArray jsonarray = new JsonArray();
        Iterator iterator = par1ChatMessageComponent.getSubComponents().iterator();

        while (iterator.hasNext())
        {
            ChatMessageComponent chatmessagecomponent1 = (ChatMessageComponent)iterator.next();

            if (chatmessagecomponent1.getText() != null)
            {
                jsonarray.add(new JsonPrimitive(chatmessagecomponent1.getText()));
            }
            else
            {
                jsonarray.add(this.serializeComponent(chatmessagecomponent1, par2Type, par3JsonSerializationContext));
            }
        }

        return jsonarray;
    }

    public Object deserialize(JsonElement par1JsonElement, Type par2Type, JsonDeserializationContext par3JsonDeserializationContext)
    {
        return this.deserializeComponent(par1JsonElement, par2Type, par3JsonDeserializationContext);
    }

    public JsonElement serialize(Object par1Obj, Type par2Type, JsonSerializationContext par3JsonSerializationContext)
    {
        return this.serializeComponent((ChatMessageComponent)par1Obj, par2Type, par3JsonSerializationContext);
    }
}
