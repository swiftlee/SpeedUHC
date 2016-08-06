package org.royalmc.listeners;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.royalmc.Framework.SQLStorage.DataColumn;
import org.royalmc.uhc.UHCMain;

public class StatListener implements Listener{
	
	private UHCMain plugin;
	public StatListener(UHCMain plugin){
		this.plugin = plugin;
	}
	
	// blocks mined, mobs/animals slain.
	@EventHandler
	public void onPlayerKill(EntityDeathEvent evt) {


        Entity dead = evt.getEntity();

        Entity killer = evt.getEntity().getKiller();
        		
				try {
					
					 if (dead instanceof Player && killer instanceof Player){
						 if (killer != null && dead != null){
				//	statement.executeUpdate("UPDATE "+plugin.getConfig().getString("databasename")+"."+plugin.getConfig().getString("tablename")+" SET " + DataColumn.TOTAL_KILLS + " = " + DataColumn.TOTAL_KILLS + " + 1 WHERE playerUUID = " + killer.getUniqueId());
						 }
					 }
					 
					 if (dead instanceof Player){
						 if(dead != null){
							 String query = "INSERT INTO {tablename} (playerUUID, {columnLabel}) VALUES ('{uuid}', 1) ON DUPLICATE KEY UPDATE {columnLabel} = {columnLabel} + 1;".replace("{tablename}", plugin.getConfig().getString("tablename")).replace("{uuid}", dead.getUniqueId().toString()).replace("{columnLabel}", DataColumn.TOTAL_DEATHS.toString());
								PreparedStatement statement = plugin.getMySQL().getConnection().prepareStatement(query);
								statement.execute();					 }
					 }
					 
					 } catch (SQLException e) {
					e.printStackTrace();
				}
        }
	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent evt){
		
		Entity damager = evt.getDamager();
		
		Entity damaged = evt.getEntity();
		
		try {
			
			 if (damager instanceof Player && damaged instanceof Player){
				 if (damaged != null && damager != null){
					 String query = ("INSERT INTO {tablename} (playerUUID, {columnLabel}) VALUES ('{uuid}', " + (int)evt.getDamage() + ") ON DUPLICATE KEY UPDATE {columnLabel} = {columnLabel} + " + (int)evt.getDamage() + ";").replace("{tablename}", plugin.getConfig().getString("tablename")).replace("{uuid}", damager.getUniqueId().toString()).replace("{columnLabel}", DataColumn.TOTAL_DAMAGE_DEALT.toString());
						PreparedStatement statement = plugin.getMySQL().getConnection().prepareStatement(query);
						statement.execute();
						query = ("INSERT INTO {tablename} (playerUUID, {columnLabel}) VALUES ('{uuid}', " + (int)evt.getDamage() + ") ON DUPLICATE KEY UPDATE {columnLabel} = {columnLabel} + " + (int)evt.getDamage() + ";").replace("{tablename}", plugin.getConfig().getString("tablename")).replace("{uuid}", damaged.getUniqueId().toString()).replace("{columnLabel}", DataColumn.TOTAL_DAMAGE_TAKEN.toString());
						statement = plugin.getMySQL().getConnection().prepareStatement(query);
						statement.execute();
				 }
			 }
			 
			 } catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	@EventHandler
	public void onPlayerFall(EntityDamageEvent evt){
		if(evt.getCause() == DamageCause.FALL){
			if(evt.getEntity() instanceof Player){
				try {
					String query = ("INSERT INTO {tablename} (playerUUID, {columnLabel}) VALUES ('{uuid}', " + (int)evt.getDamage() + ") ON DUPLICATE KEY UPDATE {columnLabel} = {columnLabel} + " + (int)evt.getDamage() + ";").replace("{tablename}", plugin.getConfig().getString("tablename")).replace("{uuid}", evt.getEntity().getUniqueId().toString()).replace("{columnLabel}", DataColumn.TOTAL_FALL_DAMAGE.toString());
					PreparedStatement statement = plugin.getMySQL().getConnection().prepareStatement(query);
					statement.execute();
					} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	@EventHandler
	public void onPlayerShootArrow(EntityShootBowEvent evt){
		if(evt.getEntity() instanceof Player){
			try {
				String query = "INSERT INTO {tablename} (playerUUID, {columnLabel}) VALUES ('{uuid}', 1) ON DUPLICATE KEY UPDATE {columnLabel} = {columnLabel} + 1;".replace("{tablename}", plugin.getConfig().getString("tablename")).replace("{uuid}", evt.getEntity().getUniqueId().toString()).replace("{columnLabel}", DataColumn.TOTAL_ARROWS_SHOT.toString());
				PreparedStatement statement = plugin.getMySQL().getConnection().prepareStatement(query);
				statement.execute();
				} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	@EventHandler
	public void onPlayerHeal(EntityRegainHealthEvent evt){
		if(evt.getEntity() instanceof Player){
			try {
				String query = ("INSERT INTO {tablename} (playerUUID, {columnLabel}) VALUES ('{uuid}', " + (int)evt.getAmount() + ") ON DUPLICATE KEY UPDATE {columnLabel} = {columnLabel} + " + (int)evt.getAmount() + ";").replace("{tablename}", plugin.getConfig().getString("tablename")).replace("{uuid}", evt.getEntity().getUniqueId().toString()).replace("{columnLabel}", DataColumn.TOTAL_HEARTS_HEALED.toString());
				PreparedStatement statement = plugin.getMySQL().getConnection().prepareStatement(query);
				statement.execute();
				} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	@EventHandler
	public void onAppleEat(PlayerItemConsumeEvent evt){
		if(evt.getItem().getType() == Material.GOLDEN_APPLE){
			try {
				String query = "INSERT INTO {tablename} (playerUUID, {columnLabel}) VALUES ('{uuid}', 1) ON DUPLICATE KEY UPDATE {columnLabel} = {columnLabel} + 1;".replace("{tablename}", plugin.getConfig().getString("tablename")).replace("{uuid}", evt.getPlayer().getUniqueId().toString()).replace("{columnLabel}", DataColumn.TOTAL_GOLDEN_APPLES.toString());
				PreparedStatement statement = plugin.getMySQL().getConnection().prepareStatement(query);
				statement.execute();
				} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	@EventHandler
	public void onEntityTame(EntityTameEvent evt){
		
		try {
			String query = "INSERT INTO {tablename} (playerUUID, {columnLabel}) VALUES ('{uuid}', 1) ON DUPLICATE KEY UPDATE {columnLabel} = {columnLabel} + 1;".replace("{tablename}", plugin.getConfig().getString("tablename")).replace("{uuid}", evt.getOwner().getUniqueId().toString()).replace("{columnLabel}", DataColumn.TOTAL_ENTITIES_TAMED.toString());
			PreparedStatement statement = plugin.getMySQL().getConnection().prepareStatement(query);
			statement.execute();
			} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent evt){
		if(evt.getPlayer().getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("netherName"))){
			try {
				String query = "INSERT INTO {tablename} (playerUUID, {columnLabel}) VALUES ('{uuid}', 1) ON DUPLICATE KEY UPDATE {columnLabel} = {columnLabel} + 1;".replace("{tablename}", plugin.getConfig().getString("tablename")).replace("{uuid}", evt.getPlayer().getUniqueId().toString()).replace("{columnLabel}", DataColumn.TOTAL_NETHERS_ENTERED.toString());
				PreparedStatement statement = plugin.getMySQL().getConnection().prepareStatement(query);
				statement.execute();
				} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(evt.getPlayer().getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("endName"))){
			try {
				String query = "INSERT INTO {tablename} (playerUUID, {columnLabel}) VALUES ('{uuid}', 1) ON DUPLICATE KEY UPDATE {columnLabel} = {columnLabel} + 1;".replace("{tablename}", plugin.getConfig().getString("tablename")).replace("{uuid}", evt.getPlayer().getUniqueId().toString()).replace("{columnLabel}", DataColumn.TOTAL_ENDS_ENTERED.toString());
				PreparedStatement statement = plugin.getMySQL().getConnection().prepareStatement(query);
				statement.execute();
				} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	@EventHandler
	public void onBlockMine(BlockBreakEvent evt){
		try {
			String query = "INSERT INTO {tablename} (playerUUID, {columnLabel}) VALUES ('{uuid}', 1) ON DUPLICATE KEY UPDATE {columnLabel} = {columnLabel} + 1;".replace("{tablename}", plugin.getConfig().getString("tablename")).replace("{uuid}", evt.getPlayer().getUniqueId().toString()).replace("{columnLabel}", DataColumn.TOTAL_BLOCKS_MINED.toString());
			PreparedStatement statement = plugin.getMySQL().getConnection().prepareStatement(query);
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	@EventHandler
	public void onEntityKill(EntityDeathEvent evt){
		if(evt.getEntity().getKiller() != null && evt.getEntity().getKiller() instanceof Player){
			try {
				String query = "INSERT INTO {tablename} (playerUUID, {columnLabel}) VALUES ('{uuid}', 1) ON DUPLICATE KEY UPDATE {columnLabel} = {columnLabel} + 1;".replace("{tablename}", plugin.getConfig().getString("tablename")).replace("{uuid}", evt.getEntity().getKiller().getUniqueId().toString()).replace("{columnLabel}", DataColumn.TOTAL_ENTITIES_SLAIN.toString());
				PreparedStatement statement = plugin.getMySQL().getConnection().prepareStatement(query);
				statement.execute();
				} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
