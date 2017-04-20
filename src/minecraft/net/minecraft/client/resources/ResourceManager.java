package net.minecraft.client.resources;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public interface ResourceManager
{
    Set getResourceDomains();

    Resource getResource(ResourceLocation resourcelocation) throws IOException;

    List getAllResources(ResourceLocation resourcelocation) throws IOException;
}
