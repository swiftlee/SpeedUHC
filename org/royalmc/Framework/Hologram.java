package org.royalmc.Framework;

import static org.royalmc.util.TextUtils.formatText;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.royalmc.uhc.UHCMain;

public class Hologram {
	
	UHCMain plugin;
	
	public Hologram(UHCMain plugin, Player p, String deathText, Player killer)
	{
		this.plugin = plugin;
		spawnHologram(p, deathText, killer);
	}

	public void spawnHologram(Player p, String text, Player killer)
	{
		World w = p.getWorld();
		
		Location loc = null;
		if(killer instanceof Player)
		loc = new Location(w, p.getLocation().getX(), p.getLocation().getY()+1, p.getLocation().getZ(), killer.getLocation().getPitch()-180f,0);
		else
			loc = new Location(w, p.getLocation().getX(), p.getLocation().getY()+1, p.getLocation().getZ(), p.getLocation().getPitch(),0);	
		ArmorStand stand = w.spawn(loc, ArmorStand.class);
		
		stand.setSmall(true);
		stand.setCustomNameVisible(true);
		stand.setArms(true);
		stand.setVisible(true);
		
		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
		skullMeta.setOwner(p.getName());
		head.setItemMeta(skullMeta);
		stand.setHelmet(head);
		stand.setGravity(false);

		stand.setCustomName(formatText(text));
	}
	
}
