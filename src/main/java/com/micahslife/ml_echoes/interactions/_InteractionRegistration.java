package com.micahslife.ml_echoes.interactions;

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.registry.CodecMapRegistry;
import com.micahslife.ml_echoes.data.MLConstants;

import java.util.logging.Level;

/**
 * Register the interaction events
 */
public class _InteractionRegistration {

    /**
     * Register the interaction events
     * @param registry the codec registry
     */
    public static void register(CodecMapRegistry.Assets<Interaction, ?> registry) {
        try {
            registry.register("ml_echoes.echo", EchoInteraction.class, EchoInteraction.CODEC);
            registry.register("ml_echoes.echo_settings", EchoSettingsInteraction.class, EchoSettingsInteraction.CODEC);

            MLConstants.LOGGER.at(Level.INFO).log("[ML_Echoes] Registered interaction events");
        } catch (Exception e) {
            MLConstants.LOGGER.at(Level.WARNING).withCause(e).log("[ML_Echoes] Failed to register interaction events");
        }
    }
}
