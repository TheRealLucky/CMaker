package i15091.project.cmaker;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.FileHandler;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;

import java.io.*;

public class CMLogger {

    public static void logInfo(Logger log, String msg) {
        log.log(Level.INFO, msg);
    }

    public static void logSevere(Logger log, String msg) {
        log.log(Level.SEVERE, msg);
    }

    public static void logWarning(Logger log, String msg) {
        log.log(Level.WARNING, msg);
    }

    public static Handler fileHandler;

    static{
        try{
            fileHandler = new FileHandler("./CMaker.log");
            fileHandler.setLevel(Level.ALL);
        } catch(Exception e) {
            e.printStackTrace();
        }

    }
}