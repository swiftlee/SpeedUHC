package org.royalmc.listeners;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.royalmc.Framework.GameState;
import org.royalmc.Framework.SQLStorage.DataColumn;
import org.royalmc.uhc.GamePlayers;
import org.royalmc.uhc.UHCMain;
import org.royalmc.util.GameUtils;

public class PlayerInteractListener implements Listener {
	
	private final UHCMain plugin;
	
	public PlayerInteractListener(UHCMain plugin){
		this.plugin = plugin;
	} 
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if(GameState.getState()==GameState.IN_LOBBY || GamePlayers.getDeadPlayers().contains(p)){
			event.setCancelled(true);
		}
		if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			ItemStack item = p.getItemInHand();
			if(item != null){
				if(item.getType()==Material.COMPASS && GamePlayers.getDeadPlayers().contains(p)) {
					event.setCancelled(true);
					p.openInventory(SpectatorGUI.getSpectatorTargetInventory());
				}
				if(item.getType()==Material.BED && (GameState.getState()==GameState.IN_LOBBY || GamePlayers.getDeadPlayers().contains(p)))
				{
					event.setCancelled(true);
					GameUtils.sendToLobbyServer(plugin, p);
				}
				if(item.getType()==Material.SKULL_ITEM && item.hasItemMeta()) {
					event.setCancelled(true);
					item.setAmount(item.getAmount()-1);
					if(item.getAmount() < 1){
						item.setType(Material.AIR);
					}
					p.setItemInHand(item);
					double hearts = plugin.getConfig().getDouble("heartsFromHead") * 2;
					double heartsAdded = (p.getHealth() <= ((double)((p.getMaxHealth() - hearts))) ? hearts : (p.getMaxHealth() - p.getHealth()));
					p.setHealth(p.getHealth() + (heartsAdded));
					p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20*60*2, plugin.getConfig().getInt("absorptionFromHead"), true), true);
					p.sendMessage(ChatColor.GREEN + "You have used your " + ChatColor.GOLD + "Golden Head" + ChatColor.GREEN + "!");
					
					
					 try {
					       String query = "INSERT INTO {tablename} (playerUUID, {columnLabel}) VALUES ('{uuid}', 1) ON DUPLICATE KEY UPDATE {columnLabel} = {columnLabel} + 1;".replace("{tablename}", plugin.getConfig().getString("tablename")).replace("{uuid}", p.getUniqueId().toString()).replace("{columnLabel}", DataColumn.TOTAL_GOLDEN_HEADS.toString());
					       PreparedStatement statement = plugin.getMySQL().getConnection().prepareStatement(query);
					       statement.execute(); 
					       } catch (SQLException e) {
					           // TODO Auto-generated catch block
					           e.printStackTrace();
					          }
					
				}
			}
		}
	}
}


