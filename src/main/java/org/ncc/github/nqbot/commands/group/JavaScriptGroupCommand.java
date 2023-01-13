package org.ncc.github.nqbot.commands.group;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.ncc.github.nqbot.commands.PackagedCommandInfo;
import javax.script.Invocable;
import javax.script.ScriptException;

/**
 * 单个被包装的命令实例
 */
public class JavaScriptGroupCommand implements GroupCommand {
    private final Invocable script;

    public JavaScriptGroupCommand(Invocable script) {
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
    public void process(PackagedCommandInfo args, Bot bot, Group target, GroupMessageEvent event) {
        try {
            this.script.invokeFunction("process",new Object[]{args,bot,target,event});
        } catch (ScriptException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
