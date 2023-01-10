package org.ncc.github.nqbot.commands.tempchat;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.GroupTempMessageEvent;
import org.ncc.github.nqbot.jssupport.JavaScriptGroupCommandLoader;
import org.ncc.github.nqbot.manager.ConfigManager;
import org.ncc.github.nqbot.commands.PackagedCommandArg;

public class ReloadTempChatCommand implements GroupTempCommand{
    @Override
    public String getHead() {
        return "reload";
    }

    @Override
    public void process(PackagedCommandArg args, Bot bot, NormalMember target, GroupTempMessageEvent event) {
        if (event.getSender().getId() == ConfigManager.CONFIG_FILE_READ.getMasterName()){
            JavaScriptGroupCommandLoader.reload();
        }
    }
}
