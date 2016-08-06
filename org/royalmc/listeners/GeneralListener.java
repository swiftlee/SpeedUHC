package org.royalmc.listeners;

import static org.royalmc.util.TextUtils.formatText;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.royalmc.Framework.GameState;
import org.royalmc.Framework.GameType;
import org.royalmc.Framework.PluginMessaging;
import org.royalmc.uhc.GamePlayers;
import org.royalmc.uhc.UHCMain;
import org.royalmc.util.GameUtils;
import org.royalmc.util.ScoreboardUtil;


public class GeneralListener implements Listener
{
	private final UHCMain plugin;
	private final List<Material> oreTypes = new ArrayList<Material>();
	private static boolean joiningAllowed = true;
	private static boolean setInQueue = false;

	public GeneralListener(UHCMain plugin){
		this.plugin = plugin;
		addOresToList();
	}

	public void addOresToList()
	{
		oreTypes.add(Material.GOLD_ORE);
		oreTypes.add(Material.IRON_ORE);
		oreTypes.add(Material.DIAMOND_ORE);
		oreTypes.add(Material.COAL_ORE);
		oreTypes.add(Material.EMERALD_ORE);
		oreTypes.add(Material.GRAVEL);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	private void onDropItem(PlayerDropItemEvent event){
		if(GameState.getState()==GameState.IN_LOBBY || GamePlayers.getDeadPlayers().contains(event.getPlayer())){
			event.setCancelled(true);
		}
	}

	@EventHandler
	private void onInventoryClick(InventoryClickEvent event){
		if(GameState.getState()==GameState.IN_LOBBY || GamePlayers.getDeadPlayers().contains(event.getWhoClicked())){
			event.setCancelled(true);
		}
	}

	public void spawnItem(ItemStack stack, Block p)
	{
		p.getWorld().dropItem(p.getLocation(), stack);
	}

	@EventHandler
	public void onEntityTame(EntityTameEvent e)
	{
		if(GameType.getType() == GameType.HORSELESS)
		{
			if(e.getEntityType() == EntityType.HORSE)
			{
				e.setCancelled(true);
				for(Entity entity : e.getEntity().getNearbyEntities(5.0, 5.0, 5.0))
				{
					if(entity instanceof Player)
					{
						Player p = (Player)entity;
						p.sendMessage(formatText("&cYou cannot tame a horse while &f&lHorseless &cis active!"));
					}
				}
			}
		}
	}

	@EventHandler
	public void onRegen(EntityRegainHealthEvent e)
	{
		if(e.getRegainReason() == RegainReason.SATIATED || e.getRegainReason() == RegainReason.REGEN)
		{
			e.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onOreBreak(BlockBreakEvent e)
	{

		Material type = e.getBlock().getType();

		if(type == Material.ARMOR_STAND)
		{
			e.setCancelled(true);
		}
		
		if(GameType.getType() == GameType.DIAMONDLESS)
		{
			if(type == Material.DIAMOND_ORE)
			{
				ExperienceOrb exp = e.getBlock().getWorld().spawn(e.getBlock().getLocation(), ExperienceOrb.class);
				exp.setExperience(e.getExpToDrop());
				e.getBlock().breakNaturally(new ItemStack(Material.AIR));
			}
		}
		else if(GameType.getType() == GameType.GOLDLESS)
		{
			if(type == Material.GOLD_ORE)
			{
				ExperienceOrb exp = e.getBlock().getWorld().spawn(e.getBlock().getLocation(), ExperienceOrb.class);
				exp.setExperience(e.getExpToDrop());
				e.getBlock().breakNaturally(new ItemStack(Material.AIR));
			}
		}
		else if(GameType.getType() == GameType.DOUBLEORES)
		{
			if(type == Material.GOLD_ORE)
			{
				int n = e.getBlock().getDrops().size()*2;
				spawnItem(new ItemStack(Material.GOLD_INGOT, n-1), e.getBlock());
			}
			else if(type == Material.IRON_ORE)
			{
				int n = e.getBlock().getDrops().size()*2;
				spawnItem(new ItemStack(Material.IRON_INGOT, n-1), e.getBlock());
			}
			else if(type == Material.DIAMOND_ORE)
			{
				int n = e.getBlock().getDrops().size()*2;
				spawnItem(new ItemStack(Material.DIAMOND, n-1), e.getBlock());
			}

		}

		if(oreTypes.contains(e.getBlock().getType()) && GameType.getType() != GameType.DOUBLEORES)
		{
			if(type == Material.GOLD_ORE)
			{
				e.getBlock().breakNaturally(new ItemStack(Material.AIR));
				spawnItem(new ItemStack(Material.GOLD_INGOT), e.getBlock());
			}
			else if(type == Material.IRON_ORE)
			{
				e.getBlock().breakNaturally(new ItemStack(Material.AIR));
				spawnItem(new ItemStack(Material.IRON_INGOT), e.getBlock());
			}
			else if(type == Material.GRAVEL)
			{
				e.getBlock().breakNaturally(new ItemStack(Material.AIR));
				spawnItem(new ItemStack(Material.FLINT), e.getBlock());
			}
		}
	}

	@EventHandler
	public void onInteractWithEntity(PlayerArmorStandManipulateEvent e)
	{
		if(GameState.getState() == GameState.PVP_PERIOD || GameState.getState() == GameState.DEATHMATCH || GameState.getState() == GameState.GAME_OVER || GameState.getState() == GameState.IN_LOBBY)
		{
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onInteractWithEntity(EntityInteractEvent e)
	{
		if(GameState.getState() == GameState.PVP_PERIOD || GameState.getState() == GameState.DEATHMATCH || GameState.getState() == GameState.GAME_OVER  || GameState.getState() == GameState.IN_LOBBY)
		{
			if(e.getEntityType() == EntityType.ARMOR_STAND)
			{
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onItemDrop(ItemSpawnEvent e)
	{	
		if(oreTypes.contains(e.getEntityType()))
		{
			e.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockPlace(BlockPlaceEvent event) {
		if(GameState.getState()==GameState.IN_LOBBY || !GamePlayers.getAlivePlayers().contains(event.getPlayer())){
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockBreak(BlockBreakEvent event) {
		if(GameState.getState()==GameState.IN_LOBBY || !GamePlayers.getAlivePlayers().contains(event.getPlayer())){
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(!GamePlayers.getAlivePlayers().contains(event.getPlayer())){
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		if(GameState.getState()==GameState.IN_LOBBY || !GamePlayers.getAlivePlayers().contains(event.getPlayer())){
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerDamaged(EntityDamageEvent e)
	{
		if(GameState.getState() == GameState.IN_LOBBY)
		{
			e.setCancelled(true);
		}
	}

	/*@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerHurt(EntityDamageEvent event){
		if(event.getEntity() instanceof Player){
			new BukkitRunnable(){public void run(){
				ScoreboardUtil.updateTabHealth((Player)event.getEntity());
			}}.runTaskLater(plugin, 1);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerRegen(EntityRegainHealthEvent event){
		if(event.getEntity() instanceof Player){
			new BukkitRunnable(){public void run(){
				ScoreboardUtil.updateTabHealth((Player)event.getEntity());
			}}.runTaskLater(plugin, 1);
		}
	}*/

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerConnect(AsyncPlayerPreLoginEvent event) {
		if(joiningAllowed){
			if(Bukkit.getOnlinePlayers().size() < 32){
				if(event.getLoginResult()==Result.KICK_FULL){
					event.setLoginResult(Result.ALLOWED);
				}
			}else{
				if(event.getLoginResult()==Result.ALLOWED){
					event.setLoginResult(Result.KICK_FULL);
				}
			}
		}else{
			event.setLoginResult(Result.KICK_OTHER);
			event.setKickMessage(formatText("\n&4The game is currently in progress."));
		}
	}

	@EventHandler
	public void onItemCraft(PrepareItemCraftEvent  e)
	{
		Material type = e.getRecipe().getResult().getType();
		if (type == Material.DIAMOND_AXE)
		{
			ItemStack stack  = new ItemStack(Material.DIAMOND_AXE);
			ItemMeta meta = stack.getItemMeta();
			meta.addEnchant(Enchantment.DURABILITY, 3, false);
			meta.addEnchant(Enchantment.DIG_SPEED, 5, false);
			stack.setItemMeta(meta);
			e.getInventory().setResult(stack);
		}
		else if(type == Material.DIAMOND_PICKAXE)
		{
			ItemStack stack  = new ItemStack(Material.DIAMOND_PICKAXE);
			ItemMeta meta = stack.getItemMeta();
			meta.addEnchant(Enchantment.DURABILITY, 3, false);
			meta.addEnchant(Enchantment.DIG_SPEED, 5, false);
			stack.setItemMeta(meta);
			e.getInventory().setResult(stack);
		}
		else if(type == Material.GOLD_AXE)
		{
			ItemStack stack  = new ItemStack(Material.GOLD_AXE);
			ItemMeta meta = stack.getItemMeta();
			meta.addEnchant(Enchantment.DURABILITY, 3, false);
			meta.addEnchant(Enchantment.DIG_SPEED, 4, false);
			stack.setItemMeta(meta);
			e.getInventory().setResult(stack);
		}
		else if(type == Material.GOLD_PICKAXE)
		{
			ItemStack stack  = new ItemStack(Material.GOLD_PICKAXE);
			ItemMeta meta = stack.getItemMeta();
			meta.addEnchant(Enchantment.DURABILITY, 3, false);
			meta.addEnchant(Enchantment.DIG_SPEED, 4, false);
			stack.setItemMeta(meta);
			e.getInventory().setResult(stack);
		}
		else if(type == Material.IRON_AXE)
		{
			ItemStack stack  = new ItemStack(Material.IRON_AXE);
			ItemMeta meta = stack.getItemMeta();
			meta.addEnchant(Enchantment.DURABILITY, 3, false);
			meta.addEnchant(Enchantment.DIG_SPEED, 3, false);
			stack.setItemMeta(meta);
			e.getInventory().setResult(stack);
		}
		else if(type == Material.IRON_PICKAXE)
		{
			ItemStack stack  = new ItemStack(Material.IRON_PICKAXE);
			ItemMeta meta = stack.getItemMeta();
			meta.addEnchant(Enchantment.DURABILITY, 3, false);
			meta.addEnchant(Enchantment.DIG_SPEED, 3, false);
			stack.setItemMeta(meta);
			e.getInventory().setResult(stack);
		}
		else if(type == Material.STONE_AXE)
		{
			ItemStack stack  = new ItemStack(Material.STONE_AXE);
			ItemMeta meta = stack.getItemMeta();
			meta.addEnchant(Enchantment.DURABILITY, 3, false);
			meta.addEnchant(Enchantment.DIG_SPEED, 2, false);
			stack.setItemMeta(meta);
			e.getInventory().setResult(stack);
		}
		else if(type == Material.STONE_PICKAXE)
		{
			ItemStack stack  = new ItemStack(Material.STONE_PICKAXE);
			ItemMeta meta = stack.getItemMeta();
			meta.addEnchant(Enchantment.DURABILITY, 3, false);
			meta.addEnchant(Enchantment.DIG_SPEED, 2, false);
			stack.setItemMeta(meta);
			e.getInventory().setResult(stack);
		}
		else if(type == Material.WOOD_AXE)
		{
			ItemStack stack  = new ItemStack(Material.WOOD_AXE);
			ItemMeta meta = stack.getItemMeta();
			meta.addEnchant(Enchantment.DURABILITY, 3, false);
			meta.addEnchant(Enchantment.DIG_SPEED, 1, false);
			stack.setItemMeta(meta);
			e.getInventory().setResult(stack);
		}
		else if(type == Material.WOOD_PICKAXE)
		{
			ItemStack stack  = new ItemStack(Material.WOOD_PICKAXE);
			ItemMeta meta = stack.getItemMeta();
			meta.addEnchant(Enchantment.DURABILITY, 3, false);
			meta.addEnchant(Enchantment.DIG_SPEED, 1, false);
			stack.setItemMeta(meta);
			e.getInventory().setResult(stack);
		}

		if(GameType.getType() == GameType.RODLESS)
		{
			if(e.getRecipe().getResult().getType() == Material.FISHING_ROD)
			{
				e.getInventory().setResult(new ItemStack(Material.AIR));
				for(HumanEntity p : e.getViewers())
				{
					Player player = (Player)p;
					player.sendMessage(formatText("&cYou cannot craft that item while &f&lRodless &cmode is enabled!"));
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent event){
		if(!joiningAllowed){
			new BukkitRunnable(){public void run(){
				event.getPlayer().kickPlayer(formatText("&4Unable to join.\n&fThe game is currently in progress."));
			}}.runTaskLater(plugin, 1);
			return;
		}
		String joinText = plugin.getConfig().getString("joinText");
		if(joinText != null && !joinText.isEmpty()){
			event.getPlayer().sendMessage(formatText(joinText));
		}
		String playerJoinedText = plugin.getConfig().getString("playerJoinedText");
		if(playerJoinedText != null && !playerJoinedText.isEmpty())
		{				
			Bukkit.broadcastMessage(formatText(playerJoinedText).replace("{player}", event.getPlayer().getName()).replace("{onlineplayers}", String.valueOf(Bukkit.getOnlinePlayers().size())).replace("{maxPlayers}", String.valueOf(Bukkit.getMaxPlayers())));
		}
		ScoreboardUtil.giveNewBoard(plugin, event.getPlayer());
		SpectatorGUI.addSpectateTarget(event.getPlayer());
		GamePlayers.getAlivePlayers().add(event.getPlayer());
		World world = Bukkit.getWorld(plugin.getConfig().getString("preGameLocation.world"));
		if(world != null){
			Location spawnLocation = new Location(world, plugin.getConfig().getDouble("preGameLocation.x"), plugin.getConfig().getDouble("preGameLocation.y"), plugin.getConfig().getDouble("preGameLocation.z"));
			spawnLocation.setPitch(plugin.getConfig().getInt("preGameLocation.pitch"));
			spawnLocation.setYaw(plugin.getConfig().getInt("preGameLocation.yaw"));
			event.getPlayer().teleport(spawnLocation, TeleportCause.PLUGIN);

			event.getPlayer().getInventory().setItem(plugin.getConfig().getInt("leaveGameItemSlot"), plugin.getLeaveGameItem());
			event.getPlayer().getInventory().setItem(plugin.getConfig().getInt("gameModeItemSlot"), plugin.getGameModeItem());
		}
		if(!setInQueue){
			setInQueue = true;
			new BukkitRunnable(){public void run(){
				new PluginMessaging(plugin, true);
			}}.runTaskLater(plugin, 1);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	private void onPlayerQuit(PlayerQuitEvent event){
		SpectatorGUI.removeSpectateTarget(event.getPlayer());
		GamePlayers.getAlivePlayers().remove(event.getPlayer());
		GamePlayers.getDeadPlayers().remove(event.getPlayer());
		GameUtils.checkForWin(plugin);
	}

	@EventHandler
	public void onWorldLoad(WorldLoadEvent event) {
		if(event.getWorld().getName().equals("world")){
			GameUtils.generateInitialLocationChunks(plugin);
		}
	}

	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent event) {
		if(event.getWorld().getName().equalsIgnoreCase("world")){
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent e)
	{
		e.setCancelled(true);
	}

	/*@EventHandler
	public void onWorldLoad(WorldLoadEvent event) {
		if(event.getWorld().getName().equals("world")){
			int chunkRadius = plugin.getConfig().getInt("worldborders.maingame.length");
			chunkRadius /= 32;//Divide by 2 to get the 'radius' in blocks, then divide by 16 to get chunk radius (2*16==32)
			//if(chunkRadius > 30){chunkRadius = 30;}//Hard limit of a 30 chunk radius (900 chunks within 40 seconds...)
			int lastDelay = 0;
			for(int x = -chunkRadius; x <= chunkRadius; x++){
				for(int z = -chunkRadius; z <= chunkRadius; z++){
					int X = x;
					int Z = z;
					lastDelay = (Math.abs(x)+Math.abs(z))*20;
					new BukkitRunnable(){public void run(){
						event.getWorld().loadChunk(X, Z, true);
					}}.runTaskLater(plugin, lastDelay);
				}
			}
			//new BukkitRunnable(){public void run(){
			//	MatchStartRunnable.setWorldLoaded(true);
			//}}.runTaskLater(plugin, lastDelay+20);
		}
	}*/

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e)
	{
		if(GameState.getState() != GameState.IN_LOBBY && !GamePlayers.getAlivePlayers().contains(e.getPlayer()))
		{
			if(e.getPlayer().getLocation().getY() >= plugin.getConfig().getDouble("spectatorYLimit"))
			{
				return;
			}
			else
			{
				Location loc = new Location(e.getPlayer().getWorld(), e.getPlayer().getLocation().getX(), plugin.getConfig().getDouble("spectatorYLimit")+1, e.getPlayer().getLocation().getZ());
				e.getPlayer().teleport(loc);
				e.getPlayer().sendMessage(formatText(plugin.getConfig().getString("spectatorYLimitMessage")).replace("{ylimit}", String.valueOf((int)plugin.getConfig().getDouble("spectatorYLimit"))));
			}
		}
	}

	private static Integer LOGS_LIMIT = 150;

	@EventHandler
	public void chop(BlockBreakEvent event) {

		Block block = event.getBlock();

		if (block.getType() == Material.LOG || block.getType() == Material.LOG_2)
		{

			ArrayList<Block> logs = new ArrayList<Block>();
			ArrayList<Block> leaves = new ArrayList<Block>();

			getTree(block, logs, leaves);

			int counter = 0;
			for(Block b : logs)
			{
				Bukkit.getScheduler().runTaskLater(plugin, new Runnable()
				{
					public void run()
					{
						if(leaves.size() > 10)
						{
							if(b.getType() != Material.AIR)
							{
								b.breakNaturally(new ItemStack(b.getType()));
								b.getWorld().playSound(b.getLocation(), Sound.DIG_WOOD, 10f, 1f);
							}
						}
					}
				}, plugin.getConfig().getLong("logBreakDelay") * (++counter));
			}

			new BukkitRunnable(){public void run(){
				for(Block b : leaves)
				{
					if(leaves.size() > 10)
					{
						b.breakNaturally();
					}
				}
			}}.runTaskLater(plugin, plugin.getConfig().getLong("logBreakDelay") * counter);
		}
	}



	private static void getTree(Block anchor, ArrayList<Block> logs, ArrayList<Block> leaves){

		//Store the original anchor type. The anchor will be shifting later on, and we want this value to be constant
		Material originalAnchorType = anchor.getType();

		//If the anchor isn't a log, don't bother checking it
		if(originalAnchorType != Material.LOG && originalAnchorType != Material.LOG_2){return;}

		//Do a safety-check so we're less likely to destroy someone's building or something
		if(logs.size() > LOGS_LIMIT) return;

		//Create a list for the for() loops to iterate through
		List<BlockFace> blockFaces = new ArrayList<>();
		blockFaces.add(BlockFace.NORTH);
		blockFaces.add(BlockFace.NORTH_EAST);
		blockFaces.add(BlockFace.EAST);
		blockFaces.add(BlockFace.SOUTH_EAST);
		blockFaces.add(BlockFace.SOUTH);
		blockFaces.add(BlockFace.SOUTH_WEST);
		blockFaces.add(BlockFace.WEST);
		blockFaces.add(BlockFace.NORTH_WEST);

		//Lucky Charms is a good cereal
		Block nextAnchor = null;

		//Loop through adjacent blocks
		for(BlockFace face : blockFaces){
			nextAnchor = anchor.getRelative(face);
			if(nextAnchor.getType()==originalAnchorType && !logs.contains(nextAnchor)){
				logs.add(nextAnchor);
				getTree(nextAnchor, logs, leaves);
			}
			else if(nextAnchor.getType().equals(Material.LEAVES) && !logs.contains(nextAnchor)){
				leaves.add(nextAnchor);
			}
		}

		//Shift anchor up one block
		anchor = anchor.getRelative(BlockFace.UP);

		//Add SELF, so the new anchor is queried too
		blockFaces.add(BlockFace.SELF);

		//Loop through adjacent blocks above the original anchor
		for(BlockFace face : blockFaces){
			nextAnchor = anchor.getRelative(face);
			if(nextAnchor.getType()==originalAnchorType && !logs.contains(nextAnchor)){
				logs.add(nextAnchor);
				getTree(nextAnchor, logs, leaves);
			}
			else if(nextAnchor.getType().equals(Material.LEAVES) && !logs.contains(nextAnchor)){
				leaves.add(nextAnchor);
			}
		}
	}

	@EventHandler
	public void onPlayerPickup(PlayerPickupItemEvent e)
	{
		Player p = e.getPlayer();
		if(GamePlayers.getDeadPlayers().contains(p))
		{
			e.setCancelled(true);
		}
	}

	public static void setJoinable(boolean isJoinable){
		joiningAllowed = isJoinable;
	}
}
