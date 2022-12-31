package org.ncc.github.nqbot.commands;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.ncc.github.nqbot.utils.Utils;

import java.io.ByteArrayInputStream;

public class RandomPic2Command implements Command{
    @Override
    public String getHead() {
        return "rpic2";
    }

    @Override
    public void process(String[] args, Bot bot, Group target, GroupMessageEvent event) {
        byte[] data = Utils.getBytes("http://api.iw233.cn/api.php?sort=random");
        Contact.sendImage(target,new ByteArrayInputStream(data));
    }
}
