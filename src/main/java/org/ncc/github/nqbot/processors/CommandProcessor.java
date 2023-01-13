package org.ncc.github.nqbot.processors;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.GroupTempMessageEvent;
import org.bukkit.Bukkit;
import org.ncc.github.nqbot.commands.PackagedCommandInfo;
import org.ncc.github.nqbot.commands.friend.FriendCommand;
import org.ncc.github.nqbot.commands.group.GroupCommand;
import org.ncc.github.nqbot.commands.tempchat.GroupTempCommand;
import org.ncc.github.nqbot.jssupport.JavaScriptGroupCommandLoader;
import org.ncc.github.nqbot.manager.CommandManager;
import org.ncc.github.nqbot.manager.ConfigManager;

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
        Thread worker = new Thread(task, "nqbot-pool-worker-" + threadCounter.getAndIncrement());
        worker.setDaemon(true);
        worker.setPriority(3);
        return worker;
    });

    /**
     * 异步处理一个群消息事件
     *
     * @param event 事件
     */
    public static void processGroupCommandAsync(GroupMessageEvent event) {
        processor.execute(() -> {
            if (currentListener.get() == null || !currentListener.get().isOnline()) {
                currentListener.set(event.getBot());
            }
            if (currentListener.get() != event.getBot()) {
                return;
            }
            if (event.getGroup().getId() == ConfigManager.CONFIG_FILE_READ.getListeningGroup()) {
                fireGroupCommandProcess(event);
            }
        });
    }

    public static void processPrivateCommandAsync(FriendMessageEvent event) {
        processor.execute(() -> fireFiendCommandProcess(event));
    }

    public static void processGroupTempCommandAsync(GroupTempMessageEvent event) {
        processor.execute(() -> fireGroupTempChatMessageProcess(event));
    }

    /**
     * 事件会在这里判断是不是命令并进一步处理
     *
     * @param event
     */
    private static void fireGroupCommandProcess(GroupMessageEvent event) {
        final PackagedCommandInfo newArg = new PackagedCommandInfo(event.getMessage());
        final String commandHead = newArg.getCommandHead();
        for (GroupCommand command : CommandManager.REGISTED_GROUP_COMMANDS) {
            checkAndCallGroupCommand(command, commandHead, event, newArg);
        }
        for (GroupCommand command : JavaScriptGroupCommandLoader.getRegistedJSCommands()) {
            checkAndCallGroupCommand(command, commandHead, event, newArg);
        }
    }

    private static void fireFiendCommandProcess(FriendMessageEvent event) {
        final PackagedCommandInfo newArg = new PackagedCommandInfo(event.getMessage());
        final String commandHead = newArg.getCommandHead();
        for (FriendCommand command : CommandManager.REGISTED_FRIEND_COMMANDS) {
            checkAndCall(command, commandHead, event, newArg);
        }
    }

    private static void fireGroupTempChatMessageProcess(GroupTempMessageEvent event) {
        final PackagedCommandInfo newArg = new PackagedCommandInfo(event.getMessage());
        final String commandHead = newArg.getCommandHead();
        for (GroupTempCommand command : CommandManager.REGISTED_GROUP_TEMP_COMMANDS) {
            checkAndCallGroupTempCommand(command, commandHead, event, newArg);
        }
    }

    /**
     * 检查并调用指定的命令
     *
     * @param friendCommand 命令的实例
     * @param commandHead   命令头，用于检索
     * @param event         当前好友事件
     * @param args          后缀
     */
    private static void checkAndCall(FriendCommand friendCommand, String commandHead, FriendMessageEvent event, PackagedCommandInfo args) {
        if (friendCommand.getHead().equalsIgnoreCase(commandHead.substring(1))) {
            logger.info(String.format("Command caught:%s Args:%s", friendCommand.getHead(), args.toString()));
            try {
                friendCommand.process(args, event.getBot(), event.getSender(), event);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error in processing Message!");
                logger.log(Level.SEVERE, e.getMessage());
                event.getSender().sendMessage("Error in processing message!");
                event.getSender().sendMessage(e.getMessage());
            }
        }
    }

    /**
     * 检查并调用指定的命令
     *
     * @param groupCommand 命令的实例
     * @param commandHead  命令头，用于检索
     * @param event        当前群事件
     * @param args         后缀
     */
    private static void checkAndCallGroupCommand(GroupCommand groupCommand, String commandHead, GroupMessageEvent event, PackagedCommandInfo args) {
        if (groupCommand.getHead().equalsIgnoreCase(commandHead.substring(1)) && event.getGroup().getId() == ConfigManager.CONFIG_FILE_READ.getListeningGroup()) {
            logger.info(String.format("Command caught:%s Args:%s", groupCommand.getHead(), args.toString()));
            try {
                groupCommand.process(args, event.getBot(), event.getGroup(), event);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error in processing Message!");
                logger.log(Level.SEVERE, e.getMessage());
                event.getGroup().sendMessage("Error in processing message!");
                event.getGroup().sendMessage(e.getMessage());
            }
        }
    }

    /**
     * 检查并调用指定的指令
     *
     * @param g           命令的实例
     * @param commandHead 命令头
     * @param event       临时会话事件
     * @param args        后缀
     */
    private static void checkAndCallGroupTempCommand(GroupTempCommand g, String commandHead, GroupTempMessageEvent event, PackagedCommandInfo args) {
        if (g.getHead().equalsIgnoreCase(commandHead.substring(1)) && event.getGroup().getId() == ConfigManager.CONFIG_FILE_READ.getListeningGroup()) {
            logger.info(String.format("Command caught:%s Args:%s", g.getHead(), args.toString()));
            try {
                g.process(args, event.getBot(), event.getSender(), event);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error in processing Message!");
                logger.log(Level.SEVERE, e.getMessage());
                event.getSender().sendMessage("Error in processing message!");
                event.getSender().sendMessage(e.getMessage());
            }
        }
    }
}
