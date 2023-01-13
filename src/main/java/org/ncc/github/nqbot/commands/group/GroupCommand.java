package org.ncc.github.nqbot.commands.group;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.ncc.github.nqbot.commands.PackagedCommandInfo;

/**
 * 群命令接口
 */
public interface GroupCommand {
    public String getHead();
    public void process(PackagedCommandInfo args, Bot bot, Group target, GroupMessageEvent event);
}
