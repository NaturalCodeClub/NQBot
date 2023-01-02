package org.ncc.github.nqbot;

import org.bukkit.plugin.java.JavaPlugin;
import org.ncc.github.nqbot.manager.BotDataManager;
import org.ncc.github.nqbot.manager.BotManager;
import org.ncc.github.nqbot.manager.ConfigManager;
import org.ncc.github.nqbot.jssupport.JavaScriptCommandLoader;
import org.ncc.github.nqbot.utils.CoreUtils;

import java.io.File;

/**
 * 插件的主类
 */
public final class NQBot extends JavaPlugin {
    @Override
    public void onEnable() {
        CoreUtils.init(this);
        BotDataManager.init();
        File jsDir = new File(CoreUtils.getDataFolder(),"commands");
        if (!jsDir.exists()){
            jsDir.mkdir();
        }
        JavaScriptCommandLoader.loadAll(jsDir);
        ConfigManager.init();
        BotManager.init();
    }

    @Override
    public void onDisable() {
        BotDataManager.saveCurrent();
        BotManager.shutdown();
        JavaScriptCommandLoader.unloadAllCommands();
    }
}
