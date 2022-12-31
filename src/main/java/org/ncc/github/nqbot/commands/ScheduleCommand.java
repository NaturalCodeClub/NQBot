package org.ncc.github.nqbot.commands;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.bukkit.Bukkit;
import org.ncc.github.nqbot.utils.StringUtil;
import org.ncc.github.nqbot.manager.BotManager;
import org.ncc.github.nqbot.manager.ConfigManager;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScheduleCommand implements Command{
    private static final Logger logger = Bukkit.getLogger();
    private static final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors());

    @Override
    public String getHead() {
        return "schedule";
    }

    @Override
    public void process(String[] args, Bot bot, Group target, GroupMessageEvent event) {
        if (event.getSender().getId() == ConfigManager.CONFIG_FILE_READ.getMasterName()){
            if (args.length == 1 && args[0].equals("stop")){
                executor.getQueue().clear();
                logger.info("Scheduled tasks cleared");
                return;
            }
            if (args.length < 3 || !StringUtil.isNumeric(args[0])){
                target.sendMessage("Invalid args.Please use #schedule <delay> <parallelCount> <string>...");
                return;
            }
            try{
                int pCount = Integer.parseInt(args[1]);
                int schtime = Integer.parseInt(args[0]);
                String message = args[2];
                if ((args.length-2)>1){
                    String[] messages = new String[args.length-2];
                    System.arraycopy(args, 2, messages, 0, args.length - 2);
                    StringBuilder builder = new StringBuilder();
                    for (String s : messages){
                        builder.append(s);
                    }
                    message = builder.toString();
                }
                String finalMessage = message;
                executor.scheduleAtFixedRate(()->{
                    try{
                        int counter = 0;
                        for (Bot bot1 : BotManager.bots) {
                            counter++;
                            bot1.getGroup(target.getId()).sendMessage(finalMessage);
                            if (counter >= pCount){
                                break;
                            }
                        }
                    }catch (Exception e){
                        logger.log(Level.SEVERE,"Error in sending scheduled message");
                        target.sendMessage(e.getMessage());
                    }
                },0,schtime, TimeUnit.SECONDS);
            }catch (Exception e){
                logger.log(Level.SEVERE,"An error caught in processing args and command",e);
            }
        }
    }
}
