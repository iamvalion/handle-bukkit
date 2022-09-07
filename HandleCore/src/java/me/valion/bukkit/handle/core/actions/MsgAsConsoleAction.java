package me.valion.bukkit.handle.core.actions;

import me.valion.bukkit.handle.core.util.FormatUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MsgAsConsoleAction extends Action
{
	private String actionString;
	
	public MsgAsConsoleAction(String actionString)
	{
		this.actionString = actionString;
	}
	
	public String getString()
	{
		return this.actionString;
	}
	
	public void run()
	{
		for (Player p : Bukkit.getOnlinePlayers())
		{
			FormatUtility.sendFormattedMessage(p, actionString);
		}
	}
}