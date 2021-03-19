package net.toshimichi.sushi.modules;

import net.minecraftforge.common.MinecraftForge;
import net.toshimichi.sushi.modules.config.Configuration;
import net.toshimichi.sushi.modules.config.Configurations;

abstract public class BaseModule implements Module {

    private final String name;
    private final Configurations provider;
    private final Modules modules;
    private final Categories categories;
    private final Configuration<String> category;
    private final Configuration<Integer> keybind;
    private boolean isEnabled;
    private boolean isPaused;

    public BaseModule(String name, Modules modules, Categories categories, Configurations provider) {
        this.name = name;
        this.provider = provider;
        this.modules = modules;
        this.categories = categories;
        this.category = provider.get("category", String.class, getDefaultCategory().getName());
        this.keybind = provider.get("keybind", Integer.class, getDefaultKeybind());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (!isPaused) {
            if (!this.isEnabled && enabled)
                onEnable();
            else if (this.isEnabled && !enabled)
                onDisable();
        }
        this.isEnabled = enabled;
    }

    @Override
    public boolean isPaused() {
        return isPaused;
    }

    @Override
    public void setPaused(boolean paused) {
        if (!this.isPaused && paused && isEnabled) {
            onDisable();
        } else if (this.isPaused && !paused && !isEnabled) {
            onEnable();
        }
        this.isPaused = paused;
    }

    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @Override
    public Configurations getConfigurations() {
        return provider;
    }

    @Override
    public ConflictType[] getConflictTypes() {
        return new ConflictType[0];
    }

    abstract public Category getDefaultCategory();

    @Override
    public Category getCategory() {
        Category result = categories.getModuleCategory(name);
        if (result != null) return result;
        return getDefaultCategory();
    }

    @Override
    public void setCategory(Category category) {
        this.category.setValue(category.getName());
    }

    abstract public int getDefaultKeybind();

    @Override
    public int getKeybind() {
        return keybind.getValue();
    }

    @Override
    public void setKeybind(int key) {
        keybind.setValue(key);
    }
}
