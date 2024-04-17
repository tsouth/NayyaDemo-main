package com.nayya.utilities.logger;

import java.util.logging.Level;

public class Logger {
    private final java.util.logging.Logger log;

    public Logger(String className) {
        log = java.util.logging.Logger.getLogger(className);
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %1$tc - %5$s %n");
    }

    public void info(String message) {
        log.info(message);
    }

    public void warning(String message) {
        log.setLevel(Level.WARNING);
        log.warning(message);
        log.setLevel(Level.INFO);
    }

    public void severe(String message) {
        log.setLevel(Level.SEVERE);
        log.severe(message);
        log.setLevel(Level.INFO);
    }
}
