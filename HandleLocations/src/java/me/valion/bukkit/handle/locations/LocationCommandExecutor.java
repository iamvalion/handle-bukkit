package me.valion.bukkit.handle.locations;

import me.valion.bukkit.handle.core.HandleCore;
import me.valion.bukkit.handle.core.util.FormatUtility;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class LocationCommandExecutor implements CommandExecutor
{
	private ConfigurationSection coreSettingsFile;
	private ConfigurationSection settingsFile;
	private ConfigurationSection locationsFile;
	
	/*
	 * Executes the command "/location"
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		coreSettingsFile = HandleCore.getFilingCabinet().getConfiguration("settings.yml");
		settingsFile = HandleCore.getFilingCabinet().getConfiguration("locations/settings.yml");
		locationsFile = HandleCore.getFilingCabinet().getConfiguration("locations/storage.yml");
		
		// Checks to see if the player has permission for "/location"
		if (sender.hasPermission("handle.cmd.location"))
		{
			// If there are no arguments, displays the command usage page
			if (args.length == 0)
			{
				displayCommandUsage(sender, label);
			}
			// Otherwise, handles command based on argument(s)
			else
			{
				// If the first argument is "set" (or a variation)
				if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("define"))
				{
					if (!(sender instanceof Player))
					{
						FormatUtility.sendFormattedMessage(sender,
								coreSettingsFile.getString("lang.sender-not-player"));
						return true;
					}
					
					// Checks to see if the player has permission for "/location set"
					if (sender.hasPermission("handle.cmd.location.set"))
					{
						if (args.length >= 2)
						{
							String[] arg1 = args[1].split(":", 2);
							
							if (arg1.length == 1)
							{
								if (sender.hasPermission("handle.cmd.location.set.public"))
								{
									Location location = ((Player)sender).getLocation();
									
									for (String l : locationsFile.getKeys(false))
									{
										if (args[1].equalsIgnoreCase(l))
										{
											args[1] = l;
										}
									}
									
									locationsFile.set(args[1] + ".location.world",
											location.getWorld().getName());
									locationsFile.set(args[1] + ".location.x", location.getX());
									locationsFile.set(args[1] + ".location.y", location.getY());
									locationsFile.set(args[1] + ".location.z", location.getZ());
									locationsFile.set(args[1] + ".location.yaw", location.getYaw());
									locationsFile.set(args[1] + ".location.pitch", location.getPitch());
									HandleCore.getFilingCabinet().saveDataFile("locations/storage.yml");
									
									FormatUtility.sendFormattedMessage(sender,
											settingsFile.getString("lang.location-set")
											.replace("{0}", args[1]));
								}
								else
								{
									FormatUtility.sendFormattedMessage(sender,
											coreSettingsFile.getString("lang.no-command-permission"));
								}
							}
							else
							{
								if (sender.hasPermission("handle.cmd.location.set.private"))
								{
									if (!arg1[0].equalsIgnoreCase(sender.getName())
											&& !arg1[0].equals("")
											&& !sender.hasPermission(
													"handle.cmd.location.set.private.others"))
									{
										FormatUtility.sendFormattedMessage(sender, coreSettingsFile
												.getString("lang.no-command-permission"));
										return true;
									}
									
									if (arg1[0].equals(""))
									{
										arg1[0] = ((Player)sender).getName();
									}
									
									String playerUuid =
											HandleCore.getFilingCabinet().getConfiguration("player-map.yml")
											.getString("name-to-uuid." + arg1[0]);
									
									if (playerUuid != null)
									{
										ConfigurationSection playerFile = HandleCore.getFilingCabinet()
												.getConfiguration("players/" + playerUuid + ".yml");
										Location location = ((Player)sender).getLocation();
										
										if (playerFile.getConfigurationSection("locations") != null)
										{
											for (String l : playerFile.getConfigurationSection("locations")
													.getKeys(false))
											{
												if (arg1[1].equalsIgnoreCase(l))
												{
													arg1[1] = l;
												}
											}
										}
										
										playerFile.set("locations." + arg1[1] + ".location.world",
												location.getWorld().getName());
										playerFile.set("locations." + arg1[1] + ".location.x",
												location.getX());
										playerFile.set("locations." + arg1[1] + ".location.y",
												location.getY());
										playerFile.set("locations." + arg1[1] + ".location.z",
												location.getZ());
										playerFile.set("locations." + arg1[1] + ".location.yaw",
												location.getYaw());
										playerFile.set("locations." + arg1[1] + ".location.pitch",
												location.getPitch());
										HandleCore.getFilingCabinet()
												.saveDataFile("players/" + playerUuid + ".yml");
										
										FormatUtility.sendFormattedMessage(sender,
												settingsFile.getString("lang.player-location-set")
												.replace("{0}", arg1[1])
												.replace("{1}", arg1[0]));
									}
									else
									{
										FormatUtility.sendFormattedMessage(sender,
												coreSettingsFile.getString("lang.player-not-found")
												.replace("{0}", arg1[0]));
									}
								}
								else
								{
									FormatUtility.sendFormattedMessage(sender,
											coreSettingsFile.getString("lang.no-command-permission"));
								}
							}
						}
						else
						{
							FormatUtility.sendFormattedMessage(sender,
									settingsFile.getString("lang.location-set-usage")
									.replace("{0}", label)
									.replace("{1}", args[0]));
						}
					}
					// If the player doesn't have permission for "/location set", displays the error message
					else
					{
						FormatUtility.sendFormattedMessage(sender,
								coreSettingsFile.getString("lang.no-command-permission"));
					}
				}
				// If the first argument is "unset" (or a variation)
				else if (args[0].equalsIgnoreCase("unset") || args[0].equalsIgnoreCase("undefine"))
				{
					// Checks to see if the player has permission for "/location unset"
					if (sender.hasPermission("handle.cmd.location.unset"))
					{
						if (args.length >= 2)
						{
							String[] arg1 = args[1].split(":", 2);
							
							if (arg1.length == 1)
							{
								if (sender.hasPermission("handle.cmd.location.unset.public"))
								{
									String locationName = null;
									
									for (String l : locationsFile.getKeys(false))
									{
										if (l.equalsIgnoreCase(arg1[0]))
										{
											locationName = l;
											break;
										}
									}
									
									if (locationName != null)
									{
										locationsFile.set(locationName, null);
										HandleCore.getFilingCabinet().saveDataFile("locations/storage.yml");
										
										FormatUtility.sendFormattedMessage(sender,
												settingsFile.getString("lang.location-unset")
												.replace("{0}", args[1]));
									}
									else
									{
										FormatUtility.sendFormattedMessage(sender, settingsFile
												.getString("lang.location-not-found").replace("{0}",
														args[1]));
									}
								}
								else
								{
									FormatUtility.sendFormattedMessage(sender,
											coreSettingsFile.getString("lang.no-command-permission"));
								}
							}
							else
							{
								if (sender.hasPermission("handle.cmd.location.unset.private"))
								{
									if (sender instanceof Player
											&& !arg1[0].equalsIgnoreCase(sender.getName())
											&& !arg1[0].equals("")
											&& !sender.hasPermission(
													"handle.cmd.location.unset.private.others"))
									{
										FormatUtility.sendFormattedMessage(sender, coreSettingsFile
												.getString("lang.no-command-permission"));
										return true;
									}
									
									if (arg1[0].equals(""))
									{
										if (sender instanceof Player)
										{
											arg1[0] = ((Player)sender).getName();
										}
										else
										{
											FormatUtility.sendFormattedMessage(sender,
													settingsFile.getString("lang.location-unset-usage")
															.replace("{0}", label)
															.replace("{1}", args[0]));
											return true;
										}
									}
									
									String playerUuid = HandleCore.getFilingCabinet()
											.getConfiguration("player-map.yml").getString("name-to-uuid."
													+ arg1[0]);
									
									if (playerUuid != null)
									{
										ConfigurationSection playerFile = HandleCore.getFilingCabinet()
												.getConfiguration("players/" + playerUuid + ".yml");
										String locationName = null;
										
										for (String l : playerFile.getConfigurationSection("locations")
												.getKeys(false))
										{
											if (l.equalsIgnoreCase(arg1[1]))
											{
												locationName = l;
												break;
											}
										}
										
										if (playerFile.get("locations." + locationName) != null)
										{
											playerFile.set("locations." + locationName, null);
											HandleCore.getFilingCabinet()
													.saveDataFile("players/" + playerUuid + ".yml");
											
											FormatUtility.sendFormattedMessage(sender,
													settingsFile.getString("lang.player-location-unset")
													.replace("{0}", arg1[1])
													.replace("{1}", arg1[0]));
										}
										else
										{
											FormatUtility.sendFormattedMessage(sender,
													settingsFile.getString("lang.player-location-not-found")
													.replace("{0}", arg1[1])
													.replace("{1}", arg1[0]));
										}
									}
									else
									{
										FormatUtility.sendFormattedMessage(sender,
												coreSettingsFile.getString("lang.player-not-found")
												.replace("{0}", arg1[0]));
									}
								}
								else
								{
									FormatUtility.sendFormattedMessage(sender,
											coreSettingsFile.getString("lang.no-command-permission"));
								}
							}
						}
						else
						{
							FormatUtility.sendFormattedMessage(sender,
									settingsFile.getString("lang.location-unset-usage")
									.replace("{0}", label)
									.replace("{1}", args[0]));
						}
					}
					// If the player doesn't have permission for "/location unset", displays the error message
					else
					{
						FormatUtility.sendFormattedMessage(sender,
								coreSettingsFile.getString("lang.no-command-permission"));
					}
				}
				else if (args[0].equalsIgnoreCase("teleport") || args[0].equalsIgnoreCase("tp"))
				{
					// Checks to see if the player has permission for "/location teleport"
					if (sender.hasPermission("handle.cmd.location.teleport"))
					{
						if (args.length >= 2)
						{
							Player player;
							boolean senderIsTarget = true;
							String[] arg1;
							
							if (args.length < 3)
							{
								if (!(sender instanceof Player))
								{
									FormatUtility.sendFormattedMessage(sender,
											settingsFile.getString("lang.location-teleport-usage")
											.replace("{0}", label)
											.replace("{1}", args[0]));
									
									return true;
								}
								
								player = (Player)sender;
								
								arg1 = args[1].split(":", 2);
							}
							else
							{
								if (sender instanceof Player
										&& !args[1].equalsIgnoreCase(sender.getName())
										&& !sender.hasPermission("handle.cmd.location.teleport.others"))
								{
									FormatUtility.sendFormattedMessage(sender,
											coreSettingsFile.getString("lang.no-command-permission"));
									return true;
								}
								
								player = Bukkit.getPlayer(args[1]);
								
								if (player == null)
								{
									FormatUtility.sendFormattedMessage(sender,
											coreSettingsFile.getString("lang.player-not-found")
											.replace("{0}", args[1]));
									
									return true;
								}
								
								arg1 = args[2].split(":", 2);
							}
							
							if (arg1.length == 1)
							{
								if (sender.hasPermission("handle.cmd.location.teleport.public"))
								{
									String locationName = null;
									
									for (String l : locationsFile.getKeys(false))
									{
										if (l.equalsIgnoreCase(arg1[0]))
										{
											locationName = l;
											break;
										}
									}
									
									if (locationsFile.get(locationName + ".location") != null)
									{
										Location location = new Location(
												Bukkit.getWorld(
														locationsFile.getString(locationName + ".location.world")),
												locationsFile.getDouble(locationName + ".location.x"),
												locationsFile.getDouble(locationName + ".location.y"),
												locationsFile.getDouble(locationName + ".location.z"),
												(float)locationsFile.getDouble(locationName + ".location.yaw"),
												(float)locationsFile.getDouble(locationName + ".location.pitch"));
										player.teleport(location);
										
										if (sender instanceof Player
												&& ((Player)sender).getName() == player.getName())
										{
											FormatUtility.sendFormattedMessage(sender,
													settingsFile.getString("lang.teleporting-to-location")
															.replace("{0}", arg1[0]));
										}
										else
										{
											FormatUtility.sendFormattedMessage(sender,
													settingsFile.getString("lang.teleporting-other-to-location")
															.replace("{0}", args[1])
															.replace("{1}", arg1[0]));
										}
									}
									else
									{
										FormatUtility.sendFormattedMessage(sender,
												settingsFile.getString("lang.location-not-found")
												.replace("{0}", arg1[0]));
									}
								}
								else
								{
									FormatUtility.sendFormattedMessage(sender,
											coreSettingsFile.getString("lang.no-command-permission"));
								}
							}
							else
							{
								if (sender.hasPermission("handle.cmd.location.teleport.private"))
								{
									if (sender instanceof Player
											&& !arg1[0].equalsIgnoreCase(sender.getName())
											&& !arg1[0].equals("")
											&& !sender.hasPermission(
													"handle.cmd.location.teleport.private.others"))
									{
										FormatUtility.sendFormattedMessage(sender, coreSettingsFile
												.getString("lang.no-command-permission"));
										return true;
									}
									
									if (arg1[0].equals(""))
									{
										arg1[0] = player.getName();
									}
									
									String playerUuid = HandleCore.getFilingCabinet().getConfiguration("player-map.yml")
											.getString("name-to-uuid." + arg1[0]);
									
									if (playerUuid != null)
									{
										ConfigurationSection playerFile = HandleCore.getFilingCabinet()
												.getConfiguration("players/" + playerUuid + ".yml");
										if (playerFile == null)
										{
											FormatUtility.sendFormattedMessage(sender,
													coreSettingsFile.getString("lang.player-not-found")
													.replace("{0}", arg1[0]));
											return true;
										}
										
										String locationName = null;
										
										if (playerFile.getConfigurationSection("locations") == null)
										{
											FormatUtility.sendFormattedMessage(sender,
													settingsFile.getString("lang.player-location-not-found")
													.replace("{0}", arg1[1])
													.replace("{1}", arg1[0]));
											return true;
										}
										
										for (String l : playerFile.getConfigurationSection("locations")
												.getKeys(false))
										{
											if (l.equalsIgnoreCase(arg1[1]))
											{
												locationName = l;
												break;
											}
										}
										
										if (playerFile.get("locations." + locationName + ".location") != null)
										{
											Location location = new Location(
													Bukkit.getWorld(playerFile.getString(
															"locations." + locationName + ".location.world")),
													playerFile.getDouble(
															"locations." + locationName + ".location.x"),
													playerFile.getDouble(
															"locations." + locationName + ".location.y"),
													playerFile.getDouble(
															"locations." + locationName + ".location.z"),
													(float)playerFile.getDouble(
															"locations." + locationName + ".location.yaw"),
													(float)playerFile.getDouble(
															"locations." + locationName + ".location.pitch"));
											player.teleport(location);
											
											if (sender instanceof Player
													&& ((Player)sender).getName() == player.getName())
											{
												FormatUtility.sendFormattedMessage(sender, settingsFile
														.getString("lang.teleporting-to-player-location")
														.replace("{0}", arg1[0])
														.replace("{1}", arg1[1]));
											}
											else
											{
												FormatUtility.sendFormattedMessage(sender, settingsFile
														.getString("lang.teleporting-other-to-player-location")
														.replace("{0}", args[1])
														.replace("{1}", arg1[0])
														.replace("{2}", arg1[1]));
											}
										}
										else
										{
											FormatUtility.sendFormattedMessage(sender,
													settingsFile.getString("lang.player-location-not-found")
													.replace("{0}", arg1[1])
													.replace("{1}", arg1[0]));
										}
									}
									else
									{
										FormatUtility.sendFormattedMessage(sender,
												coreSettingsFile.getString("lang.player-not-found")
												.replace("{0}", arg1[0]));
									}
								}
								else
								{
									FormatUtility.sendFormattedMessage(sender,
											coreSettingsFile.getString("lang.no-command-permission"));
								}
							}

						}
						else
						{
							FormatUtility.sendFormattedMessage(sender,
									settingsFile.getString("lang.location-teleport-usage")
									.replace("{0}", label)
									.replace("{1}", args[0]));
						}
					}
					else
					{
						FormatUtility.sendFormattedMessage(sender,
								coreSettingsFile.getString("lang.no-command-permission"));
					}
				}
				// If the first argument is unknown/unexpected, displays the command usage page
				else
				{
					displayCommandUsage(sender, label);
				}
			}
		}
		// If the player doesn't have permission for "/location", displays the error message
		else {
			FormatUtility.sendFormattedMessage(sender,
					coreSettingsFile.getString("lang.no-command-permission"));
		}
		
		return true;
	}
	
	/*
	 * Displays the dynamic command usage page for the "/location" command
	 */
	private void displayCommandUsage(CommandSender sender, String label)
	{
		coreSettingsFile = HandleCore.getFilingCabinet().getConfiguration("settings.yml");
				
		FormatUtility.sendFormattedMessage(sender, coreSettingsFile.getString("lang.command-usage-title")
				.replace("{0}", label));
		// Displays usage page items dependent on permissions
		boolean noCommandsToShow = true;
		if (sender.hasPermission("handle.cmd.location.set"))
		{
			String command = label + " set", desc = "Set a location";
			FormatUtility.sendFormattedMessage(sender, coreSettingsFile.getString("lang.command-usage-item")
					.replace("{0}", command)
					.replace("{1}", desc));
			noCommandsToShow = false;
		}
		if (sender.hasPermission("handle.cmd.location.teleport"))
		{
			String command = label + " teleport", desc = "Teleport to a location";
			FormatUtility.sendFormattedMessage(sender, coreSettingsFile.getString("lang.command-usage-item")
					.replace("{0}", command)
					.replace("{1}", desc));
			noCommandsToShow = false;
		}
		if (sender.hasPermission("handle.cmd.location.unset"))
		{
			String command = label + " unset", desc = "Unset a location";
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
