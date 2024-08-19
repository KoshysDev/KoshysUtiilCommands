package com.koshys.util.commands.Utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.awt.desktop.UserSessionEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EffectManager {

    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public static void spawnItemAsEffect(ServerWorld world, PlayerEntity player, Vec3d position, ItemStack itemStack, int durationSeconds, String name, double speed, boolean isNameVisible, boolean noGravity) {
        // Get the direction the player is looking and set the item entity's velocity
        Vec3d lookVec = player.getRotationVec(1.0F);
        Vec3d offset = lookVec.multiply(0.25);

        ItemEntity itemEntity = new ItemEntity(world, position.x + offset.x, position.y + offset.y, position.z + offset.z, itemStack);

        itemEntity.setCustomName(Text.literal(name));

        itemEntity.setVelocity(lookVec.multiply(speed));

        itemEntity.setNoGravity(noGravity);
        itemEntity.setPickupDelayInfinite();
        itemEntity.cannotPickup();
        itemEntity.setCustomNameVisible(isNameVisible);
        // Spawn the item entity in the world
        world.spawnEntity(itemEntity);

        // Schedule the item entity to despawn after the specified duration
        scheduler.schedule(() -> {
            world.getServer().execute(() -> { // Run removal on the server thread
                if (!itemEntity.isRemoved()) {
                    itemEntity.remove(Entity.RemovalReason.DISCARDED);
                }
            });
        }, durationSeconds, TimeUnit.SECONDS);
    }

    public static void spawnParticleEffect(ServerWorld world, PlayerEntity player, Vec3d position,
                                           ParticleEffect particleType, int count, double spread, int durationTicks) {
        // Get the direction the player is looking
        Vec3d lookVec = player.getRotationVec(1.0F);

        // Create a scheduled task to spawn particles for a specified duration
        scheduler.schedule(() -> {
            for (int i = 0; i < count; i++) {
                // Generate random offset within the spread radius
                double xOffset = (Math.random() - 0.5) * spread;
                double yOffset = (Math.random() - 0.5) * spread;
                double zOffset = (Math.random() - 0.5) * spread;

                // Spawn the particle with the offset
                world.spawnParticles(particleType,
                        position.x + xOffset,
                        position.y + yOffset,
                        position.z + zOffset,
                        1,
                        lookVec.x,
                        lookVec.y,
                        lookVec.z,
                        0.0);
            }
        }, durationTicks, TimeUnit.MILLISECONDS); // Schedule for durationTicks
    }
}