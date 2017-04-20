package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.packet.Packet19EntityAction;

@SideOnly(Side.CLIENT)
public class GuiSleepMP extends GuiChat
{
    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height - 40, I18n.getString("multiplayer.stopSleeping")));
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        if (par2 == 1)
        {
            this.wakeEntity();
        }
        else if (par2 != 28 && par2 != 156)
        {
            super.keyTyped(par1, par2);
        }
        else
        {
            String s = this.inputField.getText().trim();

            if (s.length() > 0)
            {
                this.mc.thePlayer.sendChatMessage(s);
            }

            this.inputField.setText("");
            this.mc.ingameGUI.getChatGUI().resetScroll();
        }
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.id == 1)
        {
            this.wakeEntity();
        }
        else
        {
            super.actionPerformed(par1GuiButton);
        }
    }

    /**
     * Wakes the entity from the bed
     */
    private void wakeEntity()
    {
        NetClientHandler netclienthandler = this.mc.thePlayer.sendQueue;
        netclienthandler.addToSendQueue(new Packet19EntityAction(this.mc.thePlayer, 3));
    }
}
