package com.riege.onerecord.carbulator;

import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogger {

//        MyLogger.LOGGER.severe("Schwerwiegender Fehler");
//        MyLogger.LOGGER.warning("Warnung");
//        MyLogger.LOGGER.info("Information");
//        MyLogger.LOGGER.config("Konfigurationshinweis");
//        MyLogger.LOGGER.fine("Fein");
//        MyLogger.LOGGER.finer("Feiner");
//        MyLogger.LOGGER.finest("Am feinsten");

    public static Logger LOGGER = Logger.getLogger(CarbulatorEngine.class.getPackage().getName());

    private static Level LOGLEVEL = Level.ALL;
    static {
        // replace default handler
        LOGGER.setLevel(LOGLEVEL);
        Handler handler = new ConsoleHandler();
        handler.setLevel(LOGLEVEL);
        handler.setFormatter(new SimpleFormatter() {
            private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

            @Override
            public synchronized String format(LogRecord lr) {
                return String.format(format,
                    new Date(lr.getMillis()),
                    lr.getLevel().getName(),    // not using getLocalizedName() !
                    lr.getMessage()
                );
            }
        });
        LOGGER.setUseParentHandlers(false);
        LOGGER.addHandler(handler);
    }

}
