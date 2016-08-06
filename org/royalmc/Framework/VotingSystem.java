package org.royalmc.Framework;

import static org.royalmc.util.TextUtils.formatText;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.royalmc.Framework.SQLStorage.DataColumn;
import org.royalmc.uhc.UHCMain;

public class VotingSystem implements Listener
{

	UHCMain plugin;

	public VotingSystem(UHCMain plugin)
	{
		this.plugin = plugin;
	}

	public static HashMap<Player, Integer> diamondlessPoll = new HashMap<>();
	public static HashMap<Player, Integer> goldlessPoll = new HashMap<>();
	public static HashMap<Player, Integer> horselessPoll = new HashMap<>();
	public static HashMap<Player, Integer> rodlessPoll = new HashMap<>();
	public static HashMap<Player, Integer> nonePoll = new HashMap<>();
	public static HashMap<Player, Integer> doubleOresPoll = new HashMap<>();

	public void createInv(Player p)
	{

		String query = ("SELECT {columnLabel} FROM SpeedUHCTable WHERE playerUUID = '{uuid}';")
				.replace("{columnLabel}", DataColumn.DIAMONDLESS.toString())
				.replace("{uuid}", p.getUniqueId().toString());

		try
		{

			PreparedStatement st = plugin.getMySQL().getConnection().prepareStatement(query);
			ResultSet set = st.executeQuery();

			if(p.isOp() || p.hasPermission("gameModeSelector"))
			{

				ItemStack diamondless = new ItemStack(Material.valueOf(plugin.getConfig().getString("diamondlessItem")));
				ItemMeta diamondlessMeta = diamondless.getItemMeta();
				diamondlessMeta.setDisplayName(formatText(plugin.getConfig().getString("diamondless.nameColor")) + GameType.DIAMONDLESS.toString());
				List<String> lore = plugin.getConfig().getStringList("diamondless.lore");
				if(lore != null)
				{
					for(int i = 0; i < lore.size(); i++){
						if(lore.get(i).equals("&aVotes: {amount}"))
						{
							String str = lore.get(i).replace("{amount}", String.valueOf(diamondlessPoll.size()));
							lore.set(i, formatText(str));
						}
						else
							lore.set(i, formatText(lore.get(i)));
					}
					diamondlessMeta.setLore(lore);
				}

				if(set.first())
				{
					if(set.getBoolean(DataColumn.DIAMONDLESS.toString()))
					{
						diamondlessMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
						diamondlessMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
						diamondlessMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
					}
				}

				diamondlessMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				diamondless.setItemMeta(diamondlessMeta);

				ItemStack goldless = new ItemStack(Material.valueOf(plugin.getConfig().getString("goldlessItem")));
				ItemMeta goldlessMeta = goldless.getItemMeta();
				goldlessMeta.setDisplayName(formatText(plugin.getConfig().getString("goldless.nameColor")) + GameType.GOLDLESS.toString());
				List<String> lore1 = plugin.getConfig().getStringList("goldless.lore");
				if(lore1 != null)
				{
					for(int i = 0; i < lore1.size(); i++){
						if(lore1.get(i).equals("&aVotes: {amount}"))
						{
							String str = lore1.get(i).replace("{amount}", String.valueOf(goldlessPoll.size()));
							lore1.set(i, formatText(str));
						}
						else
							lore1.set(i, formatText(lore1.get(i)));
					}
					goldlessMeta.setLore(lore1);
				}

				query = ("SELECT {columnLabel} FROM SpeedUHCTable WHERE playerUUID = '{uuid}';")
						.replace("{columnLabel}", DataColumn.GOLDLESS.toString())
						.replace("{uuid}", p.getUniqueId().toString());

				st = plugin.getMySQL().getConnection().prepareStatement(query);
				set = st.executeQuery();

				if(set.first())
				{
					if(set.getBoolean(DataColumn.GOLDLESS.toString()))
					{
						goldlessMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
						goldlessMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
						goldlessMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
					}
				}

				goldlessMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				goldless.setItemMeta(goldlessMeta);

				ItemStack horseless = new ItemStack(Material.valueOf(plugin.getConfig().getString("horselessItem")));
				ItemMeta horselessMeta = horseless.getItemMeta();
				horselessMeta.setDisplayName(formatText(plugin.getConfig().getString("horseless.nameColor")) + GameType.HORSELESS.toString());
				List<String> lore2 = plugin.getConfig().getStringList("horseless.lore");
				if(lore2 != null)
				{
					for(int i = 0; i < lore2.size(); i++){
						if(lore2.get(i).equals("&aVotes: {amount}"))
						{
							String str = lore2.get(i).replace("{amount}", String.valueOf(horselessPoll.size()));
							lore2.set(i, formatText(str));
						}
						else
							lore2.set(i, formatText(lore2.get(i)));
					}
					horselessMeta.setLore(lore2);
				}

				query = ("SELECT {columnLabel} FROM SpeedUHCTable WHERE playerUUID = '{uuid}';")
						.replace("{columnLabel}", DataColumn.HORSELESS.toString())
						.replace("{uuid}", p.getUniqueId().toString());

				st = plugin.getMySQL().getConnection().prepareStatement(query);
				set = st.executeQuery();

				if(set.first())
				{
					if(set.getBoolean(DataColumn.HORSELESS.toString()))
					{
						horselessMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
						horselessMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
						horselessMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
					}
				}

				horselessMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				horseless.setItemMeta(horselessMeta);

				ItemStack rodless = new ItemStack(Material.valueOf(plugin.getConfig().getString("rodlessItem")));
				ItemMeta rodlessMeta = rodless.getItemMeta();
				rodlessMeta.setDisplayName(formatText(plugin.getConfig().getString("rodless.nameColor")) + GameType.RODLESS.toString());
				List<String> lore3 = plugin.getConfig().getStringList("rodless.lore");
				if(lore3 != null)
				{
					for(int i = 0; i < lore3.size(); i++){
						if(lore3.get(i).equals("&aVotes: {amount}"))
						{
							String str = lore3.get(i).replace("{amount}", String.valueOf(horselessPoll.size()));
							lore3.set(i, formatText(str));
						}
						else
							lore3.set(i, formatText(lore3.get(i)));
					}
					rodlessMeta.setLore(lore3);
				}

				query = ("SELECT {columnLabel} FROM SpeedUHCTable WHERE playerUUID = '{uuid}';")
						.replace("{columnLabel}", DataColumn.RODLESS.toString())
						.replace("{uuid}", p.getUniqueId().toString());

				st = plugin.getMySQL().getConnection().prepareStatement(query);
				set = st.executeQuery();

				if(set.first())
				{
					if(set.getBoolean(DataColumn.RODLESS.toString()))
					{
						rodlessMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
						rodlessMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
						rodlessMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
					}
				}
				rodlessMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				rodless.setItemMeta(rodlessMeta);

				ItemStack none = new ItemStack(Material.valueOf(plugin.getConfig().getString("noneItem")));
				ItemMeta noneMeta = none.getItemMeta();
				noneMeta.setDisplayName(formatText(plugin.getConfig().getString("none.nameColor")) + GameType.NONE.toString());
				List<String> lore4 = plugin.getConfig().getStringList("none.lore");
				if(lore4 != null)
				{
					for(int i = 0; i < lore4.size(); i++){
						if(lore4.get(i).equals("&aVotes: {amount}"))
						{
							String str = lore4.get(i).replace("{amount}", String.valueOf(nonePoll.size()));
							lore4.set(i, formatText(str));
						}
						else
							lore4.set(i, formatText(lore4.get(i)));
					}
					noneMeta.setLore(lore4);
				}
				none.setItemMeta(noneMeta);

				ItemStack doubleOres = new ItemStack(Material.valueOf(plugin.getConfig().getString("doubleOresItem")));
				ItemMeta doubleOresMeta = doubleOres.getItemMeta();
				doubleOresMeta.setDisplayName(formatText(plugin.getConfig().getString("doubleOres.nameColor")) + GameType.DOUBLEORES.toString());
				List<String> lore5 = plugin.getConfig().getStringList("doubleOres.lore");
				if(lore5 != null)
				{
					for(int i = 0; i < lore5.size(); i++){
						if(lore5.get(i).equals("&aVotes: {amount}"))
						{
							String str = lore5.get(i).replace("{amount}", String.valueOf(doubleOresPoll.size()));
							lore5.set(i, formatText(str));
						}
						else
							lore5.set(i, formatText(lore5.get(i)));
					}
					doubleOresMeta.setLore(lore5);
				}

				query = ("SELECT {columnLabel} FROM SpeedUHCTable WHERE playerUUID = '{uuid}';")
						.replace("{columnLabel}", DataColumn.DOUBLEORES.toString())
						.replace("{uuid}", p.getUniqueId().toString());

				st = plugin.getMySQL().getConnection().prepareStatement(query);
				set = st.executeQuery();

				if(set.first())
				{
					if(set.getBoolean(DataColumn.DOUBLEORES.toString()))
					{
						doubleOresMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
						doubleOresMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
						doubleOresMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
					}
				}

				doubleOresMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				doubleOres.setItemMeta(doubleOresMeta);

				ItemStack removeVote = new ItemStack(Material.valueOf(plugin.getConfig().getString("removeVoteItem")));
				ItemMeta removeVoteMeta = removeVote.getItemMeta();
				removeVoteMeta.setDisplayName(formatText(plugin.getConfig().getString("removeVote.nameColor")) + "Remove your current vote!");
				List<String> lore6 = plugin.getConfig().getStringList("removeVote.lore");
				if(lore6 != null)
				{
					for(int i = 0; i < lore6.size(); i++)
					{
						lore6.set(i, formatText(lore6.get(i)));
					}
					removeVoteMeta.setLore(lore6);
				}
				removeVote.setItemMeta(removeVoteMeta);

				////////////////////////////////////////////////////////////

				Inventory inv = Bukkit.createInventory(p, 54, formatText(plugin.getConfig().getString("gameModeInv")));
				inv.setItem(plugin.getConfig().getInt("diamondlessSlot"), diamondless);
				inv.setItem(plugin.getConfig().getInt("goldlessSlot"), goldless);
				inv.setItem(plugin.getConfig().getInt("horselessSlot"), horseless);
				inv.setItem(plugin.getConfig().getInt("rodlessSlot"), rodless);
				inv.setItem(plugin.getConfig().getInt("doubleOresSlot"), doubleOres);
				inv.setItem(plugin.getConfig().getInt("noneSlot"), none);
				inv.setItem(plugin.getConfig().getInt("removeVoteSlot"), removeVote);

				p.openInventory(inv);
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		if(GameState.getState() == GameState.IN_LOBBY)
		{
			if(e.getPlayer().getItemInHand().getType() == Material.valueOf(plugin.getConfig().getString("gameModeItem.type")))
			{
				createInv(e.getPlayer());
			}
		}
	}

	@EventHandler
	public void onInvClick(InventoryClickEvent e)
	{
		if(GameState.getState() == GameState.IN_LOBBY)
		{

			if(e.getCurrentItem() == null)
				return;

			if(e.getInventory().getName().equals(formatText(plugin.getConfig().getString("gameModeInv"))))
			{

				if(!diamondlessPoll.containsKey((Player)e.getWhoClicked()) && 
						! goldlessPoll.containsKey((Player)e.getWhoClicked()) &&
						! horselessPoll.containsKey((Player)e.getWhoClicked()) &&
						! rodlessPoll.containsKey((Player)e.getWhoClicked()) &&
						! nonePoll.containsKey((Player)e.getWhoClicked()) &&
						! doubleOresPoll.containsKey((Player)e.getWhoClicked()))
				{
					if(e.getCurrentItem().getType() == Material.valueOf(plugin.getConfig().getString("diamondlessItem")) && e.getCurrentItem().getEnchantments().containsKey(Enchantment.ARROW_INFINITE))
					{
						diamondlessPoll.put((Player)e.getWhoClicked(), 1);

						((Player)e.getWhoClicked()).playSound(((Player)e.getWhoClicked()).getLocation(), Sound.NOTE_PLING, 10f, 1f);
						
						e.getWhoClicked().closeInventory();

						for(Player p : Bukkit.getOnlinePlayers())
						{
							if(plugin.getConfig().getString("voteMessage") != null || plugin.getConfig().getString("voteMessage") != "")
							{
								p.sendMessage(formatText(plugin.getConfig().getString("voteMessage")).replace("{player}", e.getWhoClicked().getName()).replace("{gamemode}", "Diamondless"));
							}
						}
					}
					else if(e.getCurrentItem().getType() == Material.valueOf(plugin.getConfig().getString("goldlessItem")) && e.getCurrentItem().getEnchantments().containsKey(Enchantment.ARROW_INFINITE))
					{
						goldlessPoll.put((Player)e.getWhoClicked(), 1);

						((Player)e.getWhoClicked()).playSound(((Player)e.getWhoClicked()).getLocation(), Sound.NOTE_PLING, 10f, 1f);
						
						e.getWhoClicked().closeInventory();

						for(Player p : Bukkit.getOnlinePlayers())
						{
							if(plugin.getConfig().getString("voteMessage") != null || plugin.getConfig().getString("voteMessage") != "")
							{
								p.sendMessage(formatText(plugin.getConfig().getString("voteMessage")).replace("{player}", e.getWhoClicked().getName()).replace("{gamemode}", "Goldless"));
							}
						}
					}
					else if(e.getCurrentItem().getType() == Material.valueOf(plugin.getConfig().getString("horselessItem")) && e.getCurrentItem().getEnchantments().containsKey(Enchantment.ARROW_INFINITE))
					{
						horselessPoll.put((Player)e.getWhoClicked(), 1);

						((Player)e.getWhoClicked()).playSound(((Player)e.getWhoClicked()).getLocation(), Sound.NOTE_PLING, 10f, 1f);
						
						e.getWhoClicked().closeInventory();

						for(Player p : Bukkit.getOnlinePlayers())
						{
							if(plugin.getConfig().getString("voteMessage") != null || plugin.getConfig().getString("voteMessage") != "")
							{
								p.sendMessage(formatText(plugin.getConfig().getString("voteMessage")).replace("{player}", e.getWhoClicked().getName()).replace("{gamemode}", "Horseless"));
							}
						}
					}
					else if(e.getCurrentItem().getType() == Material.valueOf(plugin.getConfig().getString("rodlessItem")) && e.getCurrentItem().getEnchantments().containsKey(Enchantment.ARROW_INFINITE))
					{
						rodlessPoll.put((Player)e.getWhoClicked(), 1);

						((Player)e.getWhoClicked()).playSound(((Player)e.getWhoClicked()).getLocation(), Sound.NOTE_PLING, 10f, 1f);
						
						e.getWhoClicked().closeInventory();

						for(Player p : Bukkit.getOnlinePlayers())
						{
							if(plugin.getConfig().getString("voteMessage") != null || plugin.getConfig().getString("voteMessage") != "")
							{
								p.sendMessage(formatText(plugin.getConfig().getString("voteMessage")).replace("{player}", e.getWhoClicked().getName()).replace("{gamemode}", "Rodless"));
							}
						}
					}
					else if(e.getCurrentItem().getType() == Material.valueOf(plugin.getConfig().getString("noneItem")))
					{
						nonePoll.put((Player)e.getWhoClicked(), 1);

						((Player)e.getWhoClicked()).playSound(((Player)e.getWhoClicked()).getLocation(), Sound.NOTE_PLING, 10f, 1f);
						
						e.getWhoClicked().closeInventory();

						for(Player p : Bukkit.getOnlinePlayers())
						{
							if(plugin.getConfig().getString("voteMessage") != null || plugin.getConfig().getString("voteMessage") != "")
							{
								p.sendMessage(formatText(plugin.getConfig().getString("voteMessage")).replace("{player}", e.getWhoClicked().getName()).replace("{gamemode}", "None"));
							}
						}
					}
					else if(e.getCurrentItem().getType() == Material.valueOf(plugin.getConfig().getString("doubleOresItem")) && e.getCurrentItem().getEnchantments().containsKey(Enchantment.ARROW_INFINITE))
					{
						doubleOresPoll.put((Player)e.getWhoClicked(), 1);

						((Player)e.getWhoClicked()).playSound(((Player)e.getWhoClicked()).getLocation(), Sound.NOTE_PLING, 10f, 1f);
						
						e.getWhoClicked().closeInventory();

						for(Player p : Bukkit.getOnlinePlayers())
						{
							if(plugin.getConfig().getString("voteMessage") != null || plugin.getConfig().getString("voteMessage") != "")
							{
								p.sendMessage(formatText(plugin.getConfig().getString("voteMessage")).replace("{player}", e.getWhoClicked().getName()).replace("{gamemode}", "Double Ores"));
							}
						}
					}
					else if(e.getCurrentItem().getType() == Material.valueOf(plugin.getConfig().getString("removeVoteItem")))
					{
						e.getWhoClicked().closeInventory();
						e.getWhoClicked().sendMessage(formatText("&cYou have not voted yet!"));
						((Player)e.getWhoClicked()).playSound(((Player)e.getWhoClicked()).getLocation(), Sound.NOTE_BASS, 10f, 1f);
					}
					else
					{
						e.getWhoClicked().closeInventory();
						e.getWhoClicked().sendMessage(formatText("&cYou have not unlocked this game mode yet!"));
						((Player)e.getWhoClicked()).playSound(((Player)e.getWhoClicked()).getLocation(), Sound.NOTE_BASS, 10f, 1f);
					}
				}
				else
				{
					Player p = (Player)e.getWhoClicked();

					if(e.getCurrentItem().getType() == Material.valueOf(plugin.getConfig().getString("removeVoteItem")))
					{
						if(diamondlessPoll.containsKey(p)){diamondlessPoll.remove(p);}
						else if(goldlessPoll.containsKey(p)){goldlessPoll.remove(p);}
						else if(horselessPoll.containsKey(p)){horselessPoll.remove(p);}
						else if(rodlessPoll.containsKey(p)){rodlessPoll.remove(p);}
						else if(nonePoll.containsKey(p)){nonePoll.remove(p);}
						else if(doubleOresPoll.containsKey(p)){doubleOresPoll.remove(p);}

						p.closeInventory();
						p.sendMessage(formatText("&cYour vote has been removed!"));
					}
				}
			}
		}
	}
}
