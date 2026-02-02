package com.micahslife.ml_echoes.commands;

import com.hypixel.hytale.server.core.command.system.CommandRegistry;
import com.micahslife.ml_echoes.data.MLConstants;

import java.util.logging.Level;

/**
 * Register the plugin commands
 */
public class _CommandRegistration {

    /**
     * Register the plugin commands
     * @param registry the command registry
     */
    public static void register(CommandRegistry registry) {
        try {
            registry.registerCommand(new PluginCommand());
            MLConstants.LOGGER.at(Level.INFO).log("[ML_Echoes] Registered /ml_echoes commands");
        } catch (Exception e) {
            MLConstants.LOGGER.at(Level.WARNING).withCause(e).log("[ML_Echoes] Failed to register commands");
        }
    }

}
