package org.royalmc.listeners;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.royalmc.uhc.UHCMain;

public class ChatDisable implements Listener{
	
	private int seconds;
	private boolean hasEnded;
	private String silenceMessage;//message sent to player notifying of new silence
	
	
	public ChatDisable(UHCMain plugin, int seconds, String message){
		this.seconds = seconds;
		this.silenceMessage = message;
		
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		
		doCountDown(plugin);
		
		Bukkit.getServer().broadcastMessage(ChatColor.RED + silenceMessage);
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent evt){
		if(!hasEnded){
			evt.setCancelled(true);
			evt.getPlayer().sendMessage(ChatColor.RED + "You may not chat for another " + ChatColor.BOLD + seconds + ChatColor.RESET + ChatColor.RED + " seconds.");
		}
	}
	
	private void doCountDown(UHCMain plugin){
		 new BukkitRunnable() {
		        
	            @Override
	            public void run() {
	            		this.cancel();
	            		hasEnded = true;
	            		Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "Chat is now enabled!");
	            }
	            
	        }.runTaskLater(plugin, seconds*20);	
	}
}
