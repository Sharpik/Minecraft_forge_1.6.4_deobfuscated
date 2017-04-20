package net.minecraft.server.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import net.minecraft.server.MinecraftServer;

@SideOnly(Side.SERVER)
class MinecraftServerGuiINNER2 implements ActionListener
{
    final JTextField field_120025_a;

    final MinecraftServerGui field_120024_b;

    MinecraftServerGuiINNER2(MinecraftServerGui par1MinecraftServerGui, JTextField par2JTextField)
    {
        this.field_120024_b = par1MinecraftServerGui;
        this.field_120025_a = par2JTextField;
    }

    public void actionPerformed(ActionEvent par1ActionEvent)
    {
        String s = this.field_120025_a.getText().trim();

        if (s.length() > 0)
        {
            MinecraftServerGui.func_120017_a(this.field_120024_b).addPendingCommand(s, MinecraftServer.getServer());
        }

        this.field_120025_a.setText("");
    }
}
