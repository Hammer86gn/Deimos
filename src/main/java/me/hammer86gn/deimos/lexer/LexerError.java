package me.hammer86gn.deimos.lexer;

import me.hammer86gn.deimos.util.Pair;
import org.jetbrains.annotations.NotNull;

/**
 * Generates a lexer error
 *
 * @param lineColumn the line and column where the error occurred
 * @param message the message to provide with the error
 */
public record LexerError(Pair<Long, Long> lineColumn, String message) {

    /**
     * Formats an error message for the lexer error
     *
     * @return the error message
     */
    public @NotNull String error() {
        return "\u001B[31m" + "[LEXER] There was an error in the lexer at %s:%s. Info: %s".formatted(
                this.lineColumn.one(),
                this.lineColumn.two(),
                this.message) + "\u001B[0m";
    }

}
