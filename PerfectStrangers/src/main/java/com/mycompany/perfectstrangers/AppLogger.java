package com.mycompany.perfectstrangers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public final class AppLogger {
    private static final Logger logger = Logger.getLogger("PerfectStrangersApp");
    private static final String LOG_DIR = "logs";
    private static final String LOG_FILE = "app.log";

    private AppLogger() {}

    public static void init() {
        try {
            Path dir = Paths.get(LOG_DIR);
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }

            FileHandler fh = new FileHandler(Paths.get(LOG_DIR, LOG_FILE).toString(), true);
            fh.setFormatter(new SimpleFormatter());
            // Attach handler both to our named logger and to the root logger so
            // all class loggers (e.g., PSInicio) also write to the same file.
            logger.addHandler(fh);
            logger.setLevel(Level.ALL);

            java.util.logging.Logger root = java.util.logging.Logger.getLogger("");
            root.addHandler(fh);

            logger.info("Logger inicializado");
        } catch (IOException e) {
            System.err.println("No se pudo inicializar el logger: " + e.getMessage());
        }
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void logException(Throwable t) {
        logger.log(Level.SEVERE, "Excepción no capturada", t);
    }
}
