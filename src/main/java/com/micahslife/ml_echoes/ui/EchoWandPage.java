package com.micahslife.ml_echoes.ui;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.micahslife.ml_echoes.components.EchoStoreComponent;
import com.micahslife.ml_echoes.components._ComponentRegistration;
import com.micahslife.ml_echoes.data.MLConstants;

import javax.annotation.Nonnull;
import java.util.logging.Level;

/**
 * ML_Echoes Echo Wand UI
 */
public class EchoWandPage extends InteractiveCustomUIPage<EchoWandPage.UIEventData> {

    // Path relative to Common/UI/Custom/
    public static final String LAYOUT = "ml_echoes/EchoWand.ui";

    private final PlayerRef playerRef;

    public EchoWandPage(@Nonnull PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismiss, UIEventData.CODEC);
        this.playerRef = playerRef;
    }

    @Override
    public void build(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull UICommandBuilder cmd,
            @Nonnull UIEventBuilder evt,
            @Nonnull Store<EntityStore> store
    ) {
        // Load base layout
        cmd.append(LAYOUT);

        // Bind rgb slider
        evt.addEventBinding(
                CustomUIEventBindingType.ValueChanged,
                "#ColorPicker",
                new EventData().append("Action", "color_changed"),
                false
        );

        // Bind accept button
        evt.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#AcceptButton",
                new EventData().append("Action", "accept").append("@RGB", "#ColorPicker.Value"),
                false
        );

        // Bind cancel button
        evt.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#CancelButton",
                new EventData().append("Action", "cancel"),
                false
        );
    }

    @Override
    public void handleDataEvent(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull Store<EntityStore> store,
            @Nonnull UIEventData data
    ) {
        if (data.action == null) return;

        switch (data.action) {
            case "color_changed":
                sendUpdate();
                break;

            case "accept":
                if (this.playerRef != null) setColorValue(data.rgb);
                this.close();
                break;

            case "cancel":
                this.close();
                break;
        }
    }

    /**
     * Update the color picker value
     * @param color The rgb string value to update to
     */
    public void updateColor(String color) {
        UICommandBuilder uiCommandBuilder = new UICommandBuilder();
        uiCommandBuilder.set("#ColorPicker.Value", color);
        sendUpdate(uiCommandBuilder, false);
    }

    /**
     * Set the color value on the player if possible
     * @param color The rgb string value to set
     */
    private void setColorValue(String color) {
        Ref<EntityStore> ref = this.playerRef.getReference();
        Store<EntityStore> store = (ref != null) ? ref.getStore() : null;
        ComponentType<EntityStore, EchoStoreComponent> echoDataComponent = _ComponentRegistration.getEchoData();

        try {
            if (store != null && echoDataComponent != null) {
                EchoStoreComponent data = store.getComponent(ref, echoDataComponent);

                if (data == null) {
                    EchoStoreComponent newComponent = new EchoStoreComponent();
                    store.putComponent(ref, _ComponentRegistration.getEchoData(), newComponent);
                    data = store.getComponent(ref, echoDataComponent);
                }
                if (data != null) {
                    int r = Integer.valueOf(color.substring(1, 3), 16);
                    int g = Integer.valueOf(color.substring(3, 5), 16);
                    int b = Integer.valueOf(color.substring(5, 7), 16);
                    data.setColor((r << 16) | (g << 8) | b);
                } else {
                    MLConstants.LOGGER.at(Level.WARNING).log("[ML_Echoes] Unable to store echo color data on player");
                }
            }
        } catch (Exception e) {
            MLConstants.LOGGER.at(Level.WARNING).withCause(e).log("[ML_Echoes] Failed to handle echo color data");
        }
    }

    /**
     * Event data class with codec for handling UI events.
     */
    public static class UIEventData {

        public static final BuilderCodec<UIEventData> CODEC = BuilderCodec.builder(UIEventData.class, UIEventData::new)
                .append(new KeyedCodec<>("Action", Codec.STRING), (e, v) -> e.action = v, e -> e.action).add()
                .append(new KeyedCodec<>("@RGB", Codec.STRING), (e, v) -> e.rgb = v, e -> e.rgb).add()
                .build();

        private String action;
        private String rgb = "#6b97be";

    }

}
