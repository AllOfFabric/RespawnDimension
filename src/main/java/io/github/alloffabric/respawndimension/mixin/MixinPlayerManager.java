package io.github.alloffabric.respawndimension.mixin;

import io.github.alloffabric.respawndimension.ConfigHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Mixin(PlayerManager.class)
public class MixinPlayerManager {

    @Shadow @Final private MinecraftServer server;
    /**
     * need this only so long as a player hasn't relogged after initially joining this world<br/>
     * yes, this is a hack, but gets around having to serialize data. might be changed in the future.
     */
    @Unique
    private static final Set<UUID> respawnedPlayers = new HashSet<>(10);

    @Redirect(method = "respawnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;isSpawnForced()Z"))
    private boolean respawndimension_isSpawnForced(ServerPlayerEntity playerEntity, ServerPlayerEntity samePlayer, DimensionType dimension, boolean alive) {
        boolean ret = playerEntity.isSpawnForced();
        return ret || playerEntity.dimension != dimension; //we don't want the "your home bed was missing or obstructed" message when teleporting to another dimension
    }

    @Inject(method = "onPlayerConnect", at = @At("RETURN"))
    private void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        if(player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.LEAVE_GAME)) == 0 && !respawnedPlayers.contains(player.getUuid())) {
            this.server.execute(() -> player.teleport(this.server.getWorld(ConfigHandler.getConfig().respawnDimension), player.getX(), player.getY(), player.getZ(), player.yaw, player.pitch));
            respawnedPlayers.add(player.getUuid());
        }
    }
}
