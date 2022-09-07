package me.valion.bukkit.handle.core.debug;

import org.bukkit.plugin.Plugin;

public class Debugger
{
    private Plugin plugin;
    private enum DebugLevel
    {
        OFF, SUCCINCT, MODERATE, VERBOSE
    }
    private DebugLevel level;
    
    public Debugger(Plugin plugin, DebugLevel level)
    {
        this.plugin = plugin;
        this.level = level;
    }
    
    /**
     * Gets the current level of logging specificity of the debugger
     *
     * @return
     */
    public DebugLevel getLevel()
    {
        return this.level;
    }
    
    /**
     * Set the desired level of logging specificity for the debugger
     *
     * @param level
     */
    public void setLevel(DebugLevel level)
    {
        this.level = level;
    }
    
    public void setLevel(String level)
    {
        this.level = Enum.valueOf(DebugLevel.class, level);
    }
    
    /**
     * Send a debug message to console (and operators, if specified)
     *
     * @param message
     * @param messageLevel
     */
    public void debug(String message, DebugLevel messageLevel)
    {
        if (messageLevel == level)
        {
            plugin.getLogger().info(message);
        }
    }
    
    public void debug(String message, String messageLevel)
    {
        debug(message, Enum.valueOf(DebugLevel.class, messageLevel));
    }
    
    public void debug(String message)
    {
        debug(message, DebugLevel.VERBOSE);
    }
}
