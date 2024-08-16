package com.koshys.util.commands.Commands.Utils;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EffectManager {

    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public static void spawnGhostTear(ServerWorld world, Vec3d position, ItemStack itemStack, int durationSeconds) {
        // Create a new item entity with the given item stack and position
        ItemEntity itemEntity = new ItemEntity(world, position.x, position.y, position.z, itemStack);

        // Set the item entity's name to "кум"
        itemEntity.setCustomName(Text.literal("кум"));

        // Set the item entity to be uncollidable (cannot be picked up)
        itemEntity.setNoGravity(true); // Optionally disable gravity
        itemEntity.setPickupDelayInfinite();

        // Spawn the item entity in the world
        world.spawnEntity(itemEntity);

        // Schedule the item entity to despawn after the specified duration
        scheduler.schedule(() -> {
            if (!itemEntity.removed) {
                itemEntity.remove(); // Remove the item entity from the world
            }
        }, durationSeconds, TimeUnit.SECONDS);
    }

    public static void despawnAllGhostTears(ServerWorld world) {
        // Get all entities in the world
        for (Entity entity : world.getEntities()) {
            // Check if the entity is an ItemEntity and its custom name is "кум"
            if (entity instanceof ItemEntity itemEntity && itemEntity.hasCustomName() && itemEntity.getCustomName().asString().equals("кум")) {
                // Remove the item entity from the world
                itemEntity.remove();
            }
        }
    }

}