package net.minecraft.command;

import com.google.common.primitives.Doubles;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public abstract class CommandBase implements ICommand
{
    private static IAdminCommand theAdmin;

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 4;
    }

    public List getCommandAliases()
    {
        return null;
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        return null;
    }

    /**
     * Parses an int from the given string.
     */
    public static int parseInt(ICommandSender par0ICommandSender, String par1Str)
    {
        try
        {
            return Integer.parseInt(par1Str);
        }
        catch (NumberFormatException numberformatexception)
        {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] {par1Str});
        }
    }

    /**
     * Parses an int from the given sring with a specified minimum.
     */
    public static int parseIntWithMin(ICommandSender par0ICommandSender, String par1Str, int par2)
    {
        return parseIntBounded(par0ICommandSender, par1Str, par2, Integer.MAX_VALUE);
    }

    /**
     * Parses an int from the given string within a specified bound.
     */
    public static int parseIntBounded(ICommandSender par0ICommandSender, String par1Str, int par2, int par3)
    {
        int k = parseInt(par0ICommandSender, par1Str);

        if (k < par2)
        {
            throw new NumberInvalidException("commands.generic.num.tooSmall", new Object[] {Integer.valueOf(k), Integer.valueOf(par2)});
        }
        else if (k > par3)
        {
            throw new NumberInvalidException("commands.generic.num.tooBig", new Object[] {Integer.valueOf(k), Integer.valueOf(par3)});
        }
        else
        {
            return k;
        }
    }

    /**
     * Parses a double from the given string or throws an exception if it's not a double.
     */
    public static double parseDouble(ICommandSender par0ICommandSender, String par1Str)
    {
        try
        {
            double d0 = Double.parseDouble(par1Str);

            if (!Doubles.isFinite(d0))
            {
                throw new NumberInvalidException("commands.generic.double.invalid", new Object[] {par1Str});
            }
            else
            {
                return d0;
            }
        }
        catch (NumberFormatException numberformatexception)
        {
            throw new NumberInvalidException("commands.generic.double.invalid", new Object[] {par1Str});
        }
    }

    public static double func_110664_a(ICommandSender par0ICommandSender, String par1Str, double par2)
    {
        return func_110661_a(par0ICommandSender, par1Str, par2, Double.MAX_VALUE);
    }

    public static double func_110661_a(ICommandSender par0ICommandSender, String par1Str, double par2, double par4)
    {
        double d2 = parseDouble(par0ICommandSender, par1Str);

        if (d2 < par2)
        {
            throw new NumberInvalidException("commands.generic.double.tooSmall", new Object[] {Double.valueOf(d2), Double.valueOf(par2)});
        }
        else if (d2 > par4)
        {
            throw new NumberInvalidException("commands.generic.double.tooBig", new Object[] {Double.valueOf(d2), Double.valueOf(par4)});
        }
        else
        {
            return d2;
        }
    }

    public static boolean func_110662_c(ICommandSender par0ICommandSender, String par1Str)
    {
        if (!par1Str.equals("true") && !par1Str.equals("1"))
        {
            if (!par1Str.equals("false") && !par1Str.equals("0"))
            {
                throw new CommandException("commands.generic.boolean.invalid", new Object[] {par1Str});
            }
            else
            {
                return false;
            }
        }
        else
        {
            return true;
        }
    }

    /**
     * Returns the given ICommandSender as a EntityPlayer or throw an exception.
     */
    public static EntityPlayerMP getCommandSenderAsPlayer(ICommandSender par0ICommandSender)
    {
        if (par0ICommandSender instanceof EntityPlayerMP)
        {
            return (EntityPlayerMP)par0ICommandSender;
        }
        else
        {
            throw new PlayerNotFoundException("You must specify which player you wish to perform this action on.", new Object[0]);
        }
    }

    public static EntityPlayerMP getPlayer(ICommandSender par0ICommandSender, String par1Str)
    {
        EntityPlayerMP entityplayermp = PlayerSelector.matchOnePlayer(par0ICommandSender, par1Str);

        if (entityplayermp != null)
        {
            return entityplayermp;
        }
        else
        {
            entityplayermp = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(par1Str);

            if (entityplayermp == null)
            {
                throw new PlayerNotFoundException();
            }
            else
            {
                return entityplayermp;
            }
        }
    }

    public static String func_96332_d(ICommandSender par0ICommandSender, String par1Str)
    {
        EntityPlayerMP entityplayermp = PlayerSelector.matchOnePlayer(par0ICommandSender, par1Str);

        if (entityplayermp != null)
        {
            return entityplayermp.getEntityName();
        }
        else if (PlayerSelector.hasArguments(par1Str))
        {
            throw new PlayerNotFoundException();
        }
        else
        {
            return par1Str;
        }
    }

    public static String func_82360_a(ICommandSender par0ICommandSender, String[] par1ArrayOfStr, int par2)
    {
        return func_82361_a(par0ICommandSender, par1ArrayOfStr, par2, false);
    }

    public static String func_82361_a(ICommandSender par0ICommandSender, String[] par1ArrayOfStr, int par2, boolean par3)
    {
        StringBuilder stringbuilder = new StringBuilder();

        for (int j = par2; j < par1ArrayOfStr.length; ++j)
        {
            if (j > par2)
            {
                stringbuilder.append(" ");
            }

            String s = par1ArrayOfStr[j];

            if (par3)
            {
                String s1 = PlayerSelector.matchPlayersAsString(par0ICommandSender, s);

                if (s1 != null)
                {
                    s = s1;
                }
                else if (PlayerSelector.hasArguments(s))
                {
                    throw new PlayerNotFoundException();
                }
            }

            stringbuilder.append(s);
        }

        return stringbuilder.toString();
    }

    public static double func_110666_a(ICommandSender par0ICommandSender, double par1, String par3Str)
    {
        return func_110665_a(par0ICommandSender, par1, par3Str, -30000000, 30000000);
    }

    public static double func_110665_a(ICommandSender par0ICommandSender, double par1, String par3Str, int par4, int par5)
    {
        boolean flag = par3Str.startsWith("~");

        if (flag && Double.isNaN(par1))
        {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] {Double.valueOf(par1)});
        }
        else
        {
            double d1 = flag ? par1 : 0.0D;

            if (!flag || par3Str.length() > 1)
            {
                boolean flag1 = par3Str.contains(".");

                if (flag)
                {
                    par3Str = par3Str.substring(1);
                }

                d1 += parseDouble(par0ICommandSender, par3Str);

                if (!flag1 && !flag)
                {
                    d1 += 0.5D;
                }
            }

            if (par4 != 0 || par5 != 0)
            {
                if (d1 < (double)par4)
                {
                    throw new NumberInvalidException("commands.generic.double.tooSmall", new Object[] {Double.valueOf(d1), Integer.valueOf(par4)});
                }

                if (d1 > (double)par5)
                {
                    throw new NumberInvalidException("commands.generic.double.tooBig", new Object[] {Double.valueOf(d1), Integer.valueOf(par5)});
                }
            }

            return d1;
        }
    }

    /**
     * Joins the given string array into a "x, y, and z" seperated string.
     */
    public static String joinNiceString(Object[] par0ArrayOfObj)
    {
        StringBuilder stringbuilder = new StringBuilder();

        for (int i = 0; i < par0ArrayOfObj.length; ++i)
        {
            String s = par0ArrayOfObj[i].toString();

            if (i > 0)
            {
                if (i == par0ArrayOfObj.length - 1)
                {
                    stringbuilder.append(" and ");
                }
                else
                {
                    stringbuilder.append(", ");
                }
            }

            stringbuilder.append(s);
        }

        return stringbuilder.toString();
    }

    public static String func_96333_a(Collection par0Collection)
    {
        return joinNiceString(par0Collection.toArray(new String[par0Collection.size()]));
    }

    public static String func_110663_b(Collection par0Collection)
    {
        String[] astring = new String[par0Collection.size()];
        int i = 0;
        EntityLivingBase entitylivingbase;

        for (Iterator iterator = par0Collection.iterator(); iterator.hasNext(); astring[i++] = entitylivingbase.getTranslatedEntityName())
        {
            entitylivingbase = (EntityLivingBase)iterator.next();
        }

        return joinNiceString(astring);
    }

    /**
     * Returns true if the given substring is exactly equal to the start of the given string (case insensitive).
     */
    public static boolean doesStringStartWith(String par0Str, String par1Str)
    {
        return par1Str.regionMatches(true, 0, par0Str, 0, par0Str.length());
    }

    /**
     * Returns a List of strings (chosen from the given strings) which the last word in the given string array is a
     * beginning-match for. (Tab completion).
     */
    public static List getListOfStringsMatchingLastWord(String[] par0ArrayOfStr, String ... par1ArrayOfStr)
    {
        String s1 = par0ArrayOfStr[par0ArrayOfStr.length - 1];
        ArrayList arraylist = new ArrayList();
        String[] astring1 = par1ArrayOfStr;
        int i = par1ArrayOfStr.length;

        for (int j = 0; j < i; ++j)
        {
            String s2 = astring1[j];

            if (doesStringStartWith(s1, s2))
            {
                arraylist.add(s2);
            }
        }

        return arraylist;
    }

    /**
     * Returns a List of strings (chosen from the given string iterable) which the last word in the given string array
     * is a beginning-match for. (Tab completion).
     */
    public static List getListOfStringsFromIterableMatchingLastWord(String[] par0ArrayOfStr, Iterable par1Iterable)
    {
        String s = par0ArrayOfStr[par0ArrayOfStr.length - 1];
        ArrayList arraylist = new ArrayList();
        Iterator iterator = par1Iterable.iterator();

        while (iterator.hasNext())
        {
            String s1 = (String)iterator.next();

            if (doesStringStartWith(s, s1))
            {
                arraylist.add(s1);
            }
        }

        return arraylist;
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] par1ArrayOfStr, int par2)
    {
        return false;
    }

    public static void notifyAdmins(ICommandSender par0ICommandSender, String par1Str, Object ... par2ArrayOfObj)
    {
        notifyAdmins(par0ICommandSender, 0, par1Str, par2ArrayOfObj);
    }

    public static void notifyAdmins(ICommandSender par0ICommandSender, int par1, String par2Str, Object ... par3ArrayOfObj)
    {
        if (theAdmin != null)
        {
            theAdmin.notifyAdmins(par0ICommandSender, par1, par2Str, par3ArrayOfObj);
        }
    }

    /**
     * Sets the static IAdminCommander.
     */
    public static void setAdminCommander(IAdminCommand par0IAdminCommand)
    {
        theAdmin = par0IAdminCommand;
    }

    /**
     * Compares the name of this command to the name of the given command.
     */
    public int compareTo(ICommand par1ICommand)
    {
        return this.getCommandName().compareTo(par1ICommand.getCommandName());
    }

    public int compareTo(Object par1Obj)
    {
        return this.compareTo((ICommand)par1Obj);
    }
}
