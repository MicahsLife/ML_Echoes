package com.micahslife.ml_echoes.listeners;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.modules.entity.component.DynamicLight;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
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
        // PlayerDisconnectEvent - When a player disconnects
        try {
            eventBus.register(PlayerDisconnectEvent.class, this::onPlayerDisconnect);
        } catch (Exception e) {
            MLConstants.LOGGER.at(Level.WARNING).withCause(e).log("[ML_Echoes] Failed to register PlayerDisconnectEvent");
        }
    }

    /**
     * Handle player disconnect event
     * @param event The player disconnect event
     */
    private void onPlayerDisconnect(PlayerDisconnectEvent event) {
        // Remove the dynamic light component if needed
        ComponentType<EntityStore, DynamicLight> lightComponent = DynamicLight.getComponentType();
        Holder<EntityStore> holder = event.getPlayerRef().getHolder();
        if (holder != null) holder.tryRemoveComponent(lightComponent);
    }

}
