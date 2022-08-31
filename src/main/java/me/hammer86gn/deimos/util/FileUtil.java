package me.hammer86gn.deimos.util;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Utilities to help read, write, and do other things with files
 */
public final class FileUtil {

    private FileUtil() { }

    /**
     * Reads a file to a string to get around pesky try-catch
     *
     * @param file the file to read
     * @return the file contents written to a string
     */
    public static @Nullable String readString(File file) {
        if (!file.exists()) return null;
        if (!file.canRead()) return null;

        BufferedReader bReader;
        try {
           FileReader reader = new FileReader(file);
           bReader = new BufferedReader(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder sb = new StringBuilder();

        try {
            String s;
            while ((s = bReader.readLine()) != null) {
                sb.append(s);
                sb.append("\n");
            }
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }

        return sb.toString();
    }

}
