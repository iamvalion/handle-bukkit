package me.valion.bukkit.handle.triggers.triggers;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TriggerManager
{
	private List<Trigger> blockAttackTriggers;
	private List<Trigger> blockInteractTriggers;
	private List<Trigger> blockUseTriggers;
	private List<Trigger> breakTriggers;
	private List<Trigger> commandTriggers;
	private List<Trigger> deathTriggers;
	private List<Trigger> joinTriggers;
	private List<Trigger> placeTriggers;
	private List<Trigger> quitTriggers;
	
	public TriggerManager()
	{
		this.blockAttackTriggers = new ArrayList<>();
		this.blockInteractTriggers = new ArrayList<>();
		this.blockUseTriggers = new ArrayList<>();
		this.breakTriggers = new ArrayList<>();
		this.commandTriggers = new ArrayList<>();
		this.deathTriggers = new ArrayList<>();
		this.joinTriggers = new ArrayList<>();
		this.placeTriggers = new ArrayList<>();
		this.quitTriggers = new ArrayList<>();
	}
	
	public void loadTriggers(ConfigurationSection... triggerSections)
	{
		Bukkit.getLogger().info("Clearing trigger map..");
		this.blockAttackTriggers.clear();
		this.blockInteractTriggers.clear();
		this.blockUseTriggers.clear();
		this.breakTriggers.clear();
		this.commandTriggers.clear();
		this.deathTriggers.clear();
		this.joinTriggers.clear();
		this.placeTriggers.clear();
		this.quitTriggers.clear();
		
		Bukkit.getLogger().info("Loading triggers from specified files..");
		for (ConfigurationSection ts : triggerSections)
		{
			Bukkit.getLogger().info("  Making sure file isn't empty..");
			if (!ts.getKeys(false).isEmpty())
			{
				Bukkit.getLogger().info("  Loading each trigger..");
				for (String t : ts.getKeys(false))
				{
					Bukkit.getLogger().info("    Making sure trigger '" + t + "' isn't null..");
					if (ts.getConfigurationSection(t) != null)
					{
						Trigger trigger = new Trigger(t, ts.getConfigurationSection(t));
						Bukkit.getLogger().info("    Trigger '" + t + "' is being added..");
						switch (trigger.getType().toUpperCase(Locale.ROOT).replace(" ", "_"))
						{
							case "BLOCK_ATTACK" -> this.blockAttackTriggers.add(trigger);
							case "BLOCK_INTERACT" -> this.blockInteractTriggers.add(trigger);
							case "BLOCK_USE" -> this.blockUseTriggers.add(trigger);
							case "BREAK" -> this.breakTriggers.add(trigger);
							case "COMMAND" -> this.commandTriggers.add(trigger);
							case "DEATH" -> this.deathTriggers.add(trigger);
							case "JOIN" -> this.joinTriggers.add(trigger);
							case "PLACE" -> this.placeTriggers.add(trigger);
							case "QUIT" -> this.quitTriggers.add(trigger);
							default -> Bukkit.getLogger().warning("    Trigger '" + t + "' has an invalid type");
						}
					}
				}
			}
		}
	}
	
	public List<Trigger> getBlockAttackTriggers()
	{
		return this.blockAttackTriggers;
	}
	
	public List<Trigger> getBlockInteractTriggers()
	{
		return this.blockInteractTriggers;
	}
	
	public List<Trigger> getBlockUseTriggers()
	{
		return this.blockUseTriggers;
	}
	
	public List<Trigger> getBreakTriggers()
	{
		return this.breakTriggers;
	}
	
	public List<Trigger> getCommandTriggers()
	{
		return this.commandTriggers;
	}

	public List<Trigger> getDeathTriggers()
	{
		return this.deathTriggers;
	}
	
	public List<Trigger> getJoinTriggers()
	{
		return this.joinTriggers;
	}
	
	public List<Trigger> getPlaceTriggers()
	{
		return this.placeTriggers;
	}
	
	public List<Trigger> getQuitTriggers()
	{
		return this.quitTriggers;
	}
}
