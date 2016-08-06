package org.royalmc.Framework;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.royalmc.uhc.UHCMain;
import org.royalmc.util.ScoreboardUtil;
import org.royalmc.util.ScoreboardUtil.ScoreType;

public class WorldBorders
{

	private final UHCMain plugin;

	public WorldBorders(UHCMain plugin)
	{
		this.plugin = plugin;
	}

	/**
	 * 		@param length is equal to TWO TIMES the actual length of a side of a border.
	 * 		EX: 450x450 box would be set as 
	 * 		@param length = 900
	 * */

	public void setWorldBorders()
	{

		double length;
		double damage;

		length = plugin.getConfig().getDouble("worldborders.maingame.length");
		damage = plugin.getConfig().getDouble("worldborders.damage");

		WorldBorder borderPerPlayer = null;

		for(Player p : Bukkit.getOnlinePlayers())
		{		
			World world = p.getWorld();
			WorldBorder border = world.getWorldBorder();
			border.setCenter(0,0); 
			border.setDamageAmount(damage); //CONFIGURABLE
			border.setSize(length); //CONFIGURABLE
			border.setDamageBuffer(0);
			borderPerPlayer = border;
			break;
		}

		for(Player p : Bukkit.getOnlinePlayers())
		{
			if(borderPerPlayer.getSize() > plugin.getConfig().getDouble("worldborders.maingame.length"))
				ScoreboardUtil.setScore(p, ScoreType.BORDER,(int)(plugin.getConfig().getDouble("worldborders.maingame.length")/2) + "x" + (int)(plugin.getConfig().getDouble("worldborders.maingame.length")/2), plugin);
			else
				ScoreboardUtil.setScore(p, ScoreType.BORDER,(int)(borderPerPlayer.getSize()/2) + "x" + (int)(borderPerPlayer.getSize()/2), plugin);
		}
	}

	@SuppressWarnings("deprecation")
	public void shrinkWorldBorders()
	{

		World worldw = Bukkit.getWorld("world");
		WorldBorder borderPerPlayer = worldw.getWorldBorder();

		for(Player p : Bukkit.getOnlinePlayers())
		{
			World world = p.getWorld();
			WorldBorder border = world.getWorldBorder();
			border.setSize(border.getSize() - plugin.getConfig().getDouble("worldborders.shrinkInterval"), plugin.getConfig().getLong("worldborders.shrinkTime"));
			break;
		}

		if(GameState.getState() != GameState.DEATHMATCH)
		{
			for(int i = 0; i < plugin.getConfig().getLong("worldborders.shrinkTime")*20; i+=20)
			{
				Bukkit.getScheduler().runTaskLater(plugin, new Runnable() 
				{
					public void run() 
					{
						for(Player p : Bukkit.getOnlinePlayers())
						{
							ScoreboardUtil.setScore(p, ScoreType.BORDER,(int)(borderPerPlayer.getSize()/2) + "x" + (int)(borderPerPlayer.getSize()/2), plugin);
						}
					}
				}, i);
			}
		}
		else
		{
			Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable()
			{	
				public void run()
				{
					for(Player p : Bukkit.getOnlinePlayers())
					{
						World world = p.getWorld();
						WorldBorder border = world.getWorldBorder();
						border.setSize(border.getSize() - 2, plugin.getConfig().getLong("worldborders.deathmatch.shrinkTime")*20);
						break;
					}
				}
			}, 0, 20*(plugin.getConfig().getLong("worldborders.deathmatch.shrinkintervalDeathmatch")));

			for(int i = 0; i < plugin.getConfig().getLong("worldborders.deathmatch.shrinkintervalDeathmatch")*20; i+=20)
			{
				Bukkit.getScheduler().runTaskLater(plugin, new Runnable() 
				{
					public void run() 
					{
						for(Player p : Bukkit.getOnlinePlayers())
						{
							ScoreboardUtil.setScore(p, ScoreType.BORDER,(int)(borderPerPlayer.getSize()/2) + "x" + (int)(borderPerPlayer.getSize()/2), plugin);
						}
					}
				}, i);
			}
		}
	}
}