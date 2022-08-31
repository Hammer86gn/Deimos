package me.hammer86gn.deimos;

import me.hammer86gn.deimos.util.LoggerFormatter;
import org.jetbrains.annotations.NotNull;

import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Deimos {

    public static final boolean DEIMOS_ENABLE_DEBUG   = true;

    public static final String DEIMOS_VERSION_MAJOR   = "1";
    public static final String DEIMOS_VERSION_MINOR   = "0";
    public static final String DEIMOS_VERSION_RELEASE = "00";

    public static final String LUA_VERSION_MAJOR      = "5";
    public static final String LUA_VERSION_MINOR      = "4";
    public static final String LUA_VERSION_RELEASE    = "4";


    private static final Logger DEIMOS_LOGGER = Logger.getLogger(Deimos.class.getName());
    static {
        DEIMOS_LOGGER.setUseParentHandlers(false);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new LoggerFormatter());
        DEIMOS_LOGGER.addHandler(handler);
    }

    private Deimos() {}

    /**
     * Returns a formatted version string mainly used for logging the version
     *
     * @return the string in the form of [major].[minor]:[release] and if debug is enabled a -DEBUG
     */
    public static @NotNull String formatVersionString() {
        return DEIMOS_VERSION_MAJOR + "." + DEIMOS_VERSION_MINOR + ":" + DEIMOS_VERSION_RELEASE + (DEIMOS_ENABLE_DEBUG == true ? "-DEBUG" : "");
    }

    public static @NotNull Logger getLogger() {
        return DEIMOS_LOGGER;
    }

}
