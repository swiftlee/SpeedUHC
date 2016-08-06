package org.royalmc.uhc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.royalmc.Framework.SQLStorage;
import org.royalmc.Framework.SQLStorage.DataColumn;
import org.royalmc.util.ScoreboardUtil;
import org.royalmc.util.ScoreboardUtil.ScoreType;

public class GamePlayers {

    private static List<Player> allPlayers = new ArrayList<>();
    private static List<Player> alivePlayers = new ArrayList<>();
    private static List<Player> deadPlayers = new ArrayList<>();
    private static HashMap<Player,Integer> killTracker = new HashMap<>();
    private static HashMap<Player,Integer> winTracker = new HashMap<>();
    private static HashMap<Player,Integer> lossTracker = new HashMap<>();

    public static void cacheAllPlayers(){
    	allPlayers.clear();
    	allPlayers.addAll(Bukkit.getOnlinePlayers());
    }
    public static List<Player> getAllPlayers() {
        return allPlayers;
    }
    
    public static List<Player> getAlivePlayers() {
        return alivePlayers;
    }

    public static List<Player> getDeadPlayers() {
        return deadPlayers;
    }
    
    public static int getKills(Player player){
    	if(killTracker.get(player) != null){
    		return killTracker.get(player);
    	}
    	return 0;
    }
    
    public static void addKill(UHCMain plugin, Player player){
    	if(killTracker.get(player) != null){
    		killTracker.put(player, killTracker.get(player)+1);
    	}else{
    		killTracker.put(player, 1);
    	}
    	ScoreboardUtil.setScore(player, ScoreType.YOUR_KILLS, String.valueOf(getKills(player)), plugin);
    	
    	//Update total kills for player in database
    	SQLStorage.queryPlayerSQLData(plugin, player, DataColumn.TOTAL_KILLS, true, false, false, 0);
    }
    
    public static int getTotalWins(Player player){
    	return winTracker.get(player) != null ? winTracker.get(player) : 0;
    }
    
    public static int getTotalLosses(Player player){
    	return lossTracker.get(player) != null ? lossTracker.get(player) : 0;
    }
    
    public static void cacheWins(Player player, int wins){
    	winTracker.put(player, wins);
    	//ScoreboardUtil.setScore(player, ScoreType.GAMES_PLAYED, String.valueOf(wins+getTotalLosses(player)));
    }
    
    public static void cacheLosses(Player player, int losses){
    	lossTracker.put(player, losses);
    	//ScoreboardUtil.setScore(player, ScoreType.GAMES_PLAYED, String.valueOf(losses+getTotalWins(player)));
    }
}
