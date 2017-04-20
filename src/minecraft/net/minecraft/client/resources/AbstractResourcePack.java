package net.minecraft.client.resources;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.data.MetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.logging.ILogAgent;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

@SideOnly(Side.CLIENT)
public abstract class AbstractResourcePack implements ResourcePack
{
    protected static final ILogAgent resourceLog = Minecraft.getMinecraft().getLogAgent();
    protected final File resourcePackFile;

    public AbstractResourcePack(File par1File)
    {
        this.resourcePackFile = par1File;
    }

    private static String locationToName(ResourceLocation par0ResourceLocation)
    {
        return String.format("%s/%s/%s", new Object[] {"assets", par0ResourceLocation.getResourceDomain(), par0ResourceLocation.getResourcePath()});
    }

    protected static String getRelativeName(File par0File, File par1File)
    {
        return par0File.toURI().relativize(par1File.toURI()).getPath();
    }

    public InputStream getInputStream(ResourceLocation par1ResourceLocation) throws IOException
    {
        return this.getInputStreamByName(locationToName(par1ResourceLocation));
    }

    public boolean resourceExists(ResourceLocation par1ResourceLocation)
    {
        return this.hasResourceName(locationToName(par1ResourceLocation));
    }

    protected abstract InputStream getInputStreamByName(String s) throws IOException;

    protected abstract boolean hasResourceName(String s);

    protected void logNameNotLowercase(String par1Str)
    {
        resourceLog.logWarningFormatted("ResourcePack: ignored non-lowercase namespace: %s in %s", new Object[] {par1Str, this.resourcePackFile});
    }

    public MetadataSection getPackMetadata(MetadataSerializer par1MetadataSerializer, String par2Str) throws IOException
    {
        return readMetadata(par1MetadataSerializer, this.getInputStreamByName("pack.mcmeta"), par2Str);
    }

    static MetadataSection readMetadata(MetadataSerializer par0MetadataSerializer, InputStream par1InputStream, String par2Str)
    {
        JsonObject jsonobject = null;
        BufferedReader bufferedreader = null;

        try
        {
            bufferedreader = new BufferedReader(new InputStreamReader(par1InputStream));
            jsonobject = (new JsonParser()).parse(bufferedreader).getAsJsonObject();
        }
        finally
        {
            IOUtils.closeQuietly(bufferedreader);
        }

        return par0MetadataSerializer.parseMetadataSection(par2Str, jsonobject);
    }

    public BufferedImage getPackImage() throws IOException
    {
        return ImageIO.read(this.getInputStreamByName("pack.png"));
    }

    public String getPackName()
    {
        return this.resourcePackFile.getName();
    }
}
