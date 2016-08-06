package org.royalmc.Framework.Timers;

import static org.royalmc.util.TextUtils.formatText;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.royalmc.uhc.UHCMain;

public class TipsRunnable extends BukkitRunnable {

	UHCMain plugin;
	List<String> tips;
	
	public TipsRunnable(UHCMain plugin)
	{
		
		this.tips = plugin.getConfig().getStringList("tips");
		this.plugin = plugin;
	}
	
	@Override
	public void run() 
	{
		Bukkit.broadcastMessage(chooseRandomTip());
	}
	
	String chooseRandomTip()
	{
		Random r = new Random();
		String tip = tips.get(r.nextInt(tips.size()));
		return formatText(tip);
	}

}
