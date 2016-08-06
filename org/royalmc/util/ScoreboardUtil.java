package org.royalmc.util;

import static org.royalmc.util.ScoreboardUtil.ScoreType.BORDER;
import static org.royalmc.util.ScoreboardUtil.ScoreType.GAME_TIME;
import static org.royalmc.util.ScoreboardUtil.ScoreType.PLAYERS_LEFT;
import static org.royalmc.util.ScoreboardUtil.ScoreType.SPECTATORS;
import static org.royalmc.util.ScoreboardUtil.ScoreType.YOUR_KILLS;
import static org.royalmc.util.TextUtils.formatText;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.royalmc.Framework.Timers.MatchStartRunnable;
import org.royalmc.Framework.Timers.TextAnimations;
import org.royalmc.uhc.GamePlayers;
import org.royalmc.uhc.UHCMain;

public class ScoreboardUtil {

	private static HashMap<UUID,Scoreboard> playerBoards = new HashMap<>();
	private static HashMap<UUID,Objective> playerSidebars = new HashMap<>();
	private static boolean usingGameSidebar = false;


	public static void giveNewBoard(UHCMain plugin, Player player){
		Scoreboard playerBoard = Bukkit.getScoreboardManager().getNewScoreboard();
		playerBoards.put(player.getUniqueId(),playerBoard);
		player.setScoreboard(playerBoard);
		applyHealthObjectives(playerBoard);

		Objective sidebarObjective = playerBoard.registerNewObjective("sidebarObjective", "dummy");
		sidebarObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
		new TextAnimations(plugin, false, sidebarObjective, player);

		sidebarObjective.getScore(formatText("&1")).setScore(6);
		sidebarObjective.getScore(formatText(plugin.getConfig().getString("lobby.playersOnlineColor") + "Players: &7" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers())).setScore(5);
		MatchStartRunnable.countdownOrPlayersToStart(sidebarObjective, player);
		sidebarObjective.getScore(formatText(plugin.getConfig().getString("lobby.serverColor") + "Server: &7" + plugin.getConfig().getString("serverName"))).setScore(3);
		sidebarObjective.getScore(formatText("&5")).setScore(2);

		new TextAnimations(plugin, true, sidebarObjective, player);
		playerSidebars.put(player.getUniqueId(), sidebarObjective);
	}

	public static void applyHealthObjectives(Scoreboard playerBoard){
		Objective nametagHealthObjective = playerBoard.getObjective("nametagHealthObj");
		if(nametagHealthObjective==null){nametagHealthObjective = playerBoard.registerNewObjective("nametagHealthObj", "health");}
		nametagHealthObjective.setDisplaySlot(DisplaySlot.BELOW_NAME);
		nametagHealthObjective.setDisplayName(formatText("&4\u2764"));
	}

	public static void fixHealthObjectives(){
		for(Scoreboard playerBoard : playerBoards.values()){
			applyHealthObjectives(playerBoard);
		}
	}

	public static void switchToGameSidebars(UHCMain plugin){
		usingGameSidebar = true;

		int size = plugin.getConfig().getInt("worldborders.maingame.length")/2;

		for(UUID uuidKey : playerSidebars.keySet()){
			Objective sidebarObjective = playerSidebars.get(uuidKey);
			if(sidebarObjective != null){
				HashSet<String> oldEntries = new HashSet<>(sidebarObjective.getScoreboard().getEntries());
				for(String entry : oldEntries){
						sidebarObjective.getScoreboard().resetScores(entry);
				}
				//new TextAnimations(plugin, false, sidebarObjective, Bukkit.getPlayer(UUID.fromString(uuidKey.toString())));
				sidebarObjective.getScore(formatText(GAME_TIME.toString(plugin)+"??:??")).setScore(9);
				sidebarObjective.getScore(formatText("&1")).setScore(8);
				sidebarObjective.getScore(formatText(PLAYERS_LEFT.toString(plugin)+GamePlayers.getAlivePlayers().size())).setScore(7);
				sidebarObjective.getScore(formatText("&3")).setScore(6);
				sidebarObjective.getScore(formatText(YOUR_KILLS.toString(plugin)+"0")).setScore(5);
				//sidebarObjective.getScore(formatText(TEAM_KILLS+"0")).setScore(1);
				sidebarObjective.getScore(formatText(SPECTATORS.toString(plugin)+GamePlayers.getDeadPlayers().size())).setScore(4);
				sidebarObjective.getScore(formatText(BORDER.toString(plugin) + size + "x" + size + "")).setScore(3);;
				//sidebarObjective.getScore(formatText("&3")).setScore(3);
				sidebarObjective.getScore(formatText("&2")).setScore(2);
				sidebarObjective.getScore(formatText("&7hub.royalmc.org")).setScore(1);
				//new TextAnimations(plugin, true, sidebarObjective, Bukkit.getPlayer(UUID.fromString(uuidKey.toString())));
				//}
			}
		}
	}

	/*public static void updateTabHealth(Player player){
		for(Scoreboard playerBoard : playerBoards.values()){
			org.bukkit.scoreboard.Team bukkitTeam = playerBoard.getEntryTeam(player.getName());
			if(bukkitTeam != null){
				NumberFormat.getInstance().setMaximumFractionDigits(1);
				String truncatedHealth = NumberFormat.getInstance().format(player.getHealth()/2);
				bukkitTeam.setSuffix(formatText("&8 - &4\u2764 ")+truncatedHealth);//2764 is a heart
			}
		}
	}*/

	public static void setScore(Player player, ScoreType scoreType, String value, UHCMain plugin){
		if((usingGameSidebar && (scoreType==GAME_TIME || scoreType==PLAYERS_LEFT || scoreType==YOUR_KILLS || scoreType==SPECTATORS || scoreType==BORDER)) ||
				!usingGameSidebar){
			Objective sidebarObjective = playerSidebars.get(player.getUniqueId());
			if(sidebarObjective != null){
				for(String oldEntry : sidebarObjective.getScoreboard().getEntries()){
					if(oldEntry.startsWith(scoreType.toString(plugin))){
						sidebarObjective.getScoreboard().resetScores(oldEntry);
						if(scoreType == GAME_TIME)
							sidebarObjective.getScore(scoreType.toString(plugin)+value).setScore(9);
						else if(scoreType == PLAYERS_LEFT)
							sidebarObjective.getScore(scoreType.toString(plugin)+value).setScore(7);
						else if(scoreType == YOUR_KILLS)
							sidebarObjective.getScore(scoreType.toString(plugin)+value).setScore(5);
						else if(scoreType == SPECTATORS)
							sidebarObjective.getScore(scoreType.toString(plugin)+value).setScore(4);
						else if(scoreType == BORDER)
							sidebarObjective.getScore(scoreType.toString(plugin)+value).setScore(3);
						else
							sidebarObjective.getScore(scoreType.toString(plugin)+value).setScore(1);

						return;
					}
				}
				sidebarObjective.getScore(scoreType.toString(plugin)+value).setScore(1);
			}
		}
	}

	public enum ScoreType {

		//In-Game Score Types
		GAME_TIME,
		PLAYERS_LEFT,
		YOUR_KILLS,
		SPECTATORS,
		BORDER;

		public String toString(UHCMain plugin){
			if(this==GAME_TIME)
				return formatText(plugin.getConfig().getString("scoreboard.gameTimeColor") + "Game Time: &7");
			else if(this==PLAYERS_LEFT)
				return formatText(plugin.getConfig().getString("scoreboard.playersLeftColor") + "Players Left: &7");
			else if(this==YOUR_KILLS)
				return formatText(plugin.getConfig().getString("scoreboard.yourKillsColor") +  "Your Kills: &7");
			else if(this==SPECTATORS)
				return formatText(plugin.getConfig().getString("scoreboard.spectatorsColor") +  "Spectators: &7");
			else if(this==BORDER)
				return formatText(plugin.getConfig().getString("scoreboard.borderColor") + "Border: &7");
			return null;
		}
	}
}
