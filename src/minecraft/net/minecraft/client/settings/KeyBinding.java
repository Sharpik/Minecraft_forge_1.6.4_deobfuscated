package net.minecraft.client.settings;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.IntHashMap;

@SideOnly(Side.CLIENT)
public class KeyBinding
{
    public static List keybindArray = new ArrayList();
    public static IntHashMap hash = new IntHashMap();
    public String keyDescription;
    public int keyCode;

    /** because _303 wanted me to call it that(Caironater) */
    public boolean pressed;
    public int pressTime;

    public static void onTick(int par0)
    {
        KeyBinding keybinding = (KeyBinding)hash.lookup(par0);

        if (keybinding != null)
        {
            ++keybinding.pressTime;
        }
    }

    public static void setKeyBindState(int par0, boolean par1)
    {
        KeyBinding keybinding = (KeyBinding)hash.lookup(par0);

        if (keybinding != null)
        {
            keybinding.pressed = par1;
        }
    }

    public static void unPressAllKeys()
    {
        Iterator iterator = keybindArray.iterator();

        while (iterator.hasNext())
        {
            KeyBinding keybinding = (KeyBinding)iterator.next();
            keybinding.unpressKey();
        }
    }

    public static void resetKeyBindingArrayAndHash()
    {
        hash.clearMap();
        Iterator iterator = keybindArray.iterator();

        while (iterator.hasNext())
        {
            KeyBinding keybinding = (KeyBinding)iterator.next();
            hash.addKey(keybinding.keyCode, keybinding);
        }
    }

    public KeyBinding(String par1Str, int par2)
    {
        this.keyDescription = par1Str;
        this.keyCode = par2;
        keybindArray.add(this);
        hash.addKey(par2, this);
    }

    public boolean isPressed()
    {
        if (this.pressTime == 0)
        {
            return false;
        }
        else
        {
            --this.pressTime;
            return true;
        }
    }

    private void unpressKey()
    {
        this.pressTime = 0;
        this.pressed = false;
    }
}
