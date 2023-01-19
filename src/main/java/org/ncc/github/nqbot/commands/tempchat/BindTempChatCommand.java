package org.ncc.github.nqbot.commands.tempchat;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.GroupTempMessageEvent;
import org.ncc.github.nqbot.bind.Bind;
import org.ncc.github.nqbot.commands.PackagedCommandInfo;

import static org.ncc.github.nqbot.data.BindFile.tempMap;

//todo complete it
public class BindTempChatCommand implements GroupTempCommand{
    @Override
    public String getHead() {
        return "bind";
    }

    @Override
    public void process(PackagedCommandInfo args, Bot bot, NormalMember target, GroupTempMessageEvent event) {
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
        Bind.handle(id,target.getNick(),name);
    }
}
