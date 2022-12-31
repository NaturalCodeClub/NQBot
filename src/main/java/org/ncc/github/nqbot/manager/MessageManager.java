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

public class MessageManager {
    private final Deque<Map<Long,Message>> messageSendQueue = new ConcurrentLinkedDeque<>();
    private final Map<Thread, Queue<Map<Long,Message>>> workerSpace = new ConcurrentHashMap<>();
    private final AtomicBoolean pollPos = new AtomicBoolean(true);

    public void doInit(Set<BotEntry> bots){
        for (BotEntry botEntry : bots){
            Thread pollWorker = new Thread(()->{
                Map<Long, Message> message;
                final boolean pollPosCP = pollPos.get();
                while (true){
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
            },"Message-Poller-"+botEntry.getCurrentQid());
            pollWorker.setDaemon(true);
            workerSpace.put(pollWorker,new ConcurrentLinkedQueue<>());
            pollWorker.start();
            pollPos.set(!pollPos.get());
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
