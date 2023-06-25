package com.burchard36.plugin.commands;

import com.burchard36.plugin.gui.SoundsGui;
import com.burchard36.plugin.SoundPlayerPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.burchard36.plugin.musepluse.utils.StringUtils.convert;


public class SoundsGuiCommand implements CommandExecutor {
    protected final SoundPlayerPlugin pluginInstance;

    public SoundsGuiCommand(final SoundPlayerPlugin pluginInstance) {
        this.pluginInstance = pluginInstance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(convert("&cYou need op!"));
            return false;
        }

        if (sender instanceof Player player) {
            this.pluginInstance.getGuiManager().openPaginatedTo(player, 0, new SoundsGui(this.pluginInstance, player));
        }
        return false;
    }
}
