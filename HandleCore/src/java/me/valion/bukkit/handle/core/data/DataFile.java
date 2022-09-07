package me.valion.bukkit.handle.core.data;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class DataFile
{
	private Plugin plugin;
	private String filePath;
	private String templateFilePath;
	private File file;
	private FileConfiguration configuration;
	private boolean copyDefaults;
	
	/*
	 * Creates a DataFile object, which is managed by a FilingCabinet object
	 */
	protected DataFile(Plugin plugin, String filePath, String templateFilePath, boolean copyDefaults)
	{
		this.plugin = plugin;
		this.filePath = filePath;
		this.templateFilePath = templateFilePath;
		this.file = new File(plugin.getDataFolder(), filePath);
		this.copyDefaults = copyDefaults;
	}
	
	/*
	 * Saves the configuration of this DataFile's template file (in the JAR) to the file at filePath
	 * 
	 * Overrides all contents of the file
	 */
	protected void saveTemplateConfiguration() throws IOException
	{
		// Creates the parent directory/directories, if they don't exist
		file.getParentFile().mkdirs();
		
		// Reads from template file in the JAR and copies its configuration
		InputStream inputStream = plugin.getResource(templateFilePath);
		OutputStream outputStream = new FileOutputStream(file);
		
		int read;
		byte[] bytes = new byte[1024];
		while ((read = inputStream.read(bytes)) != -1)
		{
			outputStream.write(bytes, 0, read);
		}
		
		inputStream.close();
		outputStream.close();
	}
	
	/*
	 * Saves this DataFile's configuration, using Bukkit's built-in method
	 */
	protected void saveConfiguration() throws IOException
	{
		configuration.save(file);
	}
	
	/*
	 * Loads this DataFile's configuration to memory, using Bukkit's built-in method
	 */
	protected void loadConfiguration() throws UnsupportedEncodingException
	{
		// Loads the configuration to memory
		configuration = YamlConfiguration.loadConfiguration(file);

		// Sets default values of the configuration based on its template data file, if it exists
		Reader reader = new InputStreamReader(plugin.getResource(templateFilePath), StandardCharsets.UTF_8);
		if (reader != null)
		{
			configuration.setDefaults(YamlConfiguration.loadConfiguration(reader));
		}
	}
	
	protected String getFilePath()
	{
		return filePath;
	}
	
	protected String getTemplateFilePath()
	{
		return templateFilePath;
	}
	
	protected File getFile()
	{
		return file;
	}
	
	protected FileConfiguration getConfiguration()
	{
		return configuration;
	}
	
	protected boolean getCopyDefaults()
	{
		return copyDefaults;
	}
}
