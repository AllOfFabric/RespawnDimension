package io.github.alloffabric.respawndimension;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

public class RespawnDimension implements ModInitializer {

    public static final String MODID = "respawn_dimension";

    @Override
    public void onInitialize() {
        ConfigHandler.reloadConfig();
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new ConfigReloader());
    }
}
