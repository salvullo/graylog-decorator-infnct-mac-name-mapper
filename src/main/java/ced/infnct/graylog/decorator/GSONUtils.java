package ced.infnct.graylog.decorator;

/**
 * Created by salvullo on 13/07/2017.
 */

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Use this class to get an instance of Gson that is configured for this application.
 */
public class GSONUtils {

    private static final GsonBuilder GSON_BUILDER = new GsonBuilder();

    static {
        GSON_BUILDER.registerTypeAdapterFactory(new ImmutableMapTypeAdapterFactory());
        GSON_BUILDER.registerTypeAdapter(ImmutableMap.class, ImmutableMapTypeAdapterFactory.newCreator());
    }

    public static Gson get() {
        return GSON_BUILDER.create();
    }

    public static boolean serializeImmutableMap(ImmutableMap map, String filename) {
        boolean result = true;

        try (JsonWriter w = new JsonWriter(new FileWriter(filename))) {

            GSONUtils.get().toJson(map, ImmutableMap.class, w);

        } catch (java.io.IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public static ImmutableMap deserializeImmutableMap(String filename) {

        ImmutableMap result = null;

        try (JsonReader reader = new JsonReader(new FileReader(filename))) {

            result = GSONUtils.get().fromJson(reader, ImmutableMap.class);

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static class ImmutableMapTypeAdapterFactory implements TypeAdapterFactory {

        public static <K, V> InstanceCreator<Map<K, V>> newCreator() {
            return type -> new HashMap<>();
        }

        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!ImmutableMap.class.isAssignableFrom(type.getRawType())) {
                return null;
            }
            final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
            return new TypeAdapter<T>() {
                @Override
                public void write(JsonWriter out, T value) throws IOException {
                    delegate.write(out, value);
                }

                @Override
                @SuppressWarnings("unchecked")
                public T read(JsonReader in) throws IOException {
                    return (T) ImmutableMap.copyOf((Map) delegate.read(in));
                }
            };
        }
    }
}
