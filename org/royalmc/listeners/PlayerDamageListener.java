package org.royalmc.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.royalmc.Framework.GameState;
import org.royalmc.uhc.GamePlayers;
import org.royalmc.uhc.UHCMain;

public class PlayerDamageListener implements Listener {
	
	private final UHCMain plugin;
	
	public PlayerDamageListener(UHCMain plugin){
		this.plugin = plugin;	
	}
	
    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
    	Entity damager = e.getDamager();
    	if(damager instanceof Projectile && ((Projectile)damager).getShooter() instanceof Player){
    		damager = (Player)((Projectile)damager).getShooter();
    	}
    	if(GamePlayers.getDeadPlayers().contains(damager))
    	{
    		e.setCancelled(true);
    		return;
    	}
    	if(e.getEntity() instanceof Player && damager instanceof Player) {
    		Player victim = (Player) e.getEntity();
    		Player playerDamager = (Player)damager;
    		if(GameState.getState()==GameState.IN_LOBBY) {
    			e.setCancelled(true);
    			return;
    		}
    		if(victim==playerDamager){return;}
    		if(!plugin.isPVPEnabled()) {
    			e.setCancelled(true);
    			playerDamager.sendMessage(ChatColor.RED + " You cannot PvP yet.");
    			return;
    		}
    	}
    }
}
