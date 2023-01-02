package org.ncc.github.nqbot.manager;

import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.utils.BotConfiguration;
import org.bukkit.Bukkit;
import org.ncc.github.nqbot.data.BotConfigEntry;
import org.ncc.github.nqbot.data.BotConfigEntryArray;
import org.ncc.github.nqbot.bot.BotEntry;
import org.ncc.github.nqbot.eventsystem.EventHub;
import org.ncc.github.nqbot.processors.CommandProcessor;
import org.ncc.github.nqbot.utils.CoreUtils;
import org.ncc.github.nqbot.utils.bot.MultiMessageSender;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * 用于管理当前已经加载的机器人
 */
public class BotManager {
    //这个字段存储当前的机器人实例
    public static final List<BotEntry> loadedBots = Collections.synchronizedList(new ArrayList<>());
    public static final Logger LOGGER = Bukkit.getLogger();
    //多机器人发送器，不过不建议使用
    public static final MultiMessageSender multiSender = new MultiMessageSender();
    //配置文件的文件夹的实例
    private static final File configFolder = new File(CoreUtils.getDataFolder(),"configs");

    /**
     * 关机
     */
    public static void shutdown(){
        for (BotEntry entry : loadedBots){
            entry.getBot().close();
        }
        loadedBots.clear();
        multiSender.shutdown();
    }

    /**
     * 初始化所有配置文件内配置的机器人
     */
    public static void init(){
        try {
            if (!configFolder.exists()){
                configFolder.mkdir();
            }
            File file = new File(configFolder,"bots.json");
            checkAndCreateConfigFile(file);
            if(file.exists()){
                BotConfigEntryArray array = BotConfigEntryArray.fromJson(new String(Files.readAllBytes(file.toPath())));
                //多线程加载
                CompletableFuture.allOf(Arrays.stream(array.entries)
                        .map(value->CompletableFuture.supplyAsync(()->{
                            BotEntry entry = new BotEntry() {
                                @Override
                                public void processEvent(Event event) {
                                    EventHub.broadcastEvent(event);
                                    if (event instanceof GroupMessageEvent){
                                        CommandProcessor.processAsync(((GroupMessageEvent) event));
                                    }
                                }
                            };
                            entry.runBot(value);
                            return entry;
                        }).whenComplete((value1,cause)->{
                            if (cause==null){
                                loadedBots.add(value1);
                            }else{
                                cause.printStackTrace();
                            }
                        })).toArray(CompletableFuture[]::new)).join();
                multiSender.doInit(loadedBots);
            }
        }catch (Exception e){
            LOGGER.log(Level.SEVERE,"Error in loading bots",e);
        }
    }

    /**
     * 检查并初始化配置文件
     * @param file 配置文件的实例
     * @throws IOException 如果创建失败
     */
    private static void checkAndCreateConfigFile(File file) throws IOException {
        if (!file.exists()) {
            LOGGER.warning("Bot config not found!Exiting..");
            file.createNewFile();
            BotConfigEntryArray array = new BotConfigEntryArray();
            array.entries = new BotConfigEntry[1];
            array.entries[0] = new BotConfigEntry(114514,"114514", BotConfiguration.MiraiProtocol.ANDROID_PAD);
            try(FileOutputStream fos = new FileOutputStream(file)){
                fos.write(array.toJson().getBytes());
                fos.flush();
            }catch (Exception e){
                e.printStackTrace();
            }
            //就这样吧
            Bukkit.shutdown();
        }
    }
}
