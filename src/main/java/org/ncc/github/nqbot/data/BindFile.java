package org.ncc.github.nqbot.data;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BindFile {
    private static final Logger logger = Bukkit.getLogger();
    private static Gson gson = new Gson();
    public static Map<Long,String> bindData = new HashMap<>();
    public static Map<Long,String> tempMap = new HashMap<>();
    private static Long l;
    public BindFile(){

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
        final String contect = gson.toJson(bindData);
        if(!file.exists()&& file.isFile()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.log(Level.SEVERE,"Error in creating bind.json",e);
            }
            try(FileOutputStream f = new FileOutputStream(file)){
                f.write(contect.getBytes(StandardCharsets.UTF_8));
                f.flush();
            } catch (FileNotFoundException e) {
                logger.log(Level.SEVERE,"Error in init bind.json",e);
            } catch (IOException e) {
                logger.log(Level.SEVERE,"Error in init bind.json",e);
            }
        }
    }
    public void readFromFile(File file){
        try {
            final String contect = new String(Files.readAllBytes(file.toPath()));
            bindData = gson.fromJson(contect,new TypeToken<Map<Long,String>>(){}.getType());
        } catch (IOException e) {
            logger.log(Level.SEVERE,"Error in reading bind.json",e);
        }
    }
//    public void writeToFile(File file){
//        if(!file.exists()){
//            if(!file.isFile()){
//                logger.log(Level.SEVERE,"Error in BindFile.java");
//            }else{
//                try {
//                    file.createNewFile();
//                } catch (IOException e) {
//                    logger.log(Level.SEVERE,"Error in creating bind.yml");
//                    logger.log(Level.SEVERE,e.getMessage());
//                }
//            }
//        }
//        BindFile = YamlConfiguration.loadConfiguration(file);
//        boolean b = BindFile.get("createTime")==null;
//        if(b){
//            BindFile.set("createTime",l);
//        }
//        try {
//            BindFile.save(file);
//        } catch (IOException e) {
//            logger.log(Level.SEVERE,"Error in add config to config.yml");
//            logger.log(Level.SEVERE,e.getMessage());
//        }
//
//    }
//    public void saveData(FileConfiguration f,File file){
//        try {
//            f.save(file);
//        } catch (IOException e) {
//            logger.log(Level.SEVERE,e.getMessage());
//        }
//    }
}
