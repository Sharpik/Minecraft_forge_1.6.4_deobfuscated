package net.minecraft.client.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.image.BufferedImage;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;

@SideOnly(Side.CLIENT)
class ThreadDownloadImageDataINNER1 extends Thread
{
    final ThreadDownloadImageData theThreadDownloadImageData;

    ThreadDownloadImageDataINNER1(ThreadDownloadImageData par1ThreadDownloadImageData)
    {
        this.theThreadDownloadImageData = par1ThreadDownloadImageData;
    }

    public void run()
    {
        HttpURLConnection httpurlconnection = null;

        try
        {
            httpurlconnection = (HttpURLConnection)(new URL(ThreadDownloadImageData.getImageUrl(this.theThreadDownloadImageData))).openConnection(Minecraft.getMinecraft().getProxy());
            httpurlconnection.setDoInput(true);
            httpurlconnection.setDoOutput(false);
            httpurlconnection.connect();

            if (httpurlconnection.getResponseCode() / 100 != 2)
            {
                return;
            }

            BufferedImage bufferedimage = ImageIO.read(httpurlconnection.getInputStream());

            if (ThreadDownloadImageData.getImageBuffer(this.theThreadDownloadImageData) != null)
            {
                bufferedimage = ThreadDownloadImageData.getImageBuffer(this.theThreadDownloadImageData).parseUserSkin(bufferedimage);
            }

            this.theThreadDownloadImageData.getBufferedImage(bufferedimage);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
        finally
        {
            if (httpurlconnection != null)
            {
                httpurlconnection.disconnect();
            }
        }
    }
}
