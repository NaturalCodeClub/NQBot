package org.ncc.github.nqbot.jssupport;

import org.bukkit.Bukkit;
import org.ncc.github.nqbot.commands.group.GroupCommand;
import org.ncc.github.nqbot.commands.group.JavaScriptGroupCommand;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JS命令加载器
 */
public class JavaScriptGroupCommandLoader {
    private static final Set<GroupCommand> REGISTED_JS_GROUP_COMMANDS = ConcurrentHashMap.newKeySet();
    private static final Logger logger = Bukkit.getLogger();
    private static File currentScriptDir;

    /**
     * 加载单个脚本
     * @param dataArray
     */
    public static void loadSingleJavaScript(byte[] dataArray){
        try{
            final ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
            engine.eval(new String(dataArray, StandardCharsets.UTF_8));
            Invocable inv = (Invocable) engine;
            REGISTED_JS_GROUP_COMMANDS.add(new JavaScriptGroupCommand(inv));
        }catch (Exception e){
            logger.log(Level.SEVERE,"Error in loading script!",e);
            e.printStackTrace();
        }
    }

    /**
     * 卸载所有的JS脚本命令
     */
    public static synchronized void unloadAllCommands(){
        REGISTED_JS_GROUP_COMMANDS.clear();
        currentScriptDir = null;
    }

    /**
     * 获取当前所有已经加载的JS脚本命令
     * @return 一个Set
     */
    public static synchronized Set<GroupCommand> getRegistedJSCommands(){
        return REGISTED_JS_GROUP_COMMANDS;
    }

    /**
     * 加载一个文件夹下所有的JS脚本
     * @param scriptsDir 脚本文件夹
     */
    public synchronized static void loadAll(File scriptsDir){
        if (scriptsDir.exists()){
            CompletableFuture.allOf(Arrays.stream(scriptsDir.listFiles())
                    .map(singleFile-> CompletableFuture.runAsync(()->{
                        try {
                            if (singleFile.getName().endsWith(".js")){
                                byte[] read = Files.readAllBytes(singleFile.toPath());
                                loadSingleJavaScript(read);
                            }
                        }catch (Exception e){
                            logger.log(Level.SEVERE,"Error in reading file!",e);
                            e.printStackTrace();
                        }
                    })).toArray(CompletableFuture[]::new)).join();
            logger.info(String.format("Load %s javascripts!", REGISTED_JS_GROUP_COMMANDS.size()));
            currentScriptDir = scriptsDir;
        }
    }

    /**
     * 重载
     */
    public synchronized static void reload(){
        REGISTED_JS_GROUP_COMMANDS.clear();
        loadAll(currentScriptDir);
    }
}
