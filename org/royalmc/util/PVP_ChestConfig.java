package org.royalmc.util;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.royalmc.uhc.UHCMain;

public class PVP_ChestConfig 
{
	UHCMain plugin;

	public static File newConfig;
	public static FileConfiguration PVP_Chests;

	public PVP_ChestConfig(UHCMain plugin){
		this.plugin = plugin;
	}

	public static void BaseSave()
	{
		try
		{
			PVP_Chests.save(newConfig);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
