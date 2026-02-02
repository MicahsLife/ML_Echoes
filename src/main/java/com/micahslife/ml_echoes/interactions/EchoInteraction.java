package com.micahslife.ml_echoes.interactions;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.util.MathUtil;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.*;
import com.hypixel.hytale.protocol.Color;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.component.DynamicLight;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.ParticleUtil;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.accessor.LocalCachedChunkAccessor;
import com.hypixel.hytale.server.core.universe.world.chunk.BlockChunk;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.micahslife.ml_echoes.components.EchoStoreComponent;
import com.micahslife.ml_echoes.components._ComponentRegistration;
import com.micahslife.ml_echoes.data.MLConstants;
import it.unimi.dsi.fastutil.longs.Long2IntMap;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.logging.Level;

/**
 * The primary interaction for the echo wand item
 */
public class EchoInteraction extends SimpleInstantInteraction {

    public static final BuilderCodec<EchoInteraction> CODEC = BuilderCodec.builder(
            EchoInteraction.class, EchoInteraction::new, SimpleInstantInteraction.CODEC
    ).build();

    @Override
    protected void firstRun(@Nonnull InteractionType interactionType, @Nonnull InteractionContext interactionContext, @Nonnull CooldownHandler cooldownHandler) {
        CommandBuffer<EntityStore> commandBuffer = interactionContext.getCommandBuffer();

        if (commandBuffer == null) {
            interactionContext.getState().state = InteractionState.Failed;
            MLConstants.LOGGER.at(Level.INFO).log("[ML_Echoes] CommandBuffer is null");
            return;
        }

        World world = commandBuffer.getExternalData().getWorld();
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

        // Tint the current area around the player
        execute(world, playerRef.getTransform().getPosition().toVector3i(), color);

        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        // Set a colored light, but make it mostly dark and less saturated
        ComponentType<EntityStore, DynamicLight> lightComponent = DynamicLight.getComponentType();
        DynamicLight light = commandBuffer.getComponent(ref, lightComponent);
        float[] hsbVals = java.awt.Color.RGBtoHSB(r, g, b, null);
        int colorInt = java.awt.Color.HSBtoRGB(hsbVals[0], Math.min(hsbVals[1], 0.1f), 0.05f);
        int r2 = (colorInt >> 16) & 0xFF;
        int g2 = (colorInt >> 8) & 0xFF;
        int b2 = colorInt & 0xFF;
        ColorLight colorLight = new ColorLight((byte) 1, (byte) r2, (byte) g2, (byte) b2);

        if (light == null) {
            DynamicLight newLight = new DynamicLight(colorLight);
            commandBuffer.putComponent(ref, lightComponent, newLight);
        } else {
            light.setColorLight(colorLight);
        }

        // Spawn a particle effect
        ParticleUtil.spawnParticleEffect(
                "EchoWind",
                playerRef.getTransform().getPosition().add(0, 0.2, 0),
                playerRef.getHeadRotation().getYaw(), 0, 0,
                1.0f,
                new Color((byte) r, (byte) g, (byte) b),
                List.of(ref),
                commandBuffer
        );
    }

    protected void execute(World world, Vector3i playerPos, int color) {
        int radius = 10;
        double sigma = 5.0;

        int minX = playerPos.getX() - 10;
        int minZ = playerPos.getZ() - 10;
        int maxX = playerPos.getX() + 10;
        int maxZ = playerPos.getZ() + 10;

        for(int cx = ChunkUtil.chunkCoordinate(minX); cx <= ChunkUtil.chunkCoordinate(maxX); ++cx) {
            for(int cz = ChunkUtil.chunkCoordinate(minZ); cz <= ChunkUtil.chunkCoordinate(maxZ); ++cz) {
                int startX = Math.max(0, minX - ChunkUtil.minBlock(cx));
                int startZ = Math.max(0, minZ - ChunkUtil.minBlock(cz));
                int endX = Math.min(32, maxX - ChunkUtil.minBlock(cx));
                int endZ = Math.min(32, maxZ - ChunkUtil.minBlock(cz));
                WorldChunk chunk = world.getNonTickingChunk(ChunkUtil.indexChunk(cx, cz));

                if (chunk != null) {
                    for(int z = startZ; z < endZ; ++z) {
                        for(int x = startX; x < endX; ++x) {
                            if (chunk.getBlockChunk() != null) chunk.getBlockChunk().setTint(x, z, color);
                        }
                    }

                    world.getNotificationHandler().updateChunk(chunk.getIndex());
                }
            }
        }

        double[] matrix = gaussianMatrix(sigma, radius);
        ChunkStore chunkStore = world.getChunkStore();
        Store<ChunkStore> chunkStoreStore = chunkStore.getStore();
        LongOpenHashSet updateChunks = new LongOpenHashSet();
        int chunkX = MathUtil.floor(playerPos.getX()) >> 5;
        int chunkZ = MathUtil.floor(playerPos.getZ()) >> 5;
        int blockX = chunkX << 5;
        int blockZ = chunkZ << 5;
        Long2IntOpenHashMap newTintMap = new Long2IntOpenHashMap();
        LocalCachedChunkAccessor accessor = LocalCachedChunkAccessor.atWorldCoords(world, blockX, blockZ, 32 + radius * 2);

        for(int x = -radius; x <= 32 + radius; ++x) {
            for(int z = -radius; z <= 32 + radius; ++z) {
                int offsetX = blockX + x;
                int offsetZ = blockZ + z;
                int blurred = blur(accessor, radius, matrix, offsetX, offsetZ);
                newTintMap.put(MathUtil.packLong(offsetX, offsetZ), blurred);
            }
        }

        for (Long2IntMap.Entry entry : newTintMap.long2IntEntrySet()) {
            long key = entry.getLongKey();
            int x = MathUtil.unpackLeft(key);
            int z = MathUtil.unpackRight(key);
            long chunkIndex = ChunkUtil.indexChunkFromBlock(x, z);
            Ref<ChunkStore> chunkRef = chunkStore.getChunkReference(chunkIndex);
            if (chunkRef != null && chunkRef.isValid()) {
                BlockChunk blockChunk = chunkStoreStore.getComponent(chunkRef, BlockChunk.getComponentType());
                if (blockChunk != null) {
                    blockChunk.setTint(x, z, entry.getIntValue());
                    updateChunks.add(chunkIndex);
                }
            }
        }

        updateChunks.forEach((chunkI) -> world.getNotificationHandler().updateChunk(chunkI));
    }

    private static int blur(@Nonnull LocalCachedChunkAccessor chunkAccessor, int radius, double[] matrix, int x, int z) {
        double r = 0.0;
        double g = 0.0;
        double b = 0.0;

        for(int ix = -radius; ix <= radius; ++ix) {
            for(int iz = -radius; iz <= radius; ++iz) {
                double factor = matrix[gaussianIndex(radius, ix, iz)];
                int ax = x + ix;
                int az = z + iz;
                WorldChunk worldChunk = chunkAccessor.getChunk(ChunkUtil.indexChunkFromBlock(ax, az));
                if (worldChunk != null) {
                    BlockChunk blockChunk = worldChunk.getBlockChunk();
                    if (blockChunk != null) {
                        int c = blockChunk.getTint(ax, az);
                        r += (double)(c >> 16 & 255) * factor;
                        g += (double)(c >> 8 & 255) * factor;
                        b += (double)(c & 255) * factor;
                    }
                }
            }
        }

        return -16777216 | MathUtil.floor(r) << 16 | MathUtil.floor(g) << 8 | MathUtil.floor(b);
    }

    private static double[] gaussianMatrix(double sigma, int radius) {
        int length = 2 * radius + 1;
        double[] matrix = new double[length * length];

        for(int x = -radius; x <= radius; ++x) {
            for(int y = -radius; y <= radius; ++y) {
                double value = gaussian2d(sigma, x, y);
                matrix[gaussianIndex(radius, x, y)] = value;
            }
        }

        double sum = 0.0;

        for(double val : matrix) {
            sum += val;
        }

        for(int i = 0; i < matrix.length; ++i) {
            matrix[i] /= sum;
        }

        return matrix;
    }

    private static double gaussian2d(double sigma, double x, double y) {
        return (double)1.0F / ((Math.PI * 2D) * sigma * sigma) * Math.pow(Math.E, -(x * x + y * y) / ((double)2.0F * sigma * sigma));
    }

    private static int gaussianIndex(int radius, int x, int y) {
        x += radius;
        y += radius;
        return x * (2 * radius + 1) + y;
    }

}
