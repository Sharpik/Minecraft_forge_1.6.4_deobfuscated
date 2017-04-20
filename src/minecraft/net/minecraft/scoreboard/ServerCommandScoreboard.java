package net.minecraft.scoreboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;

public class ServerCommandScoreboard extends CommandBase
{
    public String getCommandName()
    {
        return "scoreboard";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return "commands.scoreboard.usage";
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        if (par2ArrayOfStr.length >= 1)
        {
            if (par2ArrayOfStr[0].equalsIgnoreCase("objectives"))
            {
                if (par2ArrayOfStr.length == 1)
                {
                    throw new WrongUsageException("commands.scoreboard.objectives.usage", new Object[0]);
                }

                if (par2ArrayOfStr[1].equalsIgnoreCase("list"))
                {
                    this.getObjectivesList(par1ICommandSender);
                }
                else if (par2ArrayOfStr[1].equalsIgnoreCase("add"))
                {
                    if (par2ArrayOfStr.length < 4)
                    {
                        throw new WrongUsageException("commands.scoreboard.objectives.add.usage", new Object[0]);
                    }

                    this.addObjective(par1ICommandSender, par2ArrayOfStr, 2);
                }
                else if (par2ArrayOfStr[1].equalsIgnoreCase("remove"))
                {
                    if (par2ArrayOfStr.length != 3)
                    {
                        throw new WrongUsageException("commands.scoreboard.objectives.remove.usage", new Object[0]);
                    }

                    this.removeObjective(par1ICommandSender, par2ArrayOfStr[2]);
                }
                else
                {
                    if (!par2ArrayOfStr[1].equalsIgnoreCase("setdisplay"))
                    {
                        throw new WrongUsageException("commands.scoreboard.objectives.usage", new Object[0]);
                    }

                    if (par2ArrayOfStr.length != 3 && par2ArrayOfStr.length != 4)
                    {
                        throw new WrongUsageException("commands.scoreboard.objectives.setdisplay.usage", new Object[0]);
                    }

                    this.setObjectivesDisplay(par1ICommandSender, par2ArrayOfStr, 2);
                }

                return;
            }

            if (par2ArrayOfStr[0].equalsIgnoreCase("players"))
            {
                if (par2ArrayOfStr.length == 1)
                {
                    throw new WrongUsageException("commands.scoreboard.players.usage", new Object[0]);
                }

                if (par2ArrayOfStr[1].equalsIgnoreCase("list"))
                {
                    if (par2ArrayOfStr.length > 3)
                    {
                        throw new WrongUsageException("commands.scoreboard.players.list.usage", new Object[0]);
                    }

                    this.listPlayers(par1ICommandSender, par2ArrayOfStr, 2);
                }
                else if (par2ArrayOfStr[1].equalsIgnoreCase("add"))
                {
                    if (par2ArrayOfStr.length != 5)
                    {
                        throw new WrongUsageException("commands.scoreboard.players.add.usage", new Object[0]);
                    }

                    this.setPlayerScore(par1ICommandSender, par2ArrayOfStr, 2);
                }
                else if (par2ArrayOfStr[1].equalsIgnoreCase("remove"))
                {
                    if (par2ArrayOfStr.length != 5)
                    {
                        throw new WrongUsageException("commands.scoreboard.players.remove.usage", new Object[0]);
                    }

                    this.setPlayerScore(par1ICommandSender, par2ArrayOfStr, 2);
                }
                else if (par2ArrayOfStr[1].equalsIgnoreCase("set"))
                {
                    if (par2ArrayOfStr.length != 5)
                    {
                        throw new WrongUsageException("commands.scoreboard.players.set.usage", new Object[0]);
                    }

                    this.setPlayerScore(par1ICommandSender, par2ArrayOfStr, 2);
                }
                else
                {
                    if (!par2ArrayOfStr[1].equalsIgnoreCase("reset"))
                    {
                        throw new WrongUsageException("commands.scoreboard.players.usage", new Object[0]);
                    }

                    if (par2ArrayOfStr.length != 3)
                    {
                        throw new WrongUsageException("commands.scoreboard.players.reset.usage", new Object[0]);
                    }

                    this.resetPlayerScore(par1ICommandSender, par2ArrayOfStr, 2);
                }

                return;
            }

            if (par2ArrayOfStr[0].equalsIgnoreCase("teams"))
            {
                if (par2ArrayOfStr.length == 1)
                {
                    throw new WrongUsageException("commands.scoreboard.teams.usage", new Object[0]);
                }

                if (par2ArrayOfStr[1].equalsIgnoreCase("list"))
                {
                    if (par2ArrayOfStr.length > 3)
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.list.usage", new Object[0]);
                    }

                    this.getTeamList(par1ICommandSender, par2ArrayOfStr, 2);
                }
                else if (par2ArrayOfStr[1].equalsIgnoreCase("add"))
                {
                    if (par2ArrayOfStr.length < 3)
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.add.usage", new Object[0]);
                    }

                    this.addTeam(par1ICommandSender, par2ArrayOfStr, 2);
                }
                else if (par2ArrayOfStr[1].equalsIgnoreCase("remove"))
                {
                    if (par2ArrayOfStr.length != 3)
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.remove.usage", new Object[0]);
                    }

                    this.removeTeam(par1ICommandSender, par2ArrayOfStr, 2);
                }
                else if (par2ArrayOfStr[1].equalsIgnoreCase("empty"))
                {
                    if (par2ArrayOfStr.length != 3)
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.empty.usage", new Object[0]);
                    }

                    this.emptyTeam(par1ICommandSender, par2ArrayOfStr, 2);
                }
                else if (par2ArrayOfStr[1].equalsIgnoreCase("join"))
                {
                    if (par2ArrayOfStr.length < 4 && (par2ArrayOfStr.length != 3 || !(par1ICommandSender instanceof EntityPlayer)))
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.join.usage", new Object[0]);
                    }

                    this.joinTeam(par1ICommandSender, par2ArrayOfStr, 2);
                }
                else if (par2ArrayOfStr[1].equalsIgnoreCase("leave"))
                {
                    if (par2ArrayOfStr.length < 3 && !(par1ICommandSender instanceof EntityPlayer))
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.leave.usage", new Object[0]);
                    }

                    this.leaveTeam(par1ICommandSender, par2ArrayOfStr, 2);
                }
                else
                {
                    if (!par2ArrayOfStr[1].equalsIgnoreCase("option"))
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.usage", new Object[0]);
                    }

                    if (par2ArrayOfStr.length != 4 && par2ArrayOfStr.length != 5)
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
                    }

                    this.setTeamOption(par1ICommandSender, par2ArrayOfStr, 2);
                }

                return;
            }
        }

        throw new WrongUsageException("commands.scoreboard.usage", new Object[0]);
    }

    protected Scoreboard getScoreboardFromWorldServer()
    {
        return MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
    }

    /**
     * User-safe version of Scoreboard.getObjective, does checks against the objective not being found and whether it's
     * read-only or not. If true, the second parameter makes the function throw an exception if the objective is read-
     * only.
     */
    protected ScoreObjective getScoreObjective(String par1Str, boolean par2)
    {
        Scoreboard scoreboard = this.getScoreboardFromWorldServer();
        ScoreObjective scoreobjective = scoreboard.getObjective(par1Str);

        if (scoreobjective == null)
        {
            throw new CommandException("commands.scoreboard.objectiveNotFound", new Object[] {par1Str});
        }
        else if (par2 && scoreobjective.getCriteria().isReadOnly())
        {
            throw new CommandException("commands.scoreboard.objectiveReadOnly", new Object[] {par1Str});
        }
        else
        {
            return scoreobjective;
        }
    }

    /**
     * Returns the ScorePlayerTeam for the given team name.
     */
    protected ScorePlayerTeam getTeam(String par1Str)
    {
        Scoreboard scoreboard = this.getScoreboardFromWorldServer();
        ScorePlayerTeam scoreplayerteam = scoreboard.func_96508_e(par1Str);

        if (scoreplayerteam == null)
        {
            throw new CommandException("commands.scoreboard.teamNotFound", new Object[] {par1Str});
        }
        else
        {
            return scoreplayerteam;
        }
    }

    /**
     * Handler for the 'scoreboard objectives add' command.
     */
    protected void addObjective(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3)
    {
        String s = par2ArrayOfStr[par3++];
        String s1 = par2ArrayOfStr[par3++];
        Scoreboard scoreboard = this.getScoreboardFromWorldServer();
        ScoreObjectiveCriteria scoreobjectivecriteria = (ScoreObjectiveCriteria)ScoreObjectiveCriteria.field_96643_a.get(s1);

        if (scoreobjectivecriteria == null)
        {
            String[] astring1 = (String[])ScoreObjectiveCriteria.field_96643_a.keySet().toArray(new String[0]);
            throw new WrongUsageException("commands.scoreboard.objectives.add.wrongType", new Object[] {joinNiceString(astring1)});
        }
        else if (scoreboard.getObjective(s) != null)
        {
            throw new CommandException("commands.scoreboard.objectives.add.alreadyExists", new Object[] {s});
        }
        else if (s.length() > 16)
        {
            throw new SyntaxErrorException("commands.scoreboard.objectives.add.tooLong", new Object[] {s, Integer.valueOf(16)});
        }
        else if (s.length() == 0)
        {
            throw new WrongUsageException("commands.scoreboard.objectives.add.usage", new Object[0]);
        }
        else
        {
            if (par2ArrayOfStr.length > par3)
            {
                String s2 = func_82360_a(par1ICommandSender, par2ArrayOfStr, par3);

                if (s2.length() > 32)
                {
                    throw new SyntaxErrorException("commands.scoreboard.objectives.add.displayTooLong", new Object[] {s2, Integer.valueOf(32)});
                }

                if (s2.length() > 0)
                {
                    scoreboard.func_96535_a(s, scoreobjectivecriteria).setDisplayName(s2);
                }
                else
                {
                    scoreboard.func_96535_a(s, scoreobjectivecriteria);
                }
            }
            else
            {
                scoreboard.func_96535_a(s, scoreobjectivecriteria);
            }

            notifyAdmins(par1ICommandSender, "commands.scoreboard.objectives.add.success", new Object[] {s});
        }
    }

    /**
     * Handler for the 'scoreboard teams add' command.
     */
    protected void addTeam(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3)
    {
        String s = par2ArrayOfStr[par3++];
        Scoreboard scoreboard = this.getScoreboardFromWorldServer();

        if (scoreboard.func_96508_e(s) != null)
        {
            throw new CommandException("commands.scoreboard.teams.add.alreadyExists", new Object[] {s});
        }
        else if (s.length() > 16)
        {
            throw new SyntaxErrorException("commands.scoreboard.teams.add.tooLong", new Object[] {s, Integer.valueOf(16)});
        }
        else if (s.length() == 0)
        {
            throw new WrongUsageException("commands.scoreboard.teams.add.usage", new Object[0]);
        }
        else
        {
            if (par2ArrayOfStr.length > par3)
            {
                String s1 = func_82360_a(par1ICommandSender, par2ArrayOfStr, par3);

                if (s1.length() > 32)
                {
                    throw new SyntaxErrorException("commands.scoreboard.teams.add.displayTooLong", new Object[] {s1, Integer.valueOf(32)});
                }

                if (s1.length() > 0)
                {
                    scoreboard.createTeam(s).setTeamName(s1);
                }
                else
                {
                    scoreboard.createTeam(s);
                }
            }
            else
            {
                scoreboard.createTeam(s);
            }

            notifyAdmins(par1ICommandSender, "commands.scoreboard.teams.add.success", new Object[] {s});
        }
    }

    /**
     * Handler for the 'scoreboard teams option' command.
     */
    protected void setTeamOption(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3)
    {
        ScorePlayerTeam scoreplayerteam = this.getTeam(par2ArrayOfStr[par3++]);
        String s = par2ArrayOfStr[par3++].toLowerCase();

        if (!s.equalsIgnoreCase("color") && !s.equalsIgnoreCase("friendlyfire") && !s.equalsIgnoreCase("seeFriendlyInvisibles"))
        {
            throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
        }
        else if (par2ArrayOfStr.length == 4)
        {
            if (s.equalsIgnoreCase("color"))
            {
                throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] {s, func_96333_a(EnumChatFormatting.func_96296_a(true, false))});
            }
            else if (!s.equalsIgnoreCase("friendlyfire") && !s.equalsIgnoreCase("seeFriendlyInvisibles"))
            {
                throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
            }
            else
            {
                throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] {s, func_96333_a(Arrays.asList(new String[]{"true", "false"}))});
            }
        }
        else
        {
            String s1 = par2ArrayOfStr[par3++];

            if (s.equalsIgnoreCase("color"))
            {
                EnumChatFormatting enumchatformatting = EnumChatFormatting.func_96300_b(s1);

                if (s1 == null)
                {
                    throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] {s, func_96333_a(EnumChatFormatting.func_96296_a(true, false))});
                }

                scoreplayerteam.setNamePrefix(enumchatformatting.toString());
                scoreplayerteam.setNameSuffix(EnumChatFormatting.RESET.toString());
            }
            else if (s.equalsIgnoreCase("friendlyfire"))
            {
                if (!s1.equalsIgnoreCase("true") && !s1.equalsIgnoreCase("false"))
                {
                    throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] {s, func_96333_a(Arrays.asList(new String[]{"true", "false"}))});
                }

                scoreplayerteam.setAllowFriendlyFire(s1.equalsIgnoreCase("true"));
            }
            else if (s.equalsIgnoreCase("seeFriendlyInvisibles"))
            {
                if (!s1.equalsIgnoreCase("true") && !s1.equalsIgnoreCase("false"))
                {
                    throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] {s, func_96333_a(Arrays.asList(new String[]{"true", "false"}))});
                }

                scoreplayerteam.setSeeFriendlyInvisiblesEnabled(s1.equalsIgnoreCase("true"));
            }

            notifyAdmins(par1ICommandSender, "commands.scoreboard.teams.option.success", new Object[] {s, scoreplayerteam.func_96661_b(), s1});
        }
    }

    /**
     * Handler for the 'scoreboard teams remove' command.
     */
    protected void removeTeam(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3)
    {
        Scoreboard scoreboard = this.getScoreboardFromWorldServer();
        ScorePlayerTeam scoreplayerteam = this.getTeam(par2ArrayOfStr[par3++]);
        scoreboard.func_96511_d(scoreplayerteam);
        notifyAdmins(par1ICommandSender, "commands.scoreboard.teams.remove.success", new Object[] {scoreplayerteam.func_96661_b()});
    }

    /**
     * Handler for the 'scoreboard teams list' command.
     */
    protected void getTeamList(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3)
    {
        Scoreboard scoreboard = this.getScoreboardFromWorldServer();

        if (par2ArrayOfStr.length > par3)
        {
            ScorePlayerTeam scoreplayerteam = this.getTeam(par2ArrayOfStr[par3++]);
            Collection collection = scoreplayerteam.getMembershipCollection();

            if (collection.size() <= 0)
            {
                throw new CommandException("commands.scoreboard.teams.list.player.empty", new Object[] {scoreplayerteam.func_96661_b()});
            }

            par1ICommandSender.sendChatToPlayer(ChatMessageComponent.createFromTranslationWithSubstitutions("commands.scoreboard.teams.list.player.count", new Object[] {Integer.valueOf(collection.size()), scoreplayerteam.func_96661_b()}).setColor(EnumChatFormatting.DARK_GREEN));
            par1ICommandSender.sendChatToPlayer(ChatMessageComponent.createFromText(joinNiceString(collection.toArray())));
        }
        else
        {
            Collection collection1 = scoreboard.func_96525_g();

            if (collection1.size() <= 0)
            {
                throw new CommandException("commands.scoreboard.teams.list.empty", new Object[0]);
            }

            par1ICommandSender.sendChatToPlayer(ChatMessageComponent.createFromTranslationWithSubstitutions("commands.scoreboard.teams.list.count", new Object[] {Integer.valueOf(collection1.size())}).setColor(EnumChatFormatting.DARK_GREEN));
            Iterator iterator = collection1.iterator();

            while (iterator.hasNext())
            {
                ScorePlayerTeam scoreplayerteam1 = (ScorePlayerTeam)iterator.next();
                par1ICommandSender.sendChatToPlayer(ChatMessageComponent.createFromTranslationWithSubstitutions("commands.scoreboard.teams.list.entry", new Object[] {scoreplayerteam1.func_96661_b(), scoreplayerteam1.func_96669_c(), Integer.valueOf(scoreplayerteam1.getMembershipCollection().size())}));
            }
        }
    }

    /**
     * Handler for the 'scoreboard teams join' command.
     */
    protected void joinTeam(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3)
    {
        Scoreboard scoreboard = this.getScoreboardFromWorldServer();
        ScorePlayerTeam scoreplayerteam = scoreboard.func_96508_e(par2ArrayOfStr[par3++]);
        HashSet hashset = new HashSet();
        String s;

        if (par1ICommandSender instanceof EntityPlayer && par3 == par2ArrayOfStr.length)
        {
            s = getCommandSenderAsPlayer(par1ICommandSender).getEntityName();
            scoreboard.addPlayerToTeam(s, scoreplayerteam);
            hashset.add(s);
        }
        else
        {
            while (par3 < par2ArrayOfStr.length)
            {
                s = func_96332_d(par1ICommandSender, par2ArrayOfStr[par3++]);
                scoreboard.addPlayerToTeam(s, scoreplayerteam);
                hashset.add(s);
            }
        }

        if (!hashset.isEmpty())
        {
            notifyAdmins(par1ICommandSender, "commands.scoreboard.teams.join.success", new Object[] {Integer.valueOf(hashset.size()), scoreplayerteam.func_96661_b(), joinNiceString(hashset.toArray(new String[0]))});
        }
    }

    /**
     * Handler for the 'scoreboard teams leave' command.
     */
    protected void leaveTeam(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3)
    {
        Scoreboard scoreboard = this.getScoreboardFromWorldServer();
        HashSet hashset = new HashSet();
        HashSet hashset1 = new HashSet();
        String s;

        if (par1ICommandSender instanceof EntityPlayer && par3 == par2ArrayOfStr.length)
        {
            s = getCommandSenderAsPlayer(par1ICommandSender).getEntityName();

            if (scoreboard.removePlayerFromTeams(s))
            {
                hashset.add(s);
            }
            else
            {
                hashset1.add(s);
            }
        }
        else
        {
            while (par3 < par2ArrayOfStr.length)
            {
                s = func_96332_d(par1ICommandSender, par2ArrayOfStr[par3++]);

                if (scoreboard.removePlayerFromTeams(s))
                {
                    hashset.add(s);
                }
                else
                {
                    hashset1.add(s);
                }
            }
        }

        if (!hashset.isEmpty())
        {
            notifyAdmins(par1ICommandSender, "commands.scoreboard.teams.leave.success", new Object[] {Integer.valueOf(hashset.size()), joinNiceString(hashset.toArray(new String[0]))});
        }

        if (!hashset1.isEmpty())
        {
            throw new CommandException("commands.scoreboard.teams.leave.failure", new Object[] {Integer.valueOf(hashset1.size()), joinNiceString(hashset1.toArray(new String[0]))});
        }
    }

    /**
     * Handler for the 'scoreboard teams empty' command.
     */
    protected void emptyTeam(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3)
    {
        Scoreboard scoreboard = this.getScoreboardFromWorldServer();
        ScorePlayerTeam scoreplayerteam = this.getTeam(par2ArrayOfStr[par3++]);
        ArrayList arraylist = new ArrayList(scoreplayerteam.getMembershipCollection());

        if (arraylist.isEmpty())
        {
            throw new CommandException("commands.scoreboard.teams.empty.alreadyEmpty", new Object[] {scoreplayerteam.func_96661_b()});
        }
        else
        {
            Iterator iterator = arraylist.iterator();

            while (iterator.hasNext())
            {
                String s = (String)iterator.next();
                scoreboard.removePlayerFromTeam(s, scoreplayerteam);
            }

            notifyAdmins(par1ICommandSender, "commands.scoreboard.teams.empty.success", new Object[] {Integer.valueOf(arraylist.size()), scoreplayerteam.func_96661_b()});
        }
    }

    /**
     * Handler for the 'scoreboard objectives remove' command.
     */
    protected void removeObjective(ICommandSender par1ICommandSender, String par2Str)
    {
        Scoreboard scoreboard = this.getScoreboardFromWorldServer();
        ScoreObjective scoreobjective = this.getScoreObjective(par2Str, false);
        scoreboard.func_96519_k(scoreobjective);
        notifyAdmins(par1ICommandSender, "commands.scoreboard.objectives.remove.success", new Object[] {par2Str});
    }

    /**
     * Handler for the 'scoreboard objectives list' command.
     */
    protected void getObjectivesList(ICommandSender par1ICommandSender)
    {
        Scoreboard scoreboard = this.getScoreboardFromWorldServer();
        Collection collection = scoreboard.getScoreObjectives();

        if (collection.size() <= 0)
        {
            throw new CommandException("commands.scoreboard.objectives.list.empty", new Object[0]);
        }
        else
        {
            par1ICommandSender.sendChatToPlayer(ChatMessageComponent.createFromTranslationWithSubstitutions("commands.scoreboard.objectives.list.count", new Object[] {Integer.valueOf(collection.size())}).setColor(EnumChatFormatting.DARK_GREEN));
            Iterator iterator = collection.iterator();

            while (iterator.hasNext())
            {
                ScoreObjective scoreobjective = (ScoreObjective)iterator.next();
                par1ICommandSender.sendChatToPlayer(ChatMessageComponent.createFromTranslationWithSubstitutions("commands.scoreboard.objectives.list.entry", new Object[] {scoreobjective.getName(), scoreobjective.getDisplayName(), scoreobjective.getCriteria().func_96636_a()}));
            }
        }
    }

    /**
     * Handler for the 'scoreboard objectives setdisplay' command.
     */
    protected void setObjectivesDisplay(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3)
    {
        Scoreboard scoreboard = this.getScoreboardFromWorldServer();
        String s = par2ArrayOfStr[par3++];
        int j = Scoreboard.getObjectiveDisplaySlotNumber(s);
        ScoreObjective scoreobjective = null;

        if (par2ArrayOfStr.length == 4)
        {
            scoreobjective = this.getScoreObjective(par2ArrayOfStr[par3++], false);
        }

        if (j < 0)
        {
            throw new CommandException("commands.scoreboard.objectives.setdisplay.invalidSlot", new Object[] {s});
        }
        else
        {
            scoreboard.func_96530_a(j, scoreobjective);

            if (scoreobjective != null)
            {
                notifyAdmins(par1ICommandSender, "commands.scoreboard.objectives.setdisplay.successSet", new Object[] {Scoreboard.getObjectiveDisplaySlot(j), scoreobjective.getName()});
            }
            else
            {
                notifyAdmins(par1ICommandSender, "commands.scoreboard.objectives.setdisplay.successCleared", new Object[] {Scoreboard.getObjectiveDisplaySlot(j)});
            }
        }
    }

    /**
     * Handler for the 'scoreboard players list' command.
     */
    protected void listPlayers(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3)
    {
        Scoreboard scoreboard = this.getScoreboardFromWorldServer();

        if (par2ArrayOfStr.length > par3)
        {
            String s = func_96332_d(par1ICommandSender, par2ArrayOfStr[par3++]);
            Map map = scoreboard.func_96510_d(s);

            if (map.size() <= 0)
            {
                throw new CommandException("commands.scoreboard.players.list.player.empty", new Object[] {s});
            }

            par1ICommandSender.sendChatToPlayer(ChatMessageComponent.createFromTranslationWithSubstitutions("commands.scoreboard.players.list.player.count", new Object[] {Integer.valueOf(map.size()), s}).setColor(EnumChatFormatting.DARK_GREEN));
            Iterator iterator = map.values().iterator();

            while (iterator.hasNext())
            {
                Score score = (Score)iterator.next();
                par1ICommandSender.sendChatToPlayer(ChatMessageComponent.createFromTranslationWithSubstitutions("commands.scoreboard.players.list.player.entry", new Object[] {Integer.valueOf(score.getScorePoints()), score.func_96645_d().getDisplayName(), score.func_96645_d().getName()}));
            }
        }
        else
        {
            Collection collection = scoreboard.getObjectiveNames();

            if (collection.size() <= 0)
            {
                throw new CommandException("commands.scoreboard.players.list.empty", new Object[0]);
            }

            par1ICommandSender.sendChatToPlayer(ChatMessageComponent.createFromTranslationWithSubstitutions("commands.scoreboard.players.list.count", new Object[] {Integer.valueOf(collection.size())}).setColor(EnumChatFormatting.DARK_GREEN));
            par1ICommandSender.sendChatToPlayer(ChatMessageComponent.createFromText(joinNiceString(collection.toArray())));
        }
    }

    /**
     * Handler for the 'scoreboard players [add|remove|set]' commands.
     */
    protected void setPlayerScore(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3)
    {
        String s = par2ArrayOfStr[par3 - 1];
        String s1 = func_96332_d(par1ICommandSender, par2ArrayOfStr[par3++]);
        ScoreObjective scoreobjective = this.getScoreObjective(par2ArrayOfStr[par3++], true);
        int j = s.equalsIgnoreCase("set") ? parseInt(par1ICommandSender, par2ArrayOfStr[par3++]) : parseIntWithMin(par1ICommandSender, par2ArrayOfStr[par3++], 1);
        Scoreboard scoreboard = this.getScoreboardFromWorldServer();
        Score score = scoreboard.func_96529_a(s1, scoreobjective);

        if (s.equalsIgnoreCase("set"))
        {
            score.func_96647_c(j);
        }
        else if (s.equalsIgnoreCase("add"))
        {
            score.func_96649_a(j);
        }
        else
        {
            score.func_96646_b(j);
        }

        notifyAdmins(par1ICommandSender, "commands.scoreboard.players.set.success", new Object[] {scoreobjective.getName(), s1, Integer.valueOf(score.getScorePoints())});
    }

    /**
     * Handler for the 'scoreboard players reset' command.
     */
    protected void resetPlayerScore(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3)
    {
        Scoreboard scoreboard = this.getScoreboardFromWorldServer();
        String s = func_96332_d(par1ICommandSender, par2ArrayOfStr[par3++]);
        scoreboard.func_96515_c(s);
        notifyAdmins(par1ICommandSender, "commands.scoreboard.players.reset.success", new Object[] {s});
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        if (par2ArrayOfStr.length == 1)
        {
            return getListOfStringsMatchingLastWord(par2ArrayOfStr, new String[] {"objectives", "players", "teams"});
        }
        else
        {
            if (par2ArrayOfStr[0].equalsIgnoreCase("objectives"))
            {
                if (par2ArrayOfStr.length == 2)
                {
                    return getListOfStringsMatchingLastWord(par2ArrayOfStr, new String[] {"list", "add", "remove", "setdisplay"});
                }

                if (par2ArrayOfStr[1].equalsIgnoreCase("add"))
                {
                    if (par2ArrayOfStr.length == 4)
                    {
                        return getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr, ScoreObjectiveCriteria.field_96643_a.keySet());
                    }
                }
                else if (par2ArrayOfStr[1].equalsIgnoreCase("remove"))
                {
                    if (par2ArrayOfStr.length == 3)
                    {
                        return getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr, this.getScoreObjectivesList(false));
                    }
                }
                else if (par2ArrayOfStr[1].equalsIgnoreCase("setdisplay"))
                {
                    if (par2ArrayOfStr.length == 3)
                    {
                        return getListOfStringsMatchingLastWord(par2ArrayOfStr, new String[] {"list", "sidebar", "belowName"});
                    }

                    if (par2ArrayOfStr.length == 4)
                    {
                        return getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr, this.getScoreObjectivesList(false));
                    }
                }
            }
            else if (par2ArrayOfStr[0].equalsIgnoreCase("players"))
            {
                if (par2ArrayOfStr.length == 2)
                {
                    return getListOfStringsMatchingLastWord(par2ArrayOfStr, new String[] {"set", "add", "remove", "reset", "list"});
                }

                if (!par2ArrayOfStr[1].equalsIgnoreCase("set") && !par2ArrayOfStr[1].equalsIgnoreCase("add") && !par2ArrayOfStr[1].equalsIgnoreCase("remove"))
                {
                    if ((par2ArrayOfStr[1].equalsIgnoreCase("reset") || par2ArrayOfStr[1].equalsIgnoreCase("list")) && par2ArrayOfStr.length == 3)
                    {
                        return getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr, this.getScoreboardFromWorldServer().getObjectiveNames());
                    }
                }
                else
                {
                    if (par2ArrayOfStr.length == 3)
                    {
                        return getListOfStringsMatchingLastWord(par2ArrayOfStr, MinecraftServer.getServer().getAllUsernames());
                    }

                    if (par2ArrayOfStr.length == 4)
                    {
                        return getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr, this.getScoreObjectivesList(true));
                    }
                }
            }
            else if (par2ArrayOfStr[0].equalsIgnoreCase("teams"))
            {
                if (par2ArrayOfStr.length == 2)
                {
                    return getListOfStringsMatchingLastWord(par2ArrayOfStr, new String[] {"add", "remove", "join", "leave", "empty", "list", "option"});
                }

                if (par2ArrayOfStr[1].equalsIgnoreCase("join"))
                {
                    if (par2ArrayOfStr.length == 3)
                    {
                        return getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr, this.getScoreboardFromWorldServer().func_96531_f());
                    }

                    if (par2ArrayOfStr.length >= 4)
                    {
                        return getListOfStringsMatchingLastWord(par2ArrayOfStr, MinecraftServer.getServer().getAllUsernames());
                    }
                }
                else
                {
                    if (par2ArrayOfStr[1].equalsIgnoreCase("leave"))
                    {
                        return getListOfStringsMatchingLastWord(par2ArrayOfStr, MinecraftServer.getServer().getAllUsernames());
                    }

                    if (!par2ArrayOfStr[1].equalsIgnoreCase("empty") && !par2ArrayOfStr[1].equalsIgnoreCase("list") && !par2ArrayOfStr[1].equalsIgnoreCase("remove"))
                    {
                        if (par2ArrayOfStr[1].equalsIgnoreCase("option"))
                        {
                            if (par2ArrayOfStr.length == 3)
                            {
                                return getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr, this.getScoreboardFromWorldServer().func_96531_f());
                            }

                            if (par2ArrayOfStr.length == 4)
                            {
                                return getListOfStringsMatchingLastWord(par2ArrayOfStr, new String[] {"color", "friendlyfire", "seeFriendlyInvisibles"});
                            }

                            if (par2ArrayOfStr.length == 5)
                            {
                                if (par2ArrayOfStr[3].equalsIgnoreCase("color"))
                                {
                                    return getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr, EnumChatFormatting.func_96296_a(true, false));
                                }

                                if (par2ArrayOfStr[3].equalsIgnoreCase("friendlyfire") || par2ArrayOfStr[3].equalsIgnoreCase("seeFriendlyInvisibles"))
                                {
                                    return getListOfStringsMatchingLastWord(par2ArrayOfStr, new String[] {"true", "false"});
                                }
                            }
                        }
                    }
                    else if (par2ArrayOfStr.length == 3)
                    {
                        return getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr, this.getScoreboardFromWorldServer().func_96531_f());
                    }
                }
            }

            return null;
        }
    }

    /**
     * If the parameter is true, does not return read-only entries.
     */
    protected List getScoreObjectivesList(boolean par1)
    {
        Collection collection = this.getScoreboardFromWorldServer().getScoreObjectives();
        ArrayList arraylist = new ArrayList();
        Iterator iterator = collection.iterator();

        while (iterator.hasNext())
        {
            ScoreObjective scoreobjective = (ScoreObjective)iterator.next();

            if (!par1 || !scoreobjective.getCriteria().isReadOnly())
            {
                arraylist.add(scoreobjective.getName());
            }
        }

        return arraylist;
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] par1ArrayOfStr, int par2)
    {
        return par1ArrayOfStr[0].equalsIgnoreCase("players") ? par2 == 2 : (!par1ArrayOfStr[0].equalsIgnoreCase("teams") ? false : par2 == 2 || par2 == 3);
    }
}
