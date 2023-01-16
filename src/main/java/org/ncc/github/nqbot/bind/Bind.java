package org.ncc.github.nqbot.bind;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class Bind {
    public static String handle(Long id , String askingName, String playerName){
        Player player = Bukkit.getPlayer(playerName);
        if(!player.isOnline()){
            return "该玩家不在线，请检查您的输入";
        }
        player.sendMessage(askingName + "(QQ号:" + id + ")" + "申请与您绑定");
        player.sendMessage("若您确认无误，请输入/bind confirm进行绑定");
        return "消息发送成功";
    }
}
