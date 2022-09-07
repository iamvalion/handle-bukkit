package me.valion.bukkit.handle.locations;

import me.valion.bukkit.handle.core.HandleCore;
import me.valion.bukkit.handle.core.util.FormatUtility;
import me.valion.bukkit.handle.core.util.NumberUtility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Set;

public class LocationsCommandExecutor implements CommandExecutor
{
	private ConfigurationSection coreSettingsFile;
	private ConfigurationSection settingsFile;
	private ConfigurationSection locationsFile;
	
	/*
	 * Executes the command "/locations"
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		coreSettingsFile = HandleCore.getFilingCabinet().getConfiguration("settings.yml");
		settingsFile = HandleCore.getFilingCabinet().getConfiguration("locations/settings.yml");
		locationsFile = HandleCore.getFilingCabinet().getConfiguration("locations/storage.yml");
		
		// Checks to see if the sender has permission for "/locations"
		if (sender.hasPermission("core.cmd.locations"))
		{
			Set<String> locations = locationsFile.getKeys(false);
			
			// Displays all public locations that the sender has access to, if any			
			FormatUtility.sendFormattedMessage(sender, settingsFile.getString("lang.locations-title"));
			if (!locations.isEmpty())
			{				
				boolean noLocationsToShow = true;
				
				// Displays public locations dependent on permissions
				for (String l : locations)
				{				
					if (sender.hasPermission("core.cmd.location.teleport.public." + l))
					{						
						FormatUtility.sendFormattedMessage(sender,
								settingsFile.getString("lang.locations-item").replace("{0}", l)
								.replace("{1}", locationsFile.getString(l + ".location.world"))
								.replace("{2}", NumberUtility.formatDecimalNumber(
										locationsFile.getDouble(l + ".location.x"), 1))
								.replace("{3}", NumberUtility.formatDecimalNumber(
										locationsFile.getDouble(l + ".location.y"), 1))
								.replace("{4}", NumberUtility.formatDecimalNumber(
										locationsFile.getDouble(l + ".location.z"), 1))
								.replace("{5}", NumberUtility.formatDecimalNumber(
										locationsFile.getDouble(l + ".location.yaw"), 1))
								.replace("{6}", NumberUtility.formatDecimalNumber(
										locationsFile.getDouble(l + ".location.pitch"), 1)));
						noLocationsToShow = false;
					}
				}
				
				// If the sender doesn't have permission for any public locations, displays that there are no
				// locations to show
				if (noLocationsToShow)
				{
					FormatUtility.sendFormattedMessage(sender,
							settingsFile.getString("lang.no-locations-to-show"));
				}
			}
			// If no public locations have been set, displays error message
			else
			{
				FormatUtility.sendFormattedMessage(sender,
						settingsFile.getString("lang.no-locations-set"));
			}
			
			// Displays all private locations that the sender (player) has, if any
			if (sender instanceof Player)
			{
				String playerUuid = ((Player)sender).getUniqueId().toString();
				ConfigurationSection playerFile = HandleCore.getFilingCabinet()
							.getConfiguration("players/" + playerUuid + ".yml");
				
				FormatUtility.sendFormattedMessage(sender,
						settingsFile.getString("lang.player-locations-title")
						.replace("{0}", sender.getName()));
				// Displays private locations
				if (playerFile.getConfigurationSection("locations") != null
						&& !playerFile.getConfigurationSection("locations").getKeys(false).isEmpty())
				{
					for (String l : playerFile.getConfigurationSection("locations").getKeys(false))
					{
						FormatUtility.sendFormattedMessage(sender,
								settingsFile.getString("lang.locations-item").replace("{0}", l)
								.replace("{1}", playerFile.getString("locations." + l + ".location.world"))
								.replace("{2}", NumberUtility.formatDecimalNumber(
										playerFile.getDouble("locations." + l + ".location.x"), 1))
								.replace("{3}", NumberUtility.formatDecimalNumber(
										playerFile.getDouble("locations." + l + ".location.y"), 1))
								.replace("{4}", NumberUtility.formatDecimalNumber(
										playerFile.getDouble("locations." + l + ".location.z"), 1))
								.replace("{5}", NumberUtility.formatDecimalNumber(
										playerFile.getDouble("locations." + l + ".location.yaw"), 1))
								.replace("{6}", NumberUtility.formatDecimalNumber(
										playerFile.getDouble("locations." + l + ".location.pitch"), 1)));
					}
				}
				// If no private locations have been set, displays error message
				else
				{
					FormatUtility.sendFormattedMessage(sender,
							settingsFile.getString("lang.no-locations-set"));
				}
			}
		}
		// If the sender doesn't have permission for "/locations", displays the error message
		else {
			FormatUtility.sendFormattedMessage(sender,
					coreSettingsFile.getString("lang.no-command-permission"));
		}
		
		return false;
	}
}
