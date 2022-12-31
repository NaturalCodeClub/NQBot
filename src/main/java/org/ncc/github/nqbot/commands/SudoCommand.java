package org.ncc.github.nqbot.commands;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.bukkit.Bukkit;
import org.ncc.github.nqbot.manager.ConfigManager;
import org.ncc.github.nqbot.utils.Utils;

public class SudoCommand implements Command{
    @Override
    public String getHead() {
        return "sudo";
    }

    @Override
    public void process(String[] args, Bot bot, Group target, GroupMessageEvent event) {
        if (ConfigManager.CONFIG_FILE_READ.getMasterName() == event.getSender().getId()){
            if (args.length < 1){
                target.sendMessage("Please use #sudo <mccommand>");
                return;
            }
            String command;
            if (args.length > 1){
                command = Utils.mergeStringWithSpace(args);
            }else{
                command = args[0];
            }
            Bukkit.getLogger().info(String.format("User %s use command %s",event.getSender().getId(),command));
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),command);
        }else{
            target.sendMessage("No permission to use this command");
        }
    }
}
