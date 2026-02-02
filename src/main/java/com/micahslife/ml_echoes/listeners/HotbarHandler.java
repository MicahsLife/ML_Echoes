package com.micahslife.ml_echoes.listeners;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.protocol.packets.interaction.SyncInteractionChain;
import com.hypixel.hytale.protocol.packets.interaction.SyncInteractionChains;
import com.hypixel.hytale.server.core.io.adapter.PlayerPacketFilter;
import com.hypixel.hytale.server.core.modules.entity.component.DynamicLight;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class HotbarHandler implements PlayerPacketFilter {

    @Override
    public boolean test(PlayerRef playerRef, Packet packet) {
        if (!(packet instanceof SyncInteractionChains syncPacket)
                || playerRef.getReference() == null) {
            return false;
        }

        for (SyncInteractionChain chain : syncPacket.updates) {
            if (chain.interactionType == InteractionType.SwapFrom
                    && chain.data != null
                    && chain.initial
                    && (chain.itemInHandId != null && chain.itemInHandId.equals("ML_Echoes_Wand"))) {
                // Currently on wand and swapping away
                handleLightRemoval(playerRef);

                return false; // Prevent desync of slots since we don't care about that logic
            }
        }

        return false;
    }

    private void handleLightRemoval(PlayerRef playerRef) {
        Ref<EntityStore> ref = playerRef.getReference();
        if (ref == null || !ref.isValid()) {
            return;
        }

        Store<EntityStore> store = ref.getStore();
        World world = store.getExternalData().getWorld();

        // Execute on world thread
        world.execute(() -> {
            // Clear the color light if there is one
            ComponentType<EntityStore, DynamicLight> lightComponent = DynamicLight.getComponentType();
            store.removeComponentIfExists(ref, lightComponent);
        });
    }

}
