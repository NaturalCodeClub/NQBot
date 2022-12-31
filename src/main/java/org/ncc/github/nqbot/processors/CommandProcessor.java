package org.ncc.github.nqbot.processors;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import org.bukkit.Bukkit;
import org.ncc.github.nqbot.commands.Command;
import org.ncc.github.nqbot.manager.CommandManager;
import org.ncc.github.nqbot.manager.ConfigManager;
import org.ncc.github.nqbot.manager.JavaScriptCommandManager;

import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

public class CommandProcessor {
    private static final Logger logger = Bukkit.getLogger();
    private static final AtomicReference<Bot> currentListener = new AtomicReference<>();
    private static final AtomicInteger threadCounter = new AtomicInteger();
    public static final Executor processor = Executors.newCachedThreadPool(task -> {
        Thread worker = new Thread(task,"fqbot-pool-worker-"+threadCounter.getAndIncrement());
        worker.setDaemon(true);
        worker.setPriority(3);
        return worker;
    });

    public static void processAsync(GroupMessageEvent event){
        processor.execute(()->{
            if (currentListener.get()==null || !currentListener.get().isOnline()){
                currentListener.set(event.getBot());
            }
            if (currentListener.get()!=event.getBot()){
                return;
            }
            logger.info(String.format("[Chat][%s][%s(%s)]%s",event.getGroup().getName(),event.getSender().getNick(),event.getSender().getId(),event.getMessage()));
            fireProcessMessage(event);
        });
    }

    private static void fireProcessMessage(GroupMessageEvent event){
        final MessageChain message = event.getMessage();
        final String messageString = message.get(1).contentToString();

        final String[] fixed = messageString.split(" ");
        if (fixed.length>=1){
            final String commandHead = fixed[0];
            final String[] args = new String[fixed.length-1];
            System.arraycopy(fixed, 1, args, 0, fixed.length - 1);
            if (commandHead.startsWith("#") && commandHead.length() > 1){
                for (Command command : CommandManager.registedSystemCommands){
                    checkAndCall(command,commandHead,event,args);
                }
                for (Command jsCommand : JavaScriptCommandManager.getRegistedJSCommands()){
                    checkAndCall(jsCommand,commandHead,event,args);
                }
            }
        }
    }

    private static void checkAndCall(Command command,String commandHead,GroupMessageEvent event,String[] args){
        if (command.getHead().equals(commandHead.substring(1)) && event.getGroup().getId() == ConfigManager.CONFIG_FILE_READ.getListeningGroup()){
            logger.info(String.format("Command caught:%s Args:%s",command.getHead(), Arrays.toString(args)));
            try{
                command.process(args,event.getBot(),event.getGroup(),event);
            }catch (Exception e){
                event.getGroup().sendMessage("Error in processing message!");
                event.getGroup().sendMessage(e.getMessage());
            }
        }
    }
}
