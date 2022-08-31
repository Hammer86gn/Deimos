package me.hammer86gn.deimos.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Just a class used to format the logger found in {@link me.hammer86gn.deimos.Deimos}
 */
public final class LoggerFormatter extends Formatter {
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("hh:mm:ss");

    @Override
    public String format(LogRecord record) {
        String color = "\u001B[37m";
        if (record.getLevel() == Level.SEVERE) {
            color = "\u001B[31m";
        }
        if (record.getLevel() == Level.INFO) {
            color = "\u001B[36m";
        }

        return "\u001B[33m" + this.formatDate() + color + " [%s]\u001B[37m %s".formatted(record.getLevel().getName(), record.getMessage()) + "\u001B[37m\n";
    }

    private String formatDate() {
        return SIMPLE_DATE_FORMAT.format(new Date());
    }

}
