package org.royalmc.Framework;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.royalmc.Framework.SQLStorage.DataColumn;
import org.royalmc.uhc.UHCMain;

public class ParticlesINV implements Listener
{

	UHCMain plugin;
	
	public ParticlesINV(UHCMain plugin)
	{
		this.plugin = plugin;
	}
	
	static ItemStack Slot_1;
	static ItemStack Slot_2;
	static ItemStack Slot_3;
	static ItemStack Slot_4;
	static ItemStack Slot_5;
	static ItemStack Slot_6;
	static ItemStack Slot_7;
	static ItemStack Slot_8;
	static ItemStack Slot_9;
	static ItemStack GUI_Item;

	static Inventory PINV;

	static HashMap<String,String> PlayerSelection = new HashMap<String,String>(); //PLAYERNAME, INV SLOT (USE THIS IN DEATHEVENTS TO PLAY PARTICLES)

	@SuppressWarnings("deprecation")
	public static void AddSlots()
	{
		PINV = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', Particles_Config.Particles.getString("ParticlesINV_Name")));

		Slot_1 = new ItemStack(Material.getMaterial(Particles_Config.Particles.getInt("Particle.1")));                 
		Slot_2 = new ItemStack(Material.getMaterial(Particles_Config.Particles.getInt("Particle.2")));  
		Slot_3 = new ItemStack(Material.getMaterial(Particles_Config.Particles.getInt("Particle.3")));  
		Slot_4 = new ItemStack(Material.getMaterial(Particles_Config.Particles.getInt("Particle.4")));  
		Slot_5 = new ItemStack(Material.getMaterial(Particles_Config.Particles.getInt("Particle.5")));  
		Slot_6 = new ItemStack(Material.getMaterial(Particles_Config.Particles.getInt("Particle.6")));  
		Slot_7 = new ItemStack(Material.getMaterial(Particles_Config.Particles.getInt("Particle.7")));  
		Slot_8 = new ItemStack(Material.getMaterial(Particles_Config.Particles.getInt("Particle.8")));  
		Slot_9 = new ItemStack(Material.getMaterial(Particles_Config.Particles.getInt("Particle.9")));
		GUI_Item = new ItemStack(Material.getMaterial(Particles_Config.Particles.getInt("Item_To_Click_To_Open_GUI.GUI_Item")));
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void openGUI(Player p)
	{
		p.openInventory(PINV);
		
		String query = ("SELECT {columnLabel} FROM SpeedUHCTable WHERE playerUUID = '{uuid}';")
				.replace("{columnLabel}", DataColumn.HEART.toString())
				.replace("{uuid}", p.getUniqueId().toString());

		if(Slot_1.getType() != Material.AIR)
		{
			ItemStack Slot = Slot_1;
			ItemMeta im = Slot_1.getItemMeta();
			im.setDisplayName(ChatColor.translateAlternateColorCodes('&', Particles_Config.Particles.getString("Particle.Slot_1_Name")));
			
			try
			{
				PreparedStatement st = plugin.getMySQL().getConnection().prepareStatement(query);
				ResultSet set = st.executeQuery();
				if(set.first())
				{
					boolean hasUnlocked = set.getBoolean(DataColumn.HEART.toString());
					if(hasUnlocked)
					{
						im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
						im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
					}
				}
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
			
			Slot.setItemMeta(im);
		}

		if(Slot_2.getType() != Material.AIR)
		{
			ItemStack Slot = Slot_2;
			ItemMeta im = Slot_2.getItemMeta();
			im.setDisplayName(ChatColor.translateAlternateColorCodes('&', Particles_Config.Particles.getString("Particle.Slot_2_Name")));
			
			query = ("SELECT {columnLabel} FROM SpeedUHCTable WHERE playerUUID = '{uuid}';")
					.replace("{columnLabel}", DataColumn.RAIN.toString())
					.replace("{uuid}", p.getUniqueId().toString());

			
			try
			{
				PreparedStatement st = plugin.getMySQL().getConnection().prepareStatement(query);
				ResultSet set = st.executeQuery();
				if(set.first())
				{
					boolean hasUnlocked = set.getBoolean(DataColumn.RAIN.toString());
					if(hasUnlocked)
					{
						im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
						im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
					}
				}
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
			
			Slot.setItemMeta(im);
		}

		if(Slot_3.getType() != Material.AIR)
		{
			ItemStack Slot = Slot_3;
			ItemMeta im = Slot_3.getItemMeta();
			im.setDisplayName(ChatColor.translateAlternateColorCodes('&', Particles_Config.Particles.getString("Particle.Slot_3_Name")));
			
			query = ("SELECT {columnLabel} FROM SpeedUHCTable WHERE playerUUID = '{uuid}';")
					.replace("{columnLabel}", DataColumn.RAIN.toString())
					.replace("{uuid}", p.getUniqueId().toString());

			
			try
			{
				PreparedStatement st = plugin.getMySQL().getConnection().prepareStatement(query);
				ResultSet set = st.executeQuery();
				if(set.first())
				{
					boolean hasUnlocked = set.getBoolean(DataColumn.RAIN.toString());
					if(hasUnlocked)
					{
						im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
						im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
					}
				}
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}

			
			Slot.setItemMeta(im);
		}

		if(Slot_4.getType() != Material.AIR)
		{
			ItemStack Slot = Slot_4;
			ItemMeta im = Slot_4.getItemMeta();
			im.setDisplayName(ChatColor.translateAlternateColorCodes('&', Particles_Config.Particles.getString("Particle.Slot_4_Name")));
			
			query = ("SELECT {columnLabel} FROM SpeedUHCTable WHERE playerUUID = '{uuid}';")
					.replace("{columnLabel}", DataColumn.FLAME.toString())
					.replace("{uuid}", p.getUniqueId().toString());

			
			try
			{
				PreparedStatement st = plugin.getMySQL().getConnection().prepareStatement(query);
				ResultSet set = st.executeQuery();
				if(set.first())
				{
					boolean hasUnlocked = set.getBoolean(DataColumn.FLAME.toString());
					if(hasUnlocked)
					{
						im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
						im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
					}
				}
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}

			
			Slot.setItemMeta(im);
		}

		if(Slot_5.getType() != Material.AIR)
		{
			ItemStack Slot = Slot_5;
			ItemMeta im = Slot_5.getItemMeta();
			im.setDisplayName(ChatColor.translateAlternateColorCodes('&', Particles_Config.Particles.getString("Particle.Slot_5_Name")));

			query = ("SELECT {columnLabel} FROM SpeedUHCTable WHERE playerUUID = '{uuid}';")
					.replace("{columnLabel}", DataColumn.FIREBALL.toString())
					.replace("{uuid}", p.getUniqueId().toString());

			
			try
			{
				PreparedStatement st = plugin.getMySQL().getConnection().prepareStatement(query);
				ResultSet set = st.executeQuery();
				if(set.first())
				{
					boolean hasUnlocked = set.getBoolean(DataColumn.FIREBALL.toString());
					if(hasUnlocked)
					{
						im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
						im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
					}
				}
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}

			
			Slot.setItemMeta(im);
		}

		if(Slot_6.getType() != Material.AIR)
		{
			ItemStack Slot = Slot_6;
			ItemMeta im = Slot_6.getItemMeta();
			im.setDisplayName(ChatColor.translateAlternateColorCodes('&', Particles_Config.Particles.getString("Particle.Slot_6_Name")));
			
			query = ("SELECT {columnLabel} FROM SpeedUHCTable WHERE playerUUID = '{uuid}';")
					.replace("{columnLabel}", DataColumn.REKT.toString())
					.replace("{uuid}", p.getUniqueId().toString());

			
			try
			{
				PreparedStatement st = plugin.getMySQL().getConnection().prepareStatement(query);
				ResultSet set = st.executeQuery();
				if(set.first())
				{
					boolean hasUnlocked = set.getBoolean(DataColumn.REKT.toString());
					if(hasUnlocked)
					{
						im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
						im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
					}
				}
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}

			
			Slot.setItemMeta(im);
		}

		if(Slot_7.getType() != Material.AIR)
		{
			ItemStack Slot = Slot_7;
			ItemMeta im = Slot_7.getItemMeta();
			im.setDisplayName(ChatColor.translateAlternateColorCodes('&', Particles_Config.Particles.getString("Particle.Slot_7_Name")));
			
			query = ("SELECT {columnLabel} FROM SpeedUHCTable WHERE playerUUID = '{uuid}';")
					.replace("{columnLabel}", DataColumn.SPIRIT.toString())
					.replace("{uuid}", p.getUniqueId().toString());

			
			try
			{
				PreparedStatement st = plugin.getMySQL().getConnection().prepareStatement(query);
				ResultSet set = st.executeQuery();
				if(set.first())
				{
					boolean hasUnlocked = set.getBoolean(DataColumn.SPIRIT.toString());
					if(hasUnlocked)
					{
						im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
						im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
					}
				}
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}

			
			Slot.setItemMeta(im);
		}

		if(Slot_8.getType() != Material.AIR)
		{
			ItemStack Slot = Slot_8;
			ItemMeta im = Slot_8.getItemMeta();
			im.setDisplayName(ChatColor.translateAlternateColorCodes('&', Particles_Config.Particles.getString("Particle.Slot_8_Name")));
			
			
			Slot.setItemMeta(im);
		}

		if(Slot_9.getType() != Material.AIR)
		{
			ItemStack Slot = Slot_9;
			ItemMeta im = Slot_9.getItemMeta();
			im.setDisplayName(ChatColor.translateAlternateColorCodes('&', Particles_Config.Particles.getString("Particle.Slot_9_Name")));
			
			query = ("SELECT {columnLabel} FROM SpeedUHCTable WHERE playerUUID = '{uuid}';")
					.replace("{columnLabel}", DataColumn.JUMPMAN.toString())
					.replace("{uuid}", p.getUniqueId().toString());

			
			try
			{
				PreparedStatement st = plugin.getMySQL().getConnection().prepareStatement(query);
				ResultSet set = st.executeQuery();
				if(set.first())
				{
					boolean hasUnlocked = set.getBoolean(DataColumn.JUMPMAN.toString());
					if(hasUnlocked)
					{
						im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
						im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
					}
				}
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
			
			Slot.setItemMeta(im);
		}

		PINV.setItem(0,Slot_1);
		PINV.setItem(1,Slot_2);
		PINV.setItem(2,Slot_3);
		PINV.setItem(3,Slot_4);
		PINV.setItem(4,Slot_5);
		PINV.setItem(5,Slot_6);
		PINV.setItem(6,Slot_7);
		PINV.setItem(7,Slot_8);
		PINV.setItem(8,Slot_9);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		ItemStack Item = new ItemStack(GUI_Item.getType());

		ItemStack Slot = Item;
		ItemMeta im = Slot_1.getItemMeta();
		im.setDisplayName(ChatColor.translateAlternateColorCodes('&', Particles_Config.Particles.getString("Item_To_Click_To_Open_GUI.Name")));
		Slot.setItemMeta(im);

		e.getPlayer().getInventory().setItem(Particles_Config.Particles.getInt("Item_To_Click_To_Open_GUI.Slot_Number"), Item);
	}
	/////////////////////////////////////////////////////////////////////////////////////////////
	@EventHandler
	public void onPlayerClickGUIItem(PlayerInteractEvent e)
	{
		if (e.getPlayer().getItemInHand().getType() == GUI_Item.getType())
		{
			openGUI(e.getPlayer());
		}
	}
	/////////////////////////////////////////////////////////////////////////////////////////////
	@EventHandler
	public void PlayerVote(InventoryClickEvent e)
	{
		Player p = (Player)e.getWhoClicked();

		if(e.getInventory().equals(PINV))
		{
			if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) 
			{
				if(Slot_1.getType() != Material.AIR)
				{
					ItemStack Item1 = new ItemStack(Slot_1.getType(), PINV.getItem(0).getAmount());

					ItemStack Slot1 = Item1;
					ItemMeta im1 = Slot_1.getItemMeta();
					Slot1.setItemMeta(im1);

					if(e.getCurrentItem().equals(Item1) && e.getCurrentItem().getItemMeta().hasEnchant(Enchantment.ARROW_INFINITE))
					{
						ItemStack Item = new ItemStack(Slot_1.getType(), PINV.getItem(0).getAmount()+1);
						PINV.setItem(0,Slot1);
						e.setCancelled(true);
						e.getWhoClicked().closeInventory();
						{
							ItemMeta im = Slot_1.getItemMeta();
							Item.setItemMeta(im);

							PINV.setItem(0,Item);
							p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 20f, 5f);

							p.getPlayer().sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Selected " +  Item.getItemMeta().getDisplayName());
							PlayerSelection.put(p.getName(), String.valueOf(e.getSlot()+1));
						}
					}
				}
			}
		}

		if(e.getInventory().equals(PINV))
		{
			if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) 
			{
				if(Slot_2.getType() != Material.AIR)
				{
					ItemStack Item2 = new ItemStack(Slot_2.getType(), PINV.getItem(1).getAmount());

					ItemStack Slot2 = Item2;
					ItemMeta im1 = Slot_2.getItemMeta();
					Slot2.setItemMeta(im1);

					if(e.getCurrentItem().equals(Item2) && e.getCurrentItem().getItemMeta().hasEnchant(Enchantment.ARROW_INFINITE))
					{
						ItemStack Item = new ItemStack(Slot_2.getType(), PINV.getItem(1).getAmount()+1);
						PINV.setItem(1,Slot2);
						e.setCancelled(true);
						e.getWhoClicked().closeInventory();
						{
							ItemMeta im = Slot_2.getItemMeta();
							Item.setItemMeta(im);

							PINV.setItem(1,Item);
							p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 20f, 5f);
							p.getPlayer().sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Selected " +  Item.getItemMeta().getDisplayName());
							PlayerSelection.put(p.getName(), String.valueOf(e.getSlot()+1));
						}
					}
				}
			}
		}

		if(e.getInventory().equals(PINV))
		{
			if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) 
			{
				if(Slot_3.getType() != Material.AIR)
				{
					ItemStack Item3 = new ItemStack(Slot_3.getType(), PINV.getItem(2).getAmount());

					ItemStack Slot3 = Item3;
					ItemMeta im1 = Slot_3.getItemMeta();
					Slot3.setItemMeta(im1);

					if(e.getCurrentItem().equals(Item3) && e.getCurrentItem().getItemMeta().hasEnchant(Enchantment.ARROW_INFINITE))
					{
						ItemStack Item = new ItemStack(Slot_3.getType(), PINV.getItem(2).getAmount()+1);
						PINV.setItem(2,Slot3);
						e.setCancelled(true);
						e.getWhoClicked().closeInventory();
						{
							ItemMeta im = Slot_3.getItemMeta();
							Item.setItemMeta(im);

							PINV.setItem(2,Item);
							p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 20f, 5f);
							p.getPlayer().sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Selected " +  Item.getItemMeta().getDisplayName());
							PlayerSelection.put(p.getName(), String.valueOf(e.getSlot()+1));
						}
					}
				}
			}
		}

		if(e.getInventory().equals(PINV))
		{
			if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) 
			{
				if(Slot_4.getType() != Material.AIR)
				{
					ItemStack Item4 = new ItemStack(Slot_4.getType(), PINV.getItem(3).getAmount());

					ItemStack Slot4 = Item4;
					ItemMeta im1 = Slot_4.getItemMeta();
					Slot4.setItemMeta(im1);

					if(e.getCurrentItem().equals(Item4) && e.getCurrentItem().getItemMeta().hasEnchant(Enchantment.ARROW_INFINITE))
					{
						ItemStack Item = new ItemStack(Slot_4.getType(), PINV.getItem(3).getAmount()+1);
						PINV.setItem(3,Slot4);
						e.setCancelled(true);
						e.getWhoClicked().closeInventory();
						{
							ItemMeta im = Slot_4.getItemMeta();
							Item.setItemMeta(im);

							PINV.setItem(3,Item);
							p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 20f, 5f);
							p.getPlayer().sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Selected " +  Item.getItemMeta().getDisplayName());
							PlayerSelection.put(p.getName(), String.valueOf(e.getSlot()+1));
						}
					}
				}
			}
		}

		if(e.getInventory().equals(PINV))
		{
			if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) 
			{
				if(Slot_5.getType() != Material.AIR)
				{
					ItemStack Item5 = new ItemStack(Slot_5.getType(), PINV.getItem(4).getAmount());

					ItemStack Slot5 = Item5;
					ItemMeta im1 = Slot_5.getItemMeta();
					Slot5.setItemMeta(im1);

					if(e.getCurrentItem().equals(Item5) && e.getCurrentItem().getItemMeta().hasEnchant(Enchantment.ARROW_INFINITE))
					{
						ItemStack Item = new ItemStack(Slot_5.getType(), PINV.getItem(4).getAmount()+1);
						PINV.setItem(4,Slot5);
						e.setCancelled(true);
						e.getWhoClicked().closeInventory();
						{
							ItemMeta im = Slot_5.getItemMeta();
							Item.setItemMeta(im);

							PINV.setItem(4,Item);
							p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 20f, 5f);
							p.getPlayer().sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Selected " +  Item.getItemMeta().getDisplayName());
							PlayerSelection.put(p.getName(), String.valueOf(e.getSlot()+1));
						}
					}
				}
			}
		}

		if(e.getInventory().equals(PINV))
		{
			if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) 
			{
				if(Slot_6.getType() != Material.AIR)
				{
					ItemStack Item6 = new ItemStack(Slot_6.getType(), PINV.getItem(5).getAmount());

					ItemStack Slot6 = Item6;
					ItemMeta im1 = Slot_6.getItemMeta();
					Slot6.setItemMeta(im1);

					if(e.getCurrentItem().equals(Item6) && e.getCurrentItem().getItemMeta().hasEnchant(Enchantment.ARROW_INFINITE))
					{
						ItemStack Item = new ItemStack(Slot_6.getType(), PINV.getItem(5).getAmount()+1);
						PINV.setItem(5,Slot6);
						e.setCancelled(true);
						e.getWhoClicked().closeInventory();
						{
							ItemMeta im = Slot_6.getItemMeta();
							Item.setItemMeta(im);

							PINV.setItem(5,Item);
							p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 20f, 5f);
							p.getPlayer().sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Selected " +  Item.getItemMeta().getDisplayName());
							PlayerSelection.put(p.getName(), String.valueOf(e.getSlot()+1));
						}
					}
				}
			}
		}

		if(e.getInventory().equals(PINV))
		{
			if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) 
			{
				if(Slot_7.getType() != Material.AIR)
				{
					ItemStack Item7 = new ItemStack(Slot_7.getType(), PINV.getItem(6).getAmount());

					ItemStack Slot7 = Item7;
					ItemMeta im1 = Slot_7.getItemMeta();
					Slot7.setItemMeta(im1);

					if(e.getCurrentItem().equals(Item7) && e.getCurrentItem().getItemMeta().hasEnchant(Enchantment.ARROW_INFINITE))
					{
						ItemStack Item = new ItemStack(Slot_7.getType(), PINV.getItem(6).getAmount()+1);
						PINV.setItem(6,Slot7);
						e.setCancelled(true);
						e.getWhoClicked().closeInventory();
						{
							ItemMeta im = Slot_7.getItemMeta();
							Item.setItemMeta(im);

							PINV.setItem(6,Item);
							p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 20f, 5f);
							p.getPlayer().sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Selected " +  Item.getItemMeta().getDisplayName());
							PlayerSelection.put(p.getName(), String.valueOf(e.getSlot()+1));
						}
					}
				}
			}
		}

		if(e.getInventory().equals(PINV))
		{
			if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) 
			{
				if(Slot_8.getType() != Material.AIR)
				{
					ItemStack Item8 = new ItemStack(Slot_8.getType(), PINV.getItem(7).getAmount());

					ItemStack Slot8 = Item8;
					ItemMeta im1 = Slot_8.getItemMeta();
					Slot8.setItemMeta(im1);

					if(e.getCurrentItem().equals(Item8) && e.getCurrentItem().getItemMeta().hasEnchant(Enchantment.ARROW_INFINITE))
					{
						ItemStack Item = new ItemStack(Slot_8.getType(), PINV.getItem(7).getAmount()+1);
						PINV.setItem(7,Slot8);
						e.setCancelled(true);
						e.getWhoClicked().closeInventory();
						{
							ItemMeta im = Slot_8.getItemMeta();
							new ArrayList<String>();
							Item.setItemMeta(im);

							PINV.setItem(7,Item);
							p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 20f, 5f);
							p.getPlayer().sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Selected " +  Item.getItemMeta().getDisplayName());
							PlayerSelection.put(p.getName(), String.valueOf(e.getSlot()+1));
						}
					}
				}
			}
		}

		if(e.getInventory().equals(PINV))
		{
			if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) 
			{
				if(Slot_9.getType() != Material.AIR)
				{
					ItemStack Item9 = new ItemStack(Slot_9.getType(), PINV.getItem(8).getAmount());

					ItemStack Slot9 = Item9;
					ItemMeta im1 = Slot_9.getItemMeta();
					Slot9.setItemMeta(im1);

					if(e.getCurrentItem().equals(Item9) && e.getCurrentItem().getItemMeta().hasEnchant(Enchantment.ARROW_INFINITE))
					{
						ItemStack Item = new ItemStack(Slot_9.getType(), PINV.getItem(8).getAmount()+1);
						PINV.setItem(8,Slot9);
						e.setCancelled(true);
						e.getWhoClicked().closeInventory();
						{
							ItemMeta im = Slot_9.getItemMeta();
							new ArrayList<String>();
							Item.setItemMeta(im);

							PINV.setItem(8,Item);
							p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 20f, 5f);
							p.getPlayer().sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Selected " +  Item.getItemMeta().getDisplayName());
							PlayerSelection.put(p.getName(), String.valueOf(e.getSlot()+1));
						}
					}
				}
			}
		}
	}
}