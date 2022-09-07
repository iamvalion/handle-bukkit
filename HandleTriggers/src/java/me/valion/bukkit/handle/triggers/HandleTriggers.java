package me.valion.bukkit.handle.triggers;

import me.valion.bukkit.handle.core.HandleCore;
import me.valion.bukkit.handle.triggers.triggers.Trigger;
import me.valion.bukkit.handle.triggers.triggers.TriggerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public class HandleTriggers extends JavaPlugin
{
	private static TriggerManager triggerManager = null;
	
	@Override
	public void onEnable()
	{
		HandleCore.getFilingCabinet().addDataFile("triggers/storage/building.yml");
		HandleCore.getFilingCabinet().addDataFile("triggers/storage/commands.yml");
		HandleCore.getFilingCabinet().addDataFile("triggers/storage/interacting.yml");
		HandleCore.getFilingCabinet().addDataFile("triggers/storage/misc.yml");
		
		triggerManager = new TriggerManager();
		triggerManager.loadTriggers(HandleCore.getFilingCabinet().getConfiguration("triggers/storage/building.yml"),
				HandleCore.getFilingCabinet().getConfiguration("triggers/storage/commands.yml"),
				HandleCore.getFilingCabinet().getConfiguration("triggers/storage/interacting.yml"),
				HandleCore.getFilingCabinet().getConfiguration("triggers/storage/misc.yml"));
		
		// Registers command triggers
		try
		{
			final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			bukkitCommandMap.setAccessible(true);
			CommandMap commandMap = (CommandMap)bukkitCommandMap.get(Bukkit.getServer());
			
			Bukkit.getLogger().warning("Attempting to register command triggers...");
			for (Trigger t : triggerManager.getCommandTriggers())
			{
				Bukkit.getLogger().warning("Registering command trigger '" + t.getName() + "'");
				commandMap.register(t.getProperties().getString("command"), "handletriggers", new TriggerCommand(t));
			}
		}
		catch (Exception e)
		{
			this.getLogger().warning("There was an error registering command triggers.");
		}
		
		// Registers event triggers
		getServer().getPluginManager().registerEvents(new TriggerListener(), this);
	}
	
	public static TriggerManager getTriggerManager()
	{
		return triggerManager;
	}
}
