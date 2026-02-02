package com.micahslife.ml_echoes.commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;

import com.hypixel.hytale.server.core.permissions.HytalePermissions;
import com.hypixel.hytale.server.core.plugin.PluginManager;
import com.micahslife.ml_echoes.ML_EchoesPlugin;

import javax.annotation.Nonnull;

/**
 * /ml_echoes reload - Reload the plugin
 */
public class ReloadSubCommand extends CommandBase {

    public ReloadSubCommand() {
        super("reload", "Reload the plugin");

        requirePermission(HytalePermissions.fromCommand("usercommands"));
    }

    @Override
    protected boolean canGeneratePermission() {
        return false;
    }

    @Override
    protected void executeSync(@Nonnull CommandContext context) {
        ML_EchoesPlugin plugin = ML_EchoesPlugin.getInstance();

        if (plugin == null) {
            context.sendMessage(Message.raw("Error: Plugin not loaded"));
            return;
        }

        context.sendMessage(Message.raw("Reloading ML_Echoes..."));

        PluginManager.get().reload(plugin.getIdentifier());

        context.sendMessage(Message.raw("ML_Echoes reloaded successfully!"));
    }

}
