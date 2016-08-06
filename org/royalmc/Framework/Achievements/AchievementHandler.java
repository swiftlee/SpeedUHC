package org.royalmc.Framework.Achievements;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.NumberConversions;
import org.royalmc.Framework.GameState;
import org.royalmc.Framework.SQLStorage.DataColumn;
import org.royalmc.uhc.GamePlayers;
import org.royalmc.uhc.UHCMain;

/**
 * Created by Max604 on 22/05/2016.
 */
public class AchievementHandler implements Listener {

	String query = "";

	UHCMain plugin;

	public AchievementHandler(UHCMain plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void walker(PlayerMoveEvent e) {
		if(GameState.getState() == GameState.GRACE_PERIOD || GameState.getState() == GameState.PVP_PERIOD || GameState.getState() == GameState.DEATHMATCH)
		{
			if(GamePlayers.getAlivePlayers().contains(e.getPlayer()))
			{
				double distance = 0;
				if (e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getTo().getZ()) {
					// walking.put(e.getPlayer().getName(), //difference here);
					distance = Math.sqrt(Math.abs(NumberConversions.square(e.getFrom().getX() - e.getTo().getX())) + Math.abs(NumberConversions.square(e.getFrom().getZ() - e.getTo().getZ())));

					try
					{
						query = ("SELECT {timeStamp} FROM AData WHERE playerUUID = '{uuid}';")
								.replace("{timeStamp}", "TimeWalker")
								.replace("{uuid}", e.getPlayer().getUniqueId().toString());

						PreparedStatement st = plugin.getMySQL().getConnection().prepareStatement(query);
						ResultSet set = st.executeQuery();

						if(set.first())
						{

							int walkertime = set.getInt("TimeWalker");

							if((walkertime + 86400) <= (System.currentTimeMillis()/1000) || walkertime == 0) //Current time in seconds
							{

								query = ("INSERT INTO AData (playerUUID, {columnLabel}) VALUES ('{uuid}', {amt}) ON DUPLICATE KEY UPDATE {columnLabel} = {columnLabel} + {amt};")
										.replace("{columnLabel}", DataColumn.WALKER_ACHIEVEMENT.toString())
										.replace("{uuid}", e.getPlayer().getUniqueId().toString())
										.replace("{amt}", String.valueOf(distance));

								st = plugin.getMySQL().getConnection().prepareStatement(query);
								st.execute();

								query = ("SELECT {columnLabel} FROM AData WHERE playerUUID = '{uuid}';")
										.replace("{uuid}", e.getPlayer().getUniqueId().toString())
										.replace("{columnLabel}", DataColumn.WALKER_ACHIEVEMENT.toString());

								st = plugin.getMySQL().getConnection().prepareStatement(query);
								set = st.executeQuery();

								if(set.first())
								{
									int walked = set.getInt(DataColumn.WALKER_ACHIEVEMENT.toString());
									if(walked >= 1000)
									{
										new WalkerAchievement(plugin).reward(e.getPlayer(), plugin);

										query = ("INSERT INTO AData (playerUUID, {columnLabel}, {timeStamp}) VALUES ('{uuid}', 0, {time}) ON DUPLICATE KEY UPDATE {columnLabel} = 0, {timeStamp} = {time};")
												.replace("{columnLabel}", DataColumn.WALKER_ACHIEVEMENT.toString())
												.replace("{uuid}", e.getPlayer().getUniqueId().toString())
												.replace("{time}", String.valueOf(System.currentTimeMillis()/1000))
												.replace("{timeStamp}", "TimeWalker");

										st = plugin.getMySQL().getConnection().prepareStatement(query);
										st.execute();

									}
								}
							}
						}
					}
					catch(SQLException e1)
					{
						e1.printStackTrace();
					}
				}
			}
		}
		//TODO: Not sure how this should be handled, as this fires quite a lot.
	}

	HashMap<Player, Integer> existingTasks = new HashMap<>();	

	@EventHandler
	public void swimmer(PlayerMoveEvent e) 
	{
		if(!existingTasks.containsKey(e.getPlayer()))
		{
			if(GameState.getState() == GameState.GRACE_PERIOD || GameState.getState() == GameState.PVP_PERIOD || GameState.getState() == GameState.DEATHMATCH)
			{
				if(GamePlayers.getAlivePlayers().contains(e.getPlayer()))
				{
					new BukkitRunnable()
					{
						@Override
						public void run() 
						{
							existingTasks.put(e.getPlayer(), this.getTaskId());

							if (e.getPlayer().getLocation().getBlock().getType() == Material.WATER || e.getPlayer().getLocation().getBlock().getType() == Material.STATIONARY_WATER) 
							{
								try
								{
									query = ("SELECT {timeStamp} FROM AData WHERE playerUUID = '{uuid}';")
											.replace("{timeStamp}", "TimeSwimmer")
											.replace("{uuid}", e.getPlayer().getUniqueId().toString());

									PreparedStatement st = plugin.getMySQL().getConnection().prepareStatement(query);
									ResultSet set = st.executeQuery();

									if(set.first())
									{
										int swimmertime = set.getInt("TimeSwimmer");

										if((swimmertime + 86400) <= (System.currentTimeMillis()/1000) || swimmertime == 0) //Current time in seconds
										{
											query = ("INSERT INTO AData (playerUUID, {columnLabel}) VALUES ('{uuid}', 1) ON DUPLICATE KEY UPDATE {columnLabel} = {columnLabel} + 1;")
													.replace("{uuid}", e.getPlayer().getUniqueId().toString())
													.replace("{columnLabel}", DataColumn.SWIMMER_ACHIEVEMENT.toString());

											st = plugin.getMySQL().getConnection().prepareStatement(query);
											st.execute();

											query = ("SELECT {columnLabel} FROM AData WHERE playerUUID = '{uuid}';")
													.replace("{columnLabel}", DataColumn.SWIMMER_ACHIEVEMENT.toString())
													.replace("{uuid}", e.getPlayer().getUniqueId().toString());
											st = plugin.getMySQL().getConnection().prepareStatement(query);
											set = st.executeQuery();

											if(set.first())
											{
												int time = set.getInt(DataColumn.SWIMMER_ACHIEVEMENT.toString());

												if(time >= 180)
												{
													new SwimmerAchievement(plugin).reward(e.getPlayer(), plugin);

													query = ("INSERT INTO AData (playerUUID, {columnLabel}, {timeStamp}) VALUES ('{uuid}', 0, {time}) ON DUPLICATE KEY UPDATE {columnLabel} = 0, {timeStamp} = {time};")
															.replace("{columnLabel}", DataColumn.SWIMMER_ACHIEVEMENT.toString())
															.replace("{uuid}", e.getPlayer().getUniqueId().toString())
															.replace("{time}", String.valueOf(System.currentTimeMillis()/1000))
															.replace("{timeStamp}", "TimeSwimmer");

													st = plugin.getMySQL().getConnection().prepareStatement(query);
													st.execute();

													if(existingTasks.containsKey(e.getPlayer()))
														existingTasks.remove(e.getPlayer());

													Bukkit.getScheduler().cancelTask(this.getTaskId());
												}
											}
										}
										else
										{
											if(existingTasks.containsKey(e.getPlayer()))
												existingTasks.remove(e.getPlayer());

											Bukkit.getScheduler().cancelTask(this.getTaskId());
										}
									}
									else
									{
										if(existingTasks.containsKey(e.getPlayer()))
											existingTasks.remove(e.getPlayer());

										Bukkit.getScheduler().cancelTask(this.getTaskId());
									}
								}
								catch(SQLException e1)
								{
									if(existingTasks.containsKey(e.getPlayer()))
										existingTasks.remove(e.getPlayer());

									e1.printStackTrace();
								}
							}
							else
							{
								if(existingTasks.containsKey(e.getPlayer()))
									existingTasks.remove(e.getPlayer());

								Bukkit.getScheduler().cancelTask(this.getTaskId());
							}
						}
					}.runTaskTimer(plugin, 0, 1 * 20L);
				}
			}
		}
	}

	@EventHandler
	public void onKill(PlayerDeathEvent event) {

		if(event.getEntity().getKiller() instanceof Player)
		{
			try
			{
				query = ("SELECT {timeStamp} FROM AData WHERE playerUUID = '{uuid}';")
						.replace("{timeStamp}", "FighterTime")
						.replace("{uuid}", event.getEntity().getKiller().getUniqueId().toString());

				PreparedStatement st = plugin.getMySQL().getConnection().prepareStatement(query);
				ResultSet set = st.executeQuery();

				if(set.first())
				{
					int fightertime = set.getInt("FighterTime");

					if((fightertime + 86400) <= (System.currentTimeMillis()/1000) || fightertime == 0) //Current time in seconds
					{

						query = ("INSERT INTO AData (playerUUID, {columnLabel}) VALUES ('{uuid}', 1) ON DUPLICATE KEY UPDATE {columnLabel} = {columnLabel} + 1;")
								.replace("{uuid}", event.getEntity().getKiller().getUniqueId().toString())
								.replace("{columnLabel}", DataColumn.FIGHTER_ACHIEVEMENT.toString());

						st = plugin.getMySQL().getConnection().prepareStatement(query);
						st.execute();

						query = ("SELECT {columnLabel} FROM AData WHERE playerUUID = '{uuid}';")
								.replace("{columnLabel}", DataColumn.FIGHTER_ACHIEVEMENT.toString())
								.replace("{uuid}", event.getEntity().getKiller().getUniqueId().toString());

						st = plugin.getMySQL().getConnection().prepareStatement(query);
						set = st.executeQuery();

						if(set.first())
						{
							int k = set.getInt(DataColumn.FIGHTER_ACHIEVEMENT.toString());

							if(k >= 50)
							{
								new FighterAchievement(plugin).reward(event.getEntity().getKiller(), plugin);

								query = ("INSERT INTO AData (playerUUID, {columnLabel}, {timeStamp}) VALUES ('{uuid}', 0, {time}) ON DUPLICATE KEY UPDATE {columnLabel} = 0, {timeStamp} = {time};")
										.replace("{columnLabel}", DataColumn.FIGHTER_ACHIEVEMENT.toString())
										.replace("{uuid}", event.getEntity().getKiller().getUniqueId().toString())
										.replace("{time}", String.valueOf(System.currentTimeMillis()/1000))
										.replace("{timeStamp}", "FighterTime");

								st = plugin.getMySQL().getConnection().prepareStatement(query);
								st.execute();

							}
						}
					}
				}
			}
			catch(SQLException e1)
			{
				e1.printStackTrace();
			}
		}
	}

	@EventHandler
	public void onShoot(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		if (!(event.getDamager() instanceof Arrow)) {
			return;
		}
		if (!(((Arrow) event.getDamager()).getShooter() instanceof Player)) {
			return;
		}

		if(GameState.getState() == GameState.PVP_PERIOD || GameState.getState() == GameState.DEATHMATCH)
		{
			Player p = (Player)((Arrow) event.getDamager()).getShooter();

			try
			{
				query = ("SELECT {timeStamp} FROM AData WHERE playerUUID = '{uuid}';")
						.replace("{timeStamp}", "BowKingTime")
						.replace("{uuid}", p.getUniqueId().toString());

				PreparedStatement st = plugin.getMySQL().getConnection().prepareStatement(query);
				ResultSet set = st.executeQuery();

				if(set.first())
				{

					int bowkingtime = set.getInt("BowKingTime");

					if((bowkingtime + 86400) <= (System.currentTimeMillis()/1000) || bowkingtime == 0) //Current time in seconds
					{
						query = ("INSERT INTO AData (playerUUID, {columnLabel}) VALUES ('{uuid}', 1) ON DUPLICATE KEY UPDATE {columnLabel} = {columnLabel} + 1;")
								.replace("{columnLabel}", DataColumn.BOWKING_ACHIEVEMENT.toString())
								.replace("{uuid}", p.getUniqueId().toString());

						st = plugin.getMySQL().getConnection().prepareStatement(query);
						st.execute();

						query = ("SELECT {columnLabel} FROM AData WHERE playerUUID = '{uuid}';")
								.replace("{columnLabel}", DataColumn.BOWKING_ACHIEVEMENT.toString())
								.replace("{uuid}", p.getUniqueId().toString());
						st = plugin.getMySQL().getConnection().prepareStatement(query);
						set = st.executeQuery();

						if(set.first())
						{
							int s = set.getInt(DataColumn.BOWKING_ACHIEVEMENT.toString());
							if(s >= 100)
							{
								new BowKingAchievement(plugin).reward(p, plugin);

								query = ("INSERT INTO AData (playerUUID, {columnLabel}, {timeStamp}) VALUES ('{uuid}', 0, {time}) ON DUPLICATE KEY UPDATE {columnLabel} = 0, {timeStamp} = {time};")
										.replace("{columnLabel}", DataColumn.BOWKING_ACHIEVEMENT.toString())
										.replace("{uuid}", p.getUniqueId().toString())
										.replace("{time}", String.valueOf(System.currentTimeMillis()/1000))
										.replace("{timeStamp}", "BowKingTime");

								st = plugin.getMySQL().getConnection().prepareStatement(query);
								st.execute();
							}
						}
					}
				}
			}
			catch(SQLException e1)
			{
				e1.printStackTrace();
			}
		}
	}
}
