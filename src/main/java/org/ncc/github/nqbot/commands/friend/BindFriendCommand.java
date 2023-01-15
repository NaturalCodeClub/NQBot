package org.ncc.github.nqbot.commands.friend;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import org.ncc.github.nqbot.commands.PackagedCommandInfo;

public class BindFriendCommand implements FriendCommand{
    @Override
    public String getHead() {
        return "bind";
    }

    @Override
    public void process(PackagedCommandInfo args, Bot bot, Friend target, FriendMessageEvent event) {

    }
}
