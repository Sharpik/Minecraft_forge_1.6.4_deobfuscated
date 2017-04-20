package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.mco.GuiScreenCreateOnlineWorld;
import net.minecraft.client.gui.mco.GuiScreenPendingInvitation;
import net.minecraft.client.mco.ExceptionMcoService;
import net.minecraft.client.mco.GuiScreenConfirmationType;
import net.minecraft.client.mco.McoClient;
import net.minecraft.client.mco.McoServer;
import net.minecraft.client.mco.McoServerList;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiScreenOnlineServers extends GuiScreen
{
    private static final ResourceLocation field_130039_a = new ResourceLocation("textures/gui/widgets.png");
    private GuiScreen field_96188_a;
    private GuiSlotOnlineServerList field_96186_b;
    private static int field_96187_c;
    private static final Object field_96185_d = new Object();
    private long field_96189_n = -1L;
    private GuiButton field_96190_o;
    private GuiButton field_96198_p;
    private GuiButtonLink field_96197_q;
    private GuiButton field_96196_r;
    private String field_96195_s;
    private static McoServerList field_96194_t = new McoServerList();
    private boolean field_96193_u;
    private List field_96192_v = Lists.newArrayList();
    private volatile int field_96199_x = 0;
    private Long field_102019_y;
    private int field_104044_y;

    public GuiScreenOnlineServers(GuiScreen par1GuiScreen)
    {
        this.field_96188_a = par1GuiScreen;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        field_96194_t.func_130129_a(this.mc.getSession());

        if (!this.field_96193_u)
        {
            this.field_96193_u = true;
            this.field_96186_b = new GuiSlotOnlineServerList(this);
        }
        else
        {
            this.field_96186_b.func_104084_a(this.width, this.height, 32, this.height - 64);
        }

        this.func_96178_g();
    }

    public void func_96178_g()
    {
        this.buttonList.add(this.field_96196_r = new GuiButton(1, this.width / 2 - 154, this.height - 52, 100, 20, I18n.getString("mco.selectServer.play")));
        this.buttonList.add(this.field_96198_p = new GuiButton(2, this.width / 2 - 48, this.height - 52, 100, 20, I18n.getString("mco.selectServer.create")));
        this.buttonList.add(this.field_96190_o = new GuiButton(3, this.width / 2 + 58, this.height - 52, 100, 20, I18n.getString("mco.selectServer.configure")));
        this.buttonList.add(this.field_96197_q = new GuiButtonLink(4, this.width / 2 - 154, this.height - 28, 154, 20, I18n.getString("mco.selectServer.moreinfo")));
        this.buttonList.add(new GuiButton(0, this.width / 2 + 6, this.height - 28, 153, 20, I18n.getString("gui.cancel")));
        McoServer mcoserver = this.func_140030_b(this.field_96189_n);
        this.field_96196_r.enabled = mcoserver != null && mcoserver.field_96404_d.equals("OPEN") && !mcoserver.field_98166_h;
        this.field_96198_p.enabled = this.field_96199_x > 0;

        if (mcoserver != null && !mcoserver.field_96405_e.equals(this.mc.getSession().getUsername()))
        {
            this.field_96190_o.displayString = I18n.getString("mco.selectServer.leave");
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        ++this.field_104044_y;

        if (field_96194_t.func_130127_a())
        {
            List list = field_96194_t.func_98252_c();
            Iterator iterator = list.iterator();

            while (iterator.hasNext())
            {
                McoServer mcoserver = (McoServer)iterator.next();
                Iterator iterator1 = this.field_96192_v.iterator();

                while (iterator1.hasNext())
                {
                    McoServer mcoserver1 = (McoServer)iterator1.next();

                    if (mcoserver.field_96408_a == mcoserver1.field_96408_a)
                    {
                        mcoserver.func_96401_a(mcoserver1);

                        if (this.field_102019_y != null && this.field_102019_y.longValue() == mcoserver.field_96408_a)
                        {
                            this.field_102019_y = null;
                            mcoserver.field_96411_l = false;
                        }

                        break;
                    }
                }
            }

            this.field_96199_x = field_96194_t.func_140056_e();
            this.field_96192_v = list;
            field_96194_t.func_98250_b();
        }

        this.field_96198_p.enabled = this.field_96199_x > 0;
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.enabled)
        {
            if (par1GuiButton.id == 1)
            {
                this.func_140032_e(this.field_96189_n);
            }
            else if (par1GuiButton.id == 3)
            {
                this.func_140019_s();
            }
            else if (par1GuiButton.id == 0)
            {
                field_96194_t.func_98248_d();
                this.mc.displayGuiScreen(this.field_96188_a);
            }
            else if (par1GuiButton.id == 2)
            {
                field_96194_t.func_98248_d();
                this.mc.displayGuiScreen(new GuiScreenCreateOnlineWorld(this));
            }
            else if (par1GuiButton.id == 4)
            {
                this.field_96197_q.func_96135_a("http://realms.minecraft.net/");
            }
            else
            {
                this.field_96186_b.actionPerformed(par1GuiButton);
            }
        }
    }

    private void func_140019_s()
    {
        McoServer mcoserver = this.func_140030_b(this.field_96189_n);

        if (mcoserver != null)
        {
            if (this.mc.getSession().getUsername().equals(mcoserver.field_96405_e))
            {
                McoServer mcoserver1 = this.func_98086_a(mcoserver.field_96408_a);

                if (mcoserver1 != null)
                {
                    field_96194_t.func_98248_d();
                    this.mc.displayGuiScreen(new GuiScreenConfigureWorld(this, mcoserver1));
                }
            }
            else
            {
                String s = I18n.getString("mco.configure.world.leave.question.line1");
                String s1 = I18n.getString("mco.configure.world.leave.question.line2");
                this.mc.displayGuiScreen(new GuiScreenConfirmation(this, GuiScreenConfirmationType.Info, s, s1, 3));
            }
        }
    }

    private McoServer func_140030_b(long par1)
    {
        Iterator iterator = this.field_96192_v.iterator();
        McoServer mcoserver;

        do
        {
            if (!iterator.hasNext())
            {
                return null;
            }

            mcoserver = (McoServer)iterator.next();
        }
        while (mcoserver.field_96408_a != par1);

        return mcoserver;
    }

    private int func_140009_c(long par1)
    {
        for (int j = 0; j < this.field_96192_v.size(); ++j)
        {
            if (((McoServer)this.field_96192_v.get(j)).field_96408_a == par1)
            {
                return j;
            }
        }

        return -1;
    }

    public void confirmClicked(boolean par1, int par2)
    {
        if (par2 == 3 && par1)
        {
            (new ThreadOnlineScreen(this)).start();
        }

        this.mc.displayGuiScreen(this);
    }

    private void func_140012_t()
    {
        int i = this.func_140009_c(this.field_96189_n);

        if (this.field_96192_v.size() - 1 == i)
        {
            --i;
        }

        if (this.field_96192_v.size() == 0)
        {
            i = -1;
        }

        if (i >= 0 && i < this.field_96192_v.size())
        {
            this.field_96189_n = ((McoServer)this.field_96192_v.get(i)).field_96408_a;
        }
    }

    public void func_102018_a(long par1)
    {
        this.field_96189_n = -1L;
        this.field_102019_y = Long.valueOf(par1);
    }

    private McoServer func_98086_a(long par1)
    {
        McoClient mcoclient = new McoClient(this.mc.getSession());

        try
        {
            return mcoclient.func_98176_a(par1);
        }
        catch (ExceptionMcoService exceptionmcoservice)
        {
            this.mc.getLogAgent().logSevere(exceptionmcoservice.toString());
        }
        catch (IOException ioexception)
        {
            this.mc.getLogAgent().logWarning("Realms: could not parse response");
        }

        return null;
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        if (par2 == 59)
        {
            this.mc.gameSettings.hideServerAddress = !this.mc.gameSettings.hideServerAddress;
            this.mc.gameSettings.saveOptions();
        }
        else
        {
            if (par2 != 28 && par2 != 156)
            {
                super.keyTyped(par1, par2);
            }
            else
            {
                this.actionPerformed((GuiButton)this.buttonList.get(0));
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        this.field_96195_s = null;
        this.drawDefaultBackground();
        this.field_96186_b.drawScreen(par1, par2, par3);
        this.drawCenteredString(this.fontRenderer, I18n.getString("mco.title"), this.width / 2, 20, 16777215);
        super.drawScreen(par1, par2, par3);

        if (this.field_96195_s != null)
        {
            this.func_96165_a(this.field_96195_s, par1, par2);
        }

        this.func_130038_b(par1, par2);
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);

        if (this.func_130037_c(par1, par2) && field_96194_t.func_130124_d() != 0)
        {
            GuiScreenPendingInvitation guiscreenpendinginvitation = new GuiScreenPendingInvitation(this);
            this.mc.displayGuiScreen(guiscreenpendinginvitation);
        }
    }

    private void func_130038_b(int par1, int par2)
    {
        int k = field_96194_t.func_130124_d();
        boolean flag = this.func_130037_c(par1, par2);
        this.mc.getTextureManager().bindTexture(field_130039_a);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPushMatrix();
        this.drawTexturedModalRect(this.width / 2 + 58, 15, flag ? 166 : 182, 22, 16, 16);
        GL11.glPopMatrix();
        int l;
        int i1;

        if (k != 0)
        {
            l = 198 + (Math.min(k, 6) - 1) * 8;
            i1 = (int)(Math.max(0.0F, Math.max(MathHelper.sin((float)(10 + this.field_104044_y) * 0.57F), MathHelper.cos((float)this.field_104044_y * 0.35F))) * -6.0F);
            this.mc.getTextureManager().bindTexture(field_130039_a);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPushMatrix();
            this.drawTexturedModalRect(this.width / 2 + 58 + 4, 19 + i1, l, 22, 8, 8);
            GL11.glPopMatrix();
        }

        if (flag && k != 0)
        {
            l = par1 + 12;
            i1 = par2 - 12;
            String s = I18n.getString("mco.invites.pending");
            int j1 = this.fontRenderer.getStringWidth(s);
            this.drawGradientRect(l - 3, i1 - 3, l + j1 + 3, i1 + 8 + 3, -1073741824, -1073741824);
            this.fontRenderer.drawStringWithShadow(s, l, i1, -1);
        }
    }

    private boolean func_130037_c(int par1, int par2)
    {
        int k = this.width / 2 + 56;
        int l = this.width / 2 + 78;
        byte b0 = 13;
        byte b1 = 27;
        return k <= par1 && par1 <= l && b0 <= par2 && par2 <= b1;
    }

    private void func_140032_e(long par1)
    {
        McoServer mcoserver = this.func_140030_b(par1);

        if (mcoserver != null)
        {
            field_96194_t.func_98248_d();
            GuiScreenLongRunningTask guiscreenlongrunningtask = new GuiScreenLongRunningTask(this.mc, this, new TaskOnlineConnect(this, mcoserver));
            guiscreenlongrunningtask.func_98117_g();
            this.mc.displayGuiScreen(guiscreenlongrunningtask);
        }
    }

    private void func_101008_c(int par1, int par2, int par3, int par4)
    {
        this.mc.getTextureManager().bindTexture(field_130039_a);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPushMatrix();
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        this.drawTexturedModalRect(par1 * 2, par2 * 2, 191, 0, 16, 15);
        GL11.glPopMatrix();

        if (par3 >= par1 && par3 <= par1 + 9 && par4 >= par2 && par4 <= par2 + 9)
        {
            this.field_96195_s = I18n.getString("mco.selectServer.expired");
        }
    }

    private void func_104039_b(int par1, int par2, int par3, int par4, int par5)
    {
        if (this.field_104044_y % 20 < 10)
        {
            this.mc.getTextureManager().bindTexture(field_130039_a);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPushMatrix();
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            this.drawTexturedModalRect(par1 * 2, par2 * 2, 207, 0, 16, 15);
            GL11.glPopMatrix();
        }

        if (par3 >= par1 && par3 <= par1 + 9 && par4 >= par2 && par4 <= par2 + 9)
        {
            if (par5 == 0)
            {
                this.field_96195_s = I18n.getString("mco.selectServer.expires.soon");
            }
            else if (par5 == 1)
            {
                this.field_96195_s = I18n.getString("mco.selectServer.expires.day");
            }
            else
            {
                this.field_96195_s = I18n.getStringParams("mco.selectServer.expires.days", new Object[] {Integer.valueOf(par5)});
            }
        }
    }

    private void func_101006_d(int par1, int par2, int par3, int par4)
    {
        this.mc.getTextureManager().bindTexture(field_130039_a);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPushMatrix();
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        this.drawTexturedModalRect(par1 * 2, par2 * 2, 207, 0, 16, 15);
        GL11.glPopMatrix();

        if (par3 >= par1 && par3 <= par1 + 9 && par4 >= par2 && par4 <= par2 + 9)
        {
            this.field_96195_s = I18n.getString("mco.selectServer.open");
        }
    }

    private void func_101001_e(int par1, int par2, int par3, int par4)
    {
        this.mc.getTextureManager().bindTexture(field_130039_a);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPushMatrix();
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        this.drawTexturedModalRect(par1 * 2, par2 * 2, 223, 0, 16, 15);
        GL11.glPopMatrix();

        if (par3 >= par1 && par3 <= par1 + 9 && par4 >= par2 && par4 <= par2 + 9)
        {
            this.field_96195_s = I18n.getString("mco.selectServer.closed");
        }
    }

    protected void func_96165_a(String par1Str, int par2, int par3)
    {
        if (par1Str != null)
        {
            int k = par2 + 12;
            int l = par3 - 12;
            int i1 = this.fontRenderer.getStringWidth(par1Str);
            this.drawGradientRect(k - 3, l - 3, k + i1 + 3, l + 8 + 3, -1073741824, -1073741824);
            this.fontRenderer.drawStringWithShadow(par1Str, k, l, -1);
        }
    }

    private void func_96174_a(McoServer par1McoServer) throws IOException
    {
        if (par1McoServer.field_96414_k.equals(""))
        {
            par1McoServer.field_96414_k = EnumChatFormatting.GRAY + "" + 0;
        }

        par1McoServer.field_96415_h = 78;
        ServerAddress serveraddress = ServerAddress.func_78860_a(par1McoServer.field_96403_g);
        Socket socket = null;
        DataInputStream datainputstream = null;
        DataOutputStream dataoutputstream = null;

        try
        {
            socket = new Socket();
            socket.setSoTimeout(3000);
            socket.setTcpNoDelay(true);
            socket.setTrafficClass(18);
            socket.connect(new InetSocketAddress(serveraddress.getIP(), serveraddress.getPort()), 3000);
            datainputstream = new DataInputStream(socket.getInputStream());
            dataoutputstream = new DataOutputStream(socket.getOutputStream());
            dataoutputstream.write(254);
            dataoutputstream.write(1);

            if (datainputstream.read() != 255)
            {
                throw new IOException("Bad message");
            }

            String s = Packet.readString(datainputstream, 256);
            char[] achar = s.toCharArray();

            for (int i = 0; i < achar.length; ++i)
            {
                if (achar[i] != 167 && achar[i] != 0 && ChatAllowedCharacters.allowedCharacters.indexOf(achar[i]) < 0)
                {
                    achar[i] = 63;
                }
            }

            s = new String(achar);
            int j;
            int k;
            String[] astring;

            if (s.startsWith("\u00a7") && s.length() > 1)
            {
                astring = s.substring(1).split("\u0000");

                if (MathHelper.parseIntWithDefault(astring[0], 0) == 1)
                {
                    par1McoServer.field_96415_h = MathHelper.parseIntWithDefault(astring[1], par1McoServer.field_96415_h);
                    j = MathHelper.parseIntWithDefault(astring[4], 0);
                    k = MathHelper.parseIntWithDefault(astring[5], 0);

                    if (j >= 0 && k >= 0)
                    {
                        par1McoServer.field_96414_k = EnumChatFormatting.GRAY + "" + j;
                    }
                    else
                    {
                        par1McoServer.field_96414_k = "" + EnumChatFormatting.DARK_GRAY + "???";
                    }
                }
                else
                {
                    par1McoServer.field_96415_h = 79;
                    par1McoServer.field_96414_k = "" + EnumChatFormatting.DARK_GRAY + "???";
                }
            }
            else
            {
                astring = s.split("\u00a7");
                s = astring[0];
                j = -1;
                k = -1;

                try
                {
                    j = Integer.parseInt(astring[1]);
                    k = Integer.parseInt(astring[2]);
                }
                catch (Exception exception)
                {
                    ;
                }

                par1McoServer.field_96407_c = EnumChatFormatting.GRAY + s;

                if (j >= 0 && k > 0)
                {
                    par1McoServer.field_96414_k = EnumChatFormatting.GRAY + "" + j;
                }
                else
                {
                    par1McoServer.field_96414_k = "" + EnumChatFormatting.DARK_GRAY + "???";
                }

                par1McoServer.field_96415_h = 77;
            }
        }
        finally
        {
            try
            {
                if (datainputstream != null)
                {
                    datainputstream.close();
                }
            }
            catch (Throwable throwable)
            {
                ;
            }

            try
            {
                if (dataoutputstream != null)
                {
                    dataoutputstream.close();
                }
            }
            catch (Throwable throwable1)
            {
                ;
            }

            try
            {
                if (socket != null)
                {
                    socket.close();
                }
            }
            catch (Throwable throwable2)
            {
                ;
            }
        }
    }

    static long func_140041_a(GuiScreenOnlineServers par0GuiScreenOnlineServers)
    {
        return par0GuiScreenOnlineServers.field_96189_n;
    }

    static McoServer func_140011_a(GuiScreenOnlineServers par0GuiScreenOnlineServers, long par1)
    {
        return par0GuiScreenOnlineServers.func_140030_b(par1);
    }

    static Minecraft func_98075_b(GuiScreenOnlineServers par0GuiScreenOnlineServers)
    {
        return par0GuiScreenOnlineServers.mc;
    }

    static McoServerList func_140040_h()
    {
        return field_96194_t;
    }

    static List func_140013_c(GuiScreenOnlineServers par0GuiScreenOnlineServers)
    {
        return par0GuiScreenOnlineServers.field_96192_v;
    }

    static void func_140017_d(GuiScreenOnlineServers par0GuiScreenOnlineServers)
    {
        par0GuiScreenOnlineServers.func_140012_t();
    }

    static Minecraft func_98076_f(GuiScreenOnlineServers par0GuiScreenOnlineServers)
    {
        return par0GuiScreenOnlineServers.mc;
    }

    static Minecraft func_140037_f(GuiScreenOnlineServers par0GuiScreenOnlineServers)
    {
        return par0GuiScreenOnlineServers.mc;
    }

    static long func_140036_b(GuiScreenOnlineServers par0GuiScreenOnlineServers, long par1)
    {
        return par0GuiScreenOnlineServers.field_96189_n = par1;
    }

    static Minecraft func_140015_g(GuiScreenOnlineServers par0GuiScreenOnlineServers)
    {
        return par0GuiScreenOnlineServers.mc;
    }

    static GuiButton func_140038_h(GuiScreenOnlineServers par0GuiScreenOnlineServers)
    {
        return par0GuiScreenOnlineServers.field_96190_o;
    }

    static GuiButton func_140033_i(GuiScreenOnlineServers par0GuiScreenOnlineServers)
    {
        return par0GuiScreenOnlineServers.field_96196_r;
    }

    static void func_140008_c(GuiScreenOnlineServers par0GuiScreenOnlineServers, long par1)
    {
        par0GuiScreenOnlineServers.func_140032_e(par1);
    }

    static int func_140027_d(GuiScreenOnlineServers par0GuiScreenOnlineServers, long par1)
    {
        return par0GuiScreenOnlineServers.func_140009_c(par1);
    }

    static Minecraft func_104032_j(GuiScreenOnlineServers par0GuiScreenOnlineServers)
    {
        return par0GuiScreenOnlineServers.mc;
    }

    static FontRenderer func_140023_k(GuiScreenOnlineServers par0GuiScreenOnlineServers)
    {
        return par0GuiScreenOnlineServers.fontRenderer;
    }

    static void func_104031_c(GuiScreenOnlineServers par0GuiScreenOnlineServers, int par1, int par2, int par3, int par4)
    {
        par0GuiScreenOnlineServers.func_101008_c(par1, par2, par3, par4);
    }

    static void func_140035_b(GuiScreenOnlineServers par0GuiScreenOnlineServers, int par1, int par2, int par3, int par4)
    {
        par0GuiScreenOnlineServers.func_101001_e(par1, par2, par3, par4);
    }

    static Minecraft func_140014_l(GuiScreenOnlineServers par0GuiScreenOnlineServers)
    {
        return par0GuiScreenOnlineServers.mc;
    }

    static void func_140031_a(GuiScreenOnlineServers par0GuiScreenOnlineServers, int par1, int par2, int par3, int par4, int par5)
    {
        par0GuiScreenOnlineServers.func_104039_b(par1, par2, par3, par4, par5);
    }

    static void func_140020_c(GuiScreenOnlineServers par0GuiScreenOnlineServers, int par1, int par2, int par3, int par4)
    {
        par0GuiScreenOnlineServers.func_101006_d(par1, par2, par3, par4);
    }

    static FontRenderer func_140039_m(GuiScreenOnlineServers par0GuiScreenOnlineServers)
    {
        return par0GuiScreenOnlineServers.fontRenderer;
    }

    static FontRenderer func_98079_k(GuiScreenOnlineServers par0GuiScreenOnlineServers)
    {
        return par0GuiScreenOnlineServers.fontRenderer;
    }

    static Object func_140029_i()
    {
        return field_96185_d;
    }

    static int func_140018_j()
    {
        return field_96187_c;
    }

    static int func_140016_k()
    {
        return field_96187_c++;
    }

    static void func_140024_a(GuiScreenOnlineServers par0GuiScreenOnlineServers, McoServer par1McoServer) throws IOException
    {
        par0GuiScreenOnlineServers.func_96174_a(par1McoServer);
    }

    static int func_140021_r()
    {
        return field_96187_c--;
    }

    static FontRenderer func_110402_q(GuiScreenOnlineServers par0GuiScreenOnlineServers)
    {
        return par0GuiScreenOnlineServers.fontRenderer;
    }

    static FontRenderer func_140010_p(GuiScreenOnlineServers par0GuiScreenOnlineServers)
    {
        return par0GuiScreenOnlineServers.fontRenderer;
    }

    static Minecraft func_142023_q(GuiScreenOnlineServers par0GuiScreenOnlineServers)
    {
        return par0GuiScreenOnlineServers.mc;
    }
}
