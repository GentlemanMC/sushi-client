package net.toshimichi.sushi.modules;

import net.toshimichi.sushi.config.RootConfigurations;

public interface Module {

    String getId();

    String getName();

    boolean isTemporary();

    boolean isEnabled();

    void setEnabled(boolean enabled);

    boolean isPaused();

    void setPaused(boolean paused);

    ConflictType[] getConflictTypes();

    Category getCategory();

    void setCategory(Category category);

    Keybind getKeybind();

    void setKeybind(Keybind bind);

    RootConfigurations getConfigurations();

    ModuleFactory getModuleFactory();
}
