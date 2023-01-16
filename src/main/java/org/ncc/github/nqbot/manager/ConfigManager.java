package org.ncc.github.nqbot.manager;

import org.bukkit.Bukkit;
import org.ncc.github.nqbot.data.BindFile;
import org.ncc.github.nqbot.data.ConfigFile;
import org.ncc.github.nqbot.data.ConfigYMLFile;
import org.ncc.github.nqbot.utils.CoreUtils;

import java.io.File;
import java.util.logging.Logger;

/**
 * 管理配置文件的
 */
public class ConfigManager {
    private static final Logger LOGGER = Bukkit.getLogger();
    public static File bindData = new File(CoreUtils.getDataFolder(),"bind.yml");
    public static ConfigFile CONFIG_FILE_READ;
    //todo complete it
    public static BindFile BIND_FILE_READ = new BindFile(System.currentTimeMillis());
    public static ConfigYMLFile YML_FILE_READ = new ConfigYMLFile(3181474546L);

    /**
     * 初始化或读取当前的配置文件
     */
    public static void init(){
        LOGGER.info("Reading config...");
        final File configs = new File(CoreUtils.getDataFolder(),"configs");
        final File configYML = new File(CoreUtils.getDataFolder(),"config.yml");
        final File bindData = new File(CoreUtils.getDataFolder(),"bind.yml");

        if (configs.exists() && configs.isDirectory()){
            CONFIG_FILE_READ = ConfigFile.readFromFile(configs,"groupconfig.json");
        }
        if(CONFIG_FILE_READ == null){
            LOGGER.info("Config file does not found!Creating...");
            ConfigFile configFile = new ConfigFile(0000,1145);
            configFile.writeToFile(configs,"groupconfig.json");
            CONFIG_FILE_READ = configFile;
        }
        YML_FILE_READ.writeToFile(configYML);
        //BIND_FILE_READ.writeToFile(bindData);
        LOGGER.info("Config init successful!");
    }
}
