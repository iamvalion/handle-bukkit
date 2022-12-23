package me.valion.bukkit.handle.triggers;

import me.valion.bukkit.handle.core.HandleCore;
import me.valion.bukkit.handle.core.util.FormatUtility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

public class TriggersCommandExecutor implements CommandExecutor
{
	private HandleTriggers plugin = HandleTriggers.getPlugin(HandleTriggers.class);
	private ConfigurationSection coreSettingsFile, settingsFile;
	
	/*
	 * Executes the command "/triggers"
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		coreSettingsFile = HandleCore.getFilingCabinet().getConfiguration("settings.yml");
		settingsFile = HandleCore.getFilingCabinet().getConfiguration("triggers/settings.yml");
		
		// Checks to see if the sender has permission for "/triggers"
		if (sender.hasPermission("handle.cmd.triggers"))
		{	
			// If there are no arguments, displays the command usage page
  			if (args.length == 0)
  			{
  				displayCommandUsage(sender, label);
  			}
  			// Otherwise, handles command based on argument(s)
  			else
  			{
  				// If the first argument is "reload" (or a variation)
  				if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("load") || args[0]
  						.equalsIgnoreCase("r"))
  				{
  					// Checks to see if the sender has permission for "/triggers reload"
  	  				if (sender.hasPermission("handle.cmd.triggers.reload"))
  	  				{
  	  					// Reloads all triggers
	  	  				plugin.unloadTriggers();
						plugin.loadTriggers();
	  	  				
	  	  				FormatUtility.sendFormattedMessage(sender, settingsFile.getString("lang.reload"));
  					}
  	  				// If the sender doesn't have permission for "/triggers reload", displays the error message
  	  				else
  	  				{
  						FormatUtility.sendFormattedMessage(sender, coreSettingsFile
  								.getString("lang.no-command-permission"));
  					}
  				}
				// If the first argument is unknown/unexpected, displays the command usage page
  				else
  				{
  					displayCommandUsage(sender, label);
  				}
  			}
		}
		// If the sender doesn't have permission for "/triggers", displays the error message
		else {
			FormatUtility.sendFormattedMessage(sender, coreSettingsFile.getString("lang.no-command-permission"));
		}
		
		return true;
	}
	
	/*
	 * Displays the dynamic command usage page for the "/triggers" command
	 */
	private void displayCommandUsage(CommandSender sender, String label)
	{
		coreSettingsFile = HandleCore.getFilingCabinet().getConfiguration("settings.yml");
		settingsFile = HandleCore.getFilingCabinet().getConfiguration("triggers/settings.yml");

		FormatUtility.sendFormattedMessage(sender, coreSettingsFile.getString("lang.command-usage-title")
				.replace("{0}", label));
		// Displays usage page items dependent on permissions
		boolean noCommandsToShow = true;
		if (sender.hasPermission("handle.cmd.triggers.reload"))
		{
			String command = label + " reload",
					desc = "Reload all triggers, including re-registering trigger commands and listeners";
			FormatUtility.sendFormattedMessage(sender, coreSettingsFile.getString("lang.command-usage-item")
					.replace("{0}", command)
					.replace("{1}", desc));
			noCommandsToShow = false;
		}
		if (noCommandsToShow)
		{
			FormatUtility.sendFormattedMessage(sender, coreSettingsFile.getString("lang.no-commands-to-show"));
		}
	}
}
