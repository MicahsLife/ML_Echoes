package com.micahslife.ml_echoes.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.permissions.HytalePermissions;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import com.micahslife.ml_echoes.ML_EchoesPlugin;
import com.micahslife.ml_echoes.ui.PluginPage;

import javax.annotation.Nonnull;

/**
 * /ml_echoes menu - Open the plugin menu
 * Extends AbstractPlayerCommand to ensure proper thread handling
 * when opening custom UI pages.
 */
public class MenuSubCommand extends AbstractPlayerCommand {

    public MenuSubCommand() {
        super("menu", "Open the plugin menu");

        requirePermission(HytalePermissions.fromCommand("usercommands"));
    }

    @Override
    protected boolean canGeneratePermission() {
        return false;
    }

    /**
     * Called on the world thread with proper player context.
     */
    @Override
    protected void execute(
            @Nonnull CommandContext context,
            @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> ref,
            @Nonnull PlayerRef playerRef,
            @Nonnull World world
    ) {
        context.sendMessage(Message.raw("Opening ML_Echoes Menu..."));

        try {
            // Get the player component (safe - we're on world thread)
            Player player = store.getComponent(ref, Player.getComponentType());
            if (player == null) {
                context.sendMessage(Message.raw("Error: Could not get Player component."));
                return;
            }

            // Create and open the custom page
            PluginPage pluginPage = new PluginPage(playerRef);
            pluginPage.updateCanCraft(ML_EchoesPlugin.getConfig().getCanCraftWand());
            player.getPageManager().openCustomPage(ref, store, pluginPage);

            context.sendMessage(Message.raw("Menu opened. Press ESC to close."));
        } catch (Exception e) {
            context.sendMessage(Message.raw("Error opening menu: " + e.getMessage()));
        }
    }

}
