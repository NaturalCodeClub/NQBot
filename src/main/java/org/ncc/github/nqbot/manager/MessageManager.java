package org.ncc.github.nqbot.manager;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageContent;
import org.ncc.github.nqbot.bot.BotEntry;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public class MessageManager {
    private final Deque<Map<Long,Message>> messageSendQueue = new ConcurrentLinkedDeque<>();
    private final Map<Thread, Queue<Map<Long,Message>>> workerSpace = new ConcurrentHashMap<>();
    private final AtomicBoolean pollPos = new AtomicBoolean(true);

    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicInteger runningThreadCount = new AtomicInteger();

    public void shutdown(){
        this.running.set(false);
        while (runningThreadCount.get() > 0){
            LockSupport.parkNanos(this,1);
        }
        messageSendQueue.clear();
        workerSpace.clear();
        pollPos.set(true);
    }

    public void doInit(Set<BotEntry> bots){
        this.running.set(true);
        for (BotEntry botEntry : bots){
            Thread pollWorker = new Thread(()->{
                Map<Long, Message> message;
                final boolean pollPosCP = pollPos.get();
                while (this.running.get()){
                    try{
                        final Queue<Map<Long,Message>> selfTasks = workerSpace.get(Thread.currentThread());
                        while ((message = selfTasks.poll())!=null){
                            message.forEach((gid,OMessage)->{
                                Group gp =  botEntry.getBot().getGroup(gid);
                                if (gp!=null){
                                    gp.sendMessage(OMessage);
                                }
                            });
                        }

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
                this.runningThreadCount.getAndDecrement();
            },"Message-Poller-"+botEntry.getCurrentQid());
            pollWorker.setDaemon(true);
            this.workerSpace.put(pollWorker,new ConcurrentLinkedQueue<>());
            pollWorker.start();
            this.runningThreadCount.getAndIncrement();
            this.pollPos.set(!pollPos.get());
        }
    }

    public void send(Message message,long gid){
        final HashMap<Long,Message> task = new HashMap<>();
        task.put(gid,message);
        this.messageSendQueue.add(task);
    }

    public void send(String message,long qid){
        MessageContent messageContent = () -> message;
        this.send(messageContent,qid);
    }
}
