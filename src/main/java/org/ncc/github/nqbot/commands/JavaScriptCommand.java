package org.ncc.github.nqbot.commands;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import javax.script.Invocable;
import javax.script.ScriptException;

public class JavaScriptCommand implements Command{
    private final Invocable script;

    public JavaScriptCommand(Invocable script) {
        this.script = script;
    }

    @Override
    public String getHead(){
        try {
            return (String) script.invokeFunction("getName",new String[0]);
        } catch (ScriptException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void process(String[] args, Bot bot, Group target, GroupMessageEvent event) {
        try {
            this.script.invokeFunction("process",new Object[]{args,bot,target,event});
        } catch (ScriptException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
