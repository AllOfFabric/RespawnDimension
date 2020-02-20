/*
 * Mesh
 * Copyright (C) 2019-2020 GlassPane
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; If not, see <https://www.gnu.org/licenses>.
 */
package io.github.alloffabric.respawndimension;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.alloffabric.respawndimension.json.DimensionTypeJsonTypeAdapter;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.ApiStatus;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * used to load and store config objects.<br/>
 * serialization to/from json files.
 *
 * @deprecated will be dropped when fabric gets it's own config system
 */
@ApiStatus.Internal
@Deprecated
public final class ConfigHandler {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().setLenient().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).registerTypeAdapter(DimensionType.class, new DimensionTypeJsonTypeAdapter()).create();

    private static RespawnDimensionConfig config;

    public static RespawnDimensionConfig getConfig() {
        return config;
    }

    public static RespawnDimensionConfig reloadConfig() {
        File configFile = new File(FabricLoader.getInstance().getConfigDirectory(), RespawnDimension.MODID + ".json");
        if(!configFile.exists()) {
            try(FileWriter writer = new FileWriter(configFile)) {
                GSON.toJson(RespawnDimensionConfig.class.newInstance(), writer);
            }
            catch (IOException | IllegalAccessException | InstantiationException e) {
                throw new RuntimeException(e);
            }
        }
        try(FileReader reader = new FileReader(configFile)) {
            config = GSON.fromJson(reader, RespawnDimensionConfig.class);
        }
        catch (IOException e) {
            try {
                config = RespawnDimensionConfig.class.newInstance();
            }
            catch (InstantiationException | IllegalAccessException ex) {
                throw new RuntimeException(e);
            }
        }
        return config;
    }
}
