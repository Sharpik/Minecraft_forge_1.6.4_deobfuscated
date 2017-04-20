package net.minecraft.entity.ai.attributes;

import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.server.management.LowerStringMap;

public abstract class BaseAttributeMap
{
    protected final Map attributes = new HashMap();
    protected final Map attributesByName = new LowerStringMap();

    public AttributeInstance getAttributeInstance(Attribute par1Attribute)
    {
        return (AttributeInstance)this.attributes.get(par1Attribute);
    }

    public AttributeInstance getAttributeInstanceByName(String par1Str)
    {
        return (AttributeInstance)this.attributesByName.get(par1Str);
    }

    public abstract AttributeInstance func_111150_b(Attribute attribute);

    public Collection getAllAttributes()
    {
        return this.attributesByName.values();
    }

    public void func_111149_a(ModifiableAttributeInstance par1ModifiableAttributeInstance) {}

    public void removeAttributeModifiers(Multimap par1Multimap)
    {
        Iterator iterator = par1Multimap.entries().iterator();

        while (iterator.hasNext())
        {
            Entry entry = (Entry)iterator.next();
            AttributeInstance attributeinstance = this.getAttributeInstanceByName((String)entry.getKey());

            if (attributeinstance != null)
            {
                attributeinstance.removeModifier((AttributeModifier)entry.getValue());
            }
        }
    }

    public void applyAttributeModifiers(Multimap par1Multimap)
    {
        Iterator iterator = par1Multimap.entries().iterator();

        while (iterator.hasNext())
        {
            Entry entry = (Entry)iterator.next();
            AttributeInstance attributeinstance = this.getAttributeInstanceByName((String)entry.getKey());

            if (attributeinstance != null)
            {
                attributeinstance.removeModifier((AttributeModifier)entry.getValue());
                attributeinstance.applyModifier((AttributeModifier)entry.getValue());
            }
        }
    }
}
