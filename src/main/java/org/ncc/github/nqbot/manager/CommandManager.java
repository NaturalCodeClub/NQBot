package org.ncc.github.nqbot.manager;

import org.ncc.github.nqbot.commands.Command;
import org.ncc.github.nqbot.commands.ReloadCommand;
import org.ncc.github.nqbot.commands.SudoCommand;
import org.ncc.github.nqbot.commands.VersionCommand;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CommandManager {
    public static final Set<Command> registedSystemCommands = ConcurrentHashMap.newKeySet();

    static{
        registedSystemCommands.add(new ReloadCommand());
        registedSystemCommands.add(new SudoCommand());

        registedSystemCommands.add(new VersionCommand());
    }
}
