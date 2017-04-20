package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.LanServer;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
class GuiSlotServer extends GuiSlot
{
    /** Instance to the GUI this list is on. */
    final GuiMultiplayer parentGui;

    public GuiSlotServer(GuiMultiplayer par1GuiMultiplayer)
    {
        super(par1GuiMultiplayer.mc, par1GuiMultiplayer.width, par1GuiMultiplayer.height, 32, par1GuiMultiplayer.height - 64, 36);
        this.parentGui = par1GuiMultiplayer;
    }

    /**
     * Gets the size of the current slot list.
     */
    protected int getSize()
    {
        return GuiMultiplayer.getInternetServerList(this.parentGui).countServers() + GuiMultiplayer.getListOfLanServers(this.parentGui).size() + 1;
    }

    /**
     * the element in the slot that was clicked, boolean for wether it was double clicked or not
     */
    protected void elementClicked(int par1, boolean par2)
    {
        if (par1 < GuiMultiplayer.getInternetServerList(this.parentGui).countServers() + GuiMultiplayer.getListOfLanServers(this.parentGui).size())
        {
            int j = GuiMultiplayer.getSelectedServer(this.parentGui);
            GuiMultiplayer.getAndSetSelectedServer(this.parentGui, par1);
            ServerData serverdata = GuiMultiplayer.getInternetServerList(this.parentGui).countServers() > par1 ? GuiMultiplayer.getInternetServerList(this.parentGui).getServerData(par1) : null;
            boolean flag1 = GuiMultiplayer.getSelectedServer(this.parentGui) >= 0 && GuiMultiplayer.getSelectedServer(this.parentGui) < this.getSize() && (serverdata == null || serverdata.field_82821_f == 78);
            boolean flag2 = GuiMultiplayer.getSelectedServer(this.parentGui) < GuiMultiplayer.getInternetServerList(this.parentGui).countServers();
            GuiMultiplayer.getButtonSelect(this.parentGui).enabled = flag1;
            GuiMultiplayer.getButtonEdit(this.parentGui).enabled = flag2;
            GuiMultiplayer.getButtonDelete(this.parentGui).enabled = flag2;

            if (par2 && flag1)
            {
                GuiMultiplayer.func_74008_b(this.parentGui, par1);
            }
            else if (flag2 && GuiScreen.isShiftKeyDown() && j >= 0 && j < GuiMultiplayer.getInternetServerList(this.parentGui).countServers())
            {
                GuiMultiplayer.getInternetServerList(this.parentGui).swapServers(j, GuiMultiplayer.getSelectedServer(this.parentGui));
            }
        }
    }

    /**
     * returns true if the element passed in is currently selected
     */
    protected boolean isSelected(int par1)
    {
        return par1 == GuiMultiplayer.getSelectedServer(this.parentGui);
    }

    /**
     * return the height of the content being scrolled
     */
    protected int getContentHeight()
    {
        return this.getSize() * 36;
    }

    protected void drawBackground()
    {
        this.parentGui.drawDefaultBackground();
    }

    protected void drawSlot(int par1, int par2, int par3, int par4, Tessellator par5Tessellator)
    {
        if (par1 < GuiMultiplayer.getInternetServerList(this.parentGui).countServers())
        {
            this.func_77247_d(par1, par2, par3, par4, par5Tessellator);
        }
        else if (par1 < GuiMultiplayer.getInternetServerList(this.parentGui).countServers() + GuiMultiplayer.getListOfLanServers(this.parentGui).size())
        {
            this.func_77248_b(par1, par2, par3, par4, par5Tessellator);
        }
        else
        {
            this.func_77249_c(par1, par2, par3, par4, par5Tessellator);
        }
    }

    private void func_77248_b(int par1, int par2, int par3, int par4, Tessellator par5Tessellator)
    {
        LanServer lanserver = (LanServer)GuiMultiplayer.getListOfLanServers(this.parentGui).get(par1 - GuiMultiplayer.getInternetServerList(this.parentGui).countServers());
        this.parentGui.drawString(this.parentGui.fontRenderer, I18n.getString("lanServer.title"), par2 + 2, par3 + 1, 16777215);
        this.parentGui.drawString(this.parentGui.fontRenderer, lanserver.getServerMotd(), par2 + 2, par3 + 12, 8421504);

        if (this.parentGui.mc.gameSettings.hideServerAddress)
        {
            this.parentGui.drawString(this.parentGui.fontRenderer, I18n.getString("selectServer.hiddenAddress"), par2 + 2, par3 + 12 + 11, 3158064);
        }
        else
        {
            this.parentGui.drawString(this.parentGui.fontRenderer, lanserver.getServerIpPort(), par2 + 2, par3 + 12 + 11, 3158064);
        }
    }

    private void func_77249_c(int par1, int par2, int par3, int par4, Tessellator par5Tessellator)
    {
        this.parentGui.drawCenteredString(this.parentGui.fontRenderer, I18n.getString("lanServer.scanning"), this.parentGui.width / 2, par3 + 1, 16777215);
        String s;

        switch (GuiMultiplayer.getTicksOpened(this.parentGui) / 3 % 4)
        {
            case 0:
            default:
                s = "O o o";
                break;
            case 1:
            case 3:
                s = "o O o";
                break;
            case 2:
                s = "o o O";
        }

        this.parentGui.drawCenteredString(this.parentGui.fontRenderer, s, this.parentGui.width / 2, par3 + 12, 8421504);
    }

    private void func_77247_d(int par1, int par2, int par3, int par4, Tessellator par5Tessellator)
    {
        ServerData serverdata = GuiMultiplayer.getInternetServerList(this.parentGui).getServerData(par1);

        synchronized (GuiMultiplayer.getLock())
        {
            if (GuiMultiplayer.getThreadsPending() < 5 && !serverdata.field_78841_f)
            {
                serverdata.field_78841_f = true;
                serverdata.pingToServer = -2L;
                serverdata.serverMOTD = "";
                serverdata.populationInfo = "";
                GuiMultiplayer.increaseThreadsPending();
                (new ThreadPollServers(this, serverdata)).start();
            }
        }

        boolean flag = serverdata.field_82821_f > 78;
        boolean flag1 = serverdata.field_82821_f < 78;
        boolean flag2 = flag || flag1;
        this.parentGui.drawString(this.parentGui.fontRenderer, serverdata.serverName, par2 + 2, par3 + 1, 16777215);
        this.parentGui.drawString(this.parentGui.fontRenderer, serverdata.serverMOTD, par2 + 2, par3 + 12, 8421504);
        this.parentGui.drawString(this.parentGui.fontRenderer, serverdata.populationInfo, par2 + 215 - this.parentGui.fontRenderer.getStringWidth(serverdata.populationInfo), par3 + 12, 8421504);

        if (flag2)
        {
            String s = EnumChatFormatting.DARK_RED + serverdata.gameVersion;
            this.parentGui.drawString(this.parentGui.fontRenderer, s, par2 + 200 - this.parentGui.fontRenderer.getStringWidth(s), par3 + 1, 8421504);
        }

        if (!this.parentGui.mc.gameSettings.hideServerAddress && !serverdata.isHidingAddress())
        {
            this.parentGui.drawString(this.parentGui.fontRenderer, serverdata.serverIP, par2 + 2, par3 + 12 + 11, 3158064);
        }
        else
        {
            this.parentGui.drawString(this.parentGui.fontRenderer, I18n.getString("selectServer.hiddenAddress"), par2 + 2, par3 + 12 + 11, 3158064);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.parentGui.mc.getTextureManager().bindTexture(Gui.icons);
        byte b0 = 0;
        boolean flag3 = false;
        String s1 = "";
        int i1;

        if (flag2)
        {
            s1 = flag ? "Client out of date!" : "Server out of date!";
            i1 = 5;
        }
        else if (serverdata.field_78841_f && serverdata.pingToServer != -2L)
        {
            if (serverdata.pingToServer < 0L)
            {
                i1 = 5;
            }
            else if (serverdata.pingToServer < 150L)
            {
                i1 = 0;
            }
            else if (serverdata.pingToServer < 300L)
            {
                i1 = 1;
            }
            else if (serverdata.pingToServer < 600L)
            {
                i1 = 2;
            }
            else if (serverdata.pingToServer < 1000L)
            {
                i1 = 3;
            }
            else
            {
                i1 = 4;
            }

            if (serverdata.pingToServer < 0L)
            {
                s1 = "(no connection)";
            }
            else
            {
                s1 = serverdata.pingToServer + "ms";
            }
        }
        else
        {
            b0 = 1;
            i1 = (int)(Minecraft.getSystemTime() / 100L + (long)(par1 * 2) & 7L);

            if (i1 > 4)
            {
                i1 = 8 - i1;
            }

            s1 = "Polling..";
        }

        this.parentGui.drawTexturedModalRect(par2 + 205, par3, 0 + b0 * 10, 176 + i1 * 8, 10, 8);
        byte b1 = 4;

        if (this.mouseX >= par2 + 205 - b1 && this.mouseY >= par3 - b1 && this.mouseX <= par2 + 205 + 10 + b1 && this.mouseY <= par3 + 8 + b1)
        {
            GuiMultiplayer.getAndSetLagTooltip(this.parentGui, s1);
        }
    }
}
