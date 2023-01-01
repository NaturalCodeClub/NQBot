package org.ncc.github.nqbot.manager;

import org.ncc.github.nqbot.commands.*;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CommandManager {
    public static final Set<Command> registedSystemCommands = ConcurrentHashMap.newKeySet();

    static{
        registedSystemCommands.add(new ReloadCommand());
        registedSystemCommands.add(new SudoCommand());
    }
}
