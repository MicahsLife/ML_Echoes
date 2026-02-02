package com.micahslife.ml_echoes.systems;

import com.hypixel.hytale.component.Archetype;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.asset.type.item.config.CraftingRecipe;
import com.hypixel.hytale.server.core.event.events.ecs.CraftRecipeEvent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A player recipe system
 */
public class PlayerRecipeSystem extends EntityEventSystem<EntityStore, CraftRecipeEvent.Pre> {

    public PlayerRecipeSystem() {
        super(CraftRecipeEvent.Pre.class);
    }

    @Override
    public void handle(int index,
                       @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
                       @Nonnull Store<EntityStore> store,
                       @Nonnull CommandBuffer<EntityStore> commandBuffer,
                       @Nonnull CraftRecipeEvent.Pre craftRecipeEvent) {
        CraftingRecipe recipe = craftRecipeEvent.getCraftedRecipe();

        if (recipe.getPrimaryOutput().getItemId() != null
                && recipe.getPrimaryOutput().getItemId().equals("ML_Echoes_Wand")) {
            craftRecipeEvent.setCancelled(true);
        }
    }

    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return Archetype.empty();
    }

}
