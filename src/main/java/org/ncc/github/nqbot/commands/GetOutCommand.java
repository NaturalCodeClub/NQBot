package org.ncc.github.nqbot.commands;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.ncc.github.nqbot.utils.FunUtils;

public class GetOutCommand implements Command{

    @Override
    public String getHead() {
        return "getout";
    }

    @Override
    public void process(String[] args, Bot bot, Group target, GroupMessageEvent event) {
        if (args.length == 1){
            Contact.sendImage(target, FunUtils.getGetPic(Long.parseLong(args[0])));
            return;
        }
        if (args.length == 0){
            Contact.sendImage(target, FunUtils.getGetPic(event.getSender().getId()));
            return;
        }
        target.sendMessage("Wrong usage!Please use:#getout <qid> || #getout");
    }

}
