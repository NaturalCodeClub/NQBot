package org.ncc.github.nqbot.commands.tempchat;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.GroupTempMessageEvent;
import org.bukkit.Bukkit;
import org.ncc.github.nqbot.manager.ConfigManager;
import org.ncc.github.nqbot.commands.PackagedCommandArg;
import org.ncc.github.nqbot.utils.ConsoleSender;
import org.ncc.github.nqbot.utils.CoreUtils;

public class SudoTempChatCommand implements GroupTempCommand{
    @Override
    public String getHead() {
        return "sudo";
    }

    @Override
    public void process(PackagedCommandArg packagedArgs, Bot bot, NormalMember target, GroupTempMessageEvent event) {
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
