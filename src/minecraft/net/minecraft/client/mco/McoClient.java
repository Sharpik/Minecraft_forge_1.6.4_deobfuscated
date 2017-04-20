package net.minecraft.client.mco;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import net.minecraft.util.Session;

@SideOnly(Side.CLIENT)
public class McoClient
{
    private final String field_96390_a;
    private final String field_100007_c;
    private static String field_96388_b = "https://mcoapi.minecraft.net/";

    public McoClient(Session par1Session)
    {
        this.field_96390_a = par1Session.getSessionID();
        this.field_100007_c = par1Session.getUsername();
    }

    public ValueObjectList func_96382_a() throws ExceptionMcoService, IOException
    {
        String s = this.func_96377_a(Request.func_96358_a(field_96388_b + "worlds"));
        return ValueObjectList.func_98161_a(s);
    }

    public McoServer func_98176_a(long par1) throws ExceptionMcoService, IOException
    {
        String s = this.func_96377_a(Request.func_96358_a(field_96388_b + "worlds" + "/$ID".replace("$ID", String.valueOf(par1))));
        return McoServer.func_98165_c(s);
    }

    public McoServerAddress func_96374_a(long par1) throws ExceptionMcoService, IOException
    {
        String s = field_96388_b + "worlds" + "/$ID/join".replace("$ID", "" + par1);
        String s1 = this.func_96377_a(Request.func_96358_a(s));
        return McoServerAddress.func_98162_a(s1);
    }

    public void func_96386_a(String par1Str, String par2Str, String par3Str, String par4Str) throws ExceptionMcoService, UnsupportedEncodingException
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append(field_96388_b).append("worlds").append("/$NAME/$LOCATION_ID".replace("$NAME", this.func_96380_a(par1Str)));
        HashMap hashmap = new HashMap();

        if (par2Str != null && !par2Str.trim().equals(""))
        {
            hashmap.put("motd", par2Str);
        }

        if (par3Str != null && !par3Str.equals(""))
        {
            hashmap.put("seed", par3Str);
        }

        hashmap.put("template", par4Str);

        if (!hashmap.isEmpty())
        {
            boolean flag = true;
            Entry entry;

            for (Iterator iterator = hashmap.entrySet().iterator(); iterator.hasNext(); stringbuilder.append((String)entry.getKey()).append("=").append(this.func_96380_a((String)entry.getValue())))
            {
                entry = (Entry)iterator.next();

                if (flag)
                {
                    stringbuilder.append("?");
                    flag = false;
                }
                else
                {
                    stringbuilder.append("&");
                }
            }
        }

        this.func_96377_a(Request.func_104064_a(stringbuilder.toString(), "", 5000, 30000));
    }

    public Boolean func_96375_b() throws ExceptionMcoService, IOException
    {
        String s = field_96388_b + "mco" + "/available";
        String s1 = this.func_96377_a(Request.func_96358_a(s));
        return Boolean.valueOf(s1);
    }

    public Boolean func_140054_c() throws ExceptionMcoService, IOException
    {
        String s = field_96388_b + "mco" + "/client/outdated";
        String s1 = this.func_96377_a(Request.func_96358_a(s));
        return Boolean.valueOf(s1);
    }

    public int func_96379_c() throws ExceptionMcoService
    {
        String s = field_96388_b + "payments" + "/unused";
        String s1 = this.func_96377_a(Request.func_96358_a(s));
        return Integer.valueOf(s1).intValue();
    }

    public void func_96381_a(long par1, String par3Str) throws ExceptionMcoService
    {
        String s1 = field_96388_b + "invites" + "/$WORLD_ID/invite/$USER_NAME".replace("$WORLD_ID", String.valueOf(par1)).replace("$USER_NAME", par3Str);
        this.func_96377_a(Request.func_96355_b(s1));
    }

    public void func_140055_c(long par1) throws ExceptionMcoService
    {
        String s = field_96388_b + "invites" + "/$WORLD_ID".replace("$WORLD_ID", String.valueOf(par1));
        this.func_96377_a(Request.func_96355_b(s));
    }

    public McoServer func_96387_b(long par1, String par3Str) throws ExceptionMcoService, IOException
    {
        String s1 = field_96388_b + "invites" + "/$WORLD_ID/invite/$USER_NAME".replace("$WORLD_ID", String.valueOf(par1)).replace("$USER_NAME", par3Str);
        String s2 = this.func_96377_a(Request.func_96361_b(s1, ""));
        return McoServer.func_98165_c(s2);
    }

    public BackupList func_111232_c(long par1) throws ExceptionMcoService
    {
        String s = field_96388_b + "worlds" + "/$WORLD_ID/backups".replace("$WORLD_ID", String.valueOf(par1));
        String s1 = this.func_96377_a(Request.func_96358_a(s));
        return BackupList.func_111222_a(s1);
    }

    public void func_96384_a(long par1, String par3Str, String par4Str, int par5, int par6) throws ExceptionMcoService, UnsupportedEncodingException
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append(field_96388_b).append("worlds").append("/$WORLD_ID/$NAME".replace("$WORLD_ID", String.valueOf(par1)).replace("$NAME", this.func_96380_a(par3Str)));

        if (par4Str != null && !par4Str.trim().equals(""))
        {
            stringbuilder.append("?motd=").append(this.func_96380_a(par4Str));
        }
        else
        {
            stringbuilder.append("?motd=");
        }

        stringbuilder.append("&difficulty=").append(par5).append("&gameMode=").append(par6);
        this.func_96377_a(Request.func_96363_c(stringbuilder.toString(), ""));
    }

    public void func_111235_c(long par1, String par3Str) throws ExceptionMcoService
    {
        String s1 = field_96388_b + "worlds" + "/$WORLD_ID/backups".replace("$WORLD_ID", String.valueOf(par1)) + "?backupId=" + par3Str;
        this.func_96377_a(Request.func_96363_c(s1, ""));
    }

    public WorldTemplateList func_111231_d() throws ExceptionMcoService
    {
        String s = field_96388_b + "worlds" + "/templates";
        String s1 = this.func_96377_a(Request.func_96358_a(s));
        return WorldTemplateList.func_110735_a(s1);
    }

    public Boolean func_96383_b(long par1) throws ExceptionMcoService, IOException
    {
        String s = field_96388_b + "worlds" + "/$WORLD_ID/open".replace("$WORLD_ID", String.valueOf(par1));
        String s1 = this.func_96377_a(Request.func_96363_c(s, ""));
        return Boolean.valueOf(s1);
    }

    public Boolean func_96378_c(long par1) throws ExceptionMcoService, IOException
    {
        String s = field_96388_b + "worlds" + "/$WORLD_ID/close".replace("$WORLD_ID", String.valueOf(par1));
        String s1 = this.func_96377_a(Request.func_96363_c(s, ""));
        return Boolean.valueOf(s1);
    }

    public Boolean func_96376_d(long par1, String par3Str) throws ExceptionMcoService, UnsupportedEncodingException
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append(field_96388_b).append("worlds").append("/$WORLD_ID/reset".replace("$WORLD_ID", String.valueOf(par1)));

        if (par3Str != null && par3Str.length() > 0)
        {
            stringbuilder.append("?seed=").append(this.func_96380_a(par3Str));
        }

        String s1 = this.func_96377_a(Request.func_96353_a(stringbuilder.toString(), "", 30000, 80000));
        return Boolean.valueOf(s1);
    }

    public Boolean func_111233_e(long par1, String par3Str) throws ExceptionMcoService
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append(field_96388_b).append("worlds").append("/$WORLD_ID/reset".replace("$WORLD_ID", String.valueOf(par1)));

        if (par3Str != null)
        {
            stringbuilder.append("?template=").append(par3Str);
        }

        String s1 = this.func_96377_a(Request.func_96353_a(stringbuilder.toString(), "", 30000, 80000));
        return Boolean.valueOf(s1);
    }

    public ValueObjectSubscription func_98177_f(long par1) throws ExceptionMcoService, IOException
    {
        String s = this.func_96377_a(Request.func_96358_a(field_96388_b + "subscriptions" + "/$WORLD_ID".replace("$WORLD_ID", String.valueOf(par1))));
        return ValueObjectSubscription.func_98169_a(s);
    }

    public int func_130106_e() throws ExceptionMcoService
    {
        String s = this.func_96377_a(Request.func_96358_a(field_96388_b + "invites" + "/count/pending"));
        return Integer.parseInt(s);
    }

    public PendingInvitesList func_130108_f() throws ExceptionMcoService
    {
        String s = this.func_96377_a(Request.func_96358_a(field_96388_b + "invites" + "/pending"));
        return PendingInvitesList.func_130095_a(s);
    }

    public void func_130107_a(String par1Str) throws ExceptionMcoService
    {
        this.func_96377_a(Request.func_96363_c(field_96388_b + "invites" + "/accept/$INVITATION_ID".replace("$INVITATION_ID", par1Str), ""));
    }

    public void func_130109_b(String par1Str) throws ExceptionMcoService
    {
        this.func_96377_a(Request.func_96363_c(field_96388_b + "invites" + "/reject/$INVITATION_ID".replace("$INVITATION_ID", par1Str), ""));
    }

    private String func_96380_a(String par1Str) throws UnsupportedEncodingException
    {
        return URLEncoder.encode(par1Str, "UTF-8");
    }

    private String func_96377_a(Request par1Request) throws ExceptionMcoService
    {
        par1Request.func_100006_a("sid", this.field_96390_a);
        par1Request.func_100006_a("user", this.field_100007_c);
        par1Request.func_100006_a("version", "1.6.4");

        try
        {
            int i = par1Request.func_96362_a();

            if (i == 503)
            {
                int j = par1Request.func_111221_b();
                throw new ExceptionRetryCall(j);
            }
            else if (i >= 200 && i < 300)
            {
                return par1Request.func_96364_c();
            }
            else
            {
                throw new ExceptionMcoService(par1Request.func_96362_a(), par1Request.func_96364_c(), par1Request.func_130110_g());
            }
        }
        catch (ExceptionMcoHttp exceptionmcohttp)
        {
            throw new ExceptionMcoService(500, "Server not available!", -1);
        }
    }
}
