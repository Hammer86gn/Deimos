package me.hammer86gn.deimos.lexer;

import me.hammer86gn.deimos.util.Pair;
import org.jetbrains.annotations.Nullable;

/**
 * A token used by the lexer to store data about the code
 */
public final class LexerToken {
    private LexerTokenType type;
    private @Nullable Pair<Long, Long> lineColumn;
    private @Nullable String context;

    public LexerToken(LexerTokenType type, Pair<Long, Long> lineColumn, @Nullable String context) {
        this.type = type;
        this.lineColumn = lineColumn;
        this.context = context;
    }

    public long line() {
        assert this.lineColumn != null;
        return this.lineColumn.one();
    }

    public long column() {
        assert this.lineColumn != null;
        return this.lineColumn.two();
    }

    public LexerTokenType type() {
        return this.type;
    }

    public LexerTokenType type(LexerTokenType type) {
        this.type = type;
        return this.type;
    }

    public Pair<Long, Long> lineColumn() {
        return this.lineColumn;
    }

    public Pair<Long, Long> lineColumn(Pair<Long, Long> lineColumn) {
        this.lineColumn = lineColumn;
        return this.lineColumn;
    }

    public String context() {
        return this.context;
    }

    public String context(String context) {
        this.context = context;
        return this.context;
    }

    @Override
    public String toString() {
        return "{type=" + this.type.name() + ", loc=" + this.line() + ":" + this.column() + ", context=" + this.context + "}";
    }
}
