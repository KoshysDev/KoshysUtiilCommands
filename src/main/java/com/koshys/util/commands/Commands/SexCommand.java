package com.koshys.util.commands.Commands;

import com.koshys.util.commands.Utils.EffectManager;
import com.koshys.util.commands.Utils.KoshysChatUtils;
import com.koshys.util.commands.Utils.TranslationHelperUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

import static net.minecraft.command.argument.EntityArgumentType.player;
import static net.minecraft.command.argument.EntityArgumentType.getEntity;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SexCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("sex")
                        .then(argument("target", player())
                               .executes(SexCommand::execute))
                        .executes(SexCommand::executeWithoutTarget)
        );
    }

    private static int executeWithoutTarget(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerWorld world = source.getWorld();
        PlayerEntity player = source.getPlayerOrThrow();
        Vec3d playerPos = player.getPos();

        BlockState ironBlockState = Blocks.IRON_BLOCK.getDefaultState();
        EffectManager.spawnParticleEffect(world, player, new Vec3d(playerPos.x, playerPos.y + 0.8, playerPos.z),
                new BlockStateParticleEffect(ParticleTypes.BLOCK, ironBlockState), 18, 1, 1);

        ItemStack ghastTear = new ItemStack(Items.GHAST_TEAR);
        EffectManager.spawnItemAsEffect(world, player, new Vec3d(playerPos.x, playerPos.y + 0.5, playerPos.z),
                ghastTear, 5, "КУМ " + player.getName().getString(), 0.2, true, false);

        KoshysChatUtils.sendMessageToNearbyPlayers(world, playerPos,
                player.getName().getString() + " трахнув сам себе.", 20, false);

        return 1;
    }

    public static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerWorld world = source.getWorld();
        PlayerEntity player = source.getPlayerOrThrow();
        Entity targetEntity;
        Vec3d playerPos = player.getPos();
        int ppSize = 5;

        if (getEntity(context, "target") == null) {
            targetEntity = player;
        } else {
            targetEntity = getEntity(context, "target");

            if (player.distanceTo(targetEntity) >= ppSize) {
                player.sendMessage(Text.of("Таких довгих пісюнів не буває!"), false);
                return 1;
            }

            // Get the target's name
            String targetName = targetEntity instanceof PlayerEntity
                    ? targetEntity.getName().getString()// Use player's name for players
                    : TranslationHelperUtils.getTranslationForMob(targetEntity.getType().getName().getString()); // Use entity's translation key for others

            if (Objects.equals(targetName, player.getName().getString())) {
                KoshysChatUtils.sendMessageToNearbyPlayers(world, playerPos,
                        player.getName().getString() + " трахнув сам себе.", 20, false);
            } else {
                KoshysChatUtils.sendMessageToNearbyPlayers(world, playerPos,
                        player.getName().getString() + " трахнув " + targetName + ".", 20, false);
            }

            BlockState ironBlockState = Blocks.IRON_BLOCK.getDefaultState();
            EffectManager.spawnParticleEffect(world, player, new Vec3d(playerPos.x, playerPos.y + 0.8, playerPos.z),
                    new BlockStateParticleEffect(ParticleTypes.BLOCK, ironBlockState), 18, 1, 1);
            return 1;
        }
        return 1;
    }
}