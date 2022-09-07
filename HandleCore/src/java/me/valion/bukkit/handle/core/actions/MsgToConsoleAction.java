package me.valion.bukkit.handle.core.actions;

import org.bukkit.Bukkit;

public class MsgToConsoleAction extends Action
{
	private String actionString;
	
	public MsgToConsoleAction(String actionString)
	{
		this.actionString = actionString;
	}
	
	public String getString()
	{
		return this.actionString;
	}
	
	public void run()
	{
		Bukkit.getLogger().info(actionString);
	}
}