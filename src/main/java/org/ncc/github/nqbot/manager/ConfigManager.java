package org.ncc.github.nqbot.manager;

import org.bukkit.Bukkit;
import org.ncc.github.nqbot.data.ConfigFile;
import org.ncc.github.nqbot.utils.CoreUtils;
import java.io.File;
import java.util.logging.Logger;

/**
 * 管理配置文件的
 */
public class ConfigManager {
    private static final Logger LOGGER = Bukkit.getLogger();
    public static ConfigFile CONFIG_FILE_READ;

    /**
     * 初始化或读取当前的配置文件
     */
    public static void init(){
        LOGGER.info("Reading config...");
        final File file = new File(CoreUtils.getDataFolder(),"configs");
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
