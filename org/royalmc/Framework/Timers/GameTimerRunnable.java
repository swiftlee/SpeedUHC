package org.royalmc.Framework.Timers;

import static org.royalmc.util.TextUtils.formatText;
import static org.royalmc.util.TextUtils.formatTime;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.royalmc.Framework.GameState;
import org.royalmc.Framework.WorldBorders;
import org.royalmc.uhc.CarePackage;
import org.royalmc.uhc.GamePlayers;
import org.royalmc.uhc.UHCMain;
import org.royalmc.util.ActionBar;
import org.royalmc.util.GameUtils;
import org.royalmc.util.ScoreboardUtil;
import org.royalmc.util.Title;
import org.royalmc.util.ScoreboardUtil.ScoreType;

public class GameTimerRunnable extends BukkitRunnable {

	private final UHCMain plugin;
	long deathmatchSeconds = 1200;
	long pvpSeconds = 600;

	public GameTimerRunnable(UHCMain plugin){
		this.plugin = plugin;
		deathmatchSeconds = plugin.getDeathmatchTimeLimit();
		pvpSeconds = plugin.getPvPTimeLimit();
	}

	@Override
	public void run(){
		
		if(deathmatchSeconds > 0){deathmatchSeconds--;}
		if(pvpSeconds > 0){pvpSeconds--;}
		else if(pvpSeconds==0){
			GameState.setState(GameState.PVP_PERIOD);
			GameUtils.generateDeathmatchLocationChunks(plugin);
			pvpSeconds--;
		}

		String pvpTimeString = null;
		if(pvpSeconds <= 0){
			pvpTimeString = "&aEnabled";
		}else{
			pvpTimeString = "&f"+formatTime(pvpSeconds);
		}
		String gameTime = formatTime(deathmatchSeconds);
		for(Player player : Bukkit.getOnlinePlayers()){
			ScoreboardUtil.setScore(player, ScoreType.GAME_TIME, gameTime, plugin);
		}
		String actionMessage = formatText("&7&lDeathmatch: &f"+gameTime+"     &7&lPvP: "+pvpTimeString);
		for(Player player : Bukkit.getOnlinePlayers()){
			ActionBar.sendActionBar(player, actionMessage);
		}

		if(Bukkit.getWorld("world").getWorldBorder().getSize() >= plugin.getConfig().getDouble("deathmatchsize"))
		{
			if(pvpSeconds == (plugin.getPvPTimeLimit()/2))
			{
				new WorldBorders(plugin).shrinkWorldBorders();
			}
			else if(pvpSeconds == (plugin.getPvPTimeLimit()/4))
			{
				new WorldBorders(plugin).shrinkWorldBorders();
			}
		}

		if(deathmatchSeconds == (plugin.getDeathmatchTimeLimit()/2))
		{
			new WorldBorders(plugin).shrinkWorldBorders();
		}
		else if(deathmatchSeconds == (plugin.getDeathmatchTimeLimit()/4))
		{
			new WorldBorders(plugin).shrinkWorldBorders();
		}


		if(deathmatchSeconds <= 5){
			for(Player player : Bukkit.getOnlinePlayers()){
				if(GamePlayers.getAlivePlayers().contains(player))
				{
					new Title("&1"+deathmatchSeconds, "", 0, 1, 0).broadcast();
					plugin.playDeathmatchCountdownSound(player);
					if(deathmatchSeconds == 5){
						player.removePotionEffect(PotionEffectType.SPEED);
						player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*5, 0, true), true);
					}else if(deathmatchSeconds == 0){
						player.setWalkSpeed(0.2f);
					}
				}
				else
				{
					new Title("&1"+deathmatchSeconds, "", 0, 1, 0).broadcast();
					plugin.playDeathmatchCountdownSound(player);
				}
			}
		}
		if(deathmatchSeconds==0){
			cancel();
			GameState.setState(GameState.DEATHMATCH);
			new Title("", "&4&oDeathmatch!", 0, 1, 0).broadcast();
			GameUtils.teleportTeamsToDeathmatchLocations(plugin, Bukkit.getWorld("world"));
			for(Player p : GamePlayers.getDeadPlayers())
			{
				Location loc = new Location(p.getWorld(), 0, p.getWorld().getHighestBlockAt(0,0).getLocation().getY(), 0);
				p.teleport(loc);
			}

			World world = Bukkit.getWorld("world");

			int maxRadiusFromCenter = (int)plugin.getConfig().getDouble("worldborders.deathmatch.endlength")/2;
			Location loc = null;
			loc = GameUtils.getRandomLocation(world, maxRadiusFromCenter);
			loc = world.getHighestBlockAt(loc.getBlockX(), loc.getBlockZ()).getLocation().add(0.5,1,0.5);

			for(int i = 0; i < 25; i++)
			{
				new CarePackage(plugin, loc);
			}
			
			new DeathmatchRunnable(plugin).runTaskTimer(plugin, 0, 20);
		}
	}
}
