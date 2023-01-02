package org.ncc.github.nqbot.manager;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.ncc.github.nqbot.data.BotData;
import org.ncc.github.nqbot.utils.CoreUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 用于存储机器人的一些数据，不过目前还没用到
 */
public class BotDataManager {
    public static BotData currentData = new BotData();
    private static final Logger logger = Bukkit.getLogger();
    private static final Gson gson = new Gson();

    /**
     * 初始化或加载机器人数据
     */
    public static void init(){
        try {
            File dataFile = new File(CoreUtils.getDataFolder(),"botdata.json");
            if(dataFile.exists()){
                String jsonContent = new String(Files.readAllBytes(dataFile.toPath()));
                currentData = gson.fromJson(jsonContent,BotData.class);
            }else{
                FileOutputStream fileOutputStream = new FileOutputStream(dataFile);
                fileOutputStream.write(gson.toJson(currentData).getBytes(StandardCharsets.UTF_8));
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }catch (Exception e){
            logger.log(Level.SEVERE,"Error in init botdata manager",e);
            e.printStackTrace();
        }
    }

    public static void saveCurrent() {
        try {
            File dataFile = new File(CoreUtils.getDataFolder(),"botdata.json");
            dataFile.delete();
            FileOutputStream fileOutputStream = new FileOutputStream(dataFile);
            fileOutputStream.write(gson.toJson(currentData).getBytes(StandardCharsets.UTF_8));
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE,"Error in saving bot data",e);
            e.printStackTrace();
        }
    }
}
