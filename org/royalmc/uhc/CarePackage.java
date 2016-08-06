package org.royalmc.uhc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.server.v1_8_R3.TileEntityChest;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftChest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.royalmc.util.PVP_ChestConfig;

public class CarePackage implements Listener 
{
	static UHCMain plugin;  

	public CarePackage(UHCMain plugin, Location loc) 
	{
		CarePackage.plugin = plugin;
		SpawnChests(loc);
	}

	private static List<ItemStack> list = new ArrayList<ItemStack>();

	/////////////////////////////////////////////////////////////////////////////////
	ArrayList<Location> Chests = new ArrayList<Location>();
	@EventHandler
	public void onChestClick(PlayerInteractEvent e)
	{ 
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if(e.getClickedBlock().getType() == Material.CHEST)
			{
				if(e.getClickedBlock().getLocation().subtract(0, 1, 0).getBlock().getType() == Material.GLOWSTONE)
				{
					Chest chest = (Chest)e.getClickedBlock().getState();
					Inventory Chestinv = chest.getInventory();

					if (!Chests.contains(e.getClickedBlock().getLocation()))
					{
						if(list.size() > 0)
						{
							Chests.add(e.getClickedBlock().getLocation());

							Chestinv.setItem(RandomSlotPicker() ,ItemChooser());
							Chestinv.setItem(RandomSlotPicker() , new ItemStack(Material.AIR));
							Chestinv.setItem(RandomSlotPicker() ,ItemChooser());
							Chestinv.setItem(RandomSlotPicker() ,ItemChooser());
							Chestinv.setItem(RandomSlotPicker() , new ItemStack(Material.AIR));
							Chestinv.setItem(RandomSlotPicker() ,ItemChooser());
							Chestinv.setItem(RandomSlotPicker() ,ItemChooser());
							Chestinv.setItem(RandomSlotPicker() , new ItemStack(Material.AIR));
							Chestinv.setItem(RandomSlotPicker() ,ItemChooser());
							Chestinv.setItem(RandomSlotPicker() ,ItemChooser());
							Chestinv.setItem(RandomSlotPicker() , new ItemStack(Material.AIR));
							Chestinv.setItem(RandomSlotPicker() ,ItemChooser());
						}
						else
						{
							if(list.size() <= 0)
							{
								Chestinv.clear();
							}
						}
					}
				}
			}
		}
	}
	/////////////////////////////////////////////////////////////////////////////////
	public static void SpawnChests(Location ChestLocation)
	{
		PVPChestsDrop(ChestLocation);
	}
	/////////////////////////////////////////////////////////////////////////////////
	public static void PVPChestsDrop(Location MiddlePointLocation)

	{
		World W = MiddlePointLocation.getWorld();
		double X = MiddlePointLocation.getX() + RandomNumberPicker();
		double Y = RandomNumberPicker();
		double Z = MiddlePointLocation.getZ() + RandomNumberPicker();

		Location Temploc = new Location(W,X-15,Y,Z+15);

		Particles(Temploc);
	}
	////////////////////////////////////////////////////////////////////////////////////
	public static void Particles(Location ChestLocation)
	{
		new BukkitRunnable()
		{
			public void run()
			{
				int particles = 9;
				double CPS = ChestLocation.getY() + 2;
				Location PLNL = new Location(ChestLocation.getWorld(), ChestLocation.getX() , CPS , ChestLocation.getZ());
				{
					for(int j = 0; j < particles; j++)
					{
						ChestLocation.getWorld().playEffect(PLNL, Effect.SMOKE, j);
						ChestLocation.getWorld().playEffect(PLNL, Effect.CLOUD , j);
						ChestLocation.getWorld().playEffect(PLNL, Effect.CLOUD , j);
						ChestLocation.getWorld().playEffect(PLNL, Effect.SMOKE, j);
						ChestLocation.getWorld().playEffect(PLNL, Effect.LARGE_SMOKE , j);
						ChestLocation.getWorld().playSound(ChestLocation, Sound.EXPLODE, 7.50f, 1f);
						{
							CPS = ChestLocation.getY() + 50;

							ChestLocation.getWorld().playEffect(new Location(ChestLocation.getWorld(), ChestLocation.getX() , CPS , ChestLocation.getZ()), Effect.EXPLOSION_HUGE, 5);

							Particles2(ChestLocation);
						}
					}
				}
			}
		}.runTaskLater(plugin, 9L);
	}

	public static void Particles2(Location ChestLocation)
	{
		Location Chestloc1 = new Location(ChestLocation.getWorld(),ChestLocation.getX(),ChestLocation.getY()-10 ,ChestLocation.getZ());
		int HighestBlock = ChestLocation.getWorld().getHighestBlockYAt(ChestLocation.getBlockX(), ChestLocation.getBlockZ());
		Location Chestloc2 = new Location(Chestloc1.getWorld(),Chestloc1.getX(),HighestBlock,Chestloc1.getZ());

		new BukkitRunnable()
		{
			int count = -1;
			double CPS = Chestloc2.getY() + 50;

			public void run()
			{
				++count;

				CPS = CPS - 10;

				switch(count)
				{
				case 0:
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS , Chestloc2.getZ()), Effect.VILLAGER_THUNDERCLOUD, 1);
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS , Chestloc2.getZ()), Effect.EXPLOSION, 1);
					break;
				case 1:
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 5 , Chestloc2.getZ()), Effect.SNOWBALL_BREAK, 1);
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 5 , Chestloc2.getZ()), Effect.VILLAGER_THUNDERCLOUD, 1);
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 5 , Chestloc2.getZ()), Effect.SNOWBALL_BREAK, 1);
					break;
				case 2:
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 10 , Chestloc2.getZ()), Effect.SNOWBALL_BREAK, 1);
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 10 , Chestloc2.getZ()), Effect.VILLAGER_THUNDERCLOUD, 1);
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 10 , Chestloc2.getZ()), Effect.SNOWBALL_BREAK, 1);
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 10 , Chestloc2.getZ()), Effect.SNOWBALL_BREAK, 1);
					break;
				case 3:
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 15 , Chestloc2.getZ()), Effect.SNOWBALL_BREAK, 1);
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 15 - 5 , Chestloc2.getZ()), Effect.VILLAGER_THUNDERCLOUD, 1);
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 15 , Chestloc2.getZ()), Effect.SNOWBALL_BREAK, 1);
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 15 , Chestloc2.getZ()), Effect.SNOWBALL_BREAK, 1);
					break;
				case 4:
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 25 , Chestloc2.getZ()), Effect.SNOWBALL_BREAK, 1);
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 25 , Chestloc2.getZ()), Effect.VILLAGER_THUNDERCLOUD, 1);
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 25 , Chestloc2.getZ()), Effect.SNOWBALL_BREAK, 1);
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 25 , Chestloc2.getZ()), Effect.SNOWBALL_BREAK, 1);
					break;
				case 5:
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 25 , Chestloc2.getZ()), Effect.SNOWBALL_BREAK, 1);
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 25 , Chestloc2.getZ()), Effect.VILLAGER_THUNDERCLOUD, 1);
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 25 , Chestloc2.getZ()), Effect.SNOWBALL_BREAK, 1);
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 25 , Chestloc2.getZ()), Effect.SNOWBALL_BREAK, 1);
					break;
				case 6:
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 35 , Chestloc2.getZ()), Effect.SNOWBALL_BREAK, 1);
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 35 , Chestloc2.getZ()), Effect.VILLAGER_THUNDERCLOUD, 1);
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 35 , Chestloc2.getZ()), Effect.SNOWBALL_BREAK, 1);
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 35 , Chestloc2.getZ()), Effect.SNOWBALL_BREAK, 1);
					break;
				case 7:
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 40 , Chestloc2.getZ()), Effect.SNOWBALL_BREAK, 1);
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 40 , Chestloc2.getZ()), Effect.VILLAGER_THUNDERCLOUD, 1);
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 40 , Chestloc2.getZ()), Effect.SNOWBALL_BREAK, 1);
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 40 , Chestloc2.getZ()), Effect.SNOWBALL_BREAK, 1);
					break;
				case 8:
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 45 , Chestloc2.getZ()), Effect.SNOWBALL_BREAK, 1);
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 45 , Chestloc2.getZ()), Effect.VILLAGER_THUNDERCLOUD, 1);
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 45 , Chestloc2.getZ()), Effect.SNOWBALL_BREAK, 1);
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 45 , Chestloc2.getZ()), Effect.SNOWBALL_BREAK, 1);
					break;
				case 9:
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 50 , Chestloc2.getZ()), Effect.SNOWBALL_BREAK, 1);
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 50 , Chestloc2.getZ()), Effect.VILLAGER_THUNDERCLOUD, 1);
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 50 , Chestloc2.getZ()), Effect.SNOWBALL_BREAK, 1);
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 50 , Chestloc2.getZ()), Effect.SNOWBALL_BREAK, 1);
					break;
				case 10:
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 55 , Chestloc2.getZ()), Effect.SNOWBALL_BREAK, 1);
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 55 , Chestloc2.getZ()), Effect.VILLAGER_THUNDERCLOUD, 1);
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 55 , Chestloc2.getZ()), Effect.SNOWBALL_BREAK, 1);
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 55 , Chestloc2.getZ()), Effect.SNOWBALL_BREAK, 1);
					break;
				case 11:
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 60 , Chestloc2.getZ()), Effect.SNOWBALL_BREAK, 1);
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 60 , Chestloc2.getZ()), Effect.VILLAGER_THUNDERCLOUD, 1);
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 60 , Chestloc2.getZ()), Effect.SNOWBALL_BREAK, 1);
					Chestloc2.getWorld().playEffect(new Location(Chestloc2.getWorld(), Chestloc2.getX() , CPS - 60 , Chestloc2.getZ()), Effect.SNOWBALL_BREAK, 1);

					Chestloc2.getWorld().getBlockAt(Chestloc2).setType(Material.CHEST);

					ChestLocation.getWorld().playSound(ChestLocation, Sound.ANVIL_LAND, 3.50f, 1f);

					Chestloc2.getWorld().getBlockAt(Chestloc2.getBlockX()+1, Chestloc2.getBlockY(),Chestloc2.getBlockZ()+1).setType(Material.ENCHANTMENT_TABLE);
					Chestloc2.getWorld().getBlockAt(Chestloc2.getBlockX()-1, Chestloc2.getBlockY(),Chestloc2.getBlockZ()-1).setType(Material.WORKBENCH);
					Chestloc2.getWorld().getBlockAt(Chestloc2.getBlockX()-1, Chestloc2.getBlockY(),Chestloc2.getBlockZ()+1).setType(Material.FURNACE);
					Chestloc2.getWorld().getBlockAt(Chestloc2.getBlockX()+1, Chestloc2.getBlockY(),Chestloc2.getBlockZ()-1).setType(Material.ANVIL);

					Chestloc2.getWorld().getBlockAt(Chestloc2.getBlockX()+1, Chestloc2.getBlockY()-1,Chestloc2.getBlockZ()+1).setType(Material.COBBLESTONE);
					Chestloc2.getWorld().getBlockAt(Chestloc2.getBlockX()-1, Chestloc2.getBlockY()-1,Chestloc2.getBlockZ()-1).setType(Material.COBBLESTONE);
					Chestloc2.getWorld().getBlockAt(Chestloc2.getBlockX(), Chestloc2.getBlockY()-1,Chestloc2.getBlockZ()).setType(Material.GLOWSTONE);
					Chestloc2.getWorld().getBlockAt(Chestloc2.getBlockX()-1, Chestloc2.getBlockY()-1,Chestloc2.getBlockZ()+1).setType(Material.COBBLESTONE);
					Chestloc2.getWorld().getBlockAt(Chestloc2.getBlockX()+1, Chestloc2.getBlockY()-1,Chestloc2.getBlockZ()-1).setType(Material.COBBLESTONE);
					Chestloc2.getWorld().getBlockAt(Chestloc2.getBlockX(), Chestloc2.getBlockY()-1,Chestloc2.getBlockZ()-1).setType(Material.COBBLESTONE);
					Chestloc2.getWorld().getBlockAt(Chestloc2.getBlockX(), Chestloc2.getBlockY()-1,Chestloc2.getBlockZ()+1).setType(Material.COBBLESTONE);
					Chestloc2.getWorld().getBlockAt(Chestloc2.getBlockX()-1, Chestloc2.getBlockY()-1,Chestloc2.getBlockZ()).setType(Material.COBBLESTONE);
					Chestloc2.getWorld().getBlockAt(Chestloc2.getBlockX()+1, Chestloc2.getBlockY()-1,Chestloc2.getBlockZ()).setType(Material.COBBLESTONE);

					Chestloc2.getWorld().getBlockAt(Chestloc2.getBlockX(), Chestloc2.getBlockY(),Chestloc2.getBlockZ()-1).setType(Material.AIR);
					Chestloc2.getWorld().getBlockAt(Chestloc2.getBlockX(), Chestloc2.getBlockY(),Chestloc2.getBlockZ()+1).setType(Material.AIR);
					Chestloc2.getWorld().getBlockAt(Chestloc2.getBlockX()-1, Chestloc2.getBlockY(),Chestloc2.getBlockZ()).setType(Material.AIR);
					Chestloc2.getWorld().getBlockAt(Chestloc2.getBlockX()+1, Chestloc2.getBlockY(),Chestloc2.getBlockZ()).setType(Material.AIR);

					Chestloc2.getWorld().getBlockAt(Chestloc2.getBlockX()+1, Chestloc2.getBlockY()+1,Chestloc2.getBlockZ()+1).setType(Material.AIR);
					Chestloc2.getWorld().getBlockAt(Chestloc2.getBlockX()-1, Chestloc2.getBlockY()+1,Chestloc2.getBlockZ()-1).setType(Material.AIR);
					Chestloc2.getWorld().getBlockAt(Chestloc2.getBlockX(), Chestloc2.getBlockY()+1,Chestloc2.getBlockZ()).setType(Material.AIR);
					Chestloc2.getWorld().getBlockAt(Chestloc2.getBlockX()-1, Chestloc2.getBlockY()+1,Chestloc2.getBlockZ()+1).setType(Material.AIR);
					Chestloc2.getWorld().getBlockAt(Chestloc2.getBlockX()+1, Chestloc2.getBlockY()+1,Chestloc2.getBlockZ()-1).setType(Material.AIR);
					Chestloc2.getWorld().getBlockAt(Chestloc2.getBlockX(), Chestloc2.getBlockY()+1,Chestloc2.getBlockZ()-1).setType(Material.AIR);
					Chestloc2.getWorld().getBlockAt(Chestloc2.getBlockX(), Chestloc2.getBlockY()+1,Chestloc2.getBlockZ()+1).setType(Material.AIR);
					Chestloc2.getWorld().getBlockAt(Chestloc2.getBlockX()-1, Chestloc2.getBlockY()+1,Chestloc2.getBlockZ()).setType(Material.AIR);
					Chestloc2.getWorld().getBlockAt(Chestloc2.getBlockX()+1, Chestloc2.getBlockY()+1,Chestloc2.getBlockZ()).setType(Material.AIR);
					Chestloc2.getWorld().getBlockAt(Chestloc2.getBlockX(), Chestloc2.getBlockY()+1,Chestloc2.getBlockZ()-1).setType(Material.AIR);
					Chestloc2.getWorld().getBlockAt(Chestloc2.getBlockX(), Chestloc2.getBlockY()+1,Chestloc2.getBlockZ()+1).setType(Material.AIR);
					Chestloc2.getWorld().getBlockAt(Chestloc2.getBlockX()-1, Chestloc2.getBlockY()+1,Chestloc2.getBlockZ()).setType(Material.AIR);
					Chestloc2.getWorld().getBlockAt(Chestloc2.getBlockX()+1, Chestloc2.getBlockY()+1,Chestloc2.getBlockZ()).setType(Material.AIR);

					Chest chest = (Chest)Chestloc2.getWorld().getBlockAt(Chestloc2).getState();
					CraftChest BukkitChest = (CraftChest) chest;
					TileEntityChest NMSChest = BukkitChest.getTileEntity();
					NMSChest.a(ChatColor.YELLOW + "" +ChatColor.BOLD + "Bonus Chest");
					
					Particles3(Chestloc2);

					cancel();
					break;
				}
			}
		}.runTaskTimer(plugin, 0, 10L);
	}

	public static void Particles3(Location ChestLocation)
	{

		int particles = 20;
		float radius = 1.5f;
		for (int i = 0; i < particles; i++)
		{
			double angle, X, Z;
			angle = 2 * Math.PI * i / particles;
			X = Math.cos(angle) * radius;
			Z = Math.sin(angle) * radius;
			ChestLocation.add(X, 2, Z);
			ChestLocation.getWorld().playEffect(ChestLocation, Effect.MOBSPAWNER_FLAMES, i);
			ChestLocation.subtract(X, 2, Z);
		}
	}
	////////////////////////////////////////////////////////////////////////////////
	private static Double RandomNumberPicker() 
	{
		Double RandomNumberPicker;
		ArrayList<Integer> NP = new ArrayList<Integer>();

		NP.add(10);
		NP.add(25);
		NP.add(30);
		NP.add(45);
		NP.add(50);
		NP.add(65);
		NP.add(70);
		NP.add(85);
		NP.add(90);
		NP.add(105);
		NP.add(120);
		NP.add(135);
		NP.add(140);
		NP.add(150);
		Random random = new Random();
		int randomn = random.nextInt(NP.size()-1) + 1;
		int selector = NP.get(randomn);
		RandomNumberPicker = (double) selector;
		return RandomNumberPicker;
	}

	private static int RandomSlotPicker() 
	{
		int RandomSlotPicker;
		ArrayList<Integer> NP = new ArrayList<Integer>();

		NP.add(0);
		NP.add(1);
		NP.add(2);
		NP.add(3);
		NP.add(4);
		NP.add(5);
		NP.add(6);
		NP.add(7);
		NP.add(8);
		NP.add(9);
		NP.add(10);
		NP.add(11);
		NP.add(12);
		NP.add(13);
		NP.add(14);
		NP.add(15);
		NP.add(16);
		NP.add(17);
		NP.add(18);
		NP.add(19);
		NP.add(20);
		NP.add(21);
		NP.add(22);
		NP.add(23);
		NP.add(24);
		NP.add(25);
		NP.add(26);
		NP.add(27);
		Random random = new Random();
		int randomn = random.nextInt(NP.size()-2) +1;
		int selector = NP.get(randomn);
		RandomSlotPicker = selector;
		return RandomSlotPicker;
	}
	////////////////////////////////////////////////////////////////////////////////////
	public static ItemStack ItemChooser() 
	{
		Random random = new Random();
		int randomn = random.nextInt(list.size()-1) + 1;
		ItemStack ItemStack = list.get(randomn);
		ItemStack Item = ItemStack;
		return Item;
	}
	////////////////////////////////////////////////////////////////////////////////////
	public static void loadAvalibleItems()
	{
		if(PVP_ChestConfig.PVP_Chests.get("Items") != null)
		{
			for (String ItemList : PVP_ChestConfig.PVP_Chests.getConfigurationSection("Items").getKeys(false))
			{
				ArrayList<String> i = new ArrayList<String>();

				i.add(ItemList);

				for(String Item : i)
				{
					list.add(PVP_ChestConfig.PVP_Chests.getItemStack("Items." + Item));
					Bukkit.broadcastMessage(list.size() +"");
				}
			}
		}
	}
	////////////////////////////////////////////////////////////////////////////////////
}