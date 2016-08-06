package org.royalmc.Framework.Timers;

import static org.royalmc.util.TextUtils.*;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.royalmc.Framework.SQLStorage;
import org.royalmc.Framework.SQLStorage.DataColumn;
import org.royalmc.uhc.GamePlayers;
import org.royalmc.uhc.UHCMain;
import org.royalmc.util.ActionBar;
import org.royalmc.util.GameUtils;

public class GameFinishRunnable extends BukkitRunnable implements Listener {

	private final UHCMain plugin;
	int secondsLeft = 15;

	public GameFinishRunnable(UHCMain plugin){
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@Override
	public void run(){
		if(--secondsLeft > 0){
			for(Player player : Bukkit.getOnlinePlayers()){
				ActionBar.sendActionBar(player, "&7You will be sent to the lobby in &c{seconds}&7 second{s}.".replace("{seconds}", String.valueOf(secondsLeft)).replace("{s}", useS(secondsLeft)));
			}
		}else{
			for(Player player : Bukkit.getOnlinePlayers()){
				GameUtils.sendToLobbyServer(plugin, player);
			}
			Bukkit.shutdown();
		}

		if(secondsLeft == 13)
		{
			int amt = plugin.getConfig().getInt("coins.endgame");
			
			for(Player player : GamePlayers.getAlivePlayers())
			{
				SQLStorage.queryPlayerSQLData(plugin, player, DataColumn.TOTAL_COINS, false, true, false, amt);
				player.sendMessage(formatText(plugin.getConfig().getString("coins.message")).replace("{amount}", String.valueOf(amt)));
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onInventoryClick(InventoryClickEvent event){
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onInventoryDrag(InventoryDragEvent event){
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onEntityDamageByEntity(EntityDamageByEntityEvent event){
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onEntityDamage(EntityDamageEvent event){
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onBlockPlace(BlockPlaceEvent event){
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onBlockBreak(BlockBreakEvent event){
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onInteract(PlayerInteractEvent event){
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onInteractEntity(PlayerInteractEntityEvent event){
		event.setCancelled(true);
	}
}
