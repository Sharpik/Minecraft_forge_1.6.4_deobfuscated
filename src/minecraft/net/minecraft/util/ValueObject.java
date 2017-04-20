package net.minecraft.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@SideOnly(Side.CLIENT)
public abstract class ValueObject
{
    public String toString()
    {
        StringBuilder stringbuilder = new StringBuilder("{");
        Field[] afield = this.getClass().getFields();
        int i = afield.length;

        for (int j = 0; j < i; ++j)
        {
            Field field = afield[j];

            if (!func_96394_a(field))
            {
                try
                {
                    stringbuilder.append(field.getName()).append("=").append(field.get(this)).append(" ");
                }
                catch (IllegalAccessException illegalaccessexception)
                {
                    ;
                }
            }
        }

        stringbuilder.deleteCharAt(stringbuilder.length() - 1);
        stringbuilder.append('}');
        return stringbuilder.toString();
    }

    private static boolean func_96394_a(Field par0Field)
    {
        return Modifier.isStatic(par0Field.getModifiers());
    }
}
