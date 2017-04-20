package net.minecraft.stats;

import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;
import argo.jdom.JsonStringNode;
import argo.saj.InvalidSyntaxException;
import cpw.mods.fml.common.asm.ReobfuscationMarker;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.stats.StatPlaceholder;
import net.minecraft.util.MD5String;
import net.minecraft.util.Session;

@ReobfuscationMarker
@SideOnly(Side.CLIENT)
public class StatFileWriter
{
    private Map field_77457_a = new HashMap();
    private Map field_77455_b = new HashMap();
    private boolean field_77456_c;
    private StatsSyncher statsSyncher;

    public StatFileWriter(Session par1Session, File par2File)
    {
        File file2 = new File(par2File, "stats");

        if (!file2.exists())
        {
            file2.mkdir();
        }

        File[] afile = par2File.listFiles();
        int i = afile.length;

        for (int j = 0; j < i; ++j)
        {
            File file3 = afile[j];

            if (file3.getName().startsWith("stats_") && file3.getName().endsWith(".dat"))
            {
                File file4 = new File(file2, file3.getName());

                if (!file4.exists())
                {
                    System.out.println("Relocating " + file3.getName());
                    file3.renameTo(file4);
                }
            }
        }

        this.statsSyncher = new StatsSyncher(par1Session, this, file2);
    }

    public void readStat(StatBase par1StatBase, int par2)
    {
        this.writeStatToMap(this.field_77455_b, par1StatBase, par2);
        this.writeStatToMap(this.field_77457_a, par1StatBase, par2);
        this.field_77456_c = true;
    }

    private void writeStatToMap(Map par1Map, StatBase par2StatBase, int par3)
    {
        Integer integer = (Integer)par1Map.get(par2StatBase);
        int j = integer == null ? 0 : integer.intValue();
        par1Map.put(par2StatBase, Integer.valueOf(j + par3));
    }

    public Map func_77445_b()
    {
        return new HashMap(this.field_77455_b);
    }

    /**
     * write a whole Map of stats to the statmap
     */
    public void writeStats(Map par1Map)
    {
        if (par1Map != null)
        {
            this.field_77456_c = true;
            Iterator iterator = par1Map.keySet().iterator();

            while (iterator.hasNext())
            {
                StatBase statbase = (StatBase)iterator.next();
                this.writeStatToMap(this.field_77455_b, statbase, ((Integer)par1Map.get(statbase)).intValue());
                this.writeStatToMap(this.field_77457_a, statbase, ((Integer)par1Map.get(statbase)).intValue());
            }
        }
    }

    public void func_77452_b(Map par1Map)
    {
        if (par1Map != null)
        {
            Iterator iterator = par1Map.keySet().iterator();

            while (iterator.hasNext())
            {
                StatBase statbase = (StatBase)iterator.next();
                Integer integer = (Integer)this.field_77455_b.get(statbase);
                int i = integer == null ? 0 : integer.intValue();
                this.field_77457_a.put(statbase, Integer.valueOf(((Integer)par1Map.get(statbase)).intValue() + i));
            }
        }
    }

    public void func_77448_c(Map par1Map)
    {
        if (par1Map != null)
        {
            this.field_77456_c = true;
            Iterator iterator = par1Map.keySet().iterator();

            while (iterator.hasNext())
            {
                StatBase statbase = (StatBase)iterator.next();
                this.writeStatToMap(this.field_77455_b, statbase, ((Integer)par1Map.get(statbase)).intValue());
            }
        }
    }

    public static Map func_77453_b(String par0Str)
    {
        HashMap hashmap = new HashMap();

        try
        {
            String s1 = "local";
            StringBuilder stringbuilder = new StringBuilder();
            JsonRootNode jsonrootnode = (new JdomParser()).parse(par0Str);
            List list = jsonrootnode.getArrayNode(new Object[] {"stats-change"});
            Iterator iterator = list.iterator();

            while (iterator.hasNext())
            {
                JsonNode jsonnode = (JsonNode)iterator.next();
                Map map = jsonnode.getFields();
                Entry entry = (Entry)map.entrySet().iterator().next();
                int i = Integer.parseInt(((JsonStringNode)entry.getKey()).getText());
                int j = Integer.parseInt(((JsonNode)entry.getValue()).getText());
                boolean flag = true;
                StatBase statbase = StatList.getOneShotStat(i);

                if (statbase == null)
                {
                    flag = false;
                    statbase = (new StatPlaceholder(i)).registerStat();
                }

                stringbuilder.append(StatList.getOneShotStat(i).statGuid).append(",");
                stringbuilder.append(j).append(",");

                if (flag)
                {
                    hashmap.put(statbase, Integer.valueOf(j));
                }
            }

            MD5String md5string = new MD5String(s1);
            String s2 = md5string.getMD5String(stringbuilder.toString());

            if (!s2.equals(jsonrootnode.getStringValue(new Object[] {"checksum"})))
            {
                System.out.println("CHECKSUM MISMATCH");
                return null;
            }
        }
        catch (InvalidSyntaxException invalidsyntaxexception)
        {
            invalidsyntaxexception.printStackTrace();
        }

        return hashmap;
    }

    public static String func_77441_a(String par0Str, String par1Str, Map par2Map)
    {
        StringBuilder stringbuilder = new StringBuilder();
        StringBuilder stringbuilder1 = new StringBuilder();
        boolean flag = true;
        stringbuilder.append("{\r\n");

        if (par0Str != null && par1Str != null)
        {
            stringbuilder.append("  \"user\":{\r\n");
            stringbuilder.append("    \"name\":\"").append(par0Str).append("\",\r\n");
            stringbuilder.append("    \"sessionid\":\"").append(par1Str).append("\"\r\n");
            stringbuilder.append("  },\r\n");
        }

        stringbuilder.append("  \"stats-change\":[");
        Iterator iterator = par2Map.keySet().iterator();

        while (iterator.hasNext())
        {
            StatBase statbase = (StatBase)iterator.next();

            if (flag)
            {
                flag = false;
            }
            else
            {
                stringbuilder.append("},");
            }

            stringbuilder.append("\r\n    {\"").append(statbase.statId).append("\":").append(par2Map.get(statbase));
            stringbuilder1.append(statbase.statGuid).append(",");
            stringbuilder1.append(par2Map.get(statbase)).append(",");
        }

        if (!flag)
        {
            stringbuilder.append("}");
        }

        MD5String md5string = new MD5String(par1Str);
        stringbuilder.append("\r\n  ],\r\n");
        stringbuilder.append("  \"checksum\":\"").append(md5string.getMD5String(stringbuilder1.toString())).append("\"\r\n");
        stringbuilder.append("}");
        return stringbuilder.toString();
    }

    /**
     * Returns true if the achievement has been unlocked.
     */
    public boolean hasAchievementUnlocked(Achievement par1Achievement)
    {
        return this.field_77457_a.containsKey(par1Achievement);
    }

    /**
     * Returns true if the parent has been unlocked, or there is no parent
     */
    public boolean canUnlockAchievement(Achievement par1Achievement)
    {
        return par1Achievement.parentAchievement == null || this.hasAchievementUnlocked(par1Achievement.parentAchievement);
    }

    public int writeStat(StatBase par1StatBase)
    {
        Integer integer = (Integer)this.field_77457_a.get(par1StatBase);
        return integer == null ? 0 : integer.intValue();
    }

    public void syncStats()
    {
        this.statsSyncher.syncStatsFileWithMap(this.func_77445_b());
    }

    public void func_77449_e()
    {
        if (this.field_77456_c && this.statsSyncher.func_77425_c())
        {
            this.statsSyncher.beginSendStats(this.func_77445_b());
        }

        this.statsSyncher.func_77422_e();
    }
}
