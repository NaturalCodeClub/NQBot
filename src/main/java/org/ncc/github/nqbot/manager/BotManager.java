package org.ncc.github.nqbot.manager;

import com.google.gson.Gson;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.utils.BotConfiguration;
import org.bukkit.Bukkit;
import org.ncc.github.nqbot.bot.BotConfigEntry;
import org.ncc.github.nqbot.bot.BotEntry;
import org.ncc.github.nqbot.eventsystem.EventHub;
import org.ncc.github.nqbot.processors.CommandProcessor;
import org.ncc.github.nqbot.utils.Utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BotManager {
    public static final List<BotConfigEntry> botEntries = new Vector<>();
    public static final List<Bot> bots = new Vector<>();
    public static final Set<BotEntry> entries = ConcurrentHashMap.newKeySet();
    public static final Logger LOGGER = Bukkit.getLogger();
    public static final MessageManager multiSender= new MessageManager();

    public static void init(){
        try {
            final File configFolder = new File(Utils.getDataFolder(),"configs");
            if (!configFolder.exists()){
                configFolder.mkdir();
            }
            File file = new File(configFolder,"bots.json");
            checkAndCreateConfigFile(file);
            if(file.exists()){
                try(FileInputStream stream = new FileInputStream(file)){
                    byte[] buffer = Utils.readInputStreamToByte(stream);
                    BotConfigEntryArray array = BotConfigEntryArray.botConfigEntryArrayFromString(new String(buffer));
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
                                    bots.add(value1.getBot());
                                    entries.add(value1);
                                    botEntries.add(value1.getConfigEntry());
                                }else{
                                    cause.printStackTrace();
                                }
                            })).toArray(CompletableFuture[]::new)).join();
                }
                multiSender.doInit(entries);
            }
        }catch (Exception e){
            LOGGER.log(Level.SEVERE,"Error in loading bots",e);
        }
    }

    private static void checkAndCreateConfigFile(File file) throws IOException {
        if (!file.exists()) {
            LOGGER.warning("Bot config not found!Exiting..");
            file.createNewFile();
            BotConfigEntryArray array = new BotConfigEntryArray();
            array.entries = new BotConfigEntry[1];
            array.entries[0] = new BotConfigEntry(114514,"114514", BotConfiguration.MiraiProtocol.ANDROID_PAD);
            try(FileOutputStream fos = new FileOutputStream(file)){
                fos.write(array.getJson().getBytes());
                fos.flush();
            }catch (Exception e){
                e.printStackTrace();
            }
            Bukkit.shutdown();
        }
    }

    private static class BotConfigEntryArray{
        private static final Gson GSON = new Gson();
        public BotConfigEntry[] entries;

        public String getJson(){
            return GSON.toJson(this);
        }

        public static BotConfigEntryArray botConfigEntryArrayFromString(String jsonIn){
            return GSON.fromJson(jsonIn,BotConfigEntryArray.class);
        }
    }
}
