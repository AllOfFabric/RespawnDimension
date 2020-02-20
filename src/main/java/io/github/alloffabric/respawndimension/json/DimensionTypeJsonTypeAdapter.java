package io.github.alloffabric.respawndimension.json;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;

import java.io.IOException;

public class DimensionTypeJsonTypeAdapter extends TypeAdapter<DimensionType> {

    @Override
    public void write(JsonWriter out, DimensionType value) throws IOException {
        out.value(String.valueOf(Registry.DIMENSION_TYPE.getId(value)));
    }

    @Override
    public DimensionType read(JsonReader in) throws IOException {
        String value = in.nextString();
        return Registry.DIMENSION_TYPE.getOrEmpty(new Identifier(value)).orElseThrow(() -> new JsonSyntaxException(value + " is not a valid dimension type!"));
    }
}
