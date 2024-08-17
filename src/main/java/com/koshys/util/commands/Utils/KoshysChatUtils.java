package com.koshys.util.commands.Utils;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class KoshysChatUtils {
    public static void sendMessageToNearbyPlayers(ServerWorld world, Vec3d playerPos, String message, int radius, boolean overlay){
        for (ServerPlayerEntity nearbyPlayer : world.getPlayers()) {
            if (nearbyPlayer.getPos().distanceTo(playerPos) <= radius) {
                nearbyPlayer.sendMessage(Text.literal(message), overlay);
            }
        }
    }
}
