package org.royalmc.Framework.Timers;

import static org.royalmc.util.TextUtils.formatText;
import static org.royalmc.util.TextUtils.useS;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Objective;
import org.royalmc.Framework.GameState;
import org.royalmc.Framework.GameType;
import org.royalmc.Framework.PluginMessaging;
import org.royalmc.Framework.VotingSystem;
import org.royalmc.Framework.WorldBorders;
import org.royalmc.listeners.SpectatorGUI;
import org.royalmc.uhc.CarePackage;
import org.royalmc.uhc.GamePlayers;
import org.royalmc.uhc.UHCMain;
import org.royalmc.util.ActionBar;
import org.royalmc.util.GameUtils;
import org.royalmc.util.ScoreboardUtil;
import org.royalmc.util.Title;

public class MatchStartRunnable extends BukkitRunnable implements Listener {

	private static UHCMain plugin;
	public static long secondsLeft = 60;

	public MatchStartRunnable(UHCMain plugin){
		MatchStartRunnable.plugin = plugin;
		secondsLeft = plugin.getLobbyTimeLimit();
	}

	public static void createInitialEntry(Objective o, String text, HashMap<Player, Boolean> map, Player p)
	{
		if(map.containsKey(p))
		{
			if(map.get(p) == false)
			{
				o.getScore(formatText(text)).setScore(4);
				map.replace(p, true);
			}
		}
		else
			return;
	}

	static String str = "";
	@SuppressWarnings("deprecation")
	public static void countdownOrPlayersToStart(Objective o, Player p)
	{
		Map<Player, BukkitTask> tasks = new HashMap<>();
		HashMap<Player, Boolean>created = new HashMap<>();
		created.put(p, false);

		BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, new BukkitRunnable()
		{	
			public void run()
			{

				try
				{
					for(String oldEntry : o.getScoreboard().getEntries())
					{
						if(oldEntry.startsWith(formatText(plugin.getConfig().getString("lobby.playersOnlineColor") + "Players")))
						{
							o.getScoreboard().resetScores(oldEntry);
							o.getScore(formatText(plugin.getConfig().getString("lobby.playersOnlineColor") + "Players: &7" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers())).setScore(5);
						}
					}
				}
				catch(Exception e)
				{

				}

				if(Bukkit.getOnlinePlayers().size() >= plugin.getMinimumPlayers())
				{
					str = plugin.getConfig().getString("lobby.countdownColor") + "Start: &7" + secondsLeft + "s";

					createInitialEntry(o, str, created, p);

					try
					{
						for(String oldEntry : o.getScoreboard().getEntries())
						{	
							if(oldEntry.startsWith(formatText(plugin.getConfig().getString("lobby.countdownColor") + "Start")) || oldEntry.startsWith(formatText(plugin.getConfig().getString("lobby.playersToStartColor") + "Waiting")))
							{
								o.getScoreboard().resetScores(oldEntry);
								o.getScore(formatText(str)).setScore(4);
							} 
						}
					}
					catch(Exception e)
					{

					}
				}
				else
				{
					str = plugin.getConfig().getString("lobby.playersToStartColor") + "Waiting: &7" + (plugin.getMinimumPlayers() - Bukkit.getOnlinePlayers().size() + " players");

					createInitialEntry(o, str, created, p);

					try
					{
						for(String oldEntry : o.getScoreboard().getEntries())
						{	
							if(oldEntry.startsWith(formatText(plugin.getConfig().getString("lobby.playersToStartColor") + "Waiting")) || oldEntry.startsWith(formatText(plugin.getConfig().getString("lobby.countdownColor") + "Start")))
							{
								o.getScoreboard().resetScores(oldEntry);
								o.getScore(formatText(str)).setScore(4);
							} 
						}
					}
					catch(Exception e)
					{

					}
				}

				if(secondsLeft <= 1)
				{
					tasks.get(p).cancel();
					tasks.remove(p);
				}
			}
		}, 0, 20L);

		if(!tasks.containsKey(p))
			tasks.put(p, task);
	}

	public GameType getMostVoted()
	{

		int diamondless = VotingSystem.diamondlessPoll.size();
		int goldless = VotingSystem.goldlessPoll.size();
		int horseless = VotingSystem.horselessPoll.size();
		int rodless = VotingSystem.rodlessPoll.size();
		int none = VotingSystem.nonePoll.size();
		int doubleOres = VotingSystem.doubleOresPoll.size();

		HashMap<GameType, Integer> sizes = new HashMap<>();
		sizes.put(GameType.DIAMONDLESS, diamondless);
		sizes.put(GameType.GOLDLESS, goldless);
		sizes.put(GameType.HORSELESS, horseless);
		sizes.put(GameType.RODLESS, rodless);
		sizes.put(GameType.NONE, none);
		sizes.put(GameType.DOUBLEORES, doubleOres);

		int maxValue = Collections.max(sizes.values());

		if(maxValue == 0)
		{
			return GameType.NONE;
		}
		if(maxValue >= plugin.getConfig().getInt("votesNeeded"))
		{
			for(Entry<GameType, Integer> e : sizes.entrySet())
			{
				if(e.getValue() == maxValue)
				{
					return e.getKey();
				}
			}
		}

		sizes.clear();
		return GameType.NONE;

	}

	@Override
	public void run(){
		if(Bukkit.getOnlinePlayers().size() >= plugin.getMinimumPlayers()){
			//Player requirement met, decrement timer
			secondsLeft--;
			if(secondsLeft==0){
				//Time to start the match
					new PluginMessaging(plugin, false);//Have enough teams, players have been assigned, disable joining

				//Teleport players randomly within the play-area
				World world = Bukkit.getWorld("world");
				GameUtils.teleportTeamsToIntitialLocations(plugin, world);
				world.setTime(0);
				world.setStorm(false);

				GameType.setType(getMostVoted());

				for(Player p : Bukkit.getOnlinePlayers())
				{
					p.sendMessage(formatText(plugin.getConfig().getString("gameModeSelected")).replace("{gamemode}", getMostVoted().toString()));
				}

				VotingSystem.diamondlessPoll.clear();
				VotingSystem.goldlessPoll.clear();
				VotingSystem.horselessPoll.clear();
				VotingSystem.rodlessPoll.clear();
				VotingSystem.nonePoll.clear();

				Bukkit.getScheduler().runTaskLater(plugin, new Runnable() 
				{
					public void run() 
					{
						//Apply the world border to the world
						new WorldBorders(plugin).setWorldBorders();
					}
				}, 20*10);

				//End the lobby timer
				cancel();

				//Update GameState
				GameState.setState(GameState.GRACE_PERIOD);

				//Display the rules
				@SuppressWarnings("unused")
				BukkitTask doRules = new RulesRunnable(plugin).runTaskTimer(plugin, 6, 10);

				//Add blindness and give cooked pork
				ItemStack grilledPork = new ItemStack(Material.GRILLED_PORK);
				grilledPork.setAmount(10);
				Bukkit.getPluginManager().registerEvents(this, plugin);
				for(Player player : Bukkit.getOnlinePlayers()){
					player.getInventory().clear();
					player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*5, 0, true), true);
					player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*5, 0, true), true);
					player.getInventory().setBoots(plugin.getSpecialBoots());
					player.getInventory().addItem(grilledPork);
				}
				new BukkitRunnable(){public void run(){
					HandlerList.unregisterAll(MatchStartRunnable.this);
					for(Player player : GamePlayers.getAlivePlayers()){
						player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, plugin.getConfig().getInt("bootSpeed"), true), true);
						player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, plugin.getConfig().getInt("hasteSpeed"), true), true);
						player.setHealth(player.getMaxHealth()-1);
						player.setFoodLevel(20);
						player.setSaturation(20);
						player.setExhaustion(0);
					}
					new BukkitRunnable(){public void run(){
						ScoreboardUtil.fixHealthObjectives();
					}}.runTaskLater(plugin, 10);//Fix Player health displays
				}}.runTaskLater(plugin, 20*5);//After 5 second debuffs, give speed and allow breaking

				//new OverheadHologram(plugin);
				ScoreboardUtil.switchToGameSidebars(plugin);

				SpectatorGUI.populateInventory();

				//Set everyone into the 'alive' list
				//GamePlayers.getAlivePlayers().addAll(Bukkit.getOnlinePlayers());

				//Cache all players for win/loss distribution at the end
				GamePlayers.cacheAllPlayers();
				
				
				int maxRadiusFromCenter = (int)plugin.getConfig().getDouble("worldborders.maingame.length")/2;
				Location loc = null;
				loc = GameUtils.getRandomLocation(world, maxRadiusFromCenter);
				loc = world.getHighestBlockAt(loc.getBlockX(), loc.getBlockZ()).getLocation().add(0.5,1,0.5);
				for(int i = 0; i < 25; i++)
				{
					new CarePackage(plugin, loc);
				}

				//Start the match timer
				new GameTimerRunnable(plugin).runTaskTimer(plugin, 0, 20);
				new TipsRunnable(plugin).runTaskTimer(plugin, (plugin.getConfig().getInt("tipsDelay")*20)/2, plugin.getConfig().getInt("tipsDelay")*20);
			}else if(secondsLeft <= 10){
				int fadeInTime = plugin.getConfig().getInt("startMatchTitle.fadeIn");
				int stayTime = (int)plugin.getConfig().getDouble("startMatchTitle.stayTime");
				int fadeOutTime = plugin.getConfig().getInt("startMatchTitle.fadeOut");
				new Title(formatText(getSecondsLeftColor()+secondsLeft), "", fadeInTime, stayTime, fadeOutTime).broadcast();
				for(Player player : Bukkit.getOnlinePlayers()){
					plugin.playLobbyCountdownSound(player, (secondsLeft+4)/10f);
				}
			}else{
				String colorSwitchColor = getSecondsLeftColor();
				String chatMessage = plugin.getMsgLobbyChatTimer().replace("{seconds}", String.valueOf(secondsLeft)).replace("{colorSwitch}", colorSwitchColor);
				String actionBarMessage = plugin.getMsgLobbyActionTimer().replace("{seconds}", String.valueOf(secondsLeft)).replace("{colorSwitch}", colorSwitchColor);
				for(Player player : Bukkit.getOnlinePlayers()){
					ActionBar.sendActionBar(player, actionBarMessage);
					if(secondsLeft==20 || secondsLeft==15 || secondsLeft==10 || (plugin.getLobbyTimeLimit() >= 60 && secondsLeft==plugin.getLobbyTimeLimit()/2))
					{
						player.sendMessage(chatMessage);
					}
				}
			}
		}else{
			//Not enough players/teams in lobby
			secondsLeft = plugin.getLobbyTimeLimit();
			String message = plugin.getMsgLobbyNeedsPlayers().replace("{count}", String.valueOf(getPlayerDifference())).replace("{s}", useS(getPlayerDifference()));
			for(Player player : Bukkit.getOnlinePlayers()){
				ActionBar.sendActionBar(player, message);
			}
		}
	}

	private int getPlayerDifference(){
		return plugin.getMinimumPlayers() - Bukkit.getOnlinePlayers().size();
	}

	private String getSecondsLeftColor(){
		if(secondsLeft <= plugin.getConfig().getInt("lobbyCountdownColorSwitch.threshold")){
			return formatText(plugin.getConfig().getString("lobbyCountdownColorSwitch.colorBelowThreshold"));
		}
		return formatText(plugin.getConfig().getString("lobbyCountdownColorSwitch.colorAboveThreshold"));
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onBlockBreak(BlockBreakEvent event){
		event.setCancelled(true);
		event.getPlayer().sendMessage(formatText("&4 You cannot break stuff yet!"));
	}

	@EventHandler(priority = EventPriority.LOWEST)
	private void onSuffocate(EntityDamageEvent event){
		if(event.getCause()==DamageCause.SUFFOCATION || event.getCause()==DamageCause.LAVA){
			Block highestBlock = event.getEntity().getWorld().getHighestBlockAt(event.getEntity().getLocation());
			if(event.getCause()==DamageCause.LAVA){
				highestBlock.setType(Material.COBBLESTONE);
			}
			event.setCancelled(true);
			event.getEntity().teleport(highestBlock.getLocation().add(0.5,1,0.5), TeleportCause.PLUGIN);
		}
	}
}
