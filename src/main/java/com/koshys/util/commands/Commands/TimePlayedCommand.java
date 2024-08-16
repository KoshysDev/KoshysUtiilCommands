package com.koshys.util.commands.Commands;

import com.google.common.base.Supplier;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;

import java.util.UUID;

import static net.minecraft.command.argument.EntityArgumentType.entity;
import static net.minecraft.command.argument.EntityArgumentType.getEntity;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class TimePlayedCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("timeplayed").then(
                        argument("target", entity())
                                .executes(TimePlayedCommand::execute)
                )
        );
    }

    public static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerWorld world = source.getWorld();

        if (!(getEntity(context, "target") instanceof PlayerEntity targetEntity)) return 0;

        UUID targetUUID = targetEntity.getUuid();
        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) world.getEntity(targetUUID);

        assert serverPlayerEntity != null;
        long playTimeTicks = (long) serverPlayerEntity.getStatHandler().getStat(Stats.CUSTOM, Stats.PLAY_TIME);

        double playTimeHours = (double) playTimeTicks / 72000.0;

        if (playTimeHours <= 0.0) {
            source.sendFeedback((Supplier<Text>) () -> Text.literal("Гравця за ніком не знайдено, або він не бував на сервері."), false);
            return 1;
        } else {
            source.sendFeedback((Supplier<Text>) () -> Text.literal(targetEntity.getNameForScoreboard() + " не мацав травичку приблизно " + String.format("%.2f", playTimeHours) + " годин."), false);
            return 1;
        }
    }
}