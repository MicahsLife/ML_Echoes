package com.micahslife.ml_echoes.systems;

import com.hypixel.hytale.component.Archetype;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.event.events.ecs.DamageBlockEvent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.micahslife.ml_echoes.data.MLConstants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.logging.Level;

/**
 * An example for a basic entity event system
 * Prevents grass blocks from being broken while in adventure mode
 */
public class ECSExampleSystem extends EntityEventSystem<EntityStore, DamageBlockEvent> {

    public ECSExampleSystem() {
        super(DamageBlockEvent.class);
    }

    @Override
    public void handle(int index,
                       @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
                       @Nonnull Store<EntityStore> store,
                       @Nonnull CommandBuffer<EntityStore> commandBuffer,
                       @Nonnull DamageBlockEvent damageBlockEvent) {

        if (damageBlockEvent.getBlockType().equals(BlockType.fromString("Soil_Grass"))) {
            damageBlockEvent.setCancelled(true);
            MLConstants.LOGGER.at(Level.INFO).log("[ML_Echoes] Grass block break detected and prevented");
        }
    }

    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return Archetype.empty();
    }

}
