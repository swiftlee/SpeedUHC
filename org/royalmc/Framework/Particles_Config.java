package org.royalmc.Framework;

import java.io.File;
import java.io.IOException;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.royalmc.uhc.UHCMain;

public class Particles_Config 
{
	UHCMain plugin;

	public static File newConfig2;
	public static FileConfiguration Particles;

	public Particles_Config(UHCMain plugin){
		this.plugin = plugin;
	}

	public static void BaseSave()
	{
		try
		{
			Particles.save(newConfig2);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	@SuppressWarnings("deprecation")
	public static void BaseDefaults() {
		if(Particles_Config.Particles.get("Particle") == null)
		{
			try {
				Particles.addDefault("ParticlesINV_Name", "&3UHC &5Vote &9Particles");
				Particles.addDefault("Item_To_Click_To_Open_GUI.GUI_Item", Material.BLAZE_ROD.getId());
				Particles.addDefault("Item_To_Click_To_Open_GUI.Name", "&b&lClick To Vote For Particle");
				Particles.addDefault("Item_To_Click_To_Open_GUI.Slot_Number", 2);
				Particles.addDefault("Particle.1", Material.EXP_BOTTLE.getId());
				Particles.addDefault("Particle.Slot_1_Name", "&8Heart");
				Particles.addDefault("Particle.2", Material.AIR.getId());
				Particles.addDefault("Particle.Slot_2_Name", "&7NULL");
				Particles.addDefault("Particle.3", Material.WOOD_SWORD.getId());
				Particles.addDefault("Particle.Slot_3_Name", "&8Rain");
				Particles.addDefault("Particle.4", Material.BLAZE_ROD.getId());
				Particles.addDefault("Particle.Slot_4_Name", "&Flame");
				Particles.addDefault("Particle.5", Material.ENDER_PEARL.getId());
				Particles.addDefault("Particle.Slot_5_Name", "&8Fireball");
				Particles.addDefault("Particle.6", Material.REDSTONE_BLOCK.getId());
				Particles.addDefault("Particle.Slot_6_Name", "&8Rekt");
				Particles.addDefault("Particle.7", Material.DIAMOND.getId());
				Particles.addDefault("Particle.Slot_7_Name", "&8Spirit");
				Particles.addDefault("Particle.8", Material.AIR.getId());
				Particles.addDefault("Particle.Slot_8_Name", "&8NULL");
				Particles.addDefault("Particle.9", Material.BOW.getId());
				Particles.addDefault("Particle.Slot_9_Name", "&8JumpMan");
				Particles.options().copyDefaults(true);
				Particles.save(newConfig2);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}