package net.minecraft.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Session
{
    private final String username;
    private final String sessionId;

    public Session(String par1Str, String par2Str)
    {
        if (par1Str == null || par1Str.isEmpty())
        {
            par1Str = "MissingName";
            par2Str = "NotValid";
            System.out.println("=========================================================");
            System.out.println("Warning the username was not set for this session, typically");
            System.out.println("this means you installed Forge incorrectly. We have set your");
            System.out.println("name to \"MissingName\" and your session to nothing. Please");
            System.out.println("check your instation and post a console log from the launcher");
            System.out.println("when asking for help!");
            System.out.println("=========================================================");
            
        }
        this.username = par1Str;
        this.sessionId = par2Str;
    }

    public String getUsername()
    {
        return this.username;
    }

    public String getSessionID()
    {
        return this.sessionId;
    }
}
