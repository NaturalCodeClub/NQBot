package org.ncc.github.nqbot.commands;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.bukkit.Bukkit;
import org.ncc.github.nqbot.utils.Utils;

public class VersionCommand implements Command{
    @Override
    public String getHead() {
        return "version";
    }

    @Override
    public void process(String[] args, Bot bot, Group target, GroupMessageEvent event) {
        String message = event.getSender().getNick() + "\n NQBot version:" + Utils.version + "\n Server version:" +
                Bukkit.getVersion();
        target.sendMessage(message);
    }
}
