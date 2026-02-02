package com.micahslife.ml_echoes.interactions;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.protocol.InteractionState;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.micahslife.ml_echoes.components.EchoStoreComponent;
import com.micahslife.ml_echoes.components._ComponentRegistration;
import com.micahslife.ml_echoes.data.MLConstants;
import com.micahslife.ml_echoes.ui.EchoWandPage;

import javax.annotation.Nonnull;
import java.util.logging.Level;

/**
 * The secondary interaction for the echo wand item
 */
public class EchoSettingsInteraction extends SimpleInstantInteraction {

    public static final BuilderCodec<EchoSettingsInteraction> CODEC = BuilderCodec.builder(
            EchoSettingsInteraction.class, EchoSettingsInteraction::new, SimpleInstantInteraction.CODEC
    ).build();

    @Override
    protected void firstRun(@Nonnull InteractionType interactionType, @Nonnull InteractionContext interactionContext, @Nonnull CooldownHandler cooldownHandler) {
        CommandBuffer<EntityStore> commandBuffer = interactionContext.getCommandBuffer();

        if (commandBuffer == null) {
            interactionContext.getState().state = InteractionState.Failed;
            MLConstants.LOGGER.at(Level.INFO).log("[ML_Echoes] CommandBuffer is null");
            return;
        }

        Store<EntityStore> store = commandBuffer.getExternalData().getStore();
        Ref<EntityStore> ref = interactionContext.getEntity();
        Player player = commandBuffer.getComponent(ref, Player.getComponentType());
        PlayerRef playerRef = store.getComponent(ref, PlayerRef.getComponentType());

        // Check for valid player
        if (playerRef == null) return;
        if (player == null) {
            interactionContext.getState().state = InteractionState.Failed;
            MLConstants.LOGGER.at(Level.INFO).log("[ML_Echoes] Player is null");
            return;
        }

        // Check for valid item
        ItemStack itemStack = interactionContext.getHeldItem();
        if (itemStack == null) {
            interactionContext.getState().state = InteractionState.Failed;
            MLConstants.LOGGER.at(Level.INFO).log("[ML_Echoes] ItemStack is null");
            return;
        }

        // Ensure data from player component
        ComponentType<EntityStore, EchoStoreComponent> echoDataComponent = _ComponentRegistration.getEchoData();

        if (echoDataComponent == null) return;

        int color = 7051198;
        EchoStoreComponent data = store.getComponent(ref, echoDataComponent);
        if (data != null) color = data.getColor();

        // Open the custom page
        EchoWandPage echoPage = new EchoWandPage(playerRef);
        echoPage.updateColor(String.format("#%06X", (0xFFFFFF & color)));
        player.getPageManager().openCustomPage(ref, store, echoPage);
    }

}
