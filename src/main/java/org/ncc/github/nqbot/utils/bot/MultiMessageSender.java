package org.ncc.github.nqbot.utils.bot;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageContent;
import org.bukkit.Bukkit;
import org.ncc.github.nqbot.bot.BotEntry;
import org.ncc.github.nqbot.utils.CoreUtils;

import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * 多机器人消息发送器，这里面的消息不会按顺序发送，请注意使用
 */
public class MultiMessageSender {
    private final Deque<Map<Long,Message>> messageSendQueue = new ConcurrentLinkedDeque<>();
    private final AtomicBoolean pollPos = new AtomicBoolean(true);

    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final AtomicInteger activeCount = new AtomicInteger();

    /**
     * 关闭当前的发送器
     */
    public void shutdown(){
        this.isRunning.set(false);
        while (this.activeCount.get() > 0){
            LockSupport.parkNanos(this,1);
        }
        this.messageSendQueue.clear();
        this.pollPos.set(true);
    }

    /**
     * 初始化发送器
     * @param bots 指定的机器人
     */
    public void doInit(Collection<BotEntry> bots){
        this.isRunning.set(true);
        for (BotEntry botEntry : bots){
            Bukkit.getScheduler().runTaskAsynchronously(CoreUtils.getPlugin(),()->{
                final boolean pollPosCP = this.pollPos.get();
                while (this.isRunning.get()){
                    try{
                        Map<Long, Message> message;
                        if (pollPosCP){
                            while ((message = this.messageSendQueue.pollFirst())!=null){
                                message.forEach((gid,OMessage)->{
                                    Group gp =  botEntry.getBot().getGroup(gid);
                                    if (gp!=null){
                                        gp.sendMessage(OMessage);
                                    }
                                });
                            }
                        }else{
                            while ((message = this.messageSendQueue.pollLast())!=null){
                                message.forEach((gid,OMessage)->{
                                    Group gp =  botEntry.getBot().getGroup(gid);
                                    if (gp!=null){
                                        gp.sendMessage(OMessage);
                                    }
                                });
                            }
                        }
                        Thread.sleep(100);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                this.activeCount.getAndDecrement();
            });
            this.activeCount.getAndIncrement();
            this.pollPos.set(!pollPos.get());
        }
    }

    /**
     * 发送一个消息
     * @param message 消息
     * @param gid 群的ID
     */
    public void send(Message message,long gid){
        final HashMap<Long,Message> task = new HashMap<>();
        task.put(gid,message);
        this.messageSendQueue.add(task);
    }

    /**
     * 发送一个消息
     * @param message 消息
     * @param gid 群的ID
     */
    public void send(String message,long gid){
        MessageContent messageContent = () -> message;
        this.send(messageContent,gid);
    }
}
