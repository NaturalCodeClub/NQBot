package org.ncc.github.nqbot.commands.friend;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import org.ncc.github.nqbot.jssupport.JavaScriptGroupCommandLoader;
import org.ncc.github.nqbot.manager.ConfigManager;

public class ReloadFriendCommand implements FriendCommand{
    @Override
    public String getHead() {
        return "reload";
    }

    @Override
    public void process(String[] args, Bot bot, Friend target, FriendMessageEvent event) {
        if (event.getSender().getId() == ConfigManager.CONFIG_FILE_READ.getMasterName()){
            JavaScriptGroupCommandLoader.reload();
        }
    }
}
