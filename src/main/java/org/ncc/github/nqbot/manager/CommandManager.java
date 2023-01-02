package org.ncc.github.nqbot.manager;

import org.ncc.github.nqbot.commands.group.GroupCommand;
import org.ncc.github.nqbot.commands.group.ReloadGroupCommand;
import org.ncc.github.nqbot.commands.group.SudoGroupCommand;
import org.ncc.github.nqbot.commands.pri.PrivateCommand;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于存储已经注册的命令，支持热加载
 */
public class CommandManager {
    public static final Set<GroupCommand> REGISTED_GROUP_COMMANDS = ConcurrentHashMap.newKeySet();
    public static final Set<PrivateCommand> REGISTED_PRIVATE_COMMANDS = ConcurrentHashMap.newKeySet();

    static{
        REGISTED_GROUP_COMMANDS.add(new ReloadGroupCommand());
        REGISTED_GROUP_COMMANDS.add(new SudoGroupCommand());
    }
}
