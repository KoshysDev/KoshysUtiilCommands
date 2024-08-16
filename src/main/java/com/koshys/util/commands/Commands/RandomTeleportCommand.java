package com.koshys.util.commands.Commands;

import com.koshys.util.commands.KoshysUtiilCommands;
import com.mojang.brigadier.CommandDispatcher;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;
import java.util.Random;

import static net.minecraft.command.argument.EntityArgumentType.entity;
import static net.minecraft.command.argument.EntityArgumentType.getEntity;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class RandomTeleportCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("randomteleport")
                .then(argument("target", entity())
                        .executes(RandomTeleportCommand::execute)));
    }

    public static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerWorld world = source.getWorld();
        MinecraftServer server = world.getServer();
        Random random = new Random();

        if (!(getEntity(context, "target") instanceof PlayerEntity targetEntity)) return 0;

        // Get the target entity from the command

        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) world.getPlayerByUuid(targetEntity.getUuid());

        assert serverPlayerEntity != null;
        long playTimeTicks = serverPlayerEntity.getStatHandler().getStat(Stats.CUSTOM, Stats.PLAY_TIME);

        // Get world boundaries
        int worldSize = world.getLogicalHeight();
        int xMin = -worldSize;
        int xMax = worldSize;
        int zMin = -worldSize;
        int zMax = worldSize;

        // Generate random coordinates within boundaries
        int x = random.nextInt(xMax - xMin + 1) + xMin;
        int z = random.nextInt(zMax - zMin + 1) + zMin;

        // Find a safe location on the ground
        BlockPos target = findSafeLocation(world, new BlockPos(x, world.getLogicalHeight(), z));
        if (target == null) {
            KoshysUtiilCommands.LOGGER.info("{} can't find safe location!", Objects.requireNonNull(source.getEntity()).getName());
            return 0;
        }

        // Teleport the target entity
        Vec3d targetVec = Vec3d.ofCenter(target);
        targetEntity.teleport(world, targetVec.x, targetVec.y + 1, targetVec.z, PositionFlag.getFlags(0), targetEntity.prevYaw, targetEntity.prevPitch);
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

    // Check if a block is dangerous (lava, water, cactus, etc.)
    private static boolean isDangerousBlock(net.minecraft.block.BlockState blockState) {
        return blockState.getBlock().getName() == Blocks.LAVA.getName() ||
                blockState.getBlock().getName() == Blocks.WATER.getName() ||
                blockState.getBlock().getName() == Blocks.CACTUS.getName();
    }
}