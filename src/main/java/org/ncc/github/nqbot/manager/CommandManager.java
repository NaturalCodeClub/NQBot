package org.ncc.github.nqbot.manager;

import org.ncc.github.nqbot.commands.GroupCommand;
import org.ncc.github.nqbot.commands.ReloadGroupCommand;
import org.ncc.github.nqbot.commands.SudoGroupCommand;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于存储已经注册的命令，支持热加载
 */
public class CommandManager {
    public static final Set<GroupCommand> REGISTED_GROUP_COMMANDS = ConcurrentHashMap.newKeySet();

    static{
        REGISTED_GROUP_COMMANDS.add(new ReloadGroupCommand());
        REGISTED_GROUP_COMMANDS.add(new SudoGroupCommand());
    }
}
