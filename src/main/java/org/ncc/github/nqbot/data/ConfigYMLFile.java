package org.ncc.github.nqbot.data;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigYMLFile {
    private static final Logger logger = Bukkit.getLogger();
    private FileConfiguration configYML = null;
    private long BotMasterID;

    public ConfigYMLFile(long botMasterID){
        this.BotMasterID = botMasterID;
    }
    /**
     * @return 返回配置文件中设置的机器人主人
     * */
    public long getBotMasterID(){
        return BotMasterID;
    }
    /**
     * @return 返回configYML
     * */
    public FileConfiguration getConfig(){
        return configYML;
    }
    /**
     * 写入到文件
     * @param file 指定文件
     */
    public void writeToFile(File file){
        if(!file.exists()){
            if(!file.isFile()){
                logger.log(Level.SEVERE,"Error in ConfigYMLFile.java");
            }else{
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    logger.log(Level.SEVERE,"Error in creating config.yml");
                    logger.log(Level.SEVERE,e.getMessage());
                }
            }
        }
        boolean b = configYML.get("config.botmaster")==null;
        if(b){
            configYML.set("config.botmaster",BotMasterID);
            try {
                configYML.save(file);
            } catch (IOException e) {
                logger.log(Level.SEVERE,"Error in add config to config.yml");
                logger.log(Level.SEVERE,e.getMessage());
            }
        }

    }

}
