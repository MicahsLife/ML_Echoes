package com.micahslife.ml_echoes.listeners;

import com.hypixel.hytale.event.EventRegistry;
import com.micahslife.ml_echoes.data.MLConstants;

import java.util.logging.Level;

/**
 * Register the event listeners
 */
public class _ListenerRegistration {

    /**
     * Register the event listeners
     * @param registry the event registry
     */
    public static void register(EventRegistry registry) {
        try {
            new PlayerListener().register(registry);

            MLConstants.LOGGER.at(Level.INFO).log("[ML_Echoes] Registered event listeners");
        } catch (Exception e) {
            MLConstants.LOGGER.at(Level.WARNING).withCause(e).log("[ML_Echoes] Failed to register event listeners");
        }
    }

}
