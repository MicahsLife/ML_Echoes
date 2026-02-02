package com.micahslife.ml_echoes.systems;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The player tick system
 */
public class PlayerTickSystem extends EntityTickingSystem<EntityStore> {

    @Override
    public void tick(float v,
                     int i,
                     @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
                     @Nonnull Store<EntityStore> store,
                     @Nonnull CommandBuffer<EntityStore> commandBuffer) {

        //World world = ((EntityStore)store.getExternalData()).getWorld();
        Player player = archetypeChunk.getComponent(i, Player.getComponentType());

        if (player == null || player.getReference() == null) return;


    }

    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return Archetype.empty();
    }

}
