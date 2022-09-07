package me.valion.bukkit.handle.core.util;

import org.bukkit.command.CommandSender;

import java.util.List;

public class FormatUtility
{
	/**
	 * Formats text using Minecraft color codes
	 * 
	 * @param text The text to be formatted
	 * @param translateColors If true, translates color codes
	 * @param translateFormats If true, translates format codes
	 * @return The string of formatted text
	 */
	public static String formatMessage(String text, boolean translateColors,
			boolean translateFormats)
	{
		char[] chars = text.toCharArray();
		
		if (translateColors)
		{
			for (int i = 0; i < chars.length - 1; ++i)
			{
				if ((chars[i] == '&' || chars[i] == '§')
						&& ("0123456789AaBbCcDdEeFf".indexOf(chars[i + 1]) >= 0))
				{
					chars[i] = '§';
					chars[i + 1] = Character.toLowerCase(chars[i + 1]);
				}
			}
		}
		if (translateFormats)
		{
			for (int i = 0; i < chars.length - 1; i++)
			{
				if ((chars[i] == '&' || chars[i] == '§')
						&& ("KkLlMmNnOoRr".indexOf(chars[i + 1]) >= 0))
				{
					chars[i] = '§';
					chars[i + 1] = Character.toLowerCase(chars[i + 1]);
				}
			}
		}
		
		return new String(chars);
	}
	
	/**
	 * Overloads {@code formatMessage(String text, boolean translateColors, boolean
	 *  translateFormats)}, with {@code true} passed as the {@code translateColors} parameter and
	 *  {@code true} passed as thee {@code translateFormats} parameter
	 *  
	 * @param text The text to be formatted
	 * @return The string of formatted text
	 */
	public static String formatMessage(String text)
	{
		return formatMessage(text, true, true);
	}
	
	/**
	 * Sends a formatted message to the specified recipient
	 * 
	 * @param recipient The recipient (player or otherwise) of the message being sent
	 * @param text The text to be formatted and sent
	 * @param translateColors If true, translates color codes
	 * @param translateFormats If true, translates format codes
	 * @param multipleLines If true, splits the text into multiple lines, by the configured line
	 * 						splitter
	 */
	public static void sendFormattedMessage(CommandSender recipient, String text,
			boolean translateColors, boolean translateFormats, boolean multipleLines)
	{
		text = formatMessage(text, translateColors, translateFormats);
		List<String> lines = StringUtility.splitIntoList(text);
		
		if (multipleLines)
		{
			for (String l : lines)
			{
				recipient.sendMessage(l);
			}
		}
		else
		{
			recipient.sendMessage(text);
		}
	}
	
	/**
	 * Overloads {@code sendFormattedMessage(CommandSender recipient, String text, boolean
	 *  translateColors, boolean translateFormats, boolean multipleLines)}, with {@code true} passed
	 *  as the {@code multipleLines} parameter
	 *  
	 * @param recipient The recipient (player or otherwise) of the message being sent
	 * @param text The text to be formatted and sent
	 * @param translateColors If true, translates color codes
	 * @param translateFormats If true, translates format codes
	 */
	public static void sendFormattedMessage(CommandSender recipient, String text,
			boolean translateColors, boolean translateFormats)
	{
		sendFormattedMessage(recipient, text, translateColors, translateFormats, true);
	}
	
	/**
	 * Overloads {@code sendFormattedMessage(CommandSender recipient, String text, boolean
	 *  translateColors, boolean translateFormats, boolean multipleLines)}, with {@code true} passed
	 *  as the {@code translateColors} parameter, {@code true} passed as thee {@code
	 *  translateFormats} parameter, and {@code true} passed as the {@code multipleLines} parameter
	 *  
	 * @param recipient The recipient (player or otherwise) of the message being sent
	 * @param text The text to be formatted and sent
	 */
	public static void sendFormattedMessage(CommandSender recipient, String text)
	{
		sendFormattedMessage(recipient, text, true, true, true);
	}
	
	/*
	 * Prevents external instantiation
	 */
	private FormatUtility() {}
}
