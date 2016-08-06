package org.royalmc.Framework.Timers;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.royalmc.listeners.ChatDisable;
import org.royalmc.uhc.UHCMain;
import org.royalmc.util.TextUtils;

public class RulesRunnable extends BukkitRunnable{

	@SuppressWarnings("unused")
	private ChatDisable disableChat;
	private int ruleNumber = 0;
	private List<String> rules;
	private UHCMain plugin;

	public RulesRunnable(UHCMain plugin){

		this.plugin = plugin;

		//List<String> rule = plugin.getConfig().getStringList("rules");
		rules =  plugin.getConfig().getStringList("rules");

		disableChat = new ChatDisable(plugin, rules.size()*6, "Chat has been silenced while the rules display.");

	}

	@Override
	public void run() {

		if(!(ruleNumber+1 > rules.size())){
			Bukkit.getServer().broadcastMessage(TextUtils.prefix(rules.get(ruleNumber), plugin));
			ruleNumber++;
		}
		else
			this.cancel();
	}
}
