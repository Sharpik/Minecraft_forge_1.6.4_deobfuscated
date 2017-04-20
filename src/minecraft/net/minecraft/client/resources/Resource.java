package net.minecraft.client.resources;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.InputStream;
import net.minecraft.client.resources.data.MetadataSection;

@SideOnly(Side.CLIENT)
public interface Resource
{
    InputStream getInputStream();

    boolean hasMetadata();

    MetadataSection getMetadata(String s);
}
