package me.valion.bukkit.handle.core;

import me.valion.bukkit.handle.core.util.FormatUtility;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Map;

public class HandleCommandExecutor implements CommandExecutor
{
	private HandleCore plugin = HandleCore.getPlugin(HandleCore.class);
	private ConfigurationSection settingsFile;
	
	/*
	 * Executes the command "/handle"
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		settingsFile = HandleCore.getFilingCabinet().getConfiguration("settings.yml");
		
		// Checks to see if the sender has permission for "/handle"
		if (sender.hasPermission("handle.cmd.handle"))
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
  					// Checks to see if the sender has permission for "/handle reload"
  	  				if (sender.hasPermission("handle.cmd.handle.reload"))
  	  				{
  	  					// Reloads all data files, then saves them again to ensure defaults are copied
	  	  				HandleCore.getFilingCabinet().loadAllDataFiles();
	  	  				HandleCore.getFilingCabinet().saveAllDataFiles();
						// Reloads all triggers if HandleTriggers is present
						if (Bukkit.getPluginManager().getPlugin("HandleTriggers") != null)
						{
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "triggers reload");
						}

	  	  				FormatUtility.sendFormattedMessage(sender, settingsFile.getString("lang.reload")
	  	  						.replace("{0}", plugin.getDescription().getName()).replace("{1}", plugin
	  	  								.getDescription().getVersion()));
  					}
  	  				// If the sender doesn't have permission for "/handle reload", displays the error message
  	  				else
  	  				{
  						FormatUtility.sendFormattedMessage(sender, settingsFile
  								.getString("lang.no-command-permission"));
  					}
  				}
  				// If the first argument is "version" (or a variation)
  				else if (args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("v"))
  				{
  					// Checks to see if the sender has permission for "/handle version"
  	  				if (sender.hasPermission("handle.cmd.handle.version"))
  	  				{
  	  					FormatUtility.sendFormattedMessage(sender, settingsFile.getString("lang.version")
  	  							.replace("{0}", plugin.getDescription().getName()).replace("{1}", plugin
  	  									.getDescription().getVersion()).replace("{2}", "" + plugin
  	  											.getDescription().getAuthors()));
  	  				}
  	  				// If the sender doesn't have permission for "/handle version", displays the error message
  	  				else
  	  				{
  	  					FormatUtility.sendFormattedMessage(sender, settingsFile
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
		// If the sender doesn't have permission for "/handle", displays the error message
		else {
			FormatUtility.sendFormattedMessage(sender, settingsFile.getString("lang.no-command-permission"));
		}
		
		return true;
	}
	
	/*
	 * Displays the dynamic command usage page for the "/handle" command
	 */
	private void displayCommandUsage(CommandSender sender, String label)
	{
		settingsFile = HandleCore.getFilingCabinet().getConfiguration("settings.yml");
		
		FormatUtility.sendFormattedMessage(sender, settingsFile.getString("lang.command-usage-title")
				.replace("{0}", label));
		// Displays usage page items dependent on permissions
		boolean noCommandsToShow = true;
		if (sender.hasPermission("handle.cmd.handle.reload"))
		{
			String command = label + " reload", desc = "Reload plugin settings and configurations";
			FormatUtility.sendFormattedMessage(sender, settingsFile.getString("lang.command-usage-item")
					.replace("{0}", command)
					.replace("{1}", desc));
			noCommandsToShow = false;
		}
		if (sender.hasPermission("handle.cmd.handle.version"))
		{
			String command = label + " version", desc = "List plugin version information";
			FormatUtility.sendFormattedMessage(sender, settingsFile.getString("lang.command-usage-item")
					.replace("{0}", command)
					.replace("{1}", desc));
			noCommandsToShow = false;
		}
		if (noCommandsToShow)
		{
			FormatUtility.sendFormattedMessage(sender, settingsFile.getString("lang.no-commands-to-show"));
		}
	}
}
