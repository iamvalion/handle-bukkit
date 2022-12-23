package me.valion.bukkit.handle.locations;

import me.valion.bukkit.handle.core.HandleCore;
import org.bukkit.plugin.java.JavaPlugin;

public class HandleLocations extends JavaPlugin
{
	@Override
	public void onEnable()
	{
		// Adds data files to be managed by Handle's FilingCabinet
		HandleCore.getFilingCabinet().addDataFile("locations/settings.yml");
		HandleCore.getFilingCabinet().addDataFile("locations/storage.yml");
		
		// Registers plugin commands
		getCommand("location").setExecutor(new LocationCommandExecutor());
		getCommand("locations").setExecutor(new LocationsCommandExecutor());
	}
}
