package me.valion.bukkit.handle.triggers;

import me.valion.bukkit.handle.core.HandleCore;
import me.valion.bukkit.handle.triggers.triggers.Trigger;
import me.valion.bukkit.handle.triggers.triggers.TriggerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HandleTriggers extends JavaPlugin
{
	private static TriggerManager triggerManager;

	List<TriggerCommand> triggerCommandList;

	private TriggerListener triggerListener;
	
	@Override
	public void onEnable()
	{
		// Adds data files to be managed by Handle's FilingCabinet
		HandleCore.getFilingCabinet().addDataFile("triggers/settings.yml");
		HandleCore.getFilingCabinet().addDataFile("triggers/storage/building.yml", false);
		HandleCore.getFilingCabinet().addDataFile("triggers/storage/commands.yml", false);
		HandleCore.getFilingCabinet().addDataFile("triggers/storage/interacting.yml", false);
		HandleCore.getFilingCabinet().addDataFile("triggers/storage/misc.yml", false);

		triggerManager = new TriggerManager();

		// Registers plugin commands
		getCommand("triggers").setExecutor(new TriggersCommandExecutor());

		// Instantiates list of trigger commands
		triggerCommandList = new ArrayList<>();

		// Instantiates event trigger listener
		triggerListener = new TriggerListener();

		// Loads triggers and registers plugin commands and event listeners
		loadTriggers();
	}

	public void loadTriggers()
	{
		// Reloads trigger data files
		HandleCore.getFilingCabinet().loadDataFile("triggers/storage/building.yml");
		HandleCore.getFilingCabinet().loadDataFile("triggers/storage/commands.yml");
		HandleCore.getFilingCabinet().loadDataFile("triggers/storage/interacting.yml");
		HandleCore.getFilingCabinet().loadDataFile("triggers/storage/misc.yml");

		// Loads triggers from the configurations into the plugin's TriggerManager
		triggerManager.loadTriggers(HandleCore.getFilingCabinet().getConfiguration("triggers/storage/building.yml"),
				HandleCore.getFilingCabinet().getConfiguration("triggers/storage/commands.yml"),
				HandleCore.getFilingCabinet().getConfiguration("triggers/storage/interacting.yml"),
				HandleCore.getFilingCabinet().getConfiguration("triggers/storage/misc.yml"));

		// Registers command triggers
		try
		{
			Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			bukkitCommandMap.setAccessible(true);
			CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
			bukkitCommandMap.setAccessible(false);

			Bukkit.getLogger().warning("Attempting to register command triggers...");
			for (Trigger t : triggerManager.getCommandTriggers())
			{
				Bukkit.getLogger().info("Registering command trigger '" + t.getName() + "'...");

				String command = t.getProperties().getString("command");
				TriggerCommand tc = new TriggerCommand(t);

				triggerCommandList.add(tc);
				commandMap.register(command, "handletriggers", tc);

				Bukkit.getLogger().info("Done!");
			}
		}
		catch (Exception e)
		{
			this.getLogger().warning("There was an error registering command triggers: " + e.getMessage());
		};

		// Registers event triggers
		getServer().getPluginManager().registerEvents(triggerListener, this);
	}

	public void unloadTriggers()
	{
		// Unregisters existing command triggers
		try
		{
			Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			bukkitCommandMap.setAccessible(true);
			CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
			bukkitCommandMap.setAccessible(false);
			Field bukkitKnownCommands = commandMap.getClass().getSuperclass().getDeclaredField("knownCommands");
			bukkitKnownCommands.setAccessible(true);
			HashMap<String, Command> knownCommands = (HashMap<String, Command>) bukkitKnownCommands.get(commandMap);
			bukkitKnownCommands.setAccessible(false);

			Bukkit.getLogger().warning("Attempting to unregister command triggers...");
			for (TriggerCommand tc : triggerCommandList)
			{
				Bukkit.getLogger().info("Unregistering command trigger '" + tc.getName() + "'...");

				tc.unregister(commandMap);
				knownCommands.remove(tc.getName());
				knownCommands.remove("handletriggers:" + tc.getName());
				for (String a : tc.getAliases())
				{
					if (knownCommands.containsKey(a) && knownCommands.get(a).toString().contains(tc.getName()))
					{
						knownCommands.remove(a);
					}
				}

				Bukkit.getLogger().info("Done!");
			}
			triggerCommandList.clear();
		}
		catch (Exception e)
		{
			this.getLogger().warning("There was an error unregistering command triggers: " + e.getMessage());
		};

		// Unregisters existing event triggers
		HandlerList.unregisterAll(triggerListener);
	}
	
	public static TriggerManager getTriggerManager()
	{
		return triggerManager;
	}
}
