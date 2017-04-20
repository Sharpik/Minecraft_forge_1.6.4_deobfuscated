package net.minecraft.client.resources;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class SimpleReloadableResourceManager implements ReloadableResourceManager
{
    private static final Joiner joinerResourcePacks = Joiner.on(", ");
    private final Map domainResourceManagers = Maps.newHashMap();
    private final List reloadListeners = Lists.newArrayList();
    private final Set setResourceDomains = Sets.newLinkedHashSet();
    private final MetadataSerializer rmMetadataSerializer;

    public SimpleReloadableResourceManager(MetadataSerializer par1MetadataSerializer)
    {
        this.rmMetadataSerializer = par1MetadataSerializer;
    }

    public void reloadResourcePack(ResourcePack par1ResourcePack)
    {
        FallbackResourceManager fallbackresourcemanager;

        for (Iterator iterator = par1ResourcePack.getResourceDomains().iterator(); iterator.hasNext(); fallbackresourcemanager.addResourcePack(par1ResourcePack))
        {
            String s = (String)iterator.next();
            this.setResourceDomains.add(s);
            fallbackresourcemanager = (FallbackResourceManager)this.domainResourceManagers.get(s);

            if (fallbackresourcemanager == null)
            {
                fallbackresourcemanager = new FallbackResourceManager(this.rmMetadataSerializer);
                this.domainResourceManagers.put(s, fallbackresourcemanager);
            }
        }
    }

    public Set getResourceDomains()
    {
        return this.setResourceDomains;
    }

    public Resource getResource(ResourceLocation par1ResourceLocation) throws IOException
    {
        ResourceManager resourcemanager = (ResourceManager)this.domainResourceManagers.get(par1ResourceLocation.getResourceDomain());

        if (resourcemanager != null)
        {
            return resourcemanager.getResource(par1ResourceLocation);
        }
        else
        {
            throw new FileNotFoundException(par1ResourceLocation.toString());
        }
    }

    public List getAllResources(ResourceLocation par1ResourceLocation) throws IOException
    {
        ResourceManager resourcemanager = (ResourceManager)this.domainResourceManagers.get(par1ResourceLocation.getResourceDomain());

        if (resourcemanager != null)
        {
            return resourcemanager.getAllResources(par1ResourceLocation);
        }
        else
        {
            throw new FileNotFoundException(par1ResourceLocation.toString());
        }
    }

    private void clearResources()
    {
        this.domainResourceManagers.clear();
        this.setResourceDomains.clear();
    }

    public void reloadResources(List par1List)
    {
        this.clearResources();
        Minecraft.getMinecraft().getLogAgent().logInfo("Reloading ResourceManager: " + joinerResourcePacks.join(Iterables.transform(par1List, new SimpleReloadableResourceManagerINNER1(this))));
        Iterator iterator = par1List.iterator();

        while (iterator.hasNext())
        {
            ResourcePack resourcepack = (ResourcePack)iterator.next();
            this.reloadResourcePack(resourcepack);
        }

        this.notifyReloadListeners();
    }

    public void registerReloadListener(ResourceManagerReloadListener par1ResourceManagerReloadListener)
    {
        this.reloadListeners.add(par1ResourceManagerReloadListener);
        par1ResourceManagerReloadListener.onResourceManagerReload(this);
    }

    private void notifyReloadListeners()
    {
        Iterator iterator = this.reloadListeners.iterator();

        while (iterator.hasNext())
        {
            ResourceManagerReloadListener resourcemanagerreloadlistener = (ResourceManagerReloadListener)iterator.next();
            resourcemanagerreloadlistener.onResourceManagerReload(this);
        }
    }
}
