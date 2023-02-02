package org.ncc.github.nqbot.manager;

import org.ncc.github.nqbot.commands.friend.*;
import org.ncc.github.nqbot.commands.group.*;
import org.ncc.github.nqbot.commands.tempchat.*;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于存储已经注册的命令，支持热加载
 */
public class CommandManager {
    public static final Set<GroupCommand> REGISTED_GROUP_COMMANDS = ConcurrentHashMap.newKeySet();
    public static final Set<FriendCommand> REGISTED_FRIEND_COMMANDS = ConcurrentHashMap.newKeySet();
    public static final Set<GroupTempCommand> REGISTED_GROUP_TEMP_COMMANDS = ConcurrentHashMap.newKeySet();

    static{
        REGISTED_GROUP_COMMANDS.add(new ReloadGroupCommand());
        REGISTED_GROUP_COMMANDS.add(new SudoGroupCommand());
        REGISTED_GROUP_COMMANDS.add(new VersionGroupCommand());
        REGISTED_GROUP_COMMANDS.add(new BindGroupCommand());
        //
        REGISTED_FRIEND_COMMANDS.add(new VersionFriendCommand());
        REGISTED_FRIEND_COMMANDS.add(new ReloadFriendCommand());
        REGISTED_FRIEND_COMMANDS.add(new SudoFriendCommand());
        REGISTED_FRIEND_COMMANDS.add(new BindFriendCommand());
        //
        REGISTED_GROUP_TEMP_COMMANDS.add(new SudoTempChatCommand());
        REGISTED_GROUP_TEMP_COMMANDS.add(new ReloadTempChatCommand());
        REGISTED_GROUP_TEMP_COMMANDS.add(new VersionTempChatCommand());
        REGISTED_GROUP_TEMP_COMMANDS.add(new BindTempChatCommand());
    }
}
