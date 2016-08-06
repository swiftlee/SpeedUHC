package org.royalmc.uhc;

import static org.royalmc.util.TextUtils.formatText;

import java.io.File;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.royalmc.Framework.GameState;
import org.royalmc.Framework.GameType;
import org.royalmc.Framework.MySQL;
import org.royalmc.Framework.ParticlesINV;
import org.royalmc.Framework.Particles_Config;
import org.royalmc.Framework.SQLStorage.DataColumn;
import org.royalmc.Framework.VotingSystem;
import org.royalmc.Framework.Achievements.AchievementHandler;
import org.royalmc.Framework.Timers.MatchStartRunnable;
import org.royalmc.listeners.DeathEvents;
import org.royalmc.listeners.GeneralListener;
import org.royalmc.listeners.PlayerDamageListener;
import org.royalmc.listeners.PlayerInteractListener;
import org.royalmc.listeners.SpectatorGUI;
import org.royalmc.listeners.StatListener;
import org.royalmc.util.GameUtils;
import org.royalmc.util.PVP_ChestConfig;

import net.minecraft.server.v1_8_R3.BiomeBase;


public class UHCMain extends JavaPlugin {

	private final UHCMain plugin = this;
	private ItemStack specialBoots = null;
	private ItemStack spectatorCompass = null;
	private ItemStack leaveGameItem = null;
	private ItemStack gameModeItem = null;
	private ArrayList<Player> doubleCheck = new ArrayList<>();

	private static MySQL mysql;

	ArrayList<String> queries;

	//private static Connection connection = null;

	@Override
	public void onEnable(){

		//MYSQL SETUP

		/**
		 * @param hostname = ip or specified host
		 * @param port = default port (3306)
		 * @param databasename = your database name
		 * @param username = username for your database
		 * @param password = password for your database
		 */

		mysql = new MySQL(
				getConfig().getString("hostname"), 
				getConfig().getString("port"), 
				getConfig().getString("databasename"),
				getConfig().getString("username"), getConfig().getString("password"));

		try{
			/*connection = */mysql.openConnection();
		}catch(Exception e1){
			e1.printStackTrace();
		}

		try{
		  
			String query = (" CREATE TABLE IF NOT EXISTS {tablename} (playerUUID VARCHAR(255), "+DataColumn.TOTAL_KILLS+
					" INTEGER NOT NULL DEFAULT 0, "+DataColumn.TOTAL_WINS+" INTEGER NOT NULL DEFAULT 0, "+DataColumn.TOTAL_LOSSES+
					" INTEGER NOT NULL DEFAULT 0, " + DataColumn.TOTAL_COINS + " INTEGER NOT NULL DEFAULT 0, " + DataColumn.TOTAL_CRYSTALS + 
					" INTEGER NOT NULL DEFAULT 0, PRIMARY KEY (playerUUID))").replace("{tablename}", getConfig().getString("tablename"));
			query.concat(" DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;");
			//query.concat(" ALTER DATABASE {dbname} CHARACTER SET utf8 COLLATE utf8_unicode_ci;".replace("{dbname}", getConfig().getString("databasename")));
			PreparedStatement statement = mysql.getConnection().prepareStatement(query);
			statement.execute();

			query = (" CREATE TABLE IF NOT EXISTS AData (playerUUID VARCHAR(255), "
					+ DataColumn.SWIMMER_ACHIEVEMENT +
					" INTEGER NOT NULL DEFAULT 0, "
					+ DataColumn.WALKER_ACHIEVEMENT +
					" INTEGER NOT NULL DEFAULT 0, "
					+ DataColumn.FIGHTER_ACHIEVEMENT +
					" INTEGER NOT NULL DEFAULT 0, " 
					+ DataColumn.BOWKING_ACHIEVEMENT + 
					" INT NOT NULL DEFAULT 0, " 
					+ "TimeSwimmer" +
					" INT NOT NULL DEFAULT 0, " 
					+ "TimeWalker" + 
					" INT NOT NULL DEFAULT 0, " 
					+ "FighterTime" +
					" INT NOT NULL DEFAULT 0, " 
					+ "BowKingTime" +
					" INTEGER NOT NULL DEFAULT 0, "
					+ "PRIMARY KEY (playerUUID))");
			query.concat(" DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;");
			//query.concat(" ALTER DATABASE {dbname} CHARACTER SET utf8 COLLATE utf8_unicode_ci;".replace("{dbname}", getConfig().getString("databasename")));
			statement = mysql.getConnection().prepareStatement(query);
			statement.execute();

		}
		catch (SQLException e){
			e.printStackTrace();
		}
		catch (NullPointerException e){
			getLogger().warning("NullPointerException: Unable to get SQL database connection.");
		}

		try
		{

			String baseQuery = ("ALTER TABLE {tableName} ADD COLUMN {columnLabel} INTEGER NOT NULL DEFAULT 0;-").replace("{tableName}", getConfig().getString("tablename"));

			queries = new ArrayList<>();

			String query = baseQuery.replace("{columnLabel}", DataColumn.TOTAL_DEATHS.toString()) + baseQuery.replace("{columnLabel}", DataColumn.TOTAL_DAMAGE_DEALT.toString())
			+ baseQuery.replace("{columnLabel}", DataColumn.TOTAL_DAMAGE_TAKEN.toString()) + baseQuery.replace("{columnLabel}", DataColumn.TOTAL_FALL_DAMAGE.toString())
			+ baseQuery.replace("{columnLabel}", DataColumn.TOTAL_ARROWS_SHOT.toString())
			+ baseQuery.replace("{columnLabel}", DataColumn.TOTAL_HEARTS_HEALED.toString()) + baseQuery.replace("{columnLabel}", DataColumn.TOTAL_GOLDEN_APPLES.toString())
			+ baseQuery.replace("{columnLabel}", DataColumn.TOTAL_GOLDEN_HEADS.toString()) + baseQuery.replace("{columnLabel}", DataColumn.TOTAL_ENTITIES_TAMED.toString())
			+ baseQuery.replace("{columnLabel}", DataColumn.TOTAL_NETHERS_ENTERED.toString()) + baseQuery.replace("{columnLabel}", DataColumn.TOTAL_ENDS_ENTERED.toString())
			+ baseQuery.replace("{columnLabel}", DataColumn.TOTAL_BLOCKS_MINED.toString()) + baseQuery.trim().replace("{columnLabel}", DataColumn.TOTAL_ENTITIES_SLAIN.toString());

			for(String str : query.split("-"))
			{
				queries.add(str);
			}

			for(String queryList : queries)
			{
				//System.out.println(queryList);
				PreparedStatement statement = mysql.getConnection().prepareStatement(queryList);
				statement.execute();
			}
			queries.clear();
		}
		catch(Exception e)
		{

		}

		try
		{

			String baseQuery = ("ALTER TABLE {tableName} ADD COLUMN {columnLabel} BOOLEAN NOT NULL DEFAULT FALSE;-").replace("{tableName}", getConfig().getString("tablename"));

			queries = new ArrayList<>();

			String query = baseQuery.replace("{columnLabel}", DataColumn.SWIMMER_ACHIEVEMENT.toString()) + baseQuery.replace("{columnLabel}", DataColumn.WALKER_ACHIEVEMENT.toString())
			+ baseQuery.replace("{columnLabel}", DataColumn.FIGHTER_ACHIEVEMENT.toString()) + baseQuery.replace("{columnLabel}", DataColumn.BOWKING_ACHIEVEMENT.toString());


			for(String str : query.split("-"))
			{
				queries.add(str);
			}

			for(String queryList : queries)
			{
				//System.out.println(queryList);
				PreparedStatement statement = mysql.getConnection().prepareStatement(queryList);
				statement.execute();
			}
			queries.clear();
		}
		catch(Exception e)
		{

		}

		//END MYSQL SETUP

		//END MYSQL SETUP

		//Create the plugin config
		saveDefaultConfig();
		reloadConfig();

		PVP_ChestConfig.newConfig = new File(plugin.getDataFolder(), "PVP_Chests.yml");
		PVP_ChestConfig.PVP_Chests = YamlConfiguration.loadConfiguration(PVP_ChestConfig.newConfig);

		Particles_Config.newConfig2 = new File(plugin.getDataFolder(), "Particles_Config.yml");
		Particles_Config.Particles = YamlConfiguration.loadConfiguration(Particles_Config.newConfig2);
		Particles_Config.BaseDefaults();

		ParticlesINV.AddSlots();

		//Register the plugin events
		Bukkit.getPluginManager().registerEvents(new GeneralListener(this), this);
		Bukkit.getPluginManager().registerEvents(new PlayerDamageListener(this), this);
		Bukkit.getPluginManager().registerEvents(new DeathEvents(this), this);
		Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(this), this);
		Bukkit.getPluginManager().registerEvents(new VotingSystem(this), this);
		Bukkit.getPluginManager().registerEvents(new ParticlesINV(this), this);
		Bukkit.getPluginManager().registerEvents(new StatListener(this), this);
		Bukkit.getPluginManager().registerEvents(new AchievementHandler(this), this);

		/*BUNGEECORD CHANNELS REGISTRY*/
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

		//Delete old world
		try{
			File worldDirectory = new File("world");
			if(worldDirectory.exists() && worldDirectory.isDirectory()){
				deleteWorld(worldDirectory);
			}
		}catch(Exception e){e.printStackTrace();}

		//Remove oceans as possible biomes for when the world regenerates
		Field biomesField = null;
		try{
			biomesField = BiomeBase.class.getDeclaredField("biomes");
			biomesField.setAccessible(true);
			if(biomesField.get(null) instanceof BiomeBase[]){
				BiomeBase[] biomes = (BiomeBase[])biomesField.get(null);
				biomes[BiomeBase.DEEP_OCEAN.id] = BiomeBase.PLAINS;
				biomes[BiomeBase.OCEAN.id] = BiomeBase.FOREST;
				biomes[BiomeBase.SWAMPLAND.id] = BiomeBase.FOREST;
				biomes[BiomeBase.DESERT.id] = BiomeBase.FOREST;
				biomes[BiomeBase.DESERT_HILLS.id] = BiomeBase.FOREST;
				biomes[BiomeBase.PLAINS.id] = BiomeBase.FOREST;
				biomes[BiomeBase.JUNGLE.id] = BiomeBase.FOREST;
				biomes[BiomeBase.JUNGLE_EDGE.id] = BiomeBase.FOREST;
				biomes[BiomeBase.JUNGLE_HILLS.id] = BiomeBase.FOREST;
				biomes[BiomeBase.EXTREME_HILLS.id] = BiomeBase.FOREST;
				biomes[BiomeBase.EXTREME_HILLS_PLUS.id] = BiomeBase.FOREST;
				biomes[BiomeBase.MESA.id] = BiomeBase.FOREST;
				biomes[BiomeBase.MESA_PLATEAU.id] = BiomeBase.FOREST;
				biomes[BiomeBase.MESA_PLATEAU_F.id] = BiomeBase.FOREST;
				biomes[BiomeBase.MUSHROOM_ISLAND.id] = BiomeBase.FOREST;
				biomes[BiomeBase.MUSHROOM_SHORE.id] = BiomeBase.FOREST;
			}
		}catch(Exception e){e.printStackTrace();}
		
		//Load new world
		new BukkitRunnable(){public void run(){
			World preGameWorld = Bukkit.createWorld(new WorldCreator(getConfig().getString("preGameLocation.world")));
			preGameWorld.setDifficulty(Difficulty.PEACEFUL);
		}}.runTask(plugin);

		//Create special boots item
		try{specialBoots = new ItemStack(Material.valueOf(getConfig().getString("specialBoots.type")));}
		catch(Exception e){
			specialBoots = new ItemStack(Material.CHAINMAIL_BOOTS);
		}
		ItemMeta meta = specialBoots.getItemMeta();
		meta.addEnchant(Enchantment.DEPTH_STRIDER, plugin.getConfig().getInt("bootSpeed"), true);
		meta.setDisplayName(formatText(getConfig().getString("specialBoots.name")));
		List<String> lore = getConfig().getStringList("specialBoots.lore");
		if(lore != null){
			for(int i = 0; i < lore.size(); i++){
				lore.set(i, formatText(lore.get(i)));
			}
			meta.setLore(lore);
		}
		specialBoots.setItemMeta(meta);

		//Create leave-game item
		leaveGameItem = new ItemStack(Material.BED, 1);
		meta = leaveGameItem.getItemMeta();
		meta.setDisplayName(formatText(plugin.getConfig().getString("leaveGameItem.name")));
		lore = plugin.getConfig().getStringList("leaveGameItem.lore");
		if(lore != null){
			for(int i = 0; i < lore.size(); i++){
				lore.set(i, formatText(lore.get(i)));
			}
			meta.setLore(lore);
		}
		leaveGameItem.setItemMeta(meta);

		//Create gamemode Item
		try{
			gameModeItem = new ItemStack(Material.valueOf(plugin.getConfig().getString("gameModeItem.type")), 1);
		}
		catch(Exception e)
		{
			gameModeItem = new ItemStack(Material.BLAZE_POWDER, 1);
		}
		meta = gameModeItem.getItemMeta();
		meta.setDisplayName(formatText(plugin.getConfig().getString("gameModeItem.name")));
		lore = plugin.getConfig().getStringList("gameModeItem.lore");
		if(lore != null){
			for(int i = 0; i < lore.size(); i++){
				lore.set(i, formatText(lore.get(i)));
			}
			meta.setLore(lore);
		}
		gameModeItem.setItemMeta(meta);

		//Create spectator compass item
		spectatorCompass = new ItemStack(Material.COMPASS, 1);
		meta = spectatorCompass.getItemMeta();
		meta.setDisplayName(formatText(plugin.getConfig().getString("spectatorCompass.name")));
		lore = plugin.getConfig().getStringList("spectatorCompass.lore");
		if(lore != null){
			for(int i = 0; i < lore.size(); i++){
				lore.set(i, formatText(lore.get(i)));
			}
			meta.setLore(lore);
		}
		spectatorCompass.setItemMeta(meta);

		StatsCommand statsHandler = new StatsCommand(plugin);
		plugin.getCommand("stats").setExecutor(statsHandler);

		//Start lobby timer
		new MatchStartRunnable(plugin).runTaskTimer(plugin, 0, 20);

		//Initizalize SpectatorGUI listener
		new SpectatorGUI(plugin);
	}

	@Override
	public void onDisable()
	{
		org.bukkit.event.HandlerList.unregisterAll(plugin);
		try 
		{
			if(mysql.checkConnection())
			{
				mysql.closeConnection();
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		System.exit(0);
	}

	public MySQL getMySQL(){
		return mysql;
	}

	/*public boolean isTableNull()
	{
		try
		{
			String query = "SHOW TABLES LIKE {tablename};".replace("{tablename}", getConfig().getString("tablename"));
			PreparedStatement statement = mysql.getConnection().prepareStatement(query);
			ResultSet set = statement.executeQuery();
			boolean doesRowExist = set.next();
			set.close();
			statement.close();
			return doesRowExist;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		catch (NullPointerException e) 
		{
			getLogger().warning("NullPointerException: Unable to get SQL database connection.");
		}
		return true;
	}*/

	public ItemStack getSpecialBoots(){
		return specialBoots.clone();
	}

	public ItemStack getGoldenHead(){
		return DeathEvents.goldenHead.clone();
	}

	public ItemStack getLeaveGameItem(){
		return leaveGameItem.clone();
	}

	public ItemStack getSpectatorCompass(){
		return spectatorCompass.clone();
	}

	public ItemStack getGameModeItem()
	{
		return gameModeItem.clone();
	}

	public boolean isWearingSpecialBoots(HumanEntity humanEntity){
		if(humanEntity.getInventory().getBoots() != null){
			ItemStack bootsClone = specialBoots.clone();
			bootsClone.setDurability(humanEntity.getInventory().getBoots().getDurability());
			return bootsClone.isSimilar(humanEntity.getInventory().getBoots());
		}
		return false;
	}

	public boolean isPVPEnabled(){
		return GameState.getState()==GameState.PVP_PERIOD || GameState.getState()==GameState.DEATHMATCH;
	}

	//public GameTimerRunnable getGameTimerRunnable(){
	//	return gameTimerRunnable;
	//}

	//----------------------------------------------------------------------------------------------------------

	/*                                       {ALL CONFIG METHODS}
	/*/////////////////// 
	/*///////////////////
	/*/////////////////// 
	/*/////////////////// 
	/*////////////////*/

	public int getLobbyTimeLimit(){
		int value = getConfig().getInt("lobbyTimeLimit");
		if(value < 3){value = 3;}
		return value;
	}

	public int getMinimumPlayers(){
		int value = getConfig().getInt("minimumPlayers");
		if(value < 2){value = 2;}
		return value;
	}

	public int getDeathmatchTimeLimit(){
		int value = getConfig().getInt("deathmatchTimeLimit");
		if(value < 30){value = 30;}
		return value;
	}

	public int getPvPTimeLimit(){
		int value = getConfig().getInt("pvpTimeLimit");
		if(value < 30){value = 30;}
		return value;
	}

	public String getMsgLobbyActionTimer(){
		return formatText(getConfig().getString("lobbyStartTimeActionBar"));
	}

	public String getMsgLobbyChatTimer(){
		return formatText(getConfig().getString("lobbyStartTimeChat"));
	}

	public String getMsgLobbyNeedsPlayers(){
		return formatText(getConfig().getString("lobbyNeedPlayersMessage"));
	}

	public void playDeathmatchCountdownSound(Player player){
		Sound countdownSound = null;
		try{
			countdownSound = Sound.valueOf(plugin.getConfig().getString("sounds.deathmatchCountdown"));
		}catch(Exception e){
			countdownSound = Sound.NOTE_BASS_DRUM;
		}
		player.getWorld().playSound(player.getLocation(), countdownSound, 1, 1);
	}

	public void playLobbyCountdownSound(Player player, float pitch){
		Sound countdownSound = null;
		try{
			countdownSound = Sound.valueOf(plugin.getConfig().getString("sounds.lobbyCountdown"));
		}catch(Exception e){
			countdownSound = Sound.CLICK;
		}
		player.getWorld().playSound(player.getLocation(), countdownSound, 1, pitch);
	}

	/*                                       {END CONFIG METHODS}
	/*/////////////////// 
	/*///////////////////
	/*/////////////////// 
	/*/////////////////// 
	/*////////////////*/

	//----------------------------------------------------------------------------------------------------------

	/*                                       {DELETE WORLD METHOD}
	/*/////////////////// 
	/*///////////////////
	/*/////////////////// 
	/*/////////////////// 
	/*////////////////*/

	public static void deleteWorld(File file) /*@param file is the World directory.*/
	{
		if(file.isFile())
		{
			file.delete();
		}
		else if(file.isDirectory())
		{
			for(File f : file.listFiles())
			{
				deleteWorld(f);
			}

			file.delete();
		}
	}

	/*										 {END DELETE WORLD METHOD}
	/*/////////////////// 
	/*///////////////////
	/*/////////////////// 
	/*/////////////////// 
	/*////////////////*/

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
	{

		if(command.getName().equalsIgnoreCase("scenario"))
		{
			if(GameState.getState() != GameState.IN_LOBBY)
				sender.sendMessage(formatText(plugin.getConfig().getString("messageScenario")).replace("{gamemode}", GameType.getType().toString()));
			else
				sender.sendMessage(formatText(plugin.getConfig().getString("messageVotingNotOver")));

			return true;
		}

		if(command.getName().equalsIgnoreCase("addChestItem"))
		{
			if(sender instanceof Player && sender.isOp())
			{
				Player p = (Player)sender;
				p.getPlayer().sendMessage(formatText("&eAdded &6" + p.getInventory().getItemInHand() + "&e For Item &6" + args[1]));

				if(PVP_ChestConfig.PVP_Chests.contains("Items." + args[1]))
				{
					p.sendMessage(formatText("&cItem already exists in config."));
				}
				else
				{
					PVP_ChestConfig.PVP_Chests.set("Items." + args[1], p.getInventory().getItemInHand());
					PVP_ChestConfig.BaseSave();
				}

				p.getTargetBlock((HashSet<Material>) null, 50);
				p.getInventory().remove(p.getItemInHand());
				p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 20, 5);
			}
		}

		if(command.getName().equalsIgnoreCase("hub"))
		{
			if(sender instanceof Player)
			{
				Player p = (Player)sender;
				if(doubleCheck.contains(p))
				{
					doubleCheck.remove(p);
					GameUtils.sendToLobbyServer(plugin, p);
				}
				else
				{
					doubleCheck.add(p);

					Bukkit.getScheduler().runTaskLater(plugin, new Runnable() 
					{
						public void run() 
						{
							if(doubleCheck.size() > 0)
							{
								doubleCheck.remove(p);
								p.sendMessage(formatText("&eYou have been timed out from the &a/hub &ecommand."));
							}
						}

					}, getConfig().getLong("hubCommandTimeout"));

					p.sendMessage(formatText(getConfig().getString("messageToHub")));
				}
			}
			return true;
		}
		return false;
	}
}
