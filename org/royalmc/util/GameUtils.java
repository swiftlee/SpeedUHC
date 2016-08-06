package org.royalmc.util;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.royalmc.Framework.GameState;
import org.royalmc.Framework.SQLStorage;
import org.royalmc.uhc.GamePlayers;
import org.royalmc.uhc.UHCMain;
import org.royalmc.Framework.SQLStorage.DataColumn;
import org.royalmc.Framework.Timers.GameFinishRunnable;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import static org.royalmc.util.TextUtils.formatText;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameUtils {

	private static final Random random = new Random();
	private static List<Location> initialSpawnLocations = new ArrayList<>();
	private static List<Location> deathmatchSpawnLocations = new ArrayList<>();

	public static Location getRandomLocation(World world, int maxRadiusFromCenter){
		int x = random.nextInt((maxRadiusFromCenter*2) + 1) - maxRadiusFromCenter;
		int z = random.nextInt((maxRadiusFromCenter*2) + 1) - maxRadiusFromCenter;
		int y = world.getHighestBlockYAt(x, z) + 1;
		return new Location(world, x, y, z);
	}

	private static void populateInitialTeleportLocations(UHCMain plugin, World world){
		int maxRadiusFromCenter = (int)plugin.getConfig().getDouble("worldborders.maingame.length")/2;
		initialSpawnLocations.clear();
		for(int i = 0; i < 16; i++){
			initialSpawnLocations.add(getRandomLocation(world, maxRadiusFromCenter));
		}
	}

	private static void populateDeathmatchTeleportLocations(UHCMain plugin, World world){
		int maxRadiusFromCenter = (int)plugin.getConfig().getDouble("worldborders.deathmatch.endlength")/2;
		deathmatchSpawnLocations.clear();
		for(int i = 0; i < Bukkit.getOnlinePlayers().size(); i++){
			deathmatchSpawnLocations.add(getRandomLocation(world, maxRadiusFromCenter));
		}
	}

	public static void teleportTeamsToIntitialLocations(UHCMain plugin, World world){
		int maxRadiusFromCenter = (int)plugin.getConfig().getDouble("worldborders.maingame.length")/2;
		for(Player team : Bukkit.getOnlinePlayers()){
			Location loc = null;
			if(initialSpawnLocations.size() > 0){loc = initialSpawnLocations.remove(0);}
			else{loc = getRandomLocation(world, maxRadiusFromCenter);}
			loc = world.getHighestBlockAt(loc.getBlockX(), loc.getBlockZ()).getLocation().add(0.5,1,0.5);

			team.teleport(loc, TeleportCause.PLUGIN);
			playTeleportSound(team, plugin);
		}
	}

	public static void teleportTeamsToDeathmatchLocations(UHCMain plugin, World world){
		int maxRadiusFromCenter = (int)plugin.getConfig().getDouble("worldborders.deathmatch.endlength")/2;
		Location loc = null;
		if(deathmatchSpawnLocations.size() > 0){loc = deathmatchSpawnLocations.remove(0);}
		else{loc = getRandomLocation(world, maxRadiusFromCenter);}
		loc = world.getHighestBlockAt(loc.getBlockX(), loc.getBlockZ()).getLocation().add(0.5,1,0.5);
		for(Player player : Bukkit.getOnlinePlayers()){
			player.teleport(loc, TeleportCause.PLUGIN);
			playTeleportSound(player, plugin);
		}
	}

	public static void generateInitialLocationChunks(UHCMain plugin){
		if(initialSpawnLocations.size()==0){
			populateInitialTeleportLocations(plugin, Bukkit.getWorld("world"));
		}
		for(int i = 0; i < initialSpawnLocations.size(); i++){
			Location loc = initialSpawnLocations.get(i);
			new BukkitRunnable(){public void run(){
				int originX = loc.getChunk().getX();
				int originZ = loc.getChunk().getZ();
				for(int x = -2; x <= 2; x++){
					for(int z = -2; z <= 2; z++){
						loc.getWorld().getChunkAt(originX+x, originZ+z).load(true);
					}
				}
			}}.runTaskLater(plugin, i*60);//Schedules latest generation at 60 seconds if there's 16 locations
		}
	}

	public static void generateDeathmatchLocationChunks(UHCMain plugin){
		if(deathmatchSpawnLocations.size()==0){
			populateDeathmatchTeleportLocations(plugin, Bukkit.getWorld("world"));
		}
		for(int i = 0; i < deathmatchSpawnLocations.size(); i++){
			Location loc = deathmatchSpawnLocations.get(i);
			new BukkitRunnable(){public void run(){
				loc.getChunk().load(true);
			}}.runTaskLater(plugin, i*60);//Schedules latest generation at 60 seconds if there's 16 locations
		}
	}

	/*public static void teleportRandomLocation(UHCMain plugin, Player player, World world, int xMin, int xMax, int zMin, int zMax) {
		Random r = new Random();
		int x = r.nextInt(xMax - xMin + 1) + xMin;
		int z = r.nextInt(zMax - zMin + 1) + zMin;
		int y = world.getHighestBlockYAt(x, z) + 1;
		Location rLoc = new Location(world, x, y, z);
		player.teleport(rLoc);
		playTeleportSound(player, plugin);
	}*/

	/*public static void teleportRandomLocation(UHCMain plugin, Team team, World world, int xMin, int xMax, int zMin, int zMax) {
		Random r = new Random();
		int x = r.nextInt(xMax - xMin + 1) + xMin;
		int z = r.nextInt(zMax - zMin + 1) + zMin;
		int y = world.getHighestBlockYAt(x, z) + 1;
		Location rLoc = new Location(world, x, y, z);
		for(Player player : team.getMembers()) {
			player.teleport(rLoc);
			playTeleportSound(player, plugin);
		}
	}*/

	public static void playTeleportSound(Player player, UHCMain plugin){
		Sound teleportSound = null;
		try{
			teleportSound = Sound.valueOf(plugin.getConfig().getString("sounds.teleport"));
		}catch(Exception e){
			teleportSound = Sound.ENDERMAN_TELEPORT;
		}
		player.getWorld().playSound(player.getLocation(), teleportSound, 1, 1);

	}
	
	
	static Player lastPlayer = null;
	public static void checkForWin(UHCMain plugin){
		
		if(GameState.getState() != GameState.IN_LOBBY && GameState.getState() != GameState.GAME_OVER){
			if(GamePlayers.getAlivePlayers().size()<=1){
				//Game is over cancel all previous tasks
				Bukkit.getScheduler().cancelTasks(plugin);

				String randomColors = "&e&k|&b&k|&a&k|&c&k|&d&k|&e&k|&b&k|&a&k|&c&k|&d&k|";
				
				for(Player p : GamePlayers.getAlivePlayers())
				{
					lastPlayer = p;
					break;
				}
				
				Bukkit.broadcastMessage(formatText((randomColors + "&r &5Game Over! &f{player}&7 has won! " + randomColors).replace("{player}", lastPlayer.getDisplayName())));
				GameState.setState(GameState.GAME_OVER);

				String winTitle = plugin.getConfig().getString("winnerTitleText", "");
				String winSubtitle = plugin.getConfig().getString("winnerSubtitleText", "");

					try{
						SQLStorage.queryPlayerSQLData(plugin, lastPlayer, DataColumn.TOTAL_WINS, true, false, false, 0);
						int fadeInTime = plugin.getConfig().getInt("endGameTile.fadeIn");
						int stayTime = plugin.getConfig().getInt("endGameTitle.stayTime");
						int fadeOutTime = plugin.getConfig().getInt("endGameTitle.fadeOut");
						new Title(winTitle, winSubtitle.replace("{kills}", String.valueOf(GamePlayers.getKills(lastPlayer))), fadeInTime, stayTime, fadeOutTime).send(lastPlayer);
							new BukkitRunnable(){public void run(){
									launchRandomFirework(plugin, lastPlayer.getLocation());

							}}.runTaskTimer(plugin, 0, 20);
					}catch(Exception e){e.printStackTrace();}

				for(Player loser : GamePlayers.getAllPlayers()){
					SQLStorage.queryPlayerSQLData(plugin, loser, DataColumn.TOTAL_LOSSES, true, false, false, 0);
				}

				WorldBorder border = Bukkit.getWorld("world").getWorldBorder();
				if(border != null){
					border.setSize(border.getSize());
				}

				//Start end-game countdown
				new GameFinishRunnable(plugin).runTaskTimer(plugin, 0, 20);
			}
		}
	}

	public static void sendToLobbyServer(UHCMain plugin, Player player){
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(plugin.getConfig().getString("hubName"));
		player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}

	public static void launchRandomFirework(UHCMain plugin, Location location){
		Firework firework = (Firework)location.getWorld().spawnEntity(location, org.bukkit.entity.EntityType.FIREWORK);
		FireworkMeta fireworkMeta = firework.getFireworkMeta();
		Type type = Type.values()[random.nextInt(Type.values().length)];
		Color mainColor = Color.fromRGB(random.nextInt(256), random.nextInt(256), random.nextInt(256));//colors.get(random.nextInt(colors.size()));
		Color secondaryColor = Color.fromRGB(random.nextInt(256), random.nextInt(256), random.nextInt(256));//colors.get(random.nextInt(colors.size()));
		FireworkEffect effect = FireworkEffect.builder().flicker(random.nextBoolean()).withColor(mainColor).withFade(secondaryColor).with(type).trail(random.nextBoolean()).build();
		fireworkMeta.addEffect(effect);
		fireworkMeta.setPower(1+random.nextInt(2));
		firework.setFireworkMeta(fireworkMeta);
		//Bukkit.getScheduler().runTaskLater(plugin, new Runnable(){public void run(){
		//    firework.detonate();
		//}}, 1);
	}
}
