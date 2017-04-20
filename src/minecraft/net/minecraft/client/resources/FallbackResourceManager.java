package net.minecraft.client.resources;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class FallbackResourceManager implements ResourceManager
{
    protected final List resourcePacks = new ArrayList();
    private final MetadataSerializer frmMetadataSerializer;

    public FallbackResourceManager(MetadataSerializer par1MetadataSerializer)
    {
        this.frmMetadataSerializer = par1MetadataSerializer;
    }

    public void addResourcePack(ResourcePack par1ResourcePack)
    {
        this.resourcePacks.add(par1ResourcePack);
    }

    public Set getResourceDomains()
    {
        return null;
    }

    public Resource getResource(ResourceLocation par1ResourceLocation) throws IOException
    {
        ResourcePack resourcepack = null;
        ResourceLocation resourcelocation1 = getLocationMcmeta(par1ResourceLocation);

        for (int i = this.resourcePacks.size() - 1; i >= 0; --i)
        {
            ResourcePack resourcepack1 = (ResourcePack)this.resourcePacks.get(i);

            if (resourcepack == null && resourcepack1.resourceExists(resourcelocation1))
            {
                resourcepack = resourcepack1;
            }

            if (resourcepack1.resourceExists(par1ResourceLocation))
            {
                InputStream inputstream = null;

                if (resourcepack != null)
                {
                    inputstream = resourcepack.getInputStream(resourcelocation1);
                }

                return new SimpleResource(par1ResourceLocation, resourcepack1.getInputStream(par1ResourceLocation), inputstream, this.frmMetadataSerializer);
            }
        }

        throw new FileNotFoundException(par1ResourceLocation.toString());
    }

    public List getAllResources(ResourceLocation par1ResourceLocation) throws IOException
    {
        ArrayList arraylist = Lists.newArrayList();
        ResourceLocation resourcelocation1 = getLocationMcmeta(par1ResourceLocation);
        Iterator iterator = this.resourcePacks.iterator();

        while (iterator.hasNext())
        {
            ResourcePack resourcepack = (ResourcePack)iterator.next();

            if (resourcepack.resourceExists(par1ResourceLocation))
            {
                InputStream inputstream = resourcepack.resourceExists(resourcelocation1) ? resourcepack.getInputStream(resourcelocation1) : null;
                arraylist.add(new SimpleResource(par1ResourceLocation, resourcepack.getInputStream(par1ResourceLocation), inputstream, this.frmMetadataSerializer));
            }
        }

        if (arraylist.isEmpty())
        {
            throw new FileNotFoundException(par1ResourceLocation.toString());
        }
        else
        {
            return arraylist;
        }
    }

    static ResourceLocation getLocationMcmeta(ResourceLocation par0ResourceLocation)
    {
        return new ResourceLocation(par0ResourceLocation.getResourceDomain(), par0ResourceLocation.getResourcePath() + ".mcmeta");
    }
}
