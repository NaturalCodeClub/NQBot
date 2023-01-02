package org.ncc.github.nqbot.commands.pri;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.event.events.FriendMessageEvent;

public interface PrivateCommand {
    public String getHead();
    public void process(String[] args, Bot bot, Friend target, FriendMessageEvent event);
}
