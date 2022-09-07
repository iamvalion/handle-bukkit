package me.valion.bukkit.handle.core.data;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FilingCabinet
{
	private Plugin plugin;
	private Map<String, DataFile> dataFiles = new HashMap<>();
	
	/**
	 * Creates a {@code FilingCabinet} object, which handles any number of data (configuration) files
	 * 
	 * @param plugin The plug-in for which this {@code FilingCabinet} object is handling data files
	 */
	public FilingCabinet(Plugin plugin)
	{
		this.plugin = plugin;
	}
	
	
	/**
	 * Adds a data file to this {@code FilingCabinet} if one by that {@code filePath} doesn't exist yet
	 * 
	 * @param filePath The path of the file being saved to and loaded from (relative to the plug-in's data
	 * 					folder)
	 * @param templateFilePath The path of the file in the plug-in JAR which contains the default configuration
	 * 							for the data file
	 * @param copyDefaults If true, copies template file's configuration values to the data file (i.e. if they
	 * 						are missing from the file)
	 */
	public void addDataFile(String filePath, String templateFilePath, boolean copyDefaults)
	{
		// Exits if the specified data file is already in the filing cabinet
		if (dataFiles.get(filePath) != null)
		{
			plugin.getLogger().info("Data file \"" + filePath + "\" is already being managed.");
			return;
		}
		
		dataFiles.put(filePath, new DataFile(this.plugin, filePath, templateFilePath, copyDefaults));
		
		loadDataFile(filePath);
		saveDataFile(filePath);
	}
	
	/**
	 * Overloads {@code addDataFile(String filePath, String templateFilePath, boolean copyDefaults)}, with
	 *  {@code true} passed as the {@code copyDefaults} parameter
	 *  
	 * @param filePath The path of the file being saved to and loaded from (relative to the plug-in's data
	 *  folder)
	 * @param templateFilePath The path of the file in the plug-in JAR which contains the default configuration
	 *  for the data file
	 */
	public void addDataFile(String filePath, String templateFilePath)
	{
		addDataFile(filePath, templateFilePath, true);
	}
	
	/**
	 * Overloads {@code addDataFile(String filePath, String templateFilePath, boolean copyDefaults, boolean
	 *  createFile)}, with {@code filePath} passed as the {@code templateFilePath} parameter
	 * 
	 * @param filePath The path of the file being saved to and loaded from (relative to the plug-in's data
	 *  folder)
	 * @param copyDefaults If true, copies template file's configuration values to the data file (i.e. if they
	 *  are missing from the file)
	 */
	public void addDataFile(String filePath, boolean copyDefaults)
	{
		addDataFile(filePath, filePath, copyDefaults);
	}
	
	/**
	 * Overloads {@code addDataFile(String filePath, String templateFilePath, boolean copyDefaults, boolean
	 *  createFile)}, with {@code filePath} passed as the {@code templateFilePath} parameter and {@code true}
	 *  passed as the {@code copyDefaults} parameter
	 * 
	 * @param filePath The path of the file being saved to and loaded from (relative to the plug-in's data
	 *  folder)
	 */
	public void addDataFile(String filePath)
	{
		addDataFile(filePath, filePath, true);
	}
	
	
	/**
	 * Removes a data file from this {@code FilingCabinet} if one by that {@code filePath} exists
	 * 
	 * @param filePath The path of the file being removed
	 * @param deleteFile If true, deletes the file from the file system as well as removing it from this {@code
	 *  FilingCabinet} (in memory)
	 */
	public void removeDataFile(String filePath, boolean deleteFile)
	{
		plugin.getLogger().info("Removing data file \"" + filePath + "\"");
		
		// Deletes the file from the file system, if specified
		if (deleteFile)
		{
			try
			{
				Files.deleteIfExists(Paths.get(plugin.getDataFolder().getPath(), filePath));
			}
			catch (IOException e)
			{
				plugin.getLogger().warning("Error deleting data file \"" + filePath
						+ "\" from the system: " + e);
			}
		}
		
		dataFiles.remove(filePath);
	}
	
	/**
	 * Overloads {@code removeDataFile(String filePath, boolean deleteFile)}, with {@code false} passed as the
	 *  {@code deleteFile} parameter
	 * 
	 * @param filePath The path of the file being removed
	 */
	public void removeDataFile(String filePath)
	{
		removeDataFile(filePath, false);
	}
	
	
	/**
	 * Saves this {@code FilingCabinet}'s specified data file configuration
	 * 
	 * @param filePath The path of the file being saved to (relative to the plug-in's data folder)
	 */
	public void saveDataFile(String filePath)
	{
		DataFile dataFile = dataFiles.get(filePath);
		
		// Exits if the specified data file isn't in the filing cabinet
		if (dataFile == null)
		{
			plugin.getLogger().warning("Data file \"" + filePath + "\" not found.");
			return;
		}
		
		// Creates the data file if it does not exist
		if (!dataFile.getFile().isFile())
		{
			plugin.getLogger().info("Data file \"" + filePath +
					"\" does not exist. Creating data file from template \""
					+ dataFile.getTemplateFilePath() + "\"");
			try
			{
				dataFile.saveTemplateConfiguration();
			}
			catch (IOException e)
			{
				plugin.getLogger().warning("Error creating data file from template: " + e);
			}
		}
		
		// Ensures that the template's header and any default values are copied over to the data file, if
		// specified
		dataFile.getConfiguration().options().copyHeader(true);
		dataFile.getConfiguration().options().copyDefaults(dataFile.getCopyDefaults());
		
		// Saves the configuration to the data file
		plugin.getLogger().info("Saving to data file \"" + filePath + "\"");
		try
		{
			dataFile.saveConfiguration();
		}
		catch (IOException e)
		{
			plugin.getLogger().warning("Error saving to data file: " + e);
		}
	}
	
	/**
	 * Saves all of this {@code FilingCabinet}'s data file configurations
	 */
	public void saveAllDataFiles()
	{
		dataFiles.forEach((filePath, dataFile) -> saveDataFile(filePath));
	}
	
	
	/**
	 * Loads this {@code FilingCabinet}'s specified data file configuration
	 * 
	 * @param filePath The path of the file being loaded from (relative to the plug-in's data folder)
	 */
	public void loadDataFile(String filePath)
	{
		DataFile dataFile = dataFiles.get(filePath);

		// Exits if the specified data file isn't in the filing cabinet
		if (dataFile == null)
		{
			plugin.getLogger().warning("Data file \"" + filePath + "\" not found.");
			return;
		}
		
		// Creates the data file if it does not exist
		if (!dataFile.getFile().isFile())
		{
			plugin.getLogger().info("Data file \"" + filePath +
					"\" does not exist. Creating data file from template \""
					+ dataFile.getTemplateFilePath() + "\"");
			try
			{
				dataFile.saveTemplateConfiguration();
			}
			catch (IOException e)
			{
				plugin.getLogger().warning("Error creating data file from template: " + e);
			}
		}
		
		// Loads the data file's configuration to memory
		plugin.getLogger().info("Loading data file \"" + filePath + "\"");
		try
		{
			dataFile.loadConfiguration();
		}
		catch (UnsupportedEncodingException e)
		{
			plugin.getLogger().warning("Error loading data file: " + e);
		}
	}
	
	/**
	 * Loads all of this {@code FilingCabinet}'s data file configurations
	 */
	public void loadAllDataFiles()
	{
		dataFiles.forEach((filePath, dataFile) -> loadDataFile(filePath));
	}
	
	
	/**
	 * Gets the configuration of this {@code FilingCabinet}'s specified data file
	 * 
	 * @param filePath The path of the file being loaded from (relative to the plug-in's data folder)
	 * 
	 * @return The configuration of the file at the specified path
	 */
	public ConfigurationSection getConfiguration(String filePath)
	{
		return dataFiles.get(filePath).getConfiguration();
	}
}
