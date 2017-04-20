package net.minecraft.entity.ai.attributes;

public interface Attribute
{
    String getAttributeUnlocalizedName();

    double clampValue(double d0);

    double getDefaultValue();

    boolean getShouldWatch();
}
