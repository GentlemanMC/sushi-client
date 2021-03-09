package net.toshimichi.sushi.modules.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class GsonConfigurationProvider implements ConfigurationProvider {

    private final Gson gson;
    private JsonObject object;
    private final ArrayList<GsonConfiguration<?>> list = new ArrayList<>();

    public GsonConfigurationProvider(Gson gson, JsonObject object) {
        this.gson = gson;
        this.object = object;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Configuration<T> getConfiguration(String name, Class<T> tClass, T defaultValue, Supplier<Boolean> isValid, Configuration<?> parent) {
        if (getRawValue(name, tClass) == null)
            setRawValue(name, defaultValue);
        for (GsonConfiguration<?> loaded : list) {
            if (loaded.getName().equals(name)) {
                if (!loaded.getValueClass().equals(tClass))
                    throw new IllegalArgumentException("Supplied class is not valid");
                return (Configuration<T>) loaded;
            }
        }
        GsonConfiguration<T> conf = new GsonConfiguration<>(name, tClass, this, isValid, parent);
        list.add(conf);
        return conf;
    }

    @Override
    public List<Configuration<?>> getConfigurations() {
        return new ArrayList<>(list);
    }

    public void load(JsonObject object) {
        this.object = object;
    }

    public JsonObject save() {
        return object;
    }

    protected <T> T getRawValue(String name, Class<T> tClass) {
        JsonElement element = object.get(name);
        if (element == null) {
            return null;
        } else {
            try {
                return gson.fromJson(object.get(name), tClass);
            } catch (JsonParseException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    protected void setRawValue(String name, Object o) {
        object.add(name, gson.toJsonTree(o));
    }

}
