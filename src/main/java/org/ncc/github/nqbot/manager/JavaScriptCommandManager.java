package org.ncc.github.nqbot.manager;

import org.bukkit.Bukkit;
import org.ncc.github.nqbot.commands.Command;
import org.ncc.github.nqbot.commands.JavaScriptCommand;

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

public class JavaScriptCommandManager {
    private static final Set<Invocable> loadedScripts = ConcurrentHashMap.newKeySet();
    private static final Set<Command> registedJSCommands = ConcurrentHashMap.newKeySet();
    private static final Logger logger = Bukkit.getLogger();
    private static File currentScriptDir;

    public static void loadSingleJavaScript(byte[] dataArray){
        try{
            final ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
            engine.eval(new String(dataArray, StandardCharsets.UTF_8));
            Invocable inv = (Invocable) engine;
            registedJSCommands.add(new JavaScriptCommand(inv));
            loadedScripts.add(inv);
        }catch (Exception e){
            logger.log(Level.SEVERE,"Error in loading script!",e);
            e.printStackTrace();
        }
    }

    public static synchronized void unloadAllCommands(){
        registedJSCommands.clear();
        loadedScripts.clear();
        currentScriptDir = null;
    }

    public static synchronized Set<Command> getRegistedJSCommands(){
        return registedJSCommands;
    }

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
            logger.info(String.format("Load %s javascripts!",loadedScripts.size()));
            currentScriptDir = scriptsDir;
        }
    }

    public synchronized static Set<Invocable> getLoadedScripts(){
        return loadedScripts;
    }

    public synchronized static void reload(){
        registedJSCommands.clear();
        loadedScripts.clear();
        loadAll(currentScriptDir);
    }
}
