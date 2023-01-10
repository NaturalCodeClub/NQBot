package org.ncc.github.nqbot.commands.tempchat;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.GroupTempMessageEvent;
import org.ncc.github.nqbot.commands.PackagedCommandArg;

public interface GroupTempCommand {
    public String getHead();
    public void process(PackagedCommandArg args, Bot bot, NormalMember target, GroupTempMessageEvent event);
}
