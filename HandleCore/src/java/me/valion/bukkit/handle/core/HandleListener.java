package me.valion.bukkit.handle.core;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class HandleListener implements Listener
{	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		HandleCore.updatePlayerDataFile(event.getPlayer());
		
		// Updates some information in the data file for the player when they join
		String playerFilePath = "players/" + event.getPlayer().getUniqueId().toString() + ".yml";
		
		HandleCore.getFilingCabinet().getConfiguration(playerFilePath).set("timestamps.last-join",
				System.currentTimeMillis());
		
		HandleCore.getFilingCabinet().saveDataFile(playerFilePath);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		// Creates a data file for the player when they quit (if one doesn't exist) and updates some information
		String playerFilePath = "players/" + event.getPlayer().getUniqueId().toString() + ".yml";
		
		HandleCore.getFilingCabinet().addDataFile(playerFilePath, "players/_player.yml");

		HandleCore.getFilingCabinet().getConfiguration(playerFilePath).set("timestamps.last-quit",
				System.currentTimeMillis());
		
		HandleCore.getFilingCabinet().saveDataFile(playerFilePath);
	}
}
