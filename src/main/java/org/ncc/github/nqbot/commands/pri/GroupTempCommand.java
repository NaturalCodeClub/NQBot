package org.ncc.github.nqbot.commands.pri;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.GroupTempMessageEvent;

public interface GroupTempCommand {
    public String getHead();
    public void process(String[] args, Bot bot, NormalMember target, GroupTempMessageEvent event);
}
