package com.koshys.util.commands.Utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;

import java.util.HashMap;
import java.util.Map;

public class EntityUtils {

    private static final Map<EntityType<?>, SoundEvent> MOB_SOUNDS = new HashMap<>();

    static {
        MOB_SOUNDS.put(EntityType.PIG, SoundEvents.ENTITY_PIG_HURT);
        MOB_SOUNDS.put(EntityType.COW, SoundEvents.ENTITY_COW_HURT);
        MOB_SOUNDS.put(EntityType.CHICKEN, SoundEvents.ENTITY_CHICKEN_HURT);
        MOB_SOUNDS.put(EntityType.SHEEP, SoundEvents.ENTITY_SHEEP_HURT);
        MOB_SOUNDS.put(EntityType.WOLF, SoundEvents.ENTITY_WOLF_HURT);
        MOB_SOUNDS.put(EntityType.ZOMBIE, SoundEvents.ENTITY_ZOMBIE_HURT);
        MOB_SOUNDS.put(EntityType.SKELETON, SoundEvents.ENTITY_SKELETON_HURT);
        MOB_SOUNDS.put(EntityType.CREEPER, SoundEvents.ENTITY_CREEPER_HURT);
        MOB_SOUNDS.put(EntityType.SPIDER, SoundEvents.ENTITY_SPIDER_HURT);
        MOB_SOUNDS.put(EntityType.ENDERMAN, SoundEvents.ENTITY_ENDERMAN_HURT);
        MOB_SOUNDS.put(EntityType.SLIME, SoundEvents.ENTITY_SLIME_HURT);
        MOB_SOUNDS.put(EntityType.GHAST, SoundEvents.ENTITY_GHAST_HURT);
        MOB_SOUNDS.put(EntityType.BLAZE, SoundEvents.ENTITY_BLAZE_HURT);
        MOB_SOUNDS.put(EntityType.MAGMA_CUBE, SoundEvents.ENTITY_MAGMA_CUBE_HURT);
        MOB_SOUNDS.put(EntityType.WITCH, SoundEvents.ENTITY_WITCH_HURT);
        MOB_SOUNDS.put(EntityType.IRON_GOLEM, SoundEvents.ENTITY_IRON_GOLEM_HURT);
        MOB_SOUNDS.put(EntityType.SNOW_GOLEM, SoundEvents.ENTITY_SNOW_GOLEM_HURT);
        MOB_SOUNDS.put(EntityType.VILLAGER, SoundEvents.ENTITY_VILLAGER_HURT);
        MOB_SOUNDS.put(EntityType.GUARDIAN, SoundEvents.ENTITY_GUARDIAN_HURT);
        MOB_SOUNDS.put(EntityType.ELDER_GUARDIAN, SoundEvents.ENTITY_ELDER_GUARDIAN_HURT);
        MOB_SOUNDS.put(EntityType.SQUID, SoundEvents.ENTITY_SQUID_HURT);
        MOB_SOUNDS.put(EntityType.BAT, SoundEvents.ENTITY_BAT_AMBIENT);
        MOB_SOUNDS.put(EntityType.ENDERMITE, SoundEvents.ENTITY_ENDERMITE_HURT);
        MOB_SOUNDS.put(EntityType.RABBIT, SoundEvents.ENTITY_RABBIT_HURT);
        MOB_SOUNDS.put(EntityType.POLAR_BEAR, SoundEvents.ENTITY_POLAR_BEAR_HURT);
        MOB_SOUNDS.put(EntityType.HORSE, SoundEvents.ENTITY_HORSE_HURT);
        MOB_SOUNDS.put(EntityType.DONKEY, SoundEvents.ENTITY_DONKEY_HURT);
        MOB_SOUNDS.put(EntityType.MULE, SoundEvents.ENTITY_MULE_HURT);
        MOB_SOUNDS.put(EntityType.LLAMA, SoundEvents.ENTITY_LLAMA_HURT);
        MOB_SOUNDS.put(EntityType.OCELOT, SoundEvents.ENTITY_OCELOT_HURT);
        MOB_SOUNDS.put(EntityType.CAT, SoundEvents.ENTITY_CAT_HURT);
        MOB_SOUNDS.put(EntityType.PARROT, SoundEvents.ENTITY_PARROT_HURT);
        MOB_SOUNDS.put(EntityType.PANDA, SoundEvents.ENTITY_PANDA_HURT);
        MOB_SOUNDS.put(EntityType.PUFFERFISH, SoundEvents.ENTITY_PUFFER_FISH_HURT);
        MOB_SOUNDS.put(EntityType.SALMON, SoundEvents.ENTITY_SALMON_HURT);
        MOB_SOUNDS.put(EntityType.COD, SoundEvents.ENTITY_COD_HURT);
        MOB_SOUNDS.put(EntityType.TROPICAL_FISH, SoundEvents.ENTITY_TROPICAL_FISH_HURT);
        MOB_SOUNDS.put(EntityType.DROWNED, SoundEvents.ENTITY_DROWNED_HURT);
        MOB_SOUNDS.put(EntityType.TURTLE, SoundEvents.ENTITY_TURTLE_HURT);
        MOB_SOUNDS.put(EntityType.PHANTOM, SoundEvents.ENTITY_PHANTOM_HURT);
        MOB_SOUNDS.put(EntityType.STRAY, SoundEvents.ENTITY_STRAY_HURT);
        MOB_SOUNDS.put(EntityType.HUSK, SoundEvents.ENTITY_HUSK_HURT);
        MOB_SOUNDS.put(EntityType.WITHER_SKELETON, SoundEvents.ENTITY_WITHER_SKELETON_HURT);
        MOB_SOUNDS.put(EntityType.RAVAGER, SoundEvents.ENTITY_RAVAGER_HURT);
        MOB_SOUNDS.put(EntityType.PILLAGER, SoundEvents.ENTITY_PILLAGER_HURT);
        MOB_SOUNDS.put(EntityType.VEX, SoundEvents.ENTITY_VEX_HURT);
        MOB_SOUNDS.put(EntityType.VINDICATOR, SoundEvents.ENTITY_VINDICATOR_HURT);
        MOB_SOUNDS.put(EntityType.EVOKER, SoundEvents.ENTITY_EVOKER_HURT);
        MOB_SOUNDS.put(EntityType.SHULKER, SoundEvents.ENTITY_SHULKER_HURT);
        MOB_SOUNDS.put(EntityType.SILVERFISH, SoundEvents.ENTITY_SILVERFISH_HURT);
        MOB_SOUNDS.put(EntityType.CAVE_SPIDER, SoundEvents.ENTITY_SPIDER_HURT);
        MOB_SOUNDS.put(EntityType.WITHER, SoundEvents.ENTITY_WITHER_HURT);
        MOB_SOUNDS.put(EntityType.ARMADILLO, SoundEvents.ENTITY_ARMADILLO_HURT);
    }

    public static SoundEvent getMobSound(EntityType<?> entityType) {
        return MOB_SOUNDS.getOrDefault(entityType, null);
    }

    public static <T extends Entity> T getNearestEntity(PlayerEntity player, EntityType<T> entityType, double radius) {
        ServerWorld world = (ServerWorld) player.getWorld();
        Box searchArea = player.getBoundingBox().expand(radius, radius, radius);

        T nearestEntity = null;
        double nearestDistanceSq = Double.MAX_VALUE;

        for (Entity entity : world.getEntitiesByType(entityType, searchArea, Entity::isAlive)) {
            double distanceSq = player.squaredDistanceTo(entity);

            if (distanceSq < nearestDistanceSq) {
                nearestEntity = (T) entity;
                nearestDistanceSq = distanceSq;
            }
        }

        return nearestEntity;
    }

    public static EntityType<?> getEntityTypeByName(String entityName) {
        Identifier entityId = Identifier.tryParse(entityName);
        if (entityId == null) {
            return null; // Invalid entity name
        }

        return Registries.ENTITY_TYPE.get(entityId);
    }
}