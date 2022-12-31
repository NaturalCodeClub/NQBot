package org.ncc.github.nqbot.commands;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.ForwardMessage;
import net.mamoe.mirai.message.data.Image;
import org.ncc.github.nqbot.manager.BotDataManager;
import org.ncc.github.nqbot.processors.CommandProcessor;
import org.ncc.github.nqbot.utils.Utils;
import org.ncc.github.nqbot.utils.Woc2Util;
import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Phaser;

public class Random3PicCommand implements Command{
    private final Queue<String> lastWocUrls = new ConcurrentLinkedQueue<>();

    @Override
    public String getHead() {
        return "rpic3";
    }

    @Override
    public void process(String[] args, Bot bot, Group target, GroupMessageEvent event) {
       //自动装弹(bushi)
       if (this.lastWocUrls.isEmpty()){
           synchronized (BotDataManager.currentData){
               BotDataManager.currentData.rpic3Id++;
               BotDataManager.saveCurrent();
               this.lastWocUrls.addAll(Objects.requireNonNull(Woc2Util.getNewWocPicList(BotDataManager.currentData.rpic3Id)));
           }
       }

       final List<ForwardMessage.Node> nodes = getNewNodes(event.getSender(),Integer.MAX_VALUE/4,target);
       final List<String> priview = new ArrayList<>();
       for (int i = 0; i < nodes.size(); i++) {
           priview.add(event.getSender().getNameCard()+":"+"嗯哼哼哼啊啊啊啊啊啊啊啊啊啊啊啊啊啊");
       }
       ForwardMessage message = new ForwardMessage(priview,"Yee","Yee","Yee","Yee",nodes);
       target.sendMessage(message);
    }

    private List<ForwardMessage.Node> getNewNodes(Member sender, int time, Group target){
        final List<ForwardMessage.Node> nodes = new CopyOnWriteArrayList<>();
        final Phaser waiter = new Phaser();
        waiter.register();
        for (int i = 0; i < 6 && !this.lastWocUrls.isEmpty(); i++) {
            waiter.register();
            CommandProcessor.processor.execute(()->{
                try {
                    byte[] bytes = Utils.getBytes(this.lastWocUrls.poll());
                    final Image messgae = Contact.uploadImage(target,new ByteArrayInputStream(bytes));
                    nodes.add(new ForwardMessage.Node(sender.getId(),time,sender.getNameCard(),messgae));
                }finally {
                    waiter.arriveAndDeregister();
                }
            });
        }
        waiter.arriveAndAwaitAdvance();
        return nodes;
    }
}
