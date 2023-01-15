package org.ncc.github.nqbot.commands.group;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.ncc.github.nqbot.commands.PackagedCommandInfo;

public class BindGroupCommand implements GroupCommand{
    @Override
    public String getHead() {
        return "bind";
    }

    @Override
    public void process(PackagedCommandInfo args, Bot bot, Group target, GroupMessageEvent event) {

    }
}
