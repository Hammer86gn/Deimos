package me.hammer86gn.deimos.parser;

import me.hammer86gn.deimos.lexer.LexerToken;
import org.jetbrains.annotations.NotNull;

public record ParserError(LexerToken token, String message) {

    public @NotNull String error() {
        return token.lineColumn().toString() + " A parser error occurred at token: " + this.token.type().name() + ":\n" + this.message;
    }

}
