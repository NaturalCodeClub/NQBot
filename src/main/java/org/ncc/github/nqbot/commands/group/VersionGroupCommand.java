package org.ncc.github.nqbot.commands.group;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.bukkit.Bukkit;
import org.ncc.github.nqbot.commands.PackagedCommandArg;
import org.ncc.github.nqbot.utils.InfoUtils;

public class VersionGroupCommand implements GroupCommand{
    @Override
    public String getHead() {
        return "version";
    }

    @Override
    public void process(PackagedCommandArg args, Bot bot, Group target, GroupMessageEvent event) {
        final String name = event.getSenderName();
        final String message = "Hi!" + name + "\nServer Version:" + Bukkit.getVersion() + "\nNQBot Version" + InfoUtils.version;
        event.getGroup().sendMessage(message);
    }
}
