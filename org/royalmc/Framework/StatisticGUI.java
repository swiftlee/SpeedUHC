package org.royalmc.Framework;

import static org.royalmc.util.TextUtils.formatText;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.royalmc.Framework.SQLStorage.DataColumn;
import org.royalmc.uhc.UHCMain;

public class StatisticGUI implements Listener{

	Player player;
	Inventory statInv;
	UHCMain plugin;

	int kills;
	int wins;
	int losses;
	int deaths;
	int damageDealt;
	int damageTaken;
	int fallTaken;
	int arrowsShot;
	int heartsHealed;
	int applesEaten;
	int headsEaten;
	int entitiesTamed;
	int nethersEntered;
	int endsEntered;
	int blocksMined;
	int entitiesSlain;

	@SuppressWarnings("unused")
	public StatisticGUI(Player player, UHCMain plugin){

		try {
			PreparedStatement statement = plugin.getMySQL().getConnection().prepareStatement(("SELECT * FROM {tablename} WHERE playerUUID = '" + player.getUniqueId().toString() + "'").replace("{tablename}", plugin.getConfig().getString("tablename")));
			ResultSet result = statement.executeQuery();

			while(result.next()){
				//TOTAL_KILLS, TOTAL_WINS, TOTAL_LOSSES, TOTAL_DEATHS, TOTAL_DAMAGE_DEALT, TOTAL_DAMAGE_TAKEN, 
				//TOTAL_FALL_DAMAGE, TOTAL_ARROWS_SHOT, TOTAL_HEARTS_HEALED, TOTAL_GOLDEN_APPLES, TOTAL_GOLDEN_HEADS,
				//TOTAL_ENTITIES_TAMED, TOTAL_NETHERS_ENTERED, TOTAL_ENDS_ENTERED, TOTAL_BLOCKS_MINED, TOTAL_ENTITIES_SLAIN;

				kills = result.getInt(DataColumn.TOTAL_KILLS.toString());
				wins = result.getInt(DataColumn.TOTAL_WINS.toString());
				losses = result.getInt(DataColumn.TOTAL_LOSSES.toString());
				int coins = result.getInt(DataColumn.TOTAL_COINS.toString());
				int crystals = result.getInt(DataColumn.TOTAL_CRYSTALS.toString());
				deaths = result.getInt(DataColumn.TOTAL_DEATHS.toString());
				damageDealt = result.getInt(DataColumn.TOTAL_DAMAGE_DEALT.toString());
				damageTaken = result.getInt(DataColumn.TOTAL_DAMAGE_TAKEN.toString());
				fallTaken = result.getInt(DataColumn.TOTAL_FALL_DAMAGE.toString());
				arrowsShot = result.getInt(DataColumn.TOTAL_ARROWS_SHOT.toString());
				heartsHealed = result.getInt(DataColumn.TOTAL_HEARTS_HEALED.toString());
				applesEaten = result.getInt(DataColumn.TOTAL_GOLDEN_APPLES.toString());
				headsEaten = result.getInt(DataColumn.TOTAL_GOLDEN_HEADS.toString());
				entitiesTamed = result.getInt(DataColumn.TOTAL_ENTITIES_TAMED.toString());
				nethersEntered = result.getInt(DataColumn.TOTAL_NETHERS_ENTERED.toString());
				endsEntered = result.getInt(DataColumn.TOTAL_ENDS_ENTERED.toString());
				blocksMined = result.getInt(DataColumn.TOTAL_BLOCKS_MINED.toString());
				entitiesSlain = result.getInt(DataColumn.TOTAL_ENTITIES_SLAIN.toString());
				boolean diamondless = result.getBoolean(DataColumn.DIAMONDLESS.toString());
				boolean goldless = result.getBoolean(DataColumn.GOLDLESS.toString());
				boolean horseless = result.getBoolean(DataColumn.HORSELESS.toString());
				boolean rodless = result.getBoolean(DataColumn.RODLESS.toString());
				boolean doubleores = result.getBoolean(DataColumn.DOUBLEORES.toString());
				boolean achievementsAvailable = result.getBoolean(DataColumn.ACHIEVEMENTS_AVAILABLE.toString());
				boolean heart = result.getBoolean(DataColumn.HEART.toString());
				boolean rain = result.getBoolean(DataColumn.RAIN.toString());
				boolean fireball = result.getBoolean(DataColumn.FIREBALL.toString());
				boolean spirit = result.getBoolean(DataColumn.SPIRIT.toString());
				boolean jumpman = result.getBoolean(DataColumn.JUMPMAN.toString());
				boolean flame = result.getBoolean(DataColumn.FLAME.toString());
				boolean rekt = result.getBoolean(DataColumn.REKT.toString());
				boolean swimmer = result.getBoolean(DataColumn.SWIMMER_ACHIEVEMENT.toString());
				boolean walker = result.getBoolean(DataColumn.WALKER_ACHIEVEMENT.toString());
				boolean fighter = result.getBoolean(DataColumn.FIGHTER_ACHIEVEMENT.toString());
				boolean bowking = result.getBoolean(DataColumn.BOWKING_ACHIEVEMENT.toString());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} 

		this.player = player;
		this.plugin = plugin;
		statInv = Bukkit.createInventory(null, 54, formatText(plugin.getConfig().getString("guiNameColor") + player.getName() + "'s Statistics"));
		constructGUI();
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	private void constructGUI(){		

		//Wins, Kills, KDR, damage dealt, damage taken,
		//taken fall dmg, Arrows shot,
		//hearts healed, golden aps eaten, gold "heads" eaten, 
		//horses tamed, nethers + ends entered, blocks mined, mobs/animals slain:

		ItemStack wins = new ItemStack(Material.valueOf(plugin.getConfig().getString("winsStat.itemType")));
		ItemMeta winsMeta = wins.getItemMeta();

		winsMeta.setDisplayName(formatText(plugin.getConfig().getString("winsStat.nameColor")) + "Wins: " + this.wins);

		wins.setItemMeta(winsMeta);

		statInv.setItem(plugin.getConfig().getInt("winsStat.itemSlot"), wins);

		ItemStack loses = new ItemStack(Material.valueOf(plugin.getConfig().getString("lossesStat.itemType")));
		ItemMeta losesMeta = loses.getItemMeta();

		losesMeta.setDisplayName(formatText(plugin.getConfig().getString("lossesStat.nameColor")) + "Losses: " + losses);
		loses.setItemMeta(losesMeta);

		statInv.setItem(plugin.getConfig().getInt("lossesStat.itemSlot"), loses);

		ItemStack gamesPlayed = new ItemStack(Material.valueOf(plugin.getConfig().getString("gamesPlayedStat.itemType")));
		ItemMeta gamesPlayedMeta = gamesPlayed.getItemMeta();
		gamesPlayedMeta.setDisplayName(formatText(plugin.getConfig().getString("gamesPlayedStat.nameColor")) + "Games Played: " + (losses + this.wins));
		gamesPlayed.setItemMeta(gamesPlayedMeta);

		statInv.setItem(plugin.getConfig().getInt("gamesPlayedStat.itemSlot"), gamesPlayed);

		ItemStack kills = new ItemStack(Material.valueOf(plugin.getConfig().getString("killsStat.itemType")));
		ItemMeta killsMeta = kills.getItemMeta();
		killsMeta.setDisplayName(formatText(plugin.getConfig().getString("killsStat.nameColor")) + "Kills: " + this.kills);
		kills.setItemMeta(killsMeta);

		statInv.setItem(plugin.getConfig().getInt("killsStat.itemSlot"), kills);

		ItemStack kDR = new ItemStack(Material.valueOf(plugin.getConfig().getString("kDRStat.itemType")));
		ItemMeta kDRMeta = kDR.getItemMeta();
		DecimalFormat df = new DecimalFormat("#.###");
		kDRMeta.setDisplayName(formatText(plugin.getConfig().getString("kDRStat.nameColor")) + "KDR: " + df.format((deaths > 0 ? this.kills/deaths : 0.000)));
		kDR.setItemMeta(kDRMeta);

		statInv.setItem(plugin.getConfig().getInt("kDRStat.itemSlot"), kDR);

		ItemStack damageDealt = new ItemStack(Material.valueOf(plugin.getConfig().getString("damageDealtStat.itemType")));
		ItemMeta damageDealtMeta = damageDealt.getItemMeta();
		damageDealtMeta.setDisplayName(formatText(plugin.getConfig().getString("damageDealtStat.nameColor")) + "Damage Dealt: " + this.damageDealt);
		damageDealt.setItemMeta(damageDealtMeta);

		statInv.setItem(plugin.getConfig().getInt("damageDealtStat.itemSlot"), damageDealt);

		ItemStack damageTaken = new ItemStack(Material.valueOf(plugin.getConfig().getString("damageTakenStat.itemType")));
		ItemMeta damageTakenMeta = damageTaken.getItemMeta();
		damageTakenMeta.setDisplayName(formatText(plugin.getConfig().getString("damageTakenStat.nameColor")) + "Damage Taken: " + this.damageTaken);
		damageTaken.setItemMeta(damageTakenMeta);

		statInv.setItem(plugin.getConfig().getInt("damageTakenStat.itemSlot"), damageTaken);

		ItemStack fallDamageTaken = new ItemStack(Material.valueOf(plugin.getConfig().getString("fallDamageTakenStat.itemType")));
		ItemMeta fallDamageTakenMeta = fallDamageTaken.getItemMeta();
		fallDamageTakenMeta.setDisplayName(formatText(plugin.getConfig().getString("fallDamageTakenStat.nameColor")) + "Fall Damage Taken: " + fallTaken);
		fallDamageTaken.setItemMeta(fallDamageTakenMeta);

		statInv.setItem(plugin.getConfig().getInt("fallDamageTakenStat.itemSlot"), fallDamageTaken);

		ItemStack arrowsShot = new ItemStack(Material.valueOf(plugin.getConfig().getString("arrowsShotStat.itemType")));
		ItemMeta arrowsShotMeta = arrowsShot.getItemMeta();
		arrowsShotMeta.setDisplayName(formatText(plugin.getConfig().getString("arrowsShotStat.nameColor")) + "Arrows Shot: " + this.arrowsShot);
		arrowsShot.setItemMeta(arrowsShotMeta);

		statInv.setItem(plugin.getConfig().getInt("arrowsShotStat.itemSlot"), arrowsShot);

		ItemStack heartsHealed = new ItemStack(Material.valueOf(plugin.getConfig().getString("heartsHealedStat.itemType")));
		ItemMeta heartsHealedMeta = heartsHealed.getItemMeta();
		heartsHealedMeta.setDisplayName(formatText(plugin.getConfig().getString("heartsHealedStat.nameColor")) + "Hearts Healed: " + this.heartsHealed);
		heartsHealed.setItemMeta(heartsHealedMeta);

		statInv.setItem(plugin.getConfig().getInt("heartsHealedStat.itemSlot"), heartsHealed);

		ItemStack applesEaten = new ItemStack(Material.valueOf(plugin.getConfig().getString("applesEatenStat.itemType")));
		ItemMeta applesEatenMeta = applesEaten.getItemMeta();
		applesEatenMeta.setDisplayName(formatText(plugin.getConfig().getString("applesEatenStat.nameColor")) + "Golden Apples Eaten: " + this.applesEaten);
		applesEaten.setItemMeta(applesEatenMeta);

		statInv.setItem(plugin.getConfig().getInt("applesEatenStat.itemSlot"), applesEaten);

		ItemStack headsEaten = new ItemStack(Material.valueOf(plugin.getConfig().getString("headsEatenStat.itemType")));
		ItemMeta headsEatenMeta = headsEaten.getItemMeta();
		headsEatenMeta.setDisplayName(formatText(plugin.getConfig().getString("headsEatenStat.nameColor")) + "Golden Heads Eaten: " + this.headsEaten);
		headsEaten.setItemMeta(headsEatenMeta);

		statInv.setItem(plugin.getConfig().getInt("headsEatenStat.itemSlot"), headsEaten);

		ItemStack entitiesTamed = new ItemStack(Material.valueOf(plugin.getConfig().getString("entitiesTamedStat.itemType")));
		ItemMeta entitiesTamedMeta = entitiesTamed.getItemMeta();
		entitiesTamedMeta.setDisplayName(formatText(plugin.getConfig().getString("entitiesTamedStat.nameColor")) + "Entities Tamed: " + this.entitiesTamed);
		entitiesTamed.setItemMeta(entitiesTamedMeta);

		statInv.setItem(plugin.getConfig().getInt("entitiesTamedStat.itemSlot"), entitiesTamed);

		ItemStack nethersEntered = new ItemStack(Material.valueOf(plugin.getConfig().getString("nethersEnteredStat.itemType")));
		ItemMeta nethersEnteredMeta = nethersEntered.getItemMeta();
		nethersEnteredMeta.setDisplayName(formatText(plugin.getConfig().getString("nethersEnteredStat.nameColor")) + "Nethers Entered: " + this.nethersEntered);
		nethersEntered.setItemMeta(nethersEnteredMeta);

		statInv.setItem(plugin.getConfig().getInt("nethersEnteredStat.itemSlot"), nethersEntered);

		ItemStack endsEntered = new ItemStack(Material.valueOf(plugin.getConfig().getString("endsEnteredStat.itemType")));
		ItemMeta endsEnteredMeta = endsEntered.getItemMeta();
		endsEnteredMeta.setDisplayName(formatText(plugin.getConfig().getString("endsEnteredStat.nameColor")) + "Ends Entered: " + this.endsEntered);
		endsEntered.setItemMeta(endsEnteredMeta);

		statInv.setItem(plugin.getConfig().getInt("endsEnteredStat.itemSlot"), endsEntered);

		ItemStack blocksMined = new ItemStack(Material.valueOf(plugin.getConfig().getString("blocksMinedStat.itemType")));
		ItemMeta blocksMinedMeta = blocksMined.getItemMeta();
		blocksMinedMeta.setDisplayName(formatText(plugin.getConfig().getString("blocksMinedStat.nameColor")) + "Blocks Mined: " + this.blocksMined);
		blocksMined.setItemMeta(blocksMinedMeta);

		statInv.setItem(plugin.getConfig().getInt("blocksMinedStat.itemSlot"), blocksMined);

		ItemStack entitiesKilled = new ItemStack(Material.valueOf(plugin.getConfig().getString("entitiesKilledStat.itemType")));
		ItemMeta entitiesKilledMeta = entitiesKilled.getItemMeta();
		entitiesKilledMeta.setDisplayName(formatText(plugin.getConfig().getString("entitiesKilledStat.nameColor")) + "Mobs/Animals Slain: " + this.entitiesSlain);
		entitiesKilled.setItemMeta(entitiesKilledMeta);

		statInv.setItem(plugin.getConfig().getInt("entitiesKilledStat.itemSlot"), entitiesKilled);
	}

	@EventHandler
	private void onClick(InventoryClickEvent evt){
		if(evt.getClickedInventory() == statInv){
			evt.setCancelled(true);
		}
	}

	public void open(Player player){
		player.openInventory(statInv);
	}


}
