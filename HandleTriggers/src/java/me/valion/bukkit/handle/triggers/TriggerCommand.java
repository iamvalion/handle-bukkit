package me.valion.bukkit.handle.triggers;

import me.valion.bukkit.handle.triggers.triggers.Trigger;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class TriggerCommand extends BukkitCommand
{
	Trigger trigger;
	
	public TriggerCommand(Trigger trigger)
	{
		super(trigger.getName());
		this.trigger = trigger;
	}
	
	@Override
	public boolean execute(CommandSender sender, @NotNull String label, String[] args)
	{
		Map<String, String> placeholders = new HashMap<>();
		placeholders.put("{sender}", sender.getName());
		placeholders.put("{label}", label);
		for (int i = 0; i < args.length; i++)
		{
			placeholders.put("{arg" + (i + 1) + "}", args[i]);
		}
		placeholders.put("{args_length}", args.length + "");
		
		if (!(sender instanceof Player))
		{
			sender = null;
		}
		
		this.trigger.runActions(placeholders, (Player)sender);
		
		return false;
	}
}
