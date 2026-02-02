package com.micahslife.ml_echoes.listeners;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.micahslife.ml_echoes.data.MLConstants;

import java.util.logging.Level;

/**
 * Listeners for player events
 */
public class PlayerListener {

    /**
     * Register all player event listeners
     * @param eventBus The event registry to register listeners with
     */
    public void register(EventRegistry eventBus) {
        // PlayerReadyEvent - When a player is ready
        // TODO: Remove if not using player ready event
        try {
            eventBus.registerGlobal(PlayerReadyEvent.class, this::onPlayerReady);
        } catch (Exception e) {
            MLConstants.LOGGER.at(Level.WARNING).withCause(e).log("[ML_Echoes] Failed to register PlayerReadyEvent");
        }

        // TODO: Remove if not using player disconnect event
        // PlayerDisconnectEvent - When a player disconnects
        try {
            eventBus.registerGlobal(PlayerDisconnectEvent.class, this::onPlayerDisconnect);
        } catch (Exception e) {
            MLConstants.LOGGER.at(Level.WARNING).withCause(e).log("[ML_Echoes] Failed to register PlayerDisconnectEvent");
        }
    }

    /**
     * Handle player ready event
     * @param event The player ready event
     */
    private void onPlayerReady(PlayerReadyEvent event) {
        //String playerName = event.getPlayer().getDisplayName();
        //String worldName = event.getPlayer().getWorld() != null ? event.getPlayer().getWorld().getName() : "Unknown";


    }

    /**
     * Handle player disconnect event
     * @param event The player disconnect event
     */
    private void onPlayerDisconnect(PlayerDisconnectEvent event) {
        //String playerName = event.getPlayerRef() != null ? event.getPlayerRef().getUsername() : "Unknown";


    }

}
