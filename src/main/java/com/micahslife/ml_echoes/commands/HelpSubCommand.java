package com.micahslife.ml_echoes.commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.permissions.HytalePermissions;

import javax.annotation.Nonnull;

/**
 * /ml_echoes help - Show available commands
 */
public class HelpSubCommand extends CommandBase {

    public HelpSubCommand() {
        super("help", "Show available commands");

        requirePermission(HytalePermissions.fromCommand("usercommands"));
    }

    @Override
    protected boolean canGeneratePermission() {
        return false;
    }

    @Override
    protected void executeSync(@Nonnull CommandContext context) {
        context.sendMessage(Message.raw(""));
        context.sendMessage(Message.raw("=== ML_Echoes Commands ==="));
        context.sendMessage(Message.raw("/ml_echoes help - Show this help message"));
        context.sendMessage(Message.raw("/ml_echoes info - Show plugin information"));
        context.sendMessage(Message.raw("/ml_echoes reload - Reload the plugin"));
        context.sendMessage(Message.raw("/ml_echoes menu - Open the plugin menu"));
        context.sendMessage(Message.raw("============================"));
    }

}
