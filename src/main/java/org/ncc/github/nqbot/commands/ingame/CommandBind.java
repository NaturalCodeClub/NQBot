package org.ncc.github.nqbot.commands.ingame;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.ncc.github.nqbot.utils.CoreUtils;

import static org.ncc.github.nqbot.data.BindFile.bindData;
import static org.ncc.github.nqbot.data.BindFile.tempMap;

public class CommandBind implements CommandExecutor {
    //todo complete it
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length!=3){
            return false;
        }else{
            switch (args[1].toLowerCase()){
                case "confirm":{
                    String id = args[2];
                    Bukkit.getScheduler().runTaskAsynchronously(CoreUtils.getPlugin(),()->{
                        for (Long l : tempMap.keySet()){
                            if(tempMap.get(l).equals(sender.getName())&&CoreUtils.isNumberString(id)&&l==Long.parseLong(id)){
                                bindData.put(Long.parseLong(id),sender.getName());
                                tempMap.remove(l);
                                //ConfigManager.BIND_FILE_READ.saveData(BindFile.BindFile,ConfigManager.bindData);
                                sender.sendMessage("绑定成功");
                            }else{
                                sender.sendMessage("绑定失败，请检查您的QQID输入是否正确");
                            }
                        }
                    });
                    return true;
                }
                case "lookup":{
                    String value = args[2];
                    if(!sender.hasPermission("bind.lookup")){
                        sender.sendMessage("你没有权限");
                        return true;
                    }
                    if(CoreUtils.isNumberString(value)){
                        if(bindData.get(Long.parseLong(value)).isEmpty()) sender.sendMessage("您查询的QQ号并没有绑定玩家");
                        else sender.sendMessage("该QQ号与 " + bindData.get(Long.parseLong(value)) + " 绑定");
                        return true;
                    }else{
                        for(Long l : bindData.keySet()){
                            if(bindData.get(l).equals(value)){
                                sender.sendMessage("该玩家与 " + bindData.get(l) + " 绑定");
                                return true;
                            }
                        }
                        sender.sendMessage("该玩家没与任何QQ号绑定");
                    }
                }
            }
        }
        return true;
    }
}
