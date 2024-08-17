package com.koshys.util.commands.Commands;

import com.google.common.collect.Maps;
import com.koshys.util.commands.KoshysUtiilCommands;
import com.koshys.util.commands.Utils.EffectManager;
import com.koshys.util.commands.Utils.KoshysChatUtils;
import com.koshys.util.commands.Utils.TranslationHelperUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

import static com.koshys.util.commands.Utils.EntityUtils.getEntityTypeByName;
import static com.koshys.util.commands.Utils.EntityUtils.getNearestEntity;
import static com.koshys.util.commands.Utils.EntityUtils.getMobSound;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class MobSexCommand {
    private static final int COOLDOWN_SECONDS = 15; // Cooldown in seconds
    private static final Map<UUID, Long> cooldowns = Maps.newHashMap(); // Store cooldowns for players

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("mobsex")
                        .then(argument("entityName", StringArgumentType.word()) // String argument
                                .executes(MobSexCommand::execute))
                        .executes(MobSexCommand::executeWithoutTarget)
        );
    }

    private static int executeWithoutTarget(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        PlayerEntity player = source.getPlayerOrThrow();

        player.sendMessage(Text.of("§6Використання: /mobsex <назвава моба на англійській мові, приклад ender_dragon>"), false);

        return 1;
    }

    private static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerWorld world = source.getWorld();
        PlayerEntity player = source.getPlayerOrThrow();
        Vec3d playerPos = player.getPos();
        int ppSize = 5;

        // Check cooldown
        long currentTime = System.currentTimeMillis();
        if (cooldowns.containsKey(player.getUuid()) && !Permissions.check(source, KoshysUtiilCommands.MODID+".unlimited.sex")) {
            long lastUse = cooldowns.get(player.getUuid());
            long timeSinceLastUse = currentTime - lastUse;
            if (timeSinceLastUse < COOLDOWN_SECONDS * 1000) {
                long remainingSeconds = (COOLDOWN_SECONDS * 1000 - timeSinceLastUse) / 1000;
                source.sendFeedback((Supplier<Text>) () -> Text.literal("Зачекай ще " + remainingSeconds + " секунд перед наступним використанням команди."), true);
                return 1;
            }
        }

        // Set cooldown for the player
        cooldowns.put(player.getUuid(), currentTime);

        String entityName = StringArgumentType.getString(context, "entityName");
        EntityType<?> entityType = getEntityTypeByName(entityName);

        if (entityType == null) {
            player.sendMessage(Text.of("Такого моба не існує: " + entityName), false);
            return 0; // Failure
        }

        Entity targetEntity = getNearestEntity(player, entityType, 10);

        if (targetEntity == null) {
            player.sendMessage(Text.of("Не знайдено '" + entityName + "' поблизу."), false);
            return 0; // Failure
        }

        if (player.distanceTo(targetEntity) >= ppSize) {
            player.sendMessage(Text.of("Таких довгих пісюнів не буває!"), false);
            return 1;
        }

        // Get the target's name
        String targetName = targetEntity instanceof PlayerEntity
                ? targetEntity.getName().getString() // Use player's name for players
                : TranslationHelperUtils.getTranslationForMob(targetEntity.getType().getName().getString()); // Use entity's translation key for others

        if (Objects.equals(targetName, player.getName().getString())) {
            KoshysChatUtils.sendMessageToNearbyPlayers(world, playerPos,
                    player.getName().getString() + " трахнув сам себе.", 20, false);
        } else {
            KoshysChatUtils.sendMessageToNearbyPlayers(world, playerPos,
                    player.getName().getString() + " трахнув " + targetName + ".", 20, false);
        }

        targetEntity.playSound(getMobSound(entityType), 5, 1);

        BlockState ironBlockState = Blocks.IRON_BLOCK.getDefaultState();
        EffectManager.spawnParticleEffect(world, player, new Vec3d(playerPos.x, playerPos.y + 0.8, playerPos.z),
                new BlockStateParticleEffect(ParticleTypes.BLOCK, ironBlockState), 18, 1, 1);

        ItemStack ghastTear = new ItemStack(Items.GHAST_TEAR);
        EffectManager.spawnItemAsEffect(world, player, new Vec3d(playerPos.x, playerPos.y + 0.5, playerPos.z),
                ghastTear, 5, "КУМ " + player.getName().getString(), 0.2, true, false);

        return 1; // Success
    }
}