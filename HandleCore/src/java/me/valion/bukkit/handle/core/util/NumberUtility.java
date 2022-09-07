package me.valion.bukkit.handle.core.util;

import java.text.DecimalFormat;

public class NumberUtility
{	
	/**
	 * Checks if a string can be parsed as an integer
	 * 
	 * @param input The string to be parsed
	 * @return True if the string can be parsed as an integer
	 */
	public static boolean isInteger(final String input)
	{
		try
		{
			Integer.parseInt(input);
		}
		catch (NumberFormatException e)
		{
			return false;
		}
		
		return true;
	}
	
	public static String formatDecimalNumber(double number, int decimalPlaces)
	{
		StringBuilder pattern = new StringBuilder();
		pattern.append("#0");
		if (decimalPlaces > 0)
		{
			pattern.append(".");
			for (int i = 0; i < decimalPlaces; ++i)
			{
				pattern.append("0");
			}
		}
		
		DecimalFormat numberFormat = new DecimalFormat(pattern.toString());
		
		return(numberFormat.format(number));
	}
	
	/*
	 * Prevents external instantiation
	 */
	private NumberUtility() {}
}
