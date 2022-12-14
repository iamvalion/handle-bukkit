package me.valion.bukkit.handle.core.actions;

import bsh.Interpreter;
import me.valion.bukkit.handle.core.HandleCore;
import me.valion.bukkit.handle.core.util.StringUtility;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

public class Actor
{
	private ConfigurationSection settingsFile, actionPackagesFile;
	private List<Action> actions;
	
	public Actor()
	{
		this.actions = new ArrayList<>();
	}
	
	public void queueAction(String actionString, Map<String, String> placeholders, Player targetPlayer)
	{
		settingsFile = HandleCore.getFilingCabinet().getConfiguration("settings.yml");
		actionPackagesFile = HandleCore.getFilingCabinet().getConfiguration("action-packages.yml");
		
		actionString = actionString.trim();
		
		// Set trigger-specific variables
		if (placeholders != null && !placeholders.isEmpty())
		{
			for (Map.Entry<String, String> p : placeholders.entrySet())
			{
				actionString = actionString.replace(p.getKey(), p.getValue());
			}
		}
		// Set PlaceholderAPI variables
		actionString = PlaceholderAPI.setPlaceholders(targetPlayer, actionString);
		
		if (actionString.startsWith(settingsFile.getString("actions.prefixes.cmd-as-console")))
		{
			actionString = actionString.substring(
					settingsFile.getString("actions.prefixes.cmd-as-console").length()).trim();
			actions.add(new CmdAsConsoleAction(actionString));
		}
		else if (actionString.startsWith(settingsFile.getString("actions.prefixes.cmd-as-player")) &&
				targetPlayer != null)
		{
			actionString = actionString.substring(
					settingsFile.getString("actions.prefixes.cmd-as-player").length()).trim();
			actions.add(new CmdAsPlayerAction(actionString, targetPlayer));
		}
		else if (actionString.startsWith(settingsFile.getString("actions.prefixes.cmd-as-op")) &&
				targetPlayer != null)
		{
			actionString = actionString.substring(
					settingsFile.getString("actions.prefixes.cmd-as-op").length()).trim();
			actions.add(new CmdAsOpAction(actionString, targetPlayer));
		}
		else if (actionString.startsWith(settingsFile.getString("actions.prefixes.msg-as-console")))
		{
			actionString = actionString.substring(
					settingsFile.getString("actions.prefixes.msg-as-console").length()).trim();
			actions.add(new MsgAsConsoleAction(actionString));
		}
		else if (actionString.startsWith(settingsFile.getString("actions.prefixes.msg-as-player")) &&
				targetPlayer != null)
		{
			actionString = actionString.substring(
					settingsFile.getString("actions.prefixes.msg-as-player").length()).trim();
			actions.add(new MsgAsPlayerAction(actionString, targetPlayer));
		}
		else if (actionString.startsWith(settingsFile.getString("actions.prefixes.msg-to-console")))
		{
			actionString = actionString.substring(
					settingsFile.getString("actions.prefixes.msg-to-console").length()).trim();
			actions.add(new MsgToConsoleAction(actionString));
		}
		else if (actionString.startsWith(settingsFile.getString("actions.prefixes.msg-to-player")) &&
				targetPlayer != null)
		{
			actionString = actionString.substring(
					settingsFile.getString("actions.prefixes.msg-to-player").length()).trim();
			actions.add(new MsgToPlayerAction(actionString, targetPlayer));
		}
		else if (actionString.startsWith(settingsFile.getString("actions.prefixes.action-package")))
		{
			actionString = actionString.substring(
					settingsFile.getString("actions.prefixes.action-package").length()).trim();
			if (actionPackagesFile.get(actionString) != null)
			{
				if (actionPackagesFile.getString(actionString + ".type") != null
						&& actionPackagesFile.getStringList(actionString + ".actions") != null)
				{
					List<String> packageType = StringUtility.splitIntoList(
							actionPackagesFile.getString(actionString + ".type").trim(), " ");
					List<String> packageActions = actionPackagesFile.getStringList(actionString + ".actions");
					
					if (packageType.get(0).equalsIgnoreCase("all"))
					{
						for (String a : packageActions)
						{
							queueAction(a, targetPlayer);
						}
					}
					else if (packageType.get(0).equalsIgnoreCase("random"))
					{
						int count = 1;
						
						if (packageType.size() > 1)
						{
							count = Integer.parseInt(packageType.get(1));
						}
						
						for (String a : getRandomActions(packageActions, count))
						{
							queueAction(a, targetPlayer);
						}
					}
				}
			}
		}
		else
		{
			Bukkit.getLogger().warning("Error queueing the action '" + actionString + "'");
		}
	}
	
	public void queueAction(String actionString, Map<String, String> placeholders)
	{
		this.queueAction(actionString, placeholders, null);
	}
	
	public void queueAction(String actionString, Player targetPlayer)
	{
		this.queueAction(actionString, null, targetPlayer);
	}
	
	public void queueAction(String actionString)
	{
		this.queueAction(actionString, null, null);
	}
	
	public void queueActionsList(List<Object> actionsList, Map<String, String> placeholders,
			Player targetPlayer, boolean breakCalled)
	{
		Interpreter interpreter = new Interpreter();
		
		Bukkit.getLogger().info("for loop");
		for (int i = 0; i < actionsList.size() && !breakCalled; i++)
		{
			if (actionsList.get(i) instanceof String) {
				queueAction((String) actionsList.get(i), placeholders, targetPlayer);
				continue;
			}

			LinkedHashMap<String, Object> itemMap = (LinkedHashMap<String, Object>) actionsList.get(i);

			String condition = (String) itemMap.get("if");
			Boolean evaluation;

			Bukkit.getLogger().info("Component " + i);
			try
			{
				if (condition != null)
				{
					Bukkit.getLogger().info("  'if' is not null");
					Bukkit.getLogger().info("  'if' is '" + condition + "'");
					// Set trigger-specific variables
					if (placeholders != null && !placeholders.isEmpty())
					{
						for (Map.Entry<String, String> p : placeholders.entrySet())
						{
							condition = condition.replace(p.getKey(), p.getValue());
						}
					}
					// Set PlaceholderAPI variables
					condition = PlaceholderAPI.setPlaceholders(targetPlayer, condition);
					Bukkit.getLogger().info("  'if' is now '" + condition + "'");
					if (!(interpreter.eval(condition) instanceof Boolean))
					{
						throw new Exception();
					}
					evaluation = (Boolean) interpreter.eval(condition);
				}
				else
				{
					Bukkit.getLogger().info("  'if' is null");
					evaluation = Boolean.TRUE;
				}
				
				Bukkit.getLogger().info("  'if' evaluation is " + evaluation);

				List<Object> evaluationList;
				if (itemMap.get(evaluation) != null)
				{
					evaluationList = (List)(itemMap.get(evaluation));
				}
				else if (itemMap.get(evaluation.toString()) != null)
				{
					evaluationList = (List)(itemMap.get(evaluation.toString()));
				}
				else
				{
					continue;
				}

				for (int j = 0; j < evaluationList.size() && !breakCalled; j++)
				{
					// If Strings, set placeholders and queue the actions (unless break instruction received)
					if (evaluationList.get(j) instanceof String)
					{
						String a = (String) evaluationList.get(j);
						if (a.contains("$break"))
						{
							breakCalled = true;
							break;
						}

						Bukkit.getLogger().info("  - " + a + " <-- RAW");
						// Set trigger-specific variables
						if (placeholders != null && !placeholders.isEmpty())
						{
							for (Map.Entry<String, String> p : placeholders.entrySet())
							{
								a = a.replace(p.getKey(), p.getValue());
							}
						}
						// Set PlaceholderAPI variables
						a = PlaceholderAPI.setPlaceholders(targetPlayer, a);
						Bukkit.getLogger().info("  - " + a + " <-- PLACED");
						// Queue actions
						queueAction(a, targetPlayer);
					}
					// Else, recursively call this function
					else
					{
						Bukkit.getLogger().info("  Recursive call made");
						queueActionsList(evaluationList, placeholders, targetPlayer, false);
					}
				}
				
				if (breakCalled)
				{
					break;
				}
			}
			catch (Exception e)
			{
				Bukkit.getLogger().warning("Error: " + e.getMessage());
			}
		}

		Bukkit.getLogger().info("  'actions':");
		// Set placeholders and queue the actions
		if (!this.actions.isEmpty()){
			for (Action a : this.actions) {
				Bukkit.getLogger().info("  - " + a.getString());
			}
		}
		else
		{
			Bukkit.getLogger().info("    (empty)");
		}
	}

	public void queueActionsList(List<Object> actionsList, Map<String, String> placeholders,
			Player targetPlayer)
	{
		this.queueActionsList(actionsList, placeholders, targetPlayer, false);
	}
	
	public void queueActionsList(List<Object> actionsList, Map<String, String> placeholders)
	{
		this.queueActionsList(actionsList, placeholders, null, false);
	}
	
	public void queueActionsList(List<Object> actionsList, Player targetPlayer)
	{
		this.queueActionsList(actionsList, null, targetPlayer, false);
	}
	
	private List<String> getRandomActions(List<String> actions, int count)
	{
		Random random = new Random();
		List<String> randomActions = new ArrayList<>();
		
		for (int i = 0; i < count; i++)
		{
			// Get a random index of the actions list
			int randomIndex = random.nextInt(actions.size());
			
			// Add random action to random actions list
			randomActions.add(actions.get(randomIndex));
			
			// Remove that action from actions list to prevent duplicates
			actions.remove(randomIndex);
		}
		
		return randomActions; 
	}
	
	public void runActions()
	{
		if (actions != null && !actions.isEmpty())
		{
			for (Action a : actions)
			{
				a.run();
			}
			
			actions.clear();
		}
	}
}
