package com.igorbunova.utils;


import java.util.Map;
import com.beust.jcommander.JCommander;

import static java.util.Collections.emptyList;

/**
 * Launcher.
 */
public class Launcher {
    private final JCommander jcom;
    private final Map<String, Runnable> commands;

    public Launcher(Map<String, Runnable> commands) {
        this.jcom = new JCommander(emptyList());
        commands.forEach(jcom::addCommand);
        this.commands = commands;
    }


    public void run(String[] args) {
        jcom.parse(args);
        String key = jcom.getParsedCommand();
        Runnable cmd = commands.getOrDefault(key, jcom::usage);
        cmd.run();
    }
}
