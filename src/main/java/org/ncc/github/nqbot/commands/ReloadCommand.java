package org.ncc.github.nqbot.commands;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.ncc.github.nqbot.manager.ConfigManager;
import org.ncc.github.nqbot.manager.JavaScriptCommandManager;

public class ReloadCommand implements Command{
    @Override
    public String getHead() {
        return "reload";
    }

    @Override
    public void process(String[] args, Bot bot, Group target, GroupMessageEvent event) {
        if (event.getSender().getId() == ConfigManager.CONFIG_FILE_READ.getMasterName()){
            JavaScriptCommandManager.reload();
        }
    }
}
