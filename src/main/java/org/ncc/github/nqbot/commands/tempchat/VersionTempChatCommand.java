package org.ncc.github.nqbot.commands.tempchat;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.GroupTempMessageEvent;
import org.bukkit.Bukkit;
import org.ncc.github.nqbot.commands.PackagedCommandInfo;
import org.ncc.github.nqbot.utils.InfoUtils;

public class VersionTempChatCommand implements GroupTempCommand{
    @Override
    public String getHead() {
        return null;
    }

    @Override
    public void process(PackagedCommandInfo args, Bot bot, NormalMember target, GroupTempMessageEvent event) {
        final String name = event.getSenderName();
        final String message = "Hi!" + name + "\nServer Version:" + Bukkit.getVersion() + "\nNQBot Version:" + InfoUtils.version;
        event.getSender().sendMessage(message);
    }
}
