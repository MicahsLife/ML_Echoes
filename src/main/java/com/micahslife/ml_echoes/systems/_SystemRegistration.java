package com.micahslife.ml_echoes.systems;

import com.hypixel.hytale.component.ComponentRegistryProxy;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.micahslife.ml_echoes.data.MLConstants;

import java.util.logging.Level;

/**
 * Register the entity event systems
 */
public class _SystemRegistration {

    /**
     * Register the entity event systems
     * @param registry the entity registry
     */
    public static void register(ComponentRegistryProxy<EntityStore> registry) {
        try {
            // TODO: Remove any systems that aren't in use
            registry.registerSystem(new PlayerTickSystem());

            MLConstants.LOGGER.at(Level.INFO).log("[ML_Echoes] Registered entity event systems");
        } catch (Exception e) {
            MLConstants.LOGGER.at(Level.WARNING).withCause(e).log("[ML_Echoes] Failed to register entity event systems");
        }
    }

}
