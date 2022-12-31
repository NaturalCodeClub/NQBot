package org.ncc.github.nqbot.commands;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;

public class TestCommand implements Command{
    @Override
    public String getHead() {
        return "test";
    }

    @Override
    public void process(String[] args, Bot bot, Group target, GroupMessageEvent event) {
        multiSender.send("yeeeeeeeeeeeeeee", target.getId());
    }
}
