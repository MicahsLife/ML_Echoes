package com.micahslife.ml_echoes.ui;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
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
import com.micahslife.ml_echoes.ML_EchoesPlugin;

import javax.annotation.Nonnull;

/**
 * ML_Echoes Menu UI
 */
public class PluginPage extends InteractiveCustomUIPage<PluginPage.UIEventData> {

    // Path relative to Common/UI/Custom/
    public static final String LAYOUT = "ml_echoes/Menu.ui";

    public PluginPage(@Nonnull PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismiss, UIEventData.CODEC);
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

        // Update dynamic version
        cmd.set("#VersionText.Text", "Version: " + ML_EchoesPlugin.getInstance().getVersion() + " | By MicahsLife");

        // Bind refresh button
        evt.addEventBinding(
            CustomUIEventBindingType.Activating,
            "#AcceptButton",
                new EventData().append("Action", "accept").append("@CanCraft", "#CanCraft #CheckBox.Value"),
            false
        );

        // Bind close button
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
            case "accept":
                ML_EchoesPlugin.getConfig().setCanCraftWand(data.canCraft);
                this.close();
                break;

            case "cancel":
                this.close();
                break;
        }
    }

    /**
     * Update the can craft boolean value
     * @param canCraft The boolean value to update to
     */
    public void updateCanCraft(boolean canCraft) {
        UICommandBuilder uiCommandBuilder = new UICommandBuilder();
        uiCommandBuilder.set("#CanCraft #CheckBox.Value", canCraft);
        sendUpdate(uiCommandBuilder, false);
    }

    /**
     * Event data class with codec for handling UI events.
     */
    public static class UIEventData {

        public static final BuilderCodec<UIEventData> CODEC = BuilderCodec.builder(UIEventData.class, UIEventData::new)
                .append(new KeyedCodec<>("Action", Codec.STRING), (e, v) -> e.action = v, e -> e.action).add()
                .append(new KeyedCodec<>("@CanCraft", Codec.BOOLEAN), (e, v) -> e.canCraft = v, e -> e.canCraft).add()
                .build();

        private String action;
        private boolean canCraft = true;
    }

}
