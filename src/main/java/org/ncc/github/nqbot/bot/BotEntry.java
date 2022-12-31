package org.ncc.github.nqbot.bot;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.utils.BotConfiguration;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public BotConfigEntry getConfigEntry(){
        return this.configEntry;
    }

    private void doInitCacheFolder(BotConfiguration configuration,BotConfigEntry entry){
        File folder = new File(DATA_FOLDER,"caches-"+entry.getQid());
        if (!folder.exists()){
            folder.mkdir();
        }
        configuration.setCacheDir(folder);
    }

    public abstract void processEvent(Event event);

    public Bot getBot(){
        return this.bot;
    }

    public boolean isConnected(){
        return this.connected.get();
    }

    public long getCurrentQid(){
        return this.configEntry.getQid();
    }
}
