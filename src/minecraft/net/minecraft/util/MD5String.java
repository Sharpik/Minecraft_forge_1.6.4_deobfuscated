package net.minecraft.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SideOnly(Side.CLIENT)
public class MD5String
{
    /** The salt prepended to the string to be hashed */
    private String salt;

    public MD5String(String par1Str)
    {
        this.salt = par1Str;
    }

    /**
     * Gets the MD5 string
     */
    public String getMD5String(String par1Str)
    {
        try
        {
            String s1 = this.salt + par1Str;
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            messagedigest.update(s1.getBytes(), 0, s1.length());
            return (new BigInteger(1, messagedigest.digest())).toString(16);
        }
        catch (NoSuchAlgorithmException nosuchalgorithmexception)
        {
            throw new RuntimeException(nosuchalgorithmexception);
        }
    }
}
