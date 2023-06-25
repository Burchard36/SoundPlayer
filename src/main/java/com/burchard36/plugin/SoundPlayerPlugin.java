package com.burchard36.plugin;

import com.burchard36.plugin.commands.SoundsGuiCommand;
import com.burchard36.plugin.musepluse.config.ConfigManager;
import com.burchard36.plugin.musepluse.gui.GuiEvents;
import com.burchard36.plugin.musepluse.gui.GuiManager;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class SoundPlayerPlugin extends JavaPlugin implements Listener {
    public static SoundPlayerPlugin INSTANCE;
    @Getter
    private ConfigManager configManager;
    @Getter
    private Random random;
    @Getter
    private GuiManager guiManager;
    private GuiEvents guiEvents;
    public static NamespacedKey INCREMENT_VALUE_KEY;

    @Override
    public void onLoad() {
        INSTANCE = this;
        random = new Random();
        INCREMENT_VALUE_KEY = new NamespacedKey(this, "increment_value");
        this.configManager = new ConfigManager(this);
        this.guiManager = new GuiManager();
    }
    @Override
    public void onEnable() {
        registerEvent(this);
        this.guiEvents = new GuiEvents(guiManager);
        registerEvent(this.guiEvents);


        registerCommand("soundsgui", new SoundsGuiCommand(this));
    }

    @Override
    public void onDisable() {
    }

    public static void registerEvent(final Listener eventListener) {
        INSTANCE.getServer().getPluginManager().registerEvents(eventListener, INSTANCE);
    }

    public static void registerCommand(final String commandName, final CommandExecutor executor) {
        INSTANCE.getCommand(commandName).setExecutor(executor);
    }
}
