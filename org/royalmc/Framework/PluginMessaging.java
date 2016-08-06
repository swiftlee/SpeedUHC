package org.royalmc.Framework;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.royalmc.listeners.GeneralListener;
import org.royalmc.uhc.UHCMain;

public class PluginMessaging
{

	UHCMain plugin;

	public PluginMessaging(UHCMain pl, boolean isJoinable)
	{
		this.plugin = pl;
		
		GeneralListener.setJoinable(isJoinable);
		//Bukkit.broadcastMessage(org.royalmc.Util.TextUtils.formatText("&5 new &7PluginMessaging&8(&7pl&8, &5"+isJoinable+"&8)"));

		if(isJoinable)
			sendMessageIsJoinable();
		else
			sendMessageNotJoinable();
	}

	public void sendMessageIsJoinable()
	{
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);

		try 
		{
			out.writeUTF("Forward");
			out.writeUTF(plugin.getConfig().getString("hubName"));
			out.writeUTF("SpeedUHC");

			ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
			DataOutputStream msgout = new DataOutputStream(msgbytes);
			msgout.writeUTF("isJoinable");
			msgout.writeUTF(plugin.getConfig().getString("serverName"));//Bukkit.getServerName()
			msgout.writeShort(123);

			out.writeShort(msgbytes.toByteArray().length);
			out.write(msgbytes.toByteArray());
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		plugin.getServer().sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
	}

	public void sendMessageNotJoinable()
	{
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);

		try 
		{
			out.writeUTF("Forward");
			out.writeUTF(plugin.getConfig().getString("hubName"));
			out.writeUTF("SpeedUHC");

			ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
			DataOutputStream msgout = new DataOutputStream(msgbytes);
			msgout.writeUTF("notJoinable");
			msgout.writeUTF(plugin.getConfig().getString("serverName"));
			msgout.writeShort(123);

			out.writeShort(msgbytes.toByteArray().length);
			out.write(msgbytes.toByteArray());
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		plugin.getServer().sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
	}
}
