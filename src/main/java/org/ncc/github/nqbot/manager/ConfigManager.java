package org.ncc.github.nqbot.manager;

import org.bukkit.Bukkit;
import org.ncc.github.nqbot.utils.Utils;
import java.io.File;
import java.util.logging.Logger;

public class ConfigManager {
    private static final Logger LOGGER = Bukkit.getLogger();
    public static ConfigFile CONFIG_FILE_READ;

    public static void init(){
        LOGGER.info("Reading config...");
        final File file = new File(Utils.getDataFolder(),"configs");
        if (file.exists() && file.isDirectory()){
            CONFIG_FILE_READ = ConfigFile.readFromFile(file,"groupconfig.json");
        }
        if(CONFIG_FILE_READ == null){
            LOGGER.info("Config file does not found!Creating...");
            ConfigFile configFile = new ConfigFile(0000,1145);
            configFile.writeToFile(file,"groupconfig.json");
            CONFIG_FILE_READ = configFile;
        }
        LOGGER.info("Config init successful!");
    }
}