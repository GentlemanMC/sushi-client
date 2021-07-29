package net.toshimichi.sushi.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.toshimichi.sushi.Sushi;
import net.toshimichi.sushi.command.LogLevel;
import net.toshimichi.sushi.command.MessageHandler;
import net.toshimichi.sushi.config.Configuration;
import net.toshimichi.sushi.config.ConfigurationCategory;
import net.toshimichi.sushi.config.RootConfigurations;
import net.toshimichi.sushi.gui.hud.ElementConstructor;
import net.toshimichi.sushi.gui.hud.ElementFactory;

import java.util.ArrayList;

abstract public class BaseModule implements Module {

    private final String id;
    private final RootConfigurations provider;
    private final Modules modules;
    private final Categories categories;
    private final Configuration<String> name;
    private final Configuration<String> category;
    private final Configuration<Keybind> keybind;
    private final Configuration<Boolean> temporary;
    private final Configuration<Boolean> visible;
    private final Configuration<Boolean> toggleNotification;
    private final ModuleFactory factory;
    private final ArrayList<ElementFactory> hudElementFactories;
    private boolean isEnabled;
    private boolean isPaused;

    public BaseModule(String id, Modules modules, Categories categories, RootConfigurations provider, ModuleFactory factory) {
        this.id = id;
        this.provider = provider;
        this.modules = modules;
        this.categories = categories;
        this.factory = factory;
        ConfigurationCategory commonCategory = provider.getCategory("common", "Common Settings", "Common settings for most modules");
        commonCategory.get("id", "Module ID", "Module ID", String.class, id, () -> true, true, 80000);
        this.name = commonCategory.get("name", "Module Name", "Module name", String.class, getDefaultName(), () -> true, false, 81000);
        this.category = commonCategory.get("category", "Module Category", "Module category", String.class, getDefaultCategory().getName(), () -> true, false, 82000);
        this.keybind = provider.get("keybind", "Module Keybind", "Keybind for this module", Keybind.class, getDefaultKeybind(), () -> true, false, 83000);
        this.temporary = commonCategory.get("temporary", "Temporary Module", null, Boolean.class, isTemporaryByDefault(), () -> true, false, 84000);
        this.visible = commonCategory.get("visible", "Visible", null, Boolean.class, isVisibleByDefault(), () -> true, false, 85000);
        this.toggleNotification = commonCategory.get("toggle_notification", "Toggle Notification", null, Boolean.class, false, () -> true, false, 86000);
        commonCategory.get("reset", "Reset Settings", null, Runnable.class, provider::reset, () -> true, true, 86000);

        hudElementFactories = new ArrayList<>();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name.getValue();
    }

    @Override
    public boolean isTemporary() {
        return temporary.getValue();
    }

    @Override
    public boolean isVisible() {
        return visible.getValue();
    }

    @Override
    public void setVisible(boolean v) {
        visible.setValue(v);
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (!isPaused) {
            MessageHandler handler = Sushi.getProfile().getMessageHandler();
            if (!this.isEnabled && enabled) {
                onEnable();
                if (toggleNotification.getValue()) {
                    handler.send(LogLevel.INFO, "Enabled " + getName());
                }
            } else if (this.isEnabled && !enabled) {
                onDisable();
                if (toggleNotification.getValue()) {
                    handler.send(LogLevel.INFO, "Disabled " + getName());
                }
            }
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
    }

    public void onDisable() {
    }

    @Override
    public RootConfigurations getConfigurations() {
        return provider;
    }

    @Override
    public ConflictType[] getConflictTypes() {
        return new ConflictType[0];
    }

    @Override
    public Category getCategory() {
        Category result = categories.getModuleCategory(category.getValue());
        if (result != null) return result;
        return getDefaultCategory();
    }

    @Override
    public void setCategory(Category category) {
        this.category.setValue(category.getName());
    }

    @Override
    public Keybind getKeybind() {
        return keybind.getValue();
    }

    @Override
    public void setKeybind(Keybind bind) {
        keybind.setValue(bind);
    }

    @Override
    public ModuleFactory getModuleFactory() {
        return factory;
    }

    @Override
    public ElementFactory[] getElementFactories() {
        return hudElementFactories.toArray(new ElementFactory[0]);
    }

    abstract public String getDefaultName();

    protected void addElementFactory(ElementConstructor constructor, String id, String name) {
        hudElementFactories.add(new BaseElementFactory(constructor, id, name));
    }

    public Keybind getDefaultKeybind() {
        return new Keybind(ActivationType.TOGGLE);
    }

    abstract public Category getDefaultCategory();

    protected boolean isTemporaryByDefault() {
        return false;
    }

    protected boolean isVisibleByDefault() {
        return true;
    }

    protected Minecraft getClient() {
        return Minecraft.getMinecraft();
    }

    protected EntityPlayerSP getPlayer() {
        return getClient().player;
    }

    protected PlayerControllerMP getController() {
        return getClient().playerController;
    }

    protected WorldClient getWorld() {
        return getClient().world;
    }

    protected NetHandlerPlayClient getConnection() {
        return getPlayer().connection;
    }

    private static class BaseElementFactory implements ElementFactory {

        private final ElementConstructor constructor;
        private final String id;
        private final String name;

        public BaseElementFactory(ElementConstructor constructor, String id, String name) {
            this.constructor = constructor;
            this.id = id;
            this.name = name;
        }

        @Override
        public ElementConstructor getElementConstructor() {
            return constructor;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
