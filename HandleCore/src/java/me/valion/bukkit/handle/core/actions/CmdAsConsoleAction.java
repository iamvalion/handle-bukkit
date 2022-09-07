package me.valion.bukkit.handle.core.actions;

import org.bukkit.Bukkit;

public class CmdAsConsoleAction extends Action
{
	private String actionString;
	
	public CmdAsConsoleAction(String actionString)
	{
		this.actionString = actionString;
	}
	
	public String getString()
	{
		return this.actionString;
	}
	
	public void run()
	{
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), actionString);
	}
}
