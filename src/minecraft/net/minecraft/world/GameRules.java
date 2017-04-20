package net.minecraft.world;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class GameRules
{
    private TreeMap theGameRules = new TreeMap();

    public GameRules()
    {
        this.addGameRule("doFireTick", "true");
        this.addGameRule("mobGriefing", "true");
        this.addGameRule("keepInventory", "false");
        this.addGameRule("doMobSpawning", "true");
        this.addGameRule("doMobLoot", "true");
        this.addGameRule("doTileDrops", "true");
        this.addGameRule("commandBlockOutput", "true");
        this.addGameRule("naturalRegeneration", "true");
        this.addGameRule("doDaylightCycle", "true");
    }

    /**
     * Define a game rule and its default value.
     */
    public void addGameRule(String par1Str, String par2Str)
    {
        this.theGameRules.put(par1Str, new GameRuleValue(par2Str));
    }

    public void setOrCreateGameRule(String par1Str, String par2Str)
    {
        GameRuleValue gamerulevalue = (GameRuleValue)this.theGameRules.get(par1Str);

        if (gamerulevalue != null)
        {
            gamerulevalue.setValue(par2Str);
        }
        else
        {
            this.addGameRule(par1Str, par2Str);
        }
    }

    /**
     * Gets the string Game Rule value.
     */
    public String getGameRuleStringValue(String par1Str)
    {
        GameRuleValue gamerulevalue = (GameRuleValue)this.theGameRules.get(par1Str);
        return gamerulevalue != null ? gamerulevalue.getGameRuleStringValue() : "";
    }

    /**
     * Gets the boolean Game Rule value.
     */
    public boolean getGameRuleBooleanValue(String par1Str)
    {
        GameRuleValue gamerulevalue = (GameRuleValue)this.theGameRules.get(par1Str);
        return gamerulevalue != null ? gamerulevalue.getGameRuleBooleanValue() : false;
    }

    /**
     * Return the defined game rules as NBT.
     */
    public NBTTagCompound writeGameRulesToNBT()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound("GameRules");
        Iterator iterator = this.theGameRules.keySet().iterator();

        while (iterator.hasNext())
        {
            String s = (String)iterator.next();
            GameRuleValue gamerulevalue = (GameRuleValue)this.theGameRules.get(s);
            nbttagcompound.setString(s, gamerulevalue.getGameRuleStringValue());
        }

        return nbttagcompound;
    }

    /**
     * Set defined game rules from NBT.
     */
    public void readGameRulesFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        Collection collection = par1NBTTagCompound.getTags();
        Iterator iterator = collection.iterator();

        while (iterator.hasNext())
        {
            NBTBase nbtbase = (NBTBase)iterator.next();
            String s = nbtbase.getName();
            String s1 = par1NBTTagCompound.getString(nbtbase.getName());
            this.setOrCreateGameRule(s, s1);
        }
    }

    /**
     * Return the defined game rules.
     */
    public String[] getRules()
    {
        return (String[])this.theGameRules.keySet().toArray(new String[0]);
    }

    /**
     * Return whether the specified game rule is defined.
     */
    public boolean hasRule(String par1Str)
    {
        return this.theGameRules.containsKey(par1Str);
    }
}
