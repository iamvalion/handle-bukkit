package me.valion.bukkit.handle.core;

import me.valion.bukkit.handle.core.data.FilingCabinet;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class HandleCore extends JavaPlugin
{
	private static FilingCabinet filingCabinet = null;
	
	@Override
	public void onEnable()
	{
		// Initializes a FilingCabinet object which will manage this plugin's data files, and then adds data files to be
		// managed
		filingCabinet = new FilingCabinet(this);
		filingCabinet.addDataFile("settings.yml");
		filingCabinet.addDataFile("player-map.yml");
		filingCabinet.addDataFile("action-packages.yml", false);
		
		// Registers plugin commands
		getCommand("handle").setExecutor(new HandleCommandExecutor());

		// Registers plugin events
		getServer().getPluginManager().registerEvents(new HandleListener(), this);

		for (Player p : getServer().getOnlinePlayers())
		{
			updateStoredPlayerNameUuid(p);
		}
	}
	
	public static void updateStoredPlayerNameUuid(Player player)
	{
		String playerName = player.getName();
		String playerUuid = player.getUniqueId().toString();

		// Updates player name and UUID mapping in the player map data file
		ConfigurationSection playerMapFile = getFilingCabinet().getConfiguration("player-map.yml");

		playerMapFile.set("uuid-to-name." + playerUuid, playerName);

		if (playerMapFile.getConfigurationSection("name-to-uuid") != null && playerMapFile.get("name-to-uuid."
				+ playerName) == null)
		{
			for (String name : playerMapFile.getConfigurationSection("name-to-uuid").getKeys(false))
			{
				if (playerMapFile.get("name-to-uuid." + name).equals(playerUuid))
				{
					playerMapFile.set("name-to-uuid." + name, null);
				}
			}
		}
		playerMapFile.set("name-to-uuid." + playerName, playerUuid);

		getFilingCabinet().saveDataFile("player-map.yml");

		// Creates a data file for the player (if one doesn't exist) and updates some information
		String playerFilePath = "players/" + playerUuid + ".yml";
		
		getFilingCabinet().addDataFile(playerFilePath, "players/_player.yml");
		
		getFilingCabinet().getConfiguration(playerFilePath).set("last-name", playerName);
		getFilingCabinet().getConfiguration(playerFilePath).set("last-ip-address", player.getAddress().getHostString());
		
		getFilingCabinet().saveDataFile(playerFilePath);
	}
	
	public static FilingCabinet getFilingCabinet()
	{
		return filingCabinet;
	}
}
