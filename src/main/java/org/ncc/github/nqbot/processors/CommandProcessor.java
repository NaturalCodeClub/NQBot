package org.ncc.github.nqbot.processors;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.GroupTempMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import org.bukkit.Bukkit;
import org.ncc.github.nqbot.commands.group.GroupCommand;
import org.ncc.github.nqbot.commands.pri.GroupTempCommand;
import org.ncc.github.nqbot.commands.pri.PrivateCommand;
import org.ncc.github.nqbot.jssupport.JavaScriptGroupCommandLoader;
import org.ncc.github.nqbot.manager.CommandManager;
import org.ncc.github.nqbot.manager.ConfigManager;

import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 用于处理所有已经捕获到的命令，所有的命令都是异步处理的
 * 所以请注意线程安全:)
 */
public class CommandProcessor {
    private static final Logger logger = Bukkit.getLogger();
    private static final AtomicReference<Bot> currentListener = new AtomicReference<>();
    private static final AtomicInteger threadCounter = new AtomicInteger();
    public static final Executor processor = Executors.newCachedThreadPool(task -> {
        Thread worker = new Thread(task,"nqbot-pool-worker-"+threadCounter.getAndIncrement());
        worker.setDaemon(true);
        worker.setPriority(3);
        return worker;
    });

    /**
     * 异步处理一个群消息事件
     * @param event 事件
     */
    public static void processGroupCommandAsync(GroupMessageEvent event){
        processor.execute(()->{
            if (currentListener.get()==null || !currentListener.get().isOnline()){
                currentListener.set(event.getBot());
            }
            if (currentListener.get()!=event.getBot()){
                return;
            }
            fireGroupProcessMessage(event);
        });
    }

    public static void processPrivateCommandAsync(FriendMessageEvent event){
        processor.execute(()-> firePrivateProcessMessage(event));
    }
    public static void processGroupTempCommandAsync(GroupTempMessageEvent event){
        processor.execute(()->fireGroupTempProcessMessage(event));
    }

    /**
     * 事件会在这里判断是不是命令并进一步处理
     * @param event
     */
    private static void fireGroupProcessMessage(GroupMessageEvent event){
        final MessageChain message = event.getMessage();
        final String messageString = message.get(1).contentToString();

        final String[] fixed = messageString.split(" ");
        if (fixed.length>=1){
            final String commandHead = fixed[0];
            final String[] args = new String[fixed.length-1];
            System.arraycopy(fixed, 1, args, 0, fixed.length - 1);
            if (commandHead.startsWith("#") && commandHead.length() > 1){
                for (GroupCommand groupCommand : CommandManager.REGISTED_GROUP_COMMANDS){
                    checkAndCallGrouply(groupCommand,commandHead,event,args);
                }
                for (GroupCommand jsGroupCommand : JavaScriptGroupCommandLoader.getRegistedJSCommands()){
                    checkAndCallGrouply(jsGroupCommand,commandHead,event,args);
                }
            }
        }
    }

    private static void firePrivateProcessMessage(FriendMessageEvent event){
        final MessageChain message = event.getMessage();
        final String messageString = message.get(1).contentToString();

        final String[] fixed = messageString.split(" ");
        if (fixed.length>=1){
            final String commandHead = fixed[0];
            final String[] args = new String[fixed.length-1];
            System.arraycopy(fixed, 1, args, 0, fixed.length - 1);
            if (commandHead.startsWith("#") && commandHead.length() > 1){
                for (PrivateCommand privateCommand : CommandManager.REGISTED_PRIVATE_COMMANDS){
                    checkAndCall(privateCommand,commandHead,event,args);
                }
            }
        }
    }
    private static void fireGroupTempProcessMessage(GroupTempMessageEvent event){
        final MessageChain messages = event.getMessage();
        final String messageString = messages.get(1).contentToString();

        final String[] fixed = messageString.split(" ");
        if(fixed.length>=1){
            final String commandHead = fixed[0];
            final String[] args = new String[fixed.length-1];
            System.arraycopy(fixed,1,args,0,fixed.length - 1);
            if(commandHead.startsWith("#") && commandHead.length()>1){
                for(GroupTempCommand g: CommandManager.REGISTED_GROUP_TEMP_COMMANDS){
                    checkAndCallGroupTemp(g,commandHead,event,args);
                }
            }
        }

    }

    /**
     * 检查并调用指定的命令
     * @param privateCommand 命令的实例
     * @param commandHead 命令头，用于检索
     * @param event 当前好友事件
     * @param args 后缀
     */
    private static void checkAndCall(PrivateCommand privateCommand, String commandHead, FriendMessageEvent event, String[] args){
        if (privateCommand.getHead().equalsIgnoreCase(commandHead.substring(1))){
            logger.info(String.format("Command caught:%s Args:%s", privateCommand.getHead(), Arrays.toString(args)));
            try{
                privateCommand.process(args,event.getBot(),event.getSender(),event);
            }catch (Exception e){
                logger.log(Level.SEVERE,"Error in processing Message!");
                logger.log(Level.SEVERE,e.getMessage());
                event.getSender().sendMessage("Error in processing message!");
                event.getSender().sendMessage(e.getMessage());
            }
        }
    }

    /**
     * 检查并调用指定的命令
     * @param groupCommand 命令的实例
     * @param commandHead 命令头，用于检索
     * @param event 当前群事件
     * @param args 后缀
     */
    private static void checkAndCallGrouply(GroupCommand groupCommand, String commandHead, GroupMessageEvent event, String[] args){
        if (groupCommand.getHead().equalsIgnoreCase(commandHead.substring(1)) && event.getGroup().getId() == ConfigManager.CONFIG_FILE_READ.getListeningGroup()){
            logger.info(String.format("Command caught:%s Args:%s", groupCommand.getHead(), Arrays.toString(args)));
            try{
                groupCommand.process(args,event.getBot(),event.getGroup(),event);
            }catch (Exception e){
                logger.log(Level.SEVERE,"Error in processing Message!");
                logger.log(Level.SEVERE,e.getMessage());
                event.getGroup().sendMessage("Error in processing message!");
                event.getGroup().sendMessage(e.getMessage());
            }
        }
    }
    /**
    * 检查并调用指定的指令
    * @param g 命令的实例
     * @param commandHead 命令头
     * @param event 临时会话事件
     * @param args 后缀
    */
    private static void checkAndCallGroupTemp(GroupTempCommand g,String commandHead,GroupTempMessageEvent event,String[] args){
        if(g.getHead().equalsIgnoreCase(commandHead.substring(1))&&event.getGroup().getId() == ConfigManager.CONFIG_FILE_READ.getListeningGroup()){
            logger.info(String.format("Command caught:%s Args:%s",g.getHead(),Arrays.toString(args)));
            try{
                g.process(args,event.getBot(),event.getSender(),event);
            }
            catch (Exception e){
                logger.log(Level.SEVERE,"Error in processing Message!");
                logger.log(Level.SEVERE,e.getMessage());
                event.getSender().sendMessage("Error in processing message!");
                event.getSender().sendMessage(e.getMessage());
            }
        }

    }
}
