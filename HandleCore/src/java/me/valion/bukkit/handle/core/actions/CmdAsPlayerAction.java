package me.valion.bukkit.handle.core.actions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CmdAsPlayerAction extends Action
{
	private Player targetPlayer;
	private String actionString;
	
	public CmdAsPlayerAction(String actionString, Player targetPlayer)
	{
		this.targetPlayer = targetPlayer;
		this.actionString = actionString;
	}
	
	public String getString()
	{
		return this.actionString;
	}
	
	public void run()
	{
		Bukkit.dispatchCommand(targetPlayer, actionString);
	}
}
