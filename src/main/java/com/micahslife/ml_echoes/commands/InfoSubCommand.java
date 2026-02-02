package com.micahslife.ml_echoes.commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;

import com.hypixel.hytale.server.core.permissions.HytalePermissions;
import com.micahslife.ml_echoes.ML_EchoesPlugin;

import javax.annotation.Nonnull;

/**
 * /ml_echoes info - Show plugin information
 */
public class InfoSubCommand extends CommandBase {

    public InfoSubCommand() {
        super("info", "Show plugin information");

        requirePermission(HytalePermissions.fromCommand("usercommands"));
    }

    @Override
    protected boolean canGeneratePermission() {
        return false;
    }

    @Override
    protected void executeSync(@Nonnull CommandContext context) {
        ML_EchoesPlugin plugin = ML_EchoesPlugin.getInstance();

        context.sendMessage(Message.raw(""));
        context.sendMessage(Message.raw("=== ML_Echoes Info ==="));
        context.sendMessage(Message.raw("Name: ML_Echoes"));
        context.sendMessage(Message.raw("Version: " + plugin.getVersion()));
        context.sendMessage(Message.raw("Author: MicahsLife"));
        context.sendMessage(Message.raw("========================"));
    }

}
