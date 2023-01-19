package org.ncc.github.nqbot.commands.group;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.ncc.github.nqbot.bind.Bind;
import org.ncc.github.nqbot.commands.PackagedCommandInfo;

import static org.ncc.github.nqbot.data.BindFile.tempMap;

public class BindGroupCommand implements GroupCommand{
    @Override
    public String getHead() {
        return "bind";
    }

    @Override
    public void process(PackagedCommandInfo args, Bot bot, Group target, GroupMessageEvent event) {
        String s = "#bind ";
        String name = event.getMessage().contentToString().substring(s.length()).trim();
        Long id = event.getSender().getId();
        if(tempMap.get(id).equals(name)){
            target.sendMessage("您已请求过绑定");
            return;
        }
        if(!tempMap.get(id).isEmpty()){
            target.sendMessage("您已请求过绑定其他玩家！");
            return;
        }
        tempMap.put(id,name);
        Bind.handle(id,event.getSenderName(),name);
    }
}
