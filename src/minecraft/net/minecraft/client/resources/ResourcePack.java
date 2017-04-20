package net.minecraft.client.resources;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import net.minecraft.client.resources.data.MetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public interface ResourcePack
{
    InputStream getInputStream(ResourceLocation resourcelocation) throws IOException;

    boolean resourceExists(ResourceLocation resourcelocation);

    Set getResourceDomains();

    MetadataSection getPackMetadata(MetadataSerializer metadataserializer, String s) throws IOException;

    BufferedImage getPackImage() throws IOException;

    String getPackName();
}
