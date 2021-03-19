package net.toshimichi.sushi.modules;

import net.toshimichi.sushi.modules.config.Configurations;

@FunctionalInterface
public interface ModuleFactory {
    Module newModule(String id, String name, Modules modules, Categories categories, Configurations provider);
}
