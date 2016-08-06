package org.royalmc.listeners;

import static org.royalmc.util.TextUtils.formatText;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.royalmc.Framework.DeadBodies;
import org.royalmc.Framework.GameType;
import org.royalmc.Framework.Hologram;
import org.royalmc.Framework.SQLStorage;
import org.royalmc.Framework.SQLStorage.DataColumn;
import org.royalmc.uhc.GamePlayers;
import org.royalmc.uhc.UHCMain;
import org.royalmc.util.GameUtils;
import org.royalmc.util.ScoreboardUtil;
import org.royalmc.util.ScoreboardUtil.ScoreType;

public class DeathEvents implements Listener 
{
	UHCMain plugin;
	public static ItemStack goldenHead = null;

	public DeathEvents(UHCMain pl) 
	{
		this.plugin = pl;
	}

	public void spawnItem(Material m, Player p, int amt)
	{
		ItemStack stack = new ItemStack(m, amt);
		p.getWorld().dropItem(p.getLocation(), stack);
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) 
	{
		//Make a shorter reference to player
		Player deadPlayer = event.getEntity();

		//Name to display for {killer} in custom death messages
		String killerName = "Unknown";

		//Name to display for {shooter} in custom death messages
		String shooterName = "Unknown";
		
		try
		{
			new DeadBodies(deadPlayer);
		} 
		catch (Exception e1) 
		{
			e1.printStackTrace();
		}

		//Start handling death message
		try{
			//HP Strings
			String killerHearts = "0.0";
			String killerHealth = "0.0";
			String shooterHearts = "0.0";
			String shooterHealth = "0.0";

			//Format the default message in-case a custom one isn't found
			if(event.getDeathMessage() != null){
				event.setDeathMessage(formatText("&a")+event.getDeathMessage().replace(deadPlayer.getName(), formatText("&3"+deadPlayer.getName()+"&a")));

				Entity damager = (Player)deadPlayer.getLastDamageCause().getEntity();

				if(damager instanceof Player)
					new Hologram(plugin, deadPlayer, formatText("&a")+event.getDeathMessage().replace(deadPlayer.getName(), deadPlayer.getName()), deadPlayer.getKiller());
				else
					new Hologram(plugin, deadPlayer, formatText("&a")+event.getDeathMessage().replace(deadPlayer.getName(), deadPlayer.getName()), deadPlayer);
			}

			//Determine the cause of death and grab the custom message config key
			EntityDamageEvent lastDamageEvent = deadPlayer.getLastDamageCause();
			String deathCauseKey = (lastDamageEvent != null && lastDamageEvent.getCause() != null ? lastDamageEvent.getCause().name() : "SUICIDE");
			killerName = deathCauseKey;
			if(lastDamageEvent != null && lastDamageEvent.getCause()==DamageCause.WITHER){deathCauseKey = "WITHER_EFFECT";}
			if(lastDamageEvent != null && lastDamageEvent instanceof EntityDamageByEntityEvent && deathCauseKey.matches("ENTITY_(ATTACK|EXPLOSION)|PROJECTILE")){
				Entity damager = ((EntityDamageByEntityEvent)lastDamageEvent).getDamager();
				killerName = damager.getName();
				if(damager.getCustomName() != null){killerName = damager.getCustomName();}
				if(damager==deadPlayer){deathCauseKey = "SUICIDE";}
				else{
					boolean shooterIsSkeleton = false;
					boolean shooterIsBlaze = false;
					if(damager instanceof Damageable){
						double damagerHealth = ((Damageable)damager).getHealth();
						NumberFormat.getInstance().setMaximumFractionDigits(1);
						killerHearts = NumberFormat.getInstance().format(damagerHealth/2);
						NumberFormat.getInstance().setMaximumFractionDigits(2);
						killerHealth = NumberFormat.getInstance().format(damagerHealth);
						if(damager instanceof Player && damager != deadPlayer){
							GamePlayers.addKill(plugin, (Player)damager);
							int amt = plugin.getConfig().getInt("crystalsPerKill");
							SQLStorage.queryPlayerSQLData(plugin, (Player)damager, DataColumn.TOTAL_CRYSTALS, false, false, true, amt);
							((Player)damager).sendMessage(formatText(plugin.getConfig().getString("crystals.killMessage")).replace("{amount}", String.valueOf(amt)));
						}
					}else if(damager instanceof Projectile){
						if(((Projectile)damager).getShooter() != null && ((Projectile)damager).getShooter() instanceof Damageable){
							Damageable shooter = (Damageable)((Projectile)damager).getShooter();

							shooterIsSkeleton = shooter.getType()==EntityType.SKELETON;
							shooterIsSkeleton = shooter.getType()==EntityType.BLAZE;

							shooterName = shooter.getName();
							if(shooter.getCustomName() != null){shooterName = shooter.getCustomName();}

							double shooterHealthValue = shooter.getHealth();
							NumberFormat.getInstance().setMaximumFractionDigits(1);
							shooterHearts = NumberFormat.getInstance().format(shooterHealthValue/2);
							NumberFormat.getInstance().setMaximumFractionDigits(2);
							shooterHealth = NumberFormat.getInstance().format(shooterHealthValue);

							if(shooter instanceof Player){
								GamePlayers.addKill(plugin, (Player)shooter);
								int amt = plugin.getConfig().getInt("crystalsPerKill");
								SQLStorage.queryPlayerSQLData(plugin, (Player)shooter, DataColumn.TOTAL_CRYSTALS, false, false, true, amt);
								((Player)shooter).sendMessage(formatText(plugin.getConfig().getString("crystals.killMessage")).replace("{amount}", String.valueOf(amt)));
							}
						}
					}
					deathCauseKey = damager.getType().name();
					if(damager.getType()==EntityType.WITHER){deathCauseKey = "WITHER_BOSS";}
					if(shooterIsSkeleton){deathCauseKey = "SKELETON";}
					if(shooterIsBlaze){deathCauseKey = "BLAZE";}
				}
			}

			//Select and display a custom death message for the given cause-key
			List<String> availableMessages = plugin.getConfig().getStringList("deathMessages."+deathCauseKey);
			if(availableMessages != null){
				if(availableMessages.isEmpty()){availableMessages.add("");}
				String customMessage = availableMessages.get(new Random().nextInt(availableMessages.size()));
				if(!customMessage.isEmpty()){
				
					event.setDeathMessage(formatText(customMessage)
							.replace("{victim}", deadPlayer.getName())
							.replace("{killer}", killerName)
							.replace("{shooter}", shooterName)
							.replace("{killerHearts}", killerHearts)
							.replace("{killerHealth}", killerHealth)
							.replace("{shooterHearts}", shooterHearts)
							.replace("{shooterHealth}", shooterHealth));
				}/*else{
					//If the custom message is an empty String, don't display a death message
					event.setDeathMessage(null);
				}*/
			}
		}catch(Exception e){e.printStackTrace();}
		//End handling death message

		//Start handling death sounds
		try{
			if(deadPlayer.getKiller() != null){
				Sound killSound = null;
				try{
					killSound = Sound.valueOf(plugin.getConfig().getString("killSound"));
				}catch(Exception e){}
				if(killSound != null){
					deadPlayer.getKiller().playSound(deadPlayer.getKiller().getLocation(), killSound, 10f, 1.3f);
				}
			}
			Sound globalKillSound = null;
			try{
				globalKillSound = Sound.valueOf(plugin.getConfig().getString("globalKillSound"));
			}catch(Exception e){}
			if(globalKillSound != null){
				for(Player p : Bukkit.getOnlinePlayers()){
					if(p != deadPlayer){
						p.playSound(p.getLocation(), globalKillSound, 10f, 1.0f);
					}
				}
			}
		}catch(Exception e){e.printStackTrace();}
		//End handling death sounds

		if(GameType.getType() != GameType.GOLDLESS)
		{
			//Create golden head item
			goldenHead = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
			SkullMeta skullMeta = (SkullMeta) goldenHead.getItemMeta();
			skullMeta.setOwner(deadPlayer.getName());
			skullMeta.setDisplayName(ChatColor.GOLD + "Golden Head");
			List<String> skullLore = new ArrayList<>();
			skullLore.add(ChatColor.AQUA + "Right-Click " + ChatColor.YELLOW + "to obtain " + ChatColor.GOLD + plugin.getConfig().getInt("heartsFromHead") + " hearts!");
			skullLore.add(ChatColor.AQUA + "Effect: " + ChatColor.GOLD + "Absorption II");
			skullMeta.setLore(skullLore);
			goldenHead.setItemMeta(skullMeta);
			event.getDrops().add(goldenHead);

			if(GameType.getType() == GameType.DIAMONDLESS)
			{
				event.getDrops().add(new ItemStack(Material.DIAMOND, 1));
			}

		}
		else
		{
			//Create golden head item
			goldenHead = new ItemStack(Material.SKULL_ITEM, 2, (short)3);
			SkullMeta skullMeta = (SkullMeta) goldenHead.getItemMeta();
			skullMeta.setOwner(deadPlayer.getName());
			skullMeta.setDisplayName(ChatColor.GOLD + "Golden Head");
			List<String> skullLore = new ArrayList<>();
			skullLore.add(ChatColor.AQUA + "Right-Click " + ChatColor.YELLOW + "to obtain " + ChatColor.GOLD + plugin.getConfig().getInt("heartsFromHead") + " hearts!");
			skullLore.add(ChatColor.AQUA + "Effect: " + ChatColor.GOLD + "Absorption II");
			skullMeta.setLore(skullLore);
			goldenHead.setItemMeta(skullMeta);
			event.getDrops().add(goldenHead);

		}
		//End handling golden-head drop

		//Start handling spectator stuff
		GamePlayers.getAlivePlayers().remove(deadPlayer);
		GamePlayers.getDeadPlayers().add(deadPlayer);
		for(Player player : Bukkit.getOnlinePlayers()){
			ScoreboardUtil.setScore(player, ScoreType.SPECTATORS, String.valueOf(GamePlayers.getDeadPlayers().size()), plugin);
			ScoreboardUtil.setScore(player, ScoreType.PLAYERS_LEFT, String.valueOf(GamePlayers.getAlivePlayers().size()), plugin);
		}
		SpectatorGUI.removeSpectateTarget(deadPlayer);

		for(Player alivePlayer : GamePlayers.getAlivePlayers()){
			alivePlayer.hidePlayer(deadPlayer);
		}
		for(Player specPlayer : GamePlayers.getDeadPlayers()){
			specPlayer.showPlayer(deadPlayer);
			deadPlayer.showPlayer(specPlayer);
		}
		deadPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));//Gives Speed II
		//String playerName = (deadPlayerTeam != null ? deadPlayerTeam.getColor() : ChatColor.WHITE)+deadPlayer.getName();
		//Title title = new Title(ChatColor.DARK_RED + "You've Died!", formatText("{player}&7 &ovs&f {killer}").replace("{player}", playerName).replace("{killer}", (killerTeam != null ? killerTeam.getColor() : ChatColor.WHITE)+(!shooterName.equals("Unknown") ? shooterName : killerName)), 1, 3, 1);
		//title.send(deadPlayer);

		new BukkitRunnable(){public void run(){
			deadPlayer.spigot().respawn();

			deadPlayer.sendMessage(formatText("&c You are now spectating."));

			deadPlayer.setGameMode(GameMode.CREATIVE);
			deadPlayer.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, true), true);
			deadPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, true), true);

			deadPlayer.getInventory().clear();
			deadPlayer.getInventory().setHelmet(goldenHead);

			deadPlayer.getInventory().setItem(0, plugin.getSpectatorCompass());

			deadPlayer.getInventory().setItem(8, plugin.getLeaveGameItem());
		}}.runTaskLater(plugin, 1);
		//End handling spectator stuff
		
		GameUtils.checkForWin(plugin);
		
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDeath(EntityDeathEvent e){
		//Don't do this stuff if the dead entity is a Player
		if(e.getEntity() instanceof Player==false){
			//Replace raw items with their cooked version
			for(int i = 0; i < e.getDrops().size(); i++){
				ItemStack oldStack = e.getDrops().get(i);
				if(oldStack.getType()==Material.RAW_BEEF){e.getDrops().set(i, new ItemStack(Material.COOKED_BEEF, oldStack.getAmount()));}
				else if(oldStack.getType()==Material.RAW_CHICKEN){e.getDrops().set(i, new ItemStack(Material.COOKED_CHICKEN, oldStack.getAmount()));}
				else if(oldStack.getType()==Material.PORK){e.getDrops().set(i, new ItemStack(Material.GRILLED_PORK, oldStack.getAmount()));}
				else if(oldStack.getType()==Material.MUTTON){e.getDrops().set(i, new ItemStack(Material.COOKED_MUTTON, oldStack.getAmount()));}
				else if(oldStack.getType()==Material.RABBIT){e.getDrops().set(i, new ItemStack(Material.COOKED_RABBIT, oldStack.getAmount()));}
			}
		}
	}
}
