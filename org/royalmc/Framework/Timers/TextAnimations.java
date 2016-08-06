package org.royalmc.Framework.Timers;

import static org.royalmc.util.TextUtils.formatText;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.royalmc.uhc.UHCMain;

public class TextAnimations 
{
	UHCMain plugin;

	HashMap<Player, Boolean>created = new HashMap<>();

	public TextAnimations(UHCMain plugin, boolean ipOrTitle, Objective o, Player p)
	{
		this.plugin = plugin;

		if(ipOrTitle == true)
				ipAnim(o, p);
		else
			titleAnim(o);
	} 

	public static String replaceCharAt(String s, int pos, char c) {
		return s.substring(0, pos) + c + s.substring(pos + 1);
	}
	public static String replaceCharAt(String s, int pos, String c) {
		return s.substring(0, pos) + c + s.substring(pos + 1);
	}
	public static String insertIntoString(String s, int pos, String c) {
		return s.substring(0, pos) + c + s.substring(pos);
	}

	public static void createInitialEntry(Objective o, String text, HashMap<Player, Boolean> map, Player p)
	{

		if(map.containsKey(p))
		{
			if(map.get(p) == false)
			{
				o.getScore(formatText(text)).setScore(1);
				map.replace(p, true);
			}
		}
		else
			return;
	}

	String textA = "";
	@SuppressWarnings("deprecation")
	public void ipAnim(Objective o, Player p)
	{
		String c = plugin.getConfig().getString("ipAnim.color");
		//String text = plugin.getConfig().getString("hubAddress");
		//int totalTime = plugin.getConfig().getInt("ipAnim.totalAnimTime");
		//int delayBetweenChars = plugin.getConfig().getInt("ipAnim.delayBetweenChars");
		//int delay = plugin.getConfig().getInt("ipAnim.delayBetweenCompleteAnims");
		String prefix = plugin.getConfig().getString("ipAnim.colorPrefix");
		//ArrayList<Integer> indexes = new ArrayList<Integer>();

		/*for(int i = 0; i < text.length(); i++)
		{
			if(text.charAt(i) == '_')
			{
				indexes.add(i);
			}
		}*/

		created.put(p, false);

		String text = "&8hub.royalmc.org";

		createInitialEntry(o, "&8hub.royalmc.org", created, p);

		Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable()
		{
			public void run()
			{
				for(int i = 0; i < text.length(); i++)
				{
					int j = i;
					Bukkit.getScheduler().runTaskLater(plugin, new Runnable()
					{
						public void run()
						{	
							if(j == 0)
								textA = "&8&6h&8ub.royalmc.org".replace("&6", c).replace("&8", prefix);
							else if(j == 1)
								textA = "&8h&6u&8b.royalmc.org".replace("&6", c).replace("&8", prefix);
							else if(j == 2)
								textA = "&8hu&6b&8.royalmc.org".replace("&6", c).replace("&8", prefix);
							else if(j == 3)
								textA = "&8hub&6.&8royalmc.org".replace("&6", c).replace("&8", prefix);
							else if(j == 4)
								textA = "&8hub.&6r&8oyalmc.org".replace("&6", c).replace("&8", prefix);
							else if(j == 5)
								textA = "&8hub.r&6o&8yalmc.org".replace("&6", c).replace("&8", prefix);
							else if(j == 6)
								textA = "&8hub.ro&6y&8almc.org".replace("&6", c).replace("&8", prefix);
							else if(j == 7)
								textA = "&8hub.roy&6a&8lmc.org".replace("&6", c).replace("&8", prefix);
							else if(j == 8)
								textA = "&8hub.roya&6l&8mc.org".replace("&6", c).replace("&8", prefix);
							else if(j == 9)
								textA = "&8hub.royal&6m&8c.org".replace("&6", c).replace("&8", prefix);
							else if(j == 10)
								textA = "&8hub.royalm&6c&8.org".replace("&6", c).replace("&8", prefix);
							else if(j == 11)
								textA = "&8hub.royalmc&6.&8org".replace("&6", c).replace("&8", prefix);
							else if(j == 12)
								textA = "&8hub.royalmc.&6o&8rg".replace("&6", c).replace("&8", prefix);
							else if(j == 13)
								textA = "&8hub.royalmc.o&6r&8g".replace("&6", c).replace("&8", prefix);
							else if(j == 14)
								textA = "&8hub.royalmc.or&6g".replace("&6", c).replace("&8", prefix);
							else
								textA = "&8hub.royalmc.org".replace("&8", prefix);

							try
							{
								for(String oldEntry : o.getScoreboard().getEntries())
								{	
									if(oldEntry.startsWith(formatText(prefix)))
									{
										o.getScoreboard().resetScores(oldEntry);
										o.getScore(formatText(textA)).setScore(1);
									} 
								}
							}
							catch(Exception e)
							{

							}
						}
					}, i * 20);
				}
			}
		}, 0, text.length() * 20);

	}
	
	String textB = "";
	int z = 0;
	@SuppressWarnings("deprecation")
	public void titleAnim(Objective o)
	{
		String c1 = plugin.getConfig().getString("titleAnim.color1");
		String c2 = plugin.getConfig().getString("titleAnim.color2");
		String c3 = plugin.getConfig().getString("titleAnim.color3");
		String c4 = plugin.getConfig().getString("titleAnim.color4");
		String c5 = plugin.getConfig().getString("titleAnim.color5");
		//String text = plugin.getConfig().getString("hubAddress");
		//int totalTime = plugin.getConfig().getInt("ipAnim.totalAnimTime");
		//int delayBetweenChars = plugin.getConfig().getInt("ipAnim.delayBetweenChars");
		//int delay = plugin.getConfig().getInt("ipAnim.delayBetweenCompleteAnims");
		//ArrayList<Integer> indexes = new ArrayList<Integer>();
		String prefix = plugin.getConfig().getString("titleAnim.colorPrefix");
		/*for(int i = 0; i < text.length(); i++)
		{
			if(text.charAt(i) == '_')
			{
				indexes.add(i);
			}
		}*/

		String text = plugin.getConfig().getString("titleAnim.title");

		//createInitialEntry(o, "&0" + prefix + text);

		Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable()
		{
			public void run()
			{
				for(int i = 0; i < 6; i++)
				{
					int j = i;
					Bukkit.getScheduler().runTaskLater(plugin, new Runnable()
					{
						public void run()
						{	
							String p1 = "";
							String p2 = "";
							for(String str : text.split(" "))
							{
								if(z == 0)
								{
									p1 = str;
									z++;
								}
								else
								{
									p2 = str;
									z = 0;
								}
							}

							if(j == 0)
								textB = "&0" + c1 + p1 + " " + c2 +  p2;
							else if(j == 1)
								textB = "&0" + c2 + p1 + " " + c3 + p2;
							else if(j == 2)
								textB = "&0" + c3 + p1 + " " + c4 + p2;
							else if(j == 3)
								textB = "&0" + c4 + p1 + " " + c5 + p2;
							else if(j == 4)
								textB = "&0" + c5 + p1 + " " + c1 + p2;
							else
								textB = "&0" + prefix + text;


							o.setDisplayName(formatText(textB));

						}
					}, i * 20);
				}
			}
		}, 0, 6 * 20);

	}
}
