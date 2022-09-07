package me.valion.bukkit.handle.core.actions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CmdAsOpAction extends Action
{
	private String actionString;
	private Player targetPlayer;
	
	public CmdAsOpAction(String actionString, Player targetPlayer)
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
		boolean wasOp = targetPlayer.isOp();
		
		// Gives op to the player, if the player wasn't an op before
		if (!wasOp)
		{
			targetPlayer.setOp(true);
		}
		
		// Runs the action
		Bukkit.dispatchCommand(targetPlayer, actionString);
		
		// Removes op from the player, if the player wasn't an op before
		if (!wasOp)
		{
			targetPlayer.setOp(false);
		}
	}
}
