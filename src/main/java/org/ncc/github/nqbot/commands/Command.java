package org.ncc.github.nqbot.commands;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.ncc.github.nqbot.manager.BotManager;
import org.ncc.github.nqbot.manager.MessageManager;

public interface Command {
    MessageManager multiSender = BotManager.multiSender;
    public String getHead();
    public void process(String[] args, Bot bot, Group target, GroupMessageEvent event);
}
