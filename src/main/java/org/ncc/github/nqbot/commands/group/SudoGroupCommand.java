package org.ncc.github.nqbot.commands.group;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.bukkit.Bukkit;
import org.ncc.github.nqbot.manager.ConfigManager;
import org.ncc.github.nqbot.commands.PackagedCommandInfo;
import org.ncc.github.nqbot.utils.ConsoleSender;
import org.ncc.github.nqbot.utils.CoreUtils;

public class SudoGroupCommand implements GroupCommand {
    @Override
    public String getHead() {
        return "sudo";
    }

    @Override
    public void process(PackagedCommandInfo packagedArgs, Bot bot, Group target, GroupMessageEvent event) {
        final String[] args = packagedArgs.getArgs().stream().toArray(String[]::new);
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
