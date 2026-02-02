package com.micahslife.ml_echoes.commands;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;
import com.hypixel.hytale.server.core.permissions.HytalePermissions;

/**
 * Main command for ML_Echoes plugin
 * Usage:
 * - /ml_echoes help - Show available commands
 * - /ml_echoes info - Show plugin information
 * - /ml_echoes reload - Reload the plugin
 * - /ml_echoes menu - Open plugin menu
 */
public class PluginCommand extends AbstractCommandCollection {

    public PluginCommand() {
        super("ml_echoes", "ML_Echoes plugin commands");

        requirePermission(HytalePermissions.fromCommand("usercommands"));

        addSubCommand(new HelpSubCommand());
        addSubCommand(new InfoSubCommand());
        addSubCommand(new ReloadSubCommand());
        addSubCommand(new MenuSubCommand());
    }

    @Override
    protected boolean canGeneratePermission() {
        return false; // No permission required for base command
    }

}
