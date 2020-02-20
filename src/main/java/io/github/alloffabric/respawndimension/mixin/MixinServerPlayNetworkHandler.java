package io.github.alloffabric.respawndimension.mixin;

import io.github.alloffabric.respawndimension.ConfigHandler;
import net.minecraft.advancement.criterion.ChangedDimensionCriterion;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayNetworkHandler.class)
public class MixinServerPlayNetworkHandler {

    @ModifyArg(method = "onClientStatus", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;respawnPlayer(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/world/dimension/DimensionType;Z)Lnet/minecraft/server/network/ServerPlayerEntity;"), require = 2)
    private DimensionType respawndimension_onClientStatus$respawnPlayer(DimensionType type) {
        return ConfigHandler.getConfig().respawnDimension;
    }

    @Redirect(method = "onClientStatus", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/criterion/ChangedDimensionCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/world/dimension/DimensionType;Lnet/minecraft/world/dimension/DimensionType;)V"))
    private void respawndimension_onClientStatus$trigger(ChangedDimensionCriterion criterion, ServerPlayerEntity player, DimensionType from, DimensionType to) {
        criterion.trigger(player, from, player.dimension); //needed because vanilla hardcodes this to the overworld
    }
}
