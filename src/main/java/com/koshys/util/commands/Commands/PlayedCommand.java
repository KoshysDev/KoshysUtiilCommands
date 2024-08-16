package com.koshys.util.commands.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;

import java.util.function.Supplier;

import static net.minecraft.server.command.CommandManager.literal;

public class PlayedCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("played").executes(PlayedCommand::execute));
    }

    public static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = (ServerPlayerEntity) source.getEntity();

        if (player == null) {
            source.sendFeedback((Supplier<Text>) () -> Text.literal("Ви не гравець!"), false);
            return 1;
        }

        long playTimeTicks = player.getStatHandler().getStat(Stats.CUSTOM, Stats.PLAY_TIME);
        double playTimeHours = (double) playTimeTicks / 72000.0;

        source.sendFeedback((Supplier<Text>) () -> Text.literal("Ти не мацав травичку приблизно " + String.format("%.2f", playTimeHours) + " годин."), false);
        return 1;
    }
}