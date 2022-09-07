package me.valion.bukkit.handle.triggers.triggers;

import me.valion.bukkit.handle.core.actions.Actor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class Trigger
{
	protected String name;
	protected String type;
	protected ConfigurationSection properties; // TODO figure out how to handle and register aliases
	protected Object actions;
	protected static Actor actor = new Actor(); // TODO move this to TriggerManager
	
	public Trigger(String name, ConfigurationSection configSection)
	{
		this.name = name;
		this.type = configSection.getString("type");
		this.properties = configSection.getConfigurationSection("properties");
		this.actions = configSection.get("actions");
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getType() {
		return this.type;
	}
	
	public ConfigurationSection getProperties()
	{
		return this.properties;
	}
	
	public Object getActions()
	{
		return this.actions;
	}
	
	public void runActions(Map<String, String> placeholders, Player targetPlayer)
	{
		if (actions instanceof List)
		{
			actor.queueActionsList((List<String>)actions, placeholders, targetPlayer);
		}
		else if (actions instanceof ConfigurationSection)
		{
			actor.queueActionsSection((ConfigurationSection)actions, placeholders, targetPlayer);
		}
		else
		{
			Bukkit.getLogger().warning("There was an error running the actions for trigger '" + this.name + "'.");
		}
		
		actor.runActions();
	}
}
