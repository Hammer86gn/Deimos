package me.hammer86gn.deimos.lexer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An enum containing all the TokenTypes the lexer can produce and is the basis of the whole runtime
 */
public enum LexerTokenType {

    /* Keywords */

    AND("and"),
    BREAK("break"),
    DO("do"),
    ELSE("else"),
    ELSE_IF("elseif"),
    END("end"),
    FALSE("false"),
    FOR("for"),
    FUNCTION("function"),
    GOTO("goto"),
    IF("if"),
    IN("in"),
    LOCAL("local"),
    NIL("nil"),
    NOT("not"),
    OR("or"),
    REPEAT("repeat"),
    RETURN("return"),
    THEN("then"),
    TRUE("true"),
    UNTIL("until"),
    WHILE("while"),

    /* Symbols */

    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    MODULO("%"),
    POWER("^"),
    LENGTH("#"),
    LOGICAL_AND("&"),
    LOGICAL_NOT("~"),
    LOGICAL_OR("|"),
    SHIFT_LEFT("<<"),
    SHIFT_RIGHT(">>"),
    FLOOR("//"),

    /* Conditional */

    EQUAL_TO("=="),
    NOT_EQUAL("~="),
    LESS_THAN_OR_EQUAL("<="),
    GREATER_THAN_OR_EQUAL(">="),
    LESS_THAN("<"),
    GREATER_THAN(">"),
    EQUAL("="),

    /* Statement */
    OPEN_PARENTH("("),
    CLSE_PARENTH(")"),
    OPEN_BRACE("{"),
    CLSE_BRACE("}"),
    OPEN_BRACKET("["),
    CLSE_BRACKET("]"),
    DOUBLE_COLON("::"),
    END_OF_LINE(";"),

    COLON(":"),
    COMMA(","),
    DOT("."),
    CONCATENATE(".."),
    ELLIPSES("..."),

    /* Literal */
    FLOAT(null),
    INT(null),
    STRING(null),
    IDENTIFIER(null),

    ;

    private final @Nullable String content;

    LexerTokenType(@Nullable String content) {
        this.content = content;
    }

    @SuppressWarnings("EnhancedSwitchMigration")
    public boolean isKeyword() {
        switch (this) {
            case AND:
            case BREAK:
            case DO:
            case ELSE:
            case ELSE_IF:
            case END:
            case FALSE:
            case FOR:
            case FUNCTION:
            case GOTO:
            case IF:
            case IN:
            case LOCAL:
            case NIL:
            case NOT:
            case OR:
            case REPEAT:
            case RETURN:
            case THEN:
            case TRUE:
            case UNTIL:
            case WHILE:
                return true;
            default:
                return false;
        }
    }

    @SuppressWarnings("EnhancedSwitchMigration")
    public boolean isSymbol() {
        switch (this) {
            case EQUAL:
            case ADD:
            case SUBTRACT:
            case MULTIPLY:
            case DIVIDE:
            case MODULO:
            case POWER:
            case LENGTH:
            case LOGICAL_AND:
            case LOGICAL_NOT:
            case LOGICAL_OR:
            case SHIFT_LEFT:
            case SHIFT_RIGHT:
            case FLOOR:
                return true;
            default:
                return false;
        }
    }

    @SuppressWarnings("EnhancedSwitchMigration")
    public boolean isOperation() {
        switch (this) {
            case ADD:
            case SUBTRACT:
            case MULTIPLY:
            case DIVIDE:
            case MODULO:
            case POWER:
            case LENGTH:
            case LOGICAL_AND:
            case LOGICAL_NOT:
            case LOGICAL_OR:
            case SHIFT_LEFT:
            case SHIFT_RIGHT:
            case FLOOR:
                return true;
            default:
                return false;
        }
    }

    @SuppressWarnings("EnhancedSwitchMigration")
    public boolean isConditional() {
        switch (this) {
            case AND:
            case OR:
            case EQUAL_TO:
            case NOT_EQUAL:
            case LESS_THAN_OR_EQUAL:
            case GREATER_THAN_OR_EQUAL:
            case LESS_THAN:
            case GREATER_THAN:
                return true;
            default:
                return false;
        }
    }

    public static boolean stringIsKeyword(@NotNull String string) {
        for (LexerTokenType type : LexerTokenType.values()) {
            if (type.isKeyword() && type.content.equals(string)) {
                return true;
            }
        }
        return false;
    }

    public static @Nullable LexerTokenType keywordOf(@NotNull String string) {
        for (LexerTokenType type : LexerTokenType.values()) {
            if (type.isKeyword() && type.content.equals(string)) {
                return type;
            }
        }
        return null;
    }

    public static @Nullable LexerTokenType symbolOf(@NotNull String string) {
        for (LexerTokenType type : LexerTokenType.values()) {
            if (type.isSymbol() && type.content.equals(string)) {
                return type;
            }
        }
        return null;
    }

    public static @Nullable LexerTokenType of(@NotNull String string) {
        for (LexerTokenType type : LexerTokenType.values()) {
            if (type.content.equals(string)) {
                return type;
            }
        }
        return null;
    }


}