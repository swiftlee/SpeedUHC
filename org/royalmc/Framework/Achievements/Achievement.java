package org.royalmc.Framework.Achievements;

import static org.royalmc.util.TextUtils.formatText;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.entity.Player;
import org.royalmc.Framework.SQLStorage.DataColumn;
import org.royalmc.uhc.UHCMain;

public abstract class Achievement {

	UHCMain plugin;
	
	public Achievement(){}
	public Achievement(UHCMain plugin)
	{
		this.plugin = plugin;
	}

	protected String name;
	protected String description;
	protected int coins;

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public int getCoins() {
		return coins;
	}

	public void reward(Player player, UHCMain plugin) {

		String query = ("INSERT INTO SpeedUHCTable (playerUUID, {columnLabel}) VALUES ('{uuid}', TRUE) ON DUPLICATE KEY UPDATE {columnLabel} = TRUE;")
				.replace("{uuid}", player.getUniqueId().toString())
				.replace("{columnLabel}", name);
		try
		{
			PreparedStatement statement = plugin.getMySQL().getConnection().prepareStatement(query);
			statement.execute();
			
			player.sendMessage(formatText("&aYou have completed achievement: &e&l{name}").replace("{name}", name));
			player.sendMessage(formatText("&7Claim your reward in the &7&ngame-lobby&7!"));
			
			query = ("INSERT INTO SpeedUHCTable (playerUUID, {columnLabel}) VALUES ('{uuid}', TRUE) ON DUPLICATE KEY UPDATE {columnLabel} = TRUE;")
					.replace("{uuid}", player.getUniqueId().toString())
					.replace("{columnLabel}", DataColumn.ACHIEVEMENTS_AVAILABLE.toString());
			
			statement = plugin.getMySQL().getConnection().prepareStatement(query);
			statement.execute();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			player.sendMessage(formatText("&cError giving achievement! Contact an administrator about achievement: &f{name}").replace("{name}", name));
		}
	}
}
