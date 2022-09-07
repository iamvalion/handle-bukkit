package me.valion.bukkit.handle.core.util;

import me.valion.bukkit.handle.core.HandleCore;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Arrays;
import java.util.List;

public class StringUtility
{	
	/**
	 * Splits the string into a list of its individual components
	 * 
	 * @param text The string being split
	 * @param regex The delimiting regular expression
	 * @return The list of strings computed by splitting the string around matches of the given
	 * 			regular expression
	 */
	public static List<String> splitIntoList(String text, String regex)
	{
		return Arrays.asList(text.split(regex));
	}
	
	/**
	 * Overloads {@code splitIntoList(String text, String regex)}, with the config file's
	 * "misc.line-splitter" string passed as the {@code regex} parameter
	 * 
	 * @param text The string being split
	 * @return The list of strings computed by splitting the string around matches of the given
	 * 			regular expression
	 */
	public static List<String> splitIntoList(String text)
	{
		ConfigurationSection configFile = HandleCore.getFilingCabinet().getConfiguration("settings.yml");
		
		return splitIntoList(text, configFile.getString("misc.line-splitter"));
	}
	
	/*
	 * Prevents external instantiation
	 */
	private StringUtility() {}
}
