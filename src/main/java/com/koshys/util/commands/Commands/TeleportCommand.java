package com.koshys.util.commands.Commands;

import com.koshys.util.commands.KoshysUtiilCommands;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;
import java.util.function.Supplier;

public class TeleportCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("gmtp")
                .requires(Permissions.require(KoshysUtiilCommands.MODID+".gmtp"))
                .then(CommandManager.argument("targetPlayer", EntityArgumentType.player())
                        .executes(TeleportCommand::teleportToPlayer))
                .then(CommandManager.argument("targetPlayer", EntityArgumentType.player())
                        .then(CommandManager.argument("destinationPlayer", EntityArgumentType.player())
                                .executes(TeleportCommand::teleportPlayerToPlayer)))
                .then(CommandManager.argument("x", DoubleArgumentType.doubleArg())
                        .then(CommandManager.argument("y", DoubleArgumentType.doubleArg())
                                .then(CommandManager.argument("z", DoubleArgumentType.doubleArg())
                                        .executes(TeleportCommand::teleportToCoordinates)))));
    }

    private static int teleportToPlayer(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity sourcePlayer = context.getSource().getPlayer();
        ServerPlayerEntity targetPlayer = EntityArgumentType.getPlayer(context, "targetPlayer");

        assert sourcePlayer != null;
        sourcePlayer.teleport((ServerWorld) targetPlayer.getWorld(), targetPlayer.getX(), targetPlayer.getY(), targetPlayer.getZ(), targetPlayer.getYaw(), targetPlayer.getPitch());
        source.sendFeedback((Supplier<Text>) () -> Text.literal(sourcePlayer.getName().getString() + " телепортовано до " + targetPlayer.getName().getString()), true);

        return 1;
    }

    private static int teleportPlayerToPlayer(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity targetPlayer = EntityArgumentType.getPlayer(context, "targetPlayer");
        ServerPlayerEntity destinationPlayer = EntityArgumentType.getPlayer(context, "destinationPlayer");

        targetPlayer.teleport((ServerWorld) destinationPlayer.getWorld(), destinationPlayer.getX(), destinationPlayer.getY(), destinationPlayer.getZ(), destinationPlayer.getYaw(), destinationPlayer.getPitch());
        source.sendFeedback((Supplier<Text>) () -> Text.literal(Objects.requireNonNull(source.getPlayer()).getName().getString() + " телепортовано до " + targetPlayer.getName().getString()), true);

        return 1;
    }

    private static int teleportToCoordinates(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity sourcePlayer = context.getSource().getPlayer();
        double x = DoubleArgumentType.getDouble(context, "x");
        double y = DoubleArgumentType.getDouble(context, "y");
        double z = DoubleArgumentType.getDouble(context, "z");

        assert sourcePlayer != null;
        sourcePlayer.teleport((ServerWorld) sourcePlayer.getWorld(), x, y, z, sourcePlayer.getYaw(), sourcePlayer.getPitch());
        sourcePlayer.sendMessage(Text.literal("Teleported to " + x + ", " + y + ", " + z), false);
        source.sendFeedback((Supplier<Text>) () -> Text.literal(sourcePlayer.getName().getString() + " телепортовано на " + x + ", " + y + ", " + z), true);

        return 1;
    }
}