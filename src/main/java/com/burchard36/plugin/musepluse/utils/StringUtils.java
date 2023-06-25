package com.burchard36.plugin.musepluse.utils;

import lombok.NonNull;
import org.bukkit.ChatColor;
/**
 * A collection of utilities used in the manipulation of strings for SpigotAPI
 */
public class StringUtils {

    public static @NonNull String convert(final String toConvert) {
        return ChatColor.translateAlternateColorCodes('&', toConvert);
    }


}
