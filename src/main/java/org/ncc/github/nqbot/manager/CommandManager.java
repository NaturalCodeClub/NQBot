package org.ncc.github.nqbot.manager;

import org.ncc.github.nqbot.commands.friend.FriendCommand;
import org.ncc.github.nqbot.commands.friend.ReloadFriendCommand;
import org.ncc.github.nqbot.commands.friend.SudoFriendCommand;
import org.ncc.github.nqbot.commands.group.GroupCommand;
import org.ncc.github.nqbot.commands.group.ReloadGroupCommand;
import org.ncc.github.nqbot.commands.group.SudoGroupCommand;
import org.ncc.github.nqbot.commands.tempchat.GroupTempCommand;
import org.ncc.github.nqbot.commands.tempchat.ReloadTempChatCommand;
import org.ncc.github.nqbot.commands.tempchat.SudoTempChatCommand;

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
        REGISTED_FRIEND_COMMANDS.add(new ReloadFriendCommand());
        REGISTED_FRIEND_COMMANDS.add(new SudoFriendCommand());
        REGISTED_GROUP_TEMP_COMMANDS.add(new SudoTempChatCommand());
        REGISTED_GROUP_TEMP_COMMANDS.add(new ReloadTempChatCommand());
    }
}
