package com.igorbunova.examples;

import java.util.LinkedHashMap;
import java.util.Map;
import com.igorbunova.utils.Launcher;

/**
 * Main.
 */
public class Main {
    public static void main(String[] args) {
        Map<String, Runnable> commands = new LinkedHashMap<>();
        commands.put("load", new LoadData());
        commands.put("index", new IndexData());
        commands.put("search", new Search());

        new Launcher(commands).run(args);
    }
}
