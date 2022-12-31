package org.ncc.github.nqbot;

import net.mamoe.mirai.Bot;
import org.bukkit.plugin.java.JavaPlugin;
import org.ncc.github.nqbot.manager.BotDataManager;
import org.ncc.github.nqbot.manager.BotManager;
import org.ncc.github.nqbot.manager.ConfigManager;
import org.ncc.github.nqbot.manager.JavaScriptCommandManager;
import org.ncc.github.nqbot.utils.SeXResponse;
import org.ncc.github.nqbot.utils.Utils;

import java.io.File;

public final class NQBot extends JavaPlugin {

    @Override
    public void onEnable() {
        Utils.init(this);
        BotDataManager.init();
        if (SeXResponse.URLs.getCache().exists()){
            for (File file : SeXResponse.URLs.getCache().listFiles()){
                file.delete();
            }
        }
        File jsDir = new File(Utils.getDataFolder(),"commands");
        if (!jsDir.exists()){
            jsDir.mkdir();
        }
        JavaScriptCommandManager.loadAll(jsDir);
        ConfigManager.init();
        BotManager.init();
    }

    @Override
    public void onDisable() {
        BotDataManager.saveCurrent();
        BotManager.shutdown();
        JavaScriptCommandManager.unloadAllCommands();
    }
}
