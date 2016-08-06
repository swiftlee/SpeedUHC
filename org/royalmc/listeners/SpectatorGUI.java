package org.royalmc.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.royalmc.uhc.UHCMain;

import static org.royalmc.util.TextUtils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpectatorGUI implements Listener
{
	private final UHCMain plugin;
	private final static String customTitle = formatText("&8Available Spectating Targets");
	public static Inventory spectatorTargetInventory = Bukkit.createInventory(null, 36, customTitle);
	public static HashMap<String,ItemStack> headMap = new HashMap<>();//Cache the heads during the lobby. When the game starts, the names will be sorted and then the inventory till be populated.
	
	public SpectatorGUI(UHCMain pl){
		this.plugin = pl;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	public static Inventory getSpectatorTargetInventory(){
		return spectatorTargetInventory;
	}
	
	public static void addSpectateTarget(Player target){
		ItemStack targetHead = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta meta = (SkullMeta)targetHead.getItemMeta();
		meta.setOwner(target.getName());
		meta.setDisplayName(formatText("&3")+target.getName());
		List<String> lore = new ArrayList<>();
		lore.add(formatText("&7Click to teleport"));
		lore.add(formatText("&7to this player."));
		meta.setLore(lore);
		targetHead.setItemMeta(meta);
		headMap.put(target.getName().toLowerCase(), targetHead);
	}
	
	public static void removeSpectateTarget(Player target){
		headMap.remove(target.getName().toLowerCase());
		for(int i = 0; i < spectatorTargetInventory.getSize(); i++){
			ItemStack stack = spectatorTargetInventory.getItem(i);
			if(stack != null && stack.getItemMeta() != null && stack.getItemMeta().getDisplayName() != null){
				if(stack.getItemMeta().getDisplayName().endsWith(target.getName())){
					spectatorTargetInventory.setItem(i, new ItemStack(Material.AIR));
				}
			}
		}
	}
	
	public static void populateInventory(){
		spectatorTargetInventory.clear();
		List<String> playerNames = new ArrayList<>(headMap.keySet());
		playerNames.sort(null);
		for(String nameKey : playerNames){
			spectatorTargetInventory.addItem(headMap.get(nameKey));
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event){
		if(event.getInventory().getTitle().equalsIgnoreCase(customTitle)){
			event.setCancelled(true);
			if(event.getClickedInventory()==event.getView().getTopInventory()){
				if(event.getCurrentItem() != null && event.getCurrentItem().getType()==Material.SKULL_ITEM){
					if(event.getCurrentItem().getItemMeta() != null && event.getCurrentItem().getItemMeta() instanceof SkullMeta){
						String targetName = ((SkullMeta)event.getCurrentItem().getItemMeta()).getOwner();
						if(targetName != null){
							Player target = Bukkit.getPlayer(targetName);
							if(target != null){
								event.getWhoClicked().sendMessage(formatText("&7 Teleporting to: " + target.getName()));
								event.getWhoClicked().closeInventory();
								Location loc = target.getLocation();
								event.getWhoClicked().teleport(loc, TeleportCause.PLUGIN);
							}else{
								event.getWhoClicked().sendMessage(formatText("&4 Unable to locate player: &f"+targetName));
							}
						}
					}
				}
			}
		}
	}
}