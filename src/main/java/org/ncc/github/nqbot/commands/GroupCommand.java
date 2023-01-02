package org.ncc.github.nqbot.commands;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;

/**
 * 群命令接口
 */
public interface GroupCommand {
    public String getHead();
    public void process(String[] args, Bot bot, Group target, GroupMessageEvent event);
}
