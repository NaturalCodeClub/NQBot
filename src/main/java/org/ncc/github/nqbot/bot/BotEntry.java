package org.ncc.github.nqbot.bot;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.utils.BotConfiguration;
import org.bukkit.Bukkit;
import org.ncc.github.nqbot.data.BotConfigEntry;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 机器人实例
 */
public abstract class BotEntry {
    private static final Logger LOGGER = Bukkit.getLogger();
    private static final File DATA_FOLDER = new File("deviceInfos");
    private volatile net.mamoe.mirai.Bot bot;
    private BotConfigEntry configEntry;
    private final AtomicBoolean connected = new AtomicBoolean(false);

    static{
        try {
            File infoFile = new File("deviceInfos");
            if (!infoFile.exists()){
                infoFile.mkdir();
            }
        }catch (Exception e){
            //LOGGER.log(Level.SEVERE,e.getMessage());
            LOGGER.log(Level.SEVERE,"Error in loading bot deviceInfo");
            e.printStackTrace();
        }
    }

    /**
     * 运行机器人
     * @param configEntry 配置实例
     */
    public void runBot(BotConfigEntry configEntry){
        this.configEntry = configEntry;
        BotConfiguration configuration = new BotConfiguration() {
            {
                fileBasedDeviceInfo(new File(DATA_FOLDER,"deviceInfo-"+configEntry.getQid()+".json").getPath());
            }
        };
        configuration.setProtocol(configEntry.getProtocol());
        configuration.noBotLog();
        configuration.noNetworkLog();
        this.doInitCacheFolder(configuration,configEntry);
        this.bot = BotFactory.INSTANCE.newBot(configEntry.getQid(),configEntry.getPassword(), configuration);
        this.bot.login();
        this.bot.getEventChannel().subscribeAlways(net.mamoe.mirai.event.Event.class,this::processEvent);
        this.connected.set(true);
    }

    /**
     * 获取当前机器人的配置实例
     * @return 配置实例
     */
    public BotConfigEntry getConfigEntry(){
        return this.configEntry;
    }

    /**
     * 初始化缓存文件夹
     * @param configuration 机器人配置
     * @param entry 机器人配置实例
     */
    private void doInitCacheFolder(BotConfiguration configuration,BotConfigEntry entry){
        File folder = new File(DATA_FOLDER,"caches-"+entry.getQid());
        if (!folder.exists()){
            folder.mkdir();
        }
        configuration.setCacheDir(folder);
    }

    /**
     * 处理事件的
     * @param event 当前发生的事件
     */
    public abstract void processEvent(Event event);

    /**
     * 获取当前的机器人
     * @return Mirai形式的机器人
     */
    public Bot getBot(){
        return this.bot;
    }

    /**
     * 获取当前机器人是否已经连接到服务器
     * @return 是否已经连接到服务器
     */
    public boolean isConnected(){
        return this.connected.get();
    }

    /**
     * 获取当前机器人的QID
     * @return QID
     */
    public long getCurrentQid(){
        return this.configEntry.getQid();
    }
}
