package org.ncc.github.nqbot.commands.friend;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import org.bukkit.Bukkit;
import org.ncc.github.nqbot.manager.ConfigManager;
import org.ncc.github.nqbot.utils.ConsoleSender;
import org.ncc.github.nqbot.utils.CoreUtils;

public class SudoFriendCommand implements FriendCommand{
    @Override
    public String getHead() {
        return "sudo";
    }

    @Override
    public void process(String[] args, Bot bot, Friend target, FriendMessageEvent event) {
        if (ConfigManager.CONFIG_FILE_READ.getMasterName() == event.getSender().getId()){
            if (args.length < 1){
                target.sendMessage("Please use #sudo <mccommand>");
                return;
            }
            ConsoleSender sender = new ConsoleSender(target);
            String command;
            if (args.length > 1){
                command = CoreUtils.mergeStringWithSpace(args);
            }else{
                command = args[0];
            }
            Bukkit.getLogger().info(String.format("User %s use command %s",event.getSender().getId(),command));
            Bukkit.getScheduler().runTask(CoreUtils.getPlugin(),()-> Bukkit.getServer().dispatchCommand(sender,command));
        }else{
            target.sendMessage("No permission to use this command");
        }
    }
}
