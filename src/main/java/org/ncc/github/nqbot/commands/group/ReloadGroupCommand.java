package org.ncc.github.nqbot.commands.group;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.ncc.github.nqbot.jssupport.JavaScriptGroupCommandLoader;
import org.ncc.github.nqbot.manager.ConfigManager;
import org.ncc.github.nqbot.commands.PackagedCommandInfo;

public class ReloadGroupCommand implements GroupCommand {
    @Override
    public String getHead() {
        return "reload";
    }

    @Override
    public void process(PackagedCommandInfo args, Bot bot, Group target, GroupMessageEvent event) {
        if (event.getSender().getId() == ConfigManager.CONFIG_FILE_READ.getMasterName()){
            JavaScriptGroupCommandLoader.reload();
        }
    }
}
