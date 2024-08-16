package com.koshys.util.commands.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Random;
import java.util.function.Supplier;

import static net.minecraft.command.argument.EntityArgumentType.entity;
import static net.minecraft.command.argument.EntityArgumentType.getEntity;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class RandomTeleportCommand {
    private static final int MAX_ATTEMPTS = 10; // Maximum attempts to find a safe location

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("randomteleport")
                .then(argument("target", entity())
                        .then(argument("ignoreArea", IntegerArgumentType.integer(0))
                                .then(argument("maxPlaytimeHours", DoubleArgumentType.doubleArg(0))
                                        .executes(RandomTeleportCommand::execute)
                                )
                        )
                        .executes(RandomTeleportCommand::execute) // No optional parameters, use default values
                ));
    }

    public static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        //change this after - because world border give strange numbers
        final int worldLimit = 9500;
        ServerCommandSource source = context.getSource();
        ServerWorld world = source.getWorld();
        MinecraftServer server = world.getServer();
        Random random = new Random();

        if (!(getEntity(context, "target") instanceof PlayerEntity targetEntity)) return 0;

        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) world.getPlayerByUuid(targetEntity.getUuid());

        assert serverPlayerEntity != null;

        // Get optional parameters
        int ignoreArea = IntegerArgumentType.getInteger(context, "ignoreArea");
        double maxPlaytimeHours = DoubleArgumentType.getDouble(context, "maxPlaytimeHours");

        // Check playtime
        long playTimeTicks = serverPlayerEntity.getStatHandler().getStat(Stats.CUSTOM, Stats.PLAY_TIME);
        double playTimeHours = (double) playTimeTicks / 72000.0;
        if (playTimeHours > maxPlaytimeHours) {
            source.sendFeedback((Supplier<Text>) () -> Text.literal("Ти вже не новачок - мацай травичку"), true);
            return 1;
        }

        // Get world boundaries
        int worldSize = world.getLogicalHeight();
        int xMin = -worldSize;
        int xMax = worldLimit;
        int zMin = -worldSize;
        int zMax = worldLimit;

        // Modify boundaries to exclude the ignore area
        if (ignoreArea > 0) {
            xMin = ignoreArea;
            zMin = ignoreArea;
        }


        // Attempt to find a safe location for up to MAX_ATTEMPTS times
        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            // Generate random coordinates within boundaries
            int x = random.nextInt(xMax - xMin + 1) + xMin;
            int z = random.nextInt(zMax - zMin + 1) + zMin;

            // Find a safe location on the ground
            BlockPos target = findSafeLocation(world, new BlockPos(x, world.getLogicalHeight(), z));
            if (target == null) {
                continue; // Try again if no safe location found
            }

            // Check for water/lava above the target location
            if (!isSafeAbove(world, target)) {
                continue; // Try again if water/lava found above
            }

            // Teleport the target entity
            Vec3d targetVec = Vec3d.ofCenter(target);
            targetEntity.teleport(world, targetVec.x, targetVec.y + 1, targetVec.z, PositionFlag.getFlags(0), targetEntity.prevYaw, targetEntity.prevPitch);

            return 1; // Teleportation successful
        }

        // If no safe location found after MAX_ATTEMPTS
        source.sendFeedback((Supplier<Text>) () -> Text.literal("Не вдалося знайти безпечне місце для телепортації після декількох спроб, спробуй ще раз"), true);
        return 1;
    }

    // Find a safe location on the ground, checking for danger blocks
    private static BlockPos findSafeLocation(ServerWorld world, BlockPos initialPos) {
        for (int y = initialPos.getY(); y > 0; y--) {
            BlockPos checkPos = new BlockPos(initialPos.getX(), y, initialPos.getZ());
            if (world.getBlockState(checkPos).isSolidBlock(world, checkPos) &&
                    !isDangerousBlock(world.getBlockState(checkPos.down()))) {
                return checkPos;
            }
        }
        return null;
    }

    // Check if there is no water/lava above the given position
    private static boolean isSafeAbove(ServerWorld world, BlockPos pos) {
        for (int y = pos.getY() + 1; y < world.getLogicalHeight(); y++) {
            BlockPos checkPos = new BlockPos(pos.getX(), y, pos.getZ());
            if (world.getBlockState(checkPos).getBlock() == Blocks.WATER ||
                    world.getBlockState(checkPos).getBlock() == Blocks.LAVA) {
                return false; // Water/lava found above
            }
        }
        return true; // No water/lava found above
    }

    // Check if a block is dangerous (lava, water, cactus, etc.)
    private static boolean isDangerousBlock(net.minecraft.block.BlockState blockState) {
        return blockState.getBlock().getName() == Blocks.LAVA.getName() ||
                blockState.getBlock().getName() == Blocks.WATER.getName() ||
                blockState.getBlock().getName() == Blocks.CACTUS.getName();
    }
}