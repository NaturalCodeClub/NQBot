package org.ncc.github.nqbot.commands.ingame;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.ncc.github.nqbot.NQBot;

import static org.ncc.github.nqbot.data.BindFile.tempMap;

public class CommandBind implements CommandExecutor {
    //todo complete it
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length<2 || args.length>3){
            return false;
        }else{
            switch (args[1].toLowerCase()){
                case "confirm":{
                    Bukkit.getScheduler().runTaskAsynchronously(NQBot.getPlugin(),()->{
                        for (Long l : tempMap.keySet()){
                            if(tempMap.get(l).equals(sender.getName())){

                            }
                        }
                    });
                }
                case "lookup":{

                }
            }
        }
        return true;
    }
}
