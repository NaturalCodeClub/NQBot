package org.ncc.github.nqbot.data;

import com.google.gson.Gson;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 机器人主配置文件的类
 */
public class ConfigFile {
    private static final Logger logger = Bukkit.getLogger();
    private static final Gson GSON = new Gson();
    private final long listeningGroup;
    private final long masterName;

    public ConfigFile(long listeningGroup,long masterName){
        this.listeningGroup = listeningGroup;
        this.masterName = masterName;
    }

    /**
     * @return 当前正在监听的群的ID
     */
    public long getListeningGroup(){
        return this.listeningGroup;
    }

    /**
     * 获取主人的ID
     * @return 主人的ID
     */
    public long getMasterName() {
        return masterName;
    }

    /**
     * 写入到文件
     * @param file 指定文件夹
     * @param name 名称
     */
    public void writeToFile(File file, String name){
        final String content = GSON.toJson(this);
        final File file1 = new File(file,name);
        if (!file1.exists()){
            try {
                file1.createNewFile();
            } catch (IOException e) {
                logger.log(Level.SEVERE,"Error in creating config file",e);
            }
        }
        try (FileOutputStream outputStream = new FileOutputStream(file1)){
            outputStream.write(content.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        } catch (IOException e) {
            logger.log(Level.SEVERE,"Error in writing config file",e);
        }
    }

    /**
     * 从文件读取
     * @param file 指定文件夹
     * @param name 文件名
     * @return 已经读取到的，如果读取失败就返回null
     */
    public static ConfigFile readFromFile(File file,String name){
        final File file1 = new File(file,name);
        try{
            final String content = new String(Files.readAllBytes(file1.toPath()));
            return GSON.fromJson(content,ConfigFile.class);
        } catch (FileNotFoundException e){
            return null;
        }catch (IOException e) {
            logger.log(Level.SEVERE,"Error in reading config file",e);
        }
        return null;
    }
}
