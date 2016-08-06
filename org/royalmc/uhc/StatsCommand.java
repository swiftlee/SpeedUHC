package org.royalmc.uhc;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royalmc.Framework.StatisticGUI;

import net.md_5.bungee.api.ChatColor;

public class StatsCommand implements CommandExecutor {
	
	UHCMain plugin;
	
	public StatsCommand(UHCMain plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String base, String[] args) {
	
		if (base.equalsIgnoreCase("stats")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				
				if(args.length == 0){//player is checking their own stats
					StatisticGUI stats = new StatisticGUI(player, plugin);
					stats.open(player);
				}
				if(args.length >= 1){//player is checking another's stats
					if(Bukkit.getPlayer(args[0]) != null){
						StatisticGUI playerStats = new StatisticGUI(Bukkit.getPlayer(args[0]), plugin);
						playerStats.open(player);
					}else{
						player.sendMessage(ChatColor.RED + "That player is not online.");
					}
					
				}
			}
			
			return true;
		}
		return false;
	}
}
