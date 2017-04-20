package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.EnumChatFormatting;

@SideOnly(Side.CLIENT)
class ThreadPollServers extends Thread
{
    /** An Instnace of ServerData. */
    final ServerData pollServersServerData;

    /** Slot container for the server list */
    final GuiSlotServer serverSlotContainer;

    ThreadPollServers(GuiSlotServer par1GuiSlotServer, ServerData par2ServerData)
    {
        this.serverSlotContainer = par1GuiSlotServer;
        this.pollServersServerData = par2ServerData;
    }

    public void run()
    {
        boolean flag = false;
        label183:
        {
            label184:
            {
                label185:
                {
                    label186:
                    {
                        label187:
                        {
                            try
                            {
                                flag = true;
                                this.pollServersServerData.serverMOTD = EnumChatFormatting.DARK_GRAY + "Polling..";
                                long i = System.nanoTime();
                                GuiMultiplayer.func_82291_a(this.pollServersServerData);
                                long j = System.nanoTime();
                                this.pollServersServerData.pingToServer = (j - i) / 1000000L;
                                flag = false;
                                break label183;
                            }
                            catch (UnknownHostException unknownhostexception)
                            {
                                this.pollServersServerData.pingToServer = -1L;
                                this.pollServersServerData.serverMOTD = EnumChatFormatting.DARK_RED + "Can\'t resolve hostname";
                                flag = false;
                                break label184;
                            }
                            catch (SocketTimeoutException sockettimeoutexception)
                            {
                                this.pollServersServerData.pingToServer = -1L;
                                this.pollServersServerData.serverMOTD = EnumChatFormatting.DARK_RED + "Can\'t reach server";
                                flag = false;
                            }
                            catch (ConnectException connectexception)
                            {
                                this.pollServersServerData.pingToServer = -1L;
                                this.pollServersServerData.serverMOTD = EnumChatFormatting.DARK_RED + "Can\'t reach server";
                                flag = false;
                                break label187;
                            }
                            catch (IOException ioexception)
                            {
                                this.pollServersServerData.pingToServer = -1L;
                                this.pollServersServerData.serverMOTD = EnumChatFormatting.DARK_RED + "Communication error";
                                flag = false;
                                break label186;
                            }
                            catch (Exception exception)
                            {
                                this.pollServersServerData.pingToServer = -1L;
                                this.pollServersServerData.serverMOTD = "ERROR: " + exception.getClass();
                                flag = false;
                                break label185;
                            }
                            finally
                            {
                                if (flag)
                                {
                                    synchronized (GuiMultiplayer.getLock())
                                    {
                                        GuiMultiplayer.decreaseThreadsPending();
                                    }
                                }
                            }

                            synchronized (GuiMultiplayer.getLock())
                            {
                                GuiMultiplayer.decreaseThreadsPending();
                                return;
                            }
                        }

                        synchronized (GuiMultiplayer.getLock())
                        {
                            GuiMultiplayer.decreaseThreadsPending();
                            return;
                        }
                    }

                    synchronized (GuiMultiplayer.getLock())
                    {
                        GuiMultiplayer.decreaseThreadsPending();
                        return;
                    }
                }

                synchronized (GuiMultiplayer.getLock())
                {
                    GuiMultiplayer.decreaseThreadsPending();
                    return;
                }
            }

            synchronized (GuiMultiplayer.getLock())
            {
                GuiMultiplayer.decreaseThreadsPending();
                return;
            }
        }

        synchronized (GuiMultiplayer.getLock())
        {
            GuiMultiplayer.decreaseThreadsPending();
        }
    }
}
