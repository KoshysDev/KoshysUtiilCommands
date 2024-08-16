package com.koshys.util.commands.Commands;

import com.koshys.util.commands.Commands.Utils.KoshysChatUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

import static net.minecraft.command.argument.EntityArgumentType.entity;
import static net.minecraft.command.argument.EntityArgumentType.getEntity;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SexCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("sex")
                        .then(argument("target", entity())
                                .executes(SexCommand::execute)
                        )
                        .executes(SexCommand::executeWithoutTarget) // Add this executes
        );
    }

    private static int executeWithoutTarget(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerWorld world = source.getWorld();
        PlayerEntity player = source.getPlayerOrThrow();

        Vec3d playerPos = player.getPos();

        KoshysChatUtils.sendMessageToNearbyPlayers(world, playerPos,
                player.getName().getString() + " трахнув сам себе.", 20, false);

        return 1;
    }

    public static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerWorld world = source.getWorld();
        PlayerEntity player = source.getPlayerOrThrow(); // Get the player who executed the command
        Entity targetEntity;

        if(getEntity(context, "target") == null){
            targetEntity = player;
        }else {
            targetEntity = getEntity(context, "target");
        }

        // Get the target's name
        String targetName = targetEntity instanceof PlayerEntity
                ? targetEntity.getName().getString()// Use player's name for players
                : targetEntity.getType().getTranslationKey(); // Use entity's translation key for others

        Vec3d playerPos = player.getPos();

        if(Objects.equals(targetName, player.getName().getString())){
            KoshysChatUtils.sendMessageToNearbyPlayers(world, playerPos,
                    player.getName().getString() + " трахнув сам себе.", 20, false);
        } else {
            KoshysChatUtils.sendMessageToNearbyPlayers(world, playerPos,
                    player.getName().getString() + " трахнув " + targetName + ".", 20, false);
        }

        return 1;
    }
}