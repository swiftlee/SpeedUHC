package org.royalmc.Framework.Timers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.scheduler.BukkitRunnable;
import org.royalmc.uhc.UHCMain;

public class DeathmatchRunnable extends BukkitRunnable implements Listener {

	
	//UHCMain plugin;
	
	public DeathmatchRunnable(UHCMain plugin){
		//this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		new BukkitRunnable(){public void run(){
			org.bukkit.event.HandlerList.unregisterAll(DeathmatchRunnable.this);
		}}.runTaskLater(plugin, 20*5);
	}
	
	@Override
	public void run(){}
	
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
