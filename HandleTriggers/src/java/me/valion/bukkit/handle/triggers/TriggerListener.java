package me.valion.bukkit.handle.triggers;

import me.valion.bukkit.handle.triggers.triggers.Trigger;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;
import java.util.Map;

public class TriggerListener implements Listener
{
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		// First, check if action is a physical one other than clicking; then check each hand to prevent double firing
		if (event.getAction() == Action.PHYSICAL || event.getHand().equals(EquipmentSlot.HAND) ||
				event.getHand().equals(EquipmentSlot.OFF_HAND))
		{
			Map<String, String> placeholders = new HashMap<>();
			
			// Set placeholders, replacing null hand/block materials with "AIR"
			placeholders.put("{player}", event.getPlayer().getName());
			placeholders.put("{hand_material}", event.getItem() != null ?
					event.getItem().getType().name() : "AIR");
			if (event.getClickedBlock() != null)
			{
				placeholders.put("{block_material}", event.getClickedBlock().getType().name());
				placeholders.put("{block_world}", event.getClickedBlock().getLocation().getWorld().getName());
				placeholders.put("{block_x}", "" + Location.locToBlock(event.getClickedBlock().getLocation().getX()));
				placeholders.put("{block_y}", "" + Location.locToBlock(event.getClickedBlock().getLocation().getY()));
				placeholders.put("{block_z}", "" + Location.locToBlock(event.getClickedBlock().getLocation().getZ()));
			}
			else {
				placeholders.put("{block_material}", "AIR");
			}
			
			// Check if the trigger type "BLOCK_USE" should be used
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK ||
					event.getAction() == Action.RIGHT_CLICK_AIR)
			{
				// Run actions for all triggers with type "BLOCK_USE"
				for (Trigger t : HandleTriggers.getTriggerManager().getBlockUseTriggers())
				{
					t.runActions(placeholders, event.getPlayer());
				}
			}
			// Check if the trigger type "BLOCK_ATTACK" should be used
			else if (event.getAction() == Action.LEFT_CLICK_BLOCK ||
					event.getAction() == Action.LEFT_CLICK_AIR)
			{
				// Run actions for all triggers with type "BLOCK_ATTACK"
				for (Trigger t : HandleTriggers.getTriggerManager().getBlockAttackTriggers())
				{
					t.runActions(placeholders, event.getPlayer());
				}
			}
			// Check if the trigger type "BLOCK_INTERACT" should be used
			else
			{
				// Run actions for all triggers with type "BLOCK_INTERACT"
				for (Trigger t : HandleTriggers.getTriggerManager().getBlockInteractTriggers())
				{
					t.runActions(placeholders, event.getPlayer());
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
	{
		Map<String, String> placeholders = new HashMap<>();
		
		// Set placeholders
		placeholders.put("{player}", event.getPlayer().getName());
		placeholders.put("{hand_material}", event.getPlayer().getInventory().getItemInMainHand().getType().name());
		placeholders.put("{block_material}", event.getBlock().getType().name());
		placeholders.put("{block_world}", event.getBlock().getLocation().getWorld().getName());
		placeholders.put("{block_x}", "" + Location.locToBlock(event.getBlock().getLocation().getX()));
		placeholders.put("{block_y}", "" + Location.locToBlock(event.getBlock().getLocation().getY()));
		placeholders.put("{block_z}", "" + Location.locToBlock(event.getBlock().getLocation().getZ()));
		
		// Run actions for all triggers with type "BREAK"
		for (Trigger t : HandleTriggers.getTriggerManager().getBreakTriggers())
		{
			t.runActions(placeholders, event.getPlayer());
		}
	}
	
	@EventHandler
	public void onPLayerJoin(PlayerJoinEvent event)
	{
		Map<String, String> placeholders = new HashMap<>();
		
		// Set placeholders
		placeholders.put("{player}", event.getPlayer().getName());
		
		// Run actions for all triggers with type "PLACE"
		for (Trigger t : HandleTriggers.getTriggerManager().getJoinTriggers())
		{
			if (t.getProperties().getBoolean("cancel-message"))
			{
				event.setJoinMessage(null);
			}
			t.runActions(placeholders, event.getPlayer());
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event)
	{
		Map<String, String> placeholders = new HashMap<>();
		
		// Set placeholders
		placeholders.put("{player}", event.getPlayer().getName());
		placeholders.put("{hand_material}", event.getItemInHand().getType().name());
		placeholders.put("{block_material}", event.getBlock().getType().name());
		placeholders.put("{block_world}", (event.getBlock().getLocation().getWorld().getName()));
		placeholders.put("{block_x}", "" + Location.locToBlock(event.getBlock().getLocation().getX()));
		placeholders.put("{block_y}", "" + Location.locToBlock(event.getBlock().getLocation().getY()));
		placeholders.put("{block_z}", "" + Location.locToBlock(event.getBlock().getLocation().getZ()));
		
		// Run actions for all triggers with type "PLACE"
		for (Trigger t : HandleTriggers.getTriggerManager().getPlaceTriggers())
		{
			t.runActions(placeholders, event.getPlayer());
		}
	}
	
	@EventHandler
	public void onPLayerQuit(PlayerQuitEvent event)
	{
		Map<String, String> placeholders = new HashMap<>();
		
		// Set placeholders
		placeholders.put("{player}", event.getPlayer().getName());
		
		// Run actions for all triggers with type "PLACE"
		for (Trigger t : HandleTriggers.getTriggerManager().getQuitTriggers())
		{
			if (t.getProperties().getBoolean("cancel-message"))
			{
				event.setQuitMessage(null);
			}
			t.runActions(placeholders, event.getPlayer());
		}
	}
}
