package net.minecraft.server.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import net.minecraft.server.dedicated.DedicatedServer;

@SideOnly(Side.SERVER)
public class MinecraftServerGui extends JComponent
{
    private static boolean field_120022_a;
    private DedicatedServer field_120021_b;

    public static void func_120016_a(DedicatedServer par0DedicatedServer)
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception exception)
        {
            ;
        }

        MinecraftServerGui minecraftservergui = new MinecraftServerGui(par0DedicatedServer);
        field_120022_a = true;
        JFrame jframe = new JFrame("Minecraft server");
        jframe.add(minecraftservergui);
        jframe.pack();
        jframe.setLocationRelativeTo((Component)null);
        jframe.setVisible(true);
        jframe.addWindowListener(new MinecraftServerGuiINNER1(par0DedicatedServer));
    }

    public MinecraftServerGui(DedicatedServer par1DedicatedServer)
    {
        this.field_120021_b = par1DedicatedServer;
        this.setPreferredSize(new Dimension(854, 480));
        this.setLayout(new BorderLayout());

        try
        {
            this.add(this.func_120018_d(), "Center");
            this.add(this.func_120019_b(), "West");
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private JComponent func_120019_b()
    {
        JPanel jpanel = new JPanel(new BorderLayout());
        jpanel.add(new StatsComponent(this.field_120021_b), "North");
        jpanel.add(this.func_120020_c(), "Center");
        jpanel.setBorder(new TitledBorder(new EtchedBorder(), "Stats"));
        return jpanel;
    }

    private JComponent func_120020_c()
    {
        PlayerListComponent playerlistcomponent = new PlayerListComponent(this.field_120021_b);
        JScrollPane jscrollpane = new JScrollPane(playerlistcomponent, 22, 30);
        jscrollpane.setBorder(new TitledBorder(new EtchedBorder(), "Players"));
        return jscrollpane;
    }

    private JComponent func_120018_d()
    {
        JPanel jpanel = new JPanel(new BorderLayout());
        JTextArea jtextarea = new JTextArea();
        this.field_120021_b.getLogAgent().func_120013_a().addHandler(new TextAreaLogHandler(jtextarea));
        JScrollPane jscrollpane = new JScrollPane(jtextarea, 22, 30);
        jtextarea.setEditable(false);
        JTextField jtextfield = new JTextField();
        jtextfield.addActionListener(new MinecraftServerGuiINNER2(this, jtextfield));
        jtextarea.addFocusListener(new MinecraftServerGuiINNER3(this));
        jpanel.add(jscrollpane, "Center");
        jpanel.add(jtextfield, "South");
        jpanel.setBorder(new TitledBorder(new EtchedBorder(), "Log and chat"));
        return jpanel;
    }

    static DedicatedServer func_120017_a(MinecraftServerGui par0MinecraftServerGui)
    {
        return par0MinecraftServerGui.field_120021_b;
    }
}
