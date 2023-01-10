package org.ncc.github.nqbot.commands.friend;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import org.bukkit.Bukkit;
import org.ncc.github.nqbot.commands.PackagedCommandArg;
import org.ncc.github.nqbot.utils.InfoUtils;

public class VersionFriendCommand implements FriendCommand{
    @Override
    public String getHead() {
        return "version";
    }

    @Override
    public void process(PackagedCommandArg args, Bot bot, Friend target, FriendMessageEvent event) {
        final String name = event.getSenderName();
        final String message = "Hi!" + name + "\n" + "Server Version:" + Bukkit.getVersion() + "\nNQBot Version:" + InfoUtils.version;
        event.getSender().sendMessage(message);
    }
}
