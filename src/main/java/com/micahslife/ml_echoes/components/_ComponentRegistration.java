package com.micahslife.ml_echoes.components;

import com.hypixel.hytale.component.ComponentRegistryProxy;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.micahslife.ml_echoes.data.MLConstants;

import javax.annotation.Nullable;
import java.util.logging.Level;

/**
 * Register the components
 */
public class _ComponentRegistration {

    @Nullable
    private static ComponentType<EntityStore, EchoStoreComponent> echoData;
    public static @Nullable ComponentType<EntityStore, EchoStoreComponent> getEchoData() {
        return echoData;
    }

    /**
     * Register the components
     * @param registry the entity registry
     */
    public static void register(ComponentRegistryProxy<EntityStore> registry) {
        try {
            echoData = registry.registerComponent(EchoStoreComponent.class, "EchoStoreComponent", EchoStoreComponent.CODEC);

            MLConstants.LOGGER.at(Level.INFO).log("[ML_Echoes] Registered components");
        } catch (Exception e) {
            MLConstants.LOGGER.at(Level.WARNING).withCause(e).log("[ML_Echoes] Failed to register components");
        }
    }
}
