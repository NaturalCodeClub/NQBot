package org.ncc.github.nqbot.utils;


import org.bukkit.Bukkit;
import org.ncc.github.nqbot.manager.ConfigManager;

public class InfoUtils {
    public static long masterID = ConfigManager.YML_FILE_READ.getBotMasterID();
    public static final String version = Bukkit.getPluginManager().getPlugin("NQBot").getDescription().getVersion();
}
