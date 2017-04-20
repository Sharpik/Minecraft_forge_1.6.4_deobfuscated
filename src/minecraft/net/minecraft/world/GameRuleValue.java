package net.minecraft.world;

class GameRuleValue
{
    private String valueString;
    private boolean valueBoolean;
    private int valueInteger;
    private double valueDouble;

    public GameRuleValue(String par1Str)
    {
        this.setValue(par1Str);
    }

    /**
     * Set this game rule value.
     */
    public void setValue(String par1Str)
    {
        this.valueString = par1Str;
        this.valueBoolean = Boolean.parseBoolean(par1Str);

        try
        {
            this.valueInteger = Integer.parseInt(par1Str);
        }
        catch (NumberFormatException numberformatexception)
        {
            ;
        }

        try
        {
            this.valueDouble = Double.parseDouble(par1Str);
        }
        catch (NumberFormatException numberformatexception1)
        {
            ;
        }
    }

    /**
     * Gets the GameRule's value as String.
     */
    public String getGameRuleStringValue()
    {
        return this.valueString;
    }

    /**
     * Gets the GameRule's value as boolean.
     */
    public boolean getGameRuleBooleanValue()
    {
        return this.valueBoolean;
    }
}
