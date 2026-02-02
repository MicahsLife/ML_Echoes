package com.micahslife.ml_echoes;

import com.hypixel.hytale.server.core.io.adapter.PacketAdapters;
import com.hypixel.hytale.server.core.io.adapter.PacketFilter;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

import com.hypixel.hytale.server.core.util.Config;
import com.micahslife.ml_echoes.commands._CommandRegistration;
import com.micahslife.ml_echoes.components._ComponentRegistration;
import com.micahslife.ml_echoes.config.ConfigHandler;
import com.micahslife.ml_echoes.data.MLConstants;
import com.micahslife.ml_echoes.interactions._InteractionRegistration;
import com.micahslife.ml_echoes.listeners.HotbarHandler;
import com.micahslife.ml_echoes.listeners._ListenerRegistration;
import com.micahslife.ml_echoes.systems._SystemRegistration;

import javax.annotation.Nonnull;
import java.util.logging.Level;

/**
 * ML_Echoes - A Hytale server plugin
 */
public class ML_EchoesPlugin extends JavaPlugin {

    private static ML_EchoesPlugin instance;
    private PacketFilter inboundFilter;

    private final Config<ConfigHandler> config;

    public ML_EchoesPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        instance = this;

        this.config = this.withConfig("ML_Echoes", ConfigHandler.CODEC);
        this.config.load();
        this.config.save();
    }

    /**
     * Get the plugin instance
     * @return The plugin instance
     */
    public static ML_EchoesPlugin getInstance() {
        return instance;
    }

    /**
     * Get the plugin config
     * @return The plugin config
     */
    public static ConfigHandler getConfig() {
        return instance.config.get();
    }

    /**
     * Get the plugin version
     * @return The plugin version
     */
    public String getVersion() {
        return this.getManifest().getVersion().toString();
    }

    @Override
    protected void setup() {
        MLConstants.LOGGER.at(Level.INFO).log("[ML_Echoes] Setting up...");

        // Registrations
        _CommandRegistration.register(getCommandRegistry());
        _ListenerRegistration.register(getEventRegistry());
        _SystemRegistration.register(getEntityStoreRegistry());
        _ComponentRegistration.register(getEntityStoreRegistry());
        _InteractionRegistration.register(getCodecRegistry(Interaction.CODEC));

        // Packets
        HotbarHandler handler = new HotbarHandler();
        inboundFilter = PacketAdapters.registerInbound(handler);

        MLConstants.LOGGER.at(Level.INFO).log("[ML_Echoes] Setup complete!");
    }

    @Override
    protected void start() {
        MLConstants.LOGGER.at(Level.INFO).log("[ML_Echoes] Started!");
        MLConstants.LOGGER.at(Level.INFO).log("[ML_Echoes] Use /ml_echoes help for commands");
    }

    @Override
    protected void shutdown() {
        if (inboundFilter != null) PacketAdapters.deregisterInbound(inboundFilter);

        MLConstants.LOGGER.at(Level.INFO).log("[ML_Echoes] Shutting down...");
        instance = null;
    }

}
