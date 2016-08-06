package org.royalmc.Framework;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.royalmc.uhc.GamePlayers;
import org.royalmc.uhc.UHCMain;

public class SQLStorage {

	public static void queryPlayerSQLData(UHCMain plugin, Player player, DataColumn column, boolean increment, boolean addCoins, boolean addCrystals, int amt){
		new BukkitRunnable(){public void run(){
			try{
				//Only perform insert/update if the value is changing
				if(increment){
					String query = "INSERT INTO {tablename} (playerUUID, {columnLabel}) VALUES ('{uuid}', 1) ON DUPLICATE KEY UPDATE {columnLabel} = {columnLabel} + 1;".replace("{tablename}", plugin.getConfig().getString("tablename")).replace("{uuid}", player.getUniqueId().toString()).replace("{columnLabel}", column.toString());
					PreparedStatement statement = plugin.getMySQL().getConnection().prepareStatement(query);
					statement.execute();
				}

				if(addCoins)
				{
					String query = "INSERT INTO {tablename} (playerUUID, TotalCoins) VALUES ('{uuid}', 1) ON DUPLICATE KEY UPDATE {columnLabel} = {columnLabel} + {amt};".replace("{tablename}", plugin.getConfig().getString("tablename")).replace("{uuid}", player.getUniqueId().toString()).replace("{columnLabel}", column.toString()).replace("{amt}", String.valueOf(amt));
					PreparedStatement statement = plugin.getMySQL().getConnection().prepareStatement(query);
					statement.execute();
				}

				if(addCrystals)
				{
					String query = "INSERT INTO {tablename} (playerUUID, TotalCrystals) VALUES ('{uuid}', 1) ON DUPLICATE KEY UPDATE {columnLabel} = {columnLabel} + {amt};".replace("{tablename}", plugin.getConfig().getString("tablename")).replace("{uuid}", player.getUniqueId().toString()).replace("{columnLabel}", column.toString()).replace("{amt}", String.valueOf(amt));
					PreparedStatement statement = plugin.getMySQL().getConnection().prepareStatement(query);
					statement.execute();
				}

				//Always query, keep the scoreboard info up-to-date
				String query = "SELECT {columnLabel} FROM {tablename} WHERE playerUUID = '{uuid}';".replace("{tablename}", plugin.getConfig().getString("tablename")).replace("{uuid}", player.getUniqueId().toString()).replace("{columnLabel}", column.toString());
				PreparedStatement statement = plugin.getMySQL().getConnection().prepareStatement(query);
				ResultSet set = statement.executeQuery();

				int value = 0;//Default to 0
				if(set.first()){//Set to first row in the set if available, else do nothing
					value = set.getInt(column.toString());
				}

				//Pass value back to the main thread and update scoreboard
				if(column==DataColumn.TOTAL_WINS){updateScoreboardTotalWins(plugin, player, value);}
				else if(column==DataColumn.TOTAL_LOSSES){updateScoreboardTotalLosses(plugin, player, value);}
				else if(column==DataColumn.TOTAL_COINS){updateScoreboardTotalCoins(plugin, player, value);}
				else if(column==DataColumn.TOTAL_CRYSTALS){updateScoreboardTotalCrystals(plugin, player, value);}
			}
			catch (NullPointerException e){
				plugin.getLogger().warning("NullPointerException: No SQL database connection, cannot process "+column+".");
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}}.runTaskAsynchronously(plugin);
	}

	//SQL table column enum
	public enum DataColumn {
		TOTAL_KILLS, TOTAL_WINS, TOTAL_LOSSES, TOTAL_COINS, TOTAL_CRYSTALS,TOTAL_DEATHS, TOTAL_DAMAGE_DEALT, TOTAL_DAMAGE_TAKEN, 
		TOTAL_FALL_DAMAGE, TOTAL_ARROWS_SHOT, TOTAL_HEARTS_HEALED, TOTAL_GOLDEN_APPLES, TOTAL_GOLDEN_HEADS,
		TOTAL_ENTITIES_TAMED, TOTAL_NETHERS_ENTERED, TOTAL_ENDS_ENTERED, TOTAL_BLOCKS_MINED, TOTAL_ENTITIES_SLAIN, DIAMONDLESS, 
		GOLDLESS, HORSELESS, RODLESS, DOUBLEORES, ACHIEVEMENTS_AVAILABLE, HEART, RAIN, FIREBALL, SPIRIT, JUMPMAN, FLAME, REKT,
		SWIMMER_ACHIEVEMENT, WALKER_ACHIEVEMENT, FIGHTER_ACHIEVEMENT, BOWKING_ACHIEVEMENT;
		@Override
		public String toString(){
			if(this==TOTAL_KILLS){return "TotalKills";}
			else if(this==TOTAL_WINS){return "TotalWins";}
			else if(this==TOTAL_LOSSES){return "TotalLosses";}
			else if(this==TOTAL_DEATHS){return "TotalDeaths";}
			else if(this==TOTAL_COINS){return "TotalCoins";}
			else if(this==TOTAL_CRYSTALS){return "TotalCrystals";}
			else if(this==TOTAL_DAMAGE_DEALT){return "TotalDamageDealt";}
			else if(this==TOTAL_DAMAGE_TAKEN){return "TotalDamageTaken";}
			else if(this==TOTAL_FALL_DAMAGE){return "TotalFallDamageTaken";}
			else if(this==TOTAL_ARROWS_SHOT){return "TotalArrowsShot";}
			else if(this==TOTAL_HEARTS_HEALED){return "TotalHeartsHealed";}
			else if(this==TOTAL_GOLDEN_APPLES){return "TotalGoldenApplesEaten";}
			else if(this==TOTAL_GOLDEN_HEADS){return "TotalGoldenHeadsEaten";}
			else if(this==TOTAL_ENTITIES_TAMED){return "TotalEntitiesTamed";}
			else if(this==TOTAL_NETHERS_ENTERED){return "TotalNethersEntered";}
			else if(this==TOTAL_ENDS_ENTERED){return "TotalEndsEntered";}
			else if(this==TOTAL_BLOCKS_MINED){return "TotalBlocksMined";}
			else if(this==TOTAL_ENTITIES_SLAIN){return "TotalEntitiesSlain";}
			else if(this==DIAMONDLESS){return "Diamondless";}
			else if(this==GOLDLESS){return "Goldless";}
			else if(this==HORSELESS){return "Horseless";}
			else if(this==RODLESS){return "Rodless";}
			else if(this==DOUBLEORES){return "DoubleOres";}
			else if(this==ACHIEVEMENTS_AVAILABLE){return "AchievementsAvailable";}
			else if(this==HEART){return "Heart";}
			else if(this==RAIN){return "Rain";}
			else if(this==FIREBALL){return "FireBall";}
			else if(this==SPIRIT){return "Spirit";}
			else if(this==JUMPMAN){return "JumpMan";}
			else if(this==FLAME){return "Flame";}
			else if(this==REKT){return "Rekt";}
			else if(this==SWIMMER_ACHIEVEMENT){return "SwimmerAchievement";}
			else if(this==WALKER_ACHIEVEMENT){return "WalkerAchievement";}
			else if(this==FIGHTER_ACHIEVEMENT){return "FighterAchievement";}
			else if(this==BOWKING_ACHIEVEMENT){return "BowKingAchievement";}
			else{return name();}
		}
	}

	//The following updater methods are called inside the async query methods in order to re-sync with the main thread.
	//The scheduled synchronous tasks will apply the new data to the player's scoreboard in a thread-safe manner.

	//Updater method

	//Updater method
	private static void updateScoreboardTotalWins(UHCMain plugin, Player player, int wins){
		new BukkitRunnable(){public void run(){
			GamePlayers.cacheWins(player, wins);

		}}.runTask(plugin);
	}
	//Updater method
	private static void updateScoreboardTotalLosses(UHCMain plugin, Player player, int losses){
		new BukkitRunnable(){public void run(){
			GamePlayers.cacheLosses(player, losses);
		}}.runTask(plugin);
	}
	private static void updateScoreboardTotalCoins(UHCMain plugin, Player player, int coins){
		new BukkitRunnable(){public void run(){
			GamePlayers.cacheLosses(player, coins);
		}}.runTask(plugin);
	}
	private static void updateScoreboardTotalCrystals(UHCMain plugin, Player player, int crystals){
		new BukkitRunnable(){public void run(){
			GamePlayers.cacheLosses(player, crystals);
		}}.runTask(plugin);
	}
}
