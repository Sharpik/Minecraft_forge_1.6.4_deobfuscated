package net.minecraft.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.IntBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class ScreenShotHelper
{
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
    private static IntBuffer field_74293_b;
    private static int[] field_74294_c;

    /**
     * Takes a screenshot and saves it to the screenshots directory. Returns the filename of the screenshot.
     */
    public static String saveScreenshot(File par0File, int par1, int par2)
    {
        return func_74292_a(par0File, (String)null, par1, par2);
    }

    public static String func_74292_a(File par0File, String par1Str, int par2, int par3)
    {
        try
        {
            File file2 = new File(par0File, "screenshots");
            file2.mkdir();
            int k = par2 * par3;

            if (field_74293_b == null || field_74293_b.capacity() < k)
            {
                field_74293_b = BufferUtils.createIntBuffer(k);
                field_74294_c = new int[k];
            }

            GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            field_74293_b.clear();
            GL11.glReadPixels(0, 0, par2, par3, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, field_74293_b);
            field_74293_b.get(field_74294_c);
            func_74289_a(field_74294_c, par2, par3);
            BufferedImage bufferedimage = new BufferedImage(par2, par3, 1);
            bufferedimage.setRGB(0, 0, par2, par3, field_74294_c, 0, par2);
            File file3;

            if (par1Str == null)
            {
                file3 = func_74290_a(file2);
            }
            else
            {
                file3 = new File(file2, par1Str);
            }

            ImageIO.write(bufferedimage, "png", file3);
            return "Saved screenshot as " + file3.getName();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            return "Failed to save: " + exception;
        }
    }

    private static File func_74290_a(File par0File)
    {
        String s = dateFormat.format(new Date()).toString();
        int i = 1;

        while (true)
        {
            File file2 = new File(par0File, s + (i == 1 ? "" : "_" + i) + ".png");

            if (!file2.exists())
            {
                return file2;
            }

            ++i;
        }
    }

    private static void func_74289_a(int[] par0ArrayOfInteger, int par1, int par2)
    {
        int[] aint1 = new int[par1];
        int k = par2 / 2;

        for (int l = 0; l < k; ++l)
        {
            System.arraycopy(par0ArrayOfInteger, l * par1, aint1, 0, par1);
            System.arraycopy(par0ArrayOfInteger, (par2 - 1 - l) * par1, par0ArrayOfInteger, l * par1, par1);
            System.arraycopy(aint1, 0, par0ArrayOfInteger, (par2 - 1 - l) * par1, par1);
        }
    }
}
