package org.ncc.github.nqbot.data;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BindFile {
    private static final Logger logger = Bukkit.getLogger();
    private FileConfiguration BindFile;
    public static Map<Long,String> bindData = new HashMap<>();
    public static Map<Long,String> tempMap = new HashMap<>();
    private static long l;
    public BindFile(long timestamp){
        l = timestamp;
    }
    public String getName(long id){
        return bindData.get(id);
    }
    public long getID(String name){
        Long l = null;
        for (Long l1 : bindData.keySet()){
            if(bindData.get(l1).equals(name)){
                l = l1;
                break;
            }
        }
        return l;
    }
    /**
     * 写入到文件
     * @param file 指定文件
     */
    public void writeToFile(File file){
        if(!file.exists()){
            if(!file.isFile()){
                logger.log(Level.SEVERE,"Error in BindFile.java");
            }else{
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    logger.log(Level.SEVERE,"Error in creating bind.yml");
                    logger.log(Level.SEVERE,e.getMessage());
                }
            }
        }
        BindFile = YamlConfiguration.loadConfiguration(file);
        boolean b = BindFile.get("createTime")==null;
        if(b){
            BindFile.set("createTime",l);
        }
        try {
            BindFile.save(file);
        } catch (IOException e) {
            logger.log(Level.SEVERE,"Error in add config to config.yml");
            logger.log(Level.SEVERE,e.getMessage());
        }

    }
}
