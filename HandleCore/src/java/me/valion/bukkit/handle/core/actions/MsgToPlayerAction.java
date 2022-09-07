package me.valion.bukkit.handle.core.actions;

import me.valion.bukkit.handle.core.util.FormatUtility;
import org.bukkit.entity.Player;

public class MsgToPlayerAction extends Action
{
	private String actionString;
	private Player targetPlayer;
	
	public MsgToPlayerAction(String actionString, Player targetPlayer)
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
		FormatUtility.sendFormattedMessage(targetPlayer, actionString);
	}
}