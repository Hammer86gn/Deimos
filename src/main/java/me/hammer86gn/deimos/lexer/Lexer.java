package me.hammer86gn.deimos.lexer;

import me.hammer86gn.deimos.Deimos;
import me.hammer86gn.deimos.util.FileUtil;
import me.hammer86gn.deimos.util.Pair;
import me.hammer86gn.deimos.util.exeception.LexerErrorException;
import me.hammer86gn.deimos.util.exeception.LexerNotInitializedException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * NOT THREAD SAFE
 */
public final class Lexer {
    private static Lexer instance;
    private static String identifierChars = "acdefghijklmnopqrstuvwxyz1234567890_";
    private static String digitChars = "1234567890.-";


    private boolean init;

    private String sourceName;
    private String source;


    private char current;
    private int index;

    private long lineNumber;
    private long columnNumber;

    private LexerToken currentToken;
    private String currentContext;

    private boolean findingToken;

    private LinkedList<LexerToken> tokens;
    private LinkedList<LexerError> lexerErrors;
    private boolean hasErrors;

    private Lexer() {
        instance = this;

        this.init = false;

        this.sourceName = "";
        this.source = "";

        this.current = '\0';
        this.index = 0;

        this.lineNumber = 0;
        this.columnNumber = 0;

        this.currentToken = null;
        this.currentContext = "";

        this.findingToken = true;

        this.tokens = new LinkedList<>();
        this.lexerErrors = new LinkedList<>();
        this.hasErrors = false;
    }

    /**
     * Initializes the lexer to get ready for lexing
     *
     * @param sourceName the name of the source to lex
     * @param source the code from the source to lex
     */
    public void init(String sourceName, String source) {
        this.sourceName = sourceName;
        this.source = source;


        this.index = 0;
        this.lineNumber = 0;
        this.columnNumber = 0;
        this.tokens = new LinkedList<>();
        this.lexerErrors = new LinkedList<>();
        this.hasErrors = false;
        this.findingToken = true;


        this.init = true;

        if (Deimos.DEIMOS_ENABLE_DEBUG) {
            Deimos.getLogger().info("[LEXER] Initialized Lexer");
        }
    }

    /**
     * Initializes the lexer to get ready for lexing
     *
     * @param source the code from the source to lex
     */
    public void init(String source) {
        this.init("$Local", source);
    }

    /**
     * Initializes the lexer to get ready for lexing
     *
     * @param file the file to lex
     */
    public void init(File file) {
        this.init(file.getName(), FileUtil.readString(file));
    }

    /**
     * Begins the lexical process
     */
    public void lex() {
        if (!this.init) {
            throw new LexerNotInitializedException();
        }

        long codeLength = this.source.length();
        while (this.index < codeLength) {
            this.current = this.source.charAt(this.index);
            if (this.current == '\n') {
                this.lineNumber += 1;
            }


            if (this.findingToken) {
                this.findToken();
            } else {
                this.continueToken();
            }
            this.index += 1;
            this.columnNumber += 1;
        }

        if (Deimos.DEIMOS_ENABLE_DEBUG) {
            Deimos.getLogger().info("[LEXER] Finished lexing source %s".formatted(this.sourceName));

            Deimos.getLogger().info("[LEXER] Printing all tokens");
            this.tokens.forEach(token -> {
                Deimos.getLogger().info("[LEXER] " + token.toString());
            });
            Deimos.getLogger().info("[LEXER] Finished printing tokens");
        }


        if (this.lexerErrors.size() > 0) {
            this.hasErrors = true;
            for (LexerError error : this.lexerErrors) {
                Deimos.getLogger().log(Level.SEVERE, error.error());
            }
        }
    }

    /**
     * Finds the initial start of a token
     */
    public void findToken() {
        switch (this.current) {
            case ' ': {
                this.columnNumber++;
                break;
            }

            case '\n':
            case ';': {
                this.currentToken = new LexerToken(LexerTokenType.END_OF_LINE, new Pair<>(this.lineNumber, this.columnNumber), null);
                this.finishToken();
                break;
            }
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9': {
                this.findingToken = false;
                this.currentToken = new LexerToken(LexerTokenType.INT, new Pair<>(this.lineNumber, this.columnNumber), null);
                this.currentContext += this.current;
                if (!Character.isDigit(this.peek())) {
                    this.finishToken();
                }
                break;
            }
            case '"':
            case '\'': {
                this.findingToken = false;
                this.currentToken = new LexerToken(LexerTokenType.STRING, new Pair<>(this.lineNumber, this.columnNumber), null);
                this.currentContext += this.current;
                break;
            }

            case '(':
            case ')':
            case '[':
            case ']':
            case '{':
            case '}':
            case ',':
            case '+':
            case '-':
            case '*':
            case '%':
            case '^':
            case '#':
            case '&':
            case '|': {
                this.currentToken = new LexerToken(LexerTokenType.of(String.valueOf(this.current)), new Pair<>(this.lineNumber, this.columnNumber), null);
                this.currentContext += this.current;
                this.finishToken();
                break;
            }

            case ':':
            case '=':
            case '/': {
                String search = String.valueOf(this.current);
                if (this.current == this.peek()) {
                    search += this.peek();
                }
                this.currentToken = new LexerToken(LexerTokenType.of(search), new Pair<>(this.lineNumber, this.columnNumber), null);
                this.currentContext = search;
                this.index += search.length() - 1;
                this.finishToken();
                break;
            }


            case '~': {
                if (this.peek() == '=') {
                    this.currentToken = new LexerToken(LexerTokenType.of(String.valueOf("~=")), new Pair<>(this.lineNumber, this.columnNumber), null);
                    this.currentContext = "~=";
                    this.index += 1;
                    this.finishToken();
                } else {
                    this.currentToken = new LexerToken(LexerTokenType.symbolOf(String.valueOf(this.current)), new Pair<>(this.lineNumber, this.columnNumber), null);
                    this.currentContext += this.current;
                    this.finishToken();
                }
                break;
            }

            case '<':
            case '>': {
                if (this.current == this.peek()) {
                    this.currentToken = new LexerToken(LexerTokenType.of(String.valueOf(this.current) + this.current), new Pair<>(this.lineNumber, this.columnNumber), null);
                    this.currentContext += String.valueOf(this.current) + this.current;
                    this.finishToken();
                } else if(this.peek() == '=') {
                    this.currentToken = new LexerToken(LexerTokenType.of(this.current + "="), new Pair<>(this.lineNumber, this.columnNumber), null);
                    this.currentContext += this.current + "=";
                    this.finishToken();
                } else {
                    this.currentToken = new LexerToken(LexerTokenType.symbolOf(String.valueOf(this.current)), new Pair<>(this.lineNumber, this.columnNumber), null);
                    this.currentContext += this.current;
                    this.finishToken();
                }
                break;
            }

            case '.': {
                if (this.peek() == '.') {
                    Deimos.getLogger().info("[LEXER] Found double dot");
                    if (this.peekpeek() == '.') {
                        Deimos.getLogger().info("[LEXER] Found triple dot");
                        this.currentContext = "...";
                        this.currentToken = new LexerToken(LexerTokenType.ELLIPSES, new Pair<>(this.lineNumber, this.columnNumber), null);
                        this.finishToken();
                        this.index += 2;
                    } else {
                        Deimos.getLogger().info("[LEXER] Found Finalizing Double dot");
                        this.currentContext = "..";
                        this.currentToken = new LexerToken(LexerTokenType.CONCATENATE, new Pair<>(this.lineNumber, this.columnNumber), null);
                        this.finishToken();
                        this.index += 1;
                    }
                } else {
                    Deimos.getLogger().info("[LEXER] Found only one dot");
                    this.currentContext = ".";
                    this.currentToken = new LexerToken(LexerTokenType.DOT, new Pair<>(this.lineNumber, this.columnNumber), null);
                    this.finishToken();
                }
                break;
            }

            default: {
                this.findingToken = false;
                this.currentToken = new LexerToken(LexerTokenType.IDENTIFIER, new Pair<>(this.lineNumber, this.columnNumber), null);
                this.currentContext += this.current;
                this.identifierCheck();

                break;
            }
        }
    }

    /**
     * Continues the token after getting an initial type
     */
    public void continueToken() {
        if (this.currentToken != null && this.currentToken.type() == LexerTokenType.INT || this.currentToken.type() == LexerTokenType.FLOAT) {
            this.continueNumber();
        }
        if (this.currentToken != null && this.currentToken.type() == LexerTokenType.STRING) {
            this.continueString();
        }
        if (this.currentToken != null && this.currentToken.type() == LexerTokenType.IDENTIFIER) {
            this.continueIdentifier();
        }
    }

    public void continueNumber() {
        this.currentContext += this.current;
        if (this.current == '.') {
            if (this.currentToken.type() == LexerTokenType.FLOAT) this.appendLexerError("Cannot have two decimals in a number");
        }
        if (!Character.isDigit(this.peek())) {
            this.finishToken();
        }
    }

    public void continueString() {
        if (this.current == '\\') {
            this.handleEscape();
        } else {
            if (this.currentContext.length() > 1) {
                if (this.current == this.currentContext.charAt(0)) {
                    this.currentContext += this.current;
                    this.finishToken();
                }
            }
            this.currentContext += this.current;
        }
    }

    public void continueIdentifier() {
        this.currentContext += this.current;

        //System.out.println("In identifier " + this.index + " checking " + (this.index + 1) + " current: " + this.current + " peek: " + this.peek() );
        this.identifierCheck();
    }

    private void identifierCheck() {
        if (!Lexer.identifierChars.contains(String.valueOf(this.peek())) || this.peek() == '.') {
            if (LexerTokenType.stringIsKeyword(this.currentContext)) {
                this.currentToken.type(LexerTokenType.of(this.currentContext));
            }
            this.finishToken();
        }
    }

    private void handleEscape() {
        char peek = this.peek();
        switch (peek) {
            case '\\':
            case '"':
            case '\'': {
                this.currentContext += peek;
                this.index += 1;
                break;
            }
            default: {
                this.appendLexerError("Unknown escape character");
            }
        }
    }

    /**
     * Finishes the token and adds it to the last
     */
    public void finishToken() {
        if (Deimos.DEIMOS_ENABLE_DEBUG) {
            Deimos.getLogger().info("[LEXER] Completed Token!" + this.currentToken.type().name() + "with context " + this.currentContext);
        }
        this.currentToken.context(this.currentContext);
        this.tokens.add(this.currentToken);
        this.currentContext = "";
        this.findingToken = true;
        this.currentToken = null;
    }

    private void appendLexerError(@NotNull String message) {
        this.lexerErrors.add(new LexerError(new Pair<>(this.lineNumber, this.columnNumber), message));
    }

    private char peek() {
        if (this.source.length() <= this.index + 1)
            return '\0';

        return this.source.charAt(this.index + 1);
    }

    private char peekpeek() {
        if (this.source.length() < this.index + 2)
            return '\0';
        return this.source.charAt(this.index + 2);
    }

    /**
     * Gets the tokens that were lexed
     * @throws LexerErrorException if there were any errors when lexing
     * @return linked list of lexer tokens
     */
    public LinkedList<LexerToken> getTokens() {
        if (this.hasErrors) {
            throw new LexerErrorException();
        }
        return this.tokens;
    }

    /**
     * Check whether the lexer has errors or not
     * @return true if there are errors
     */
    public boolean hasErrors() {
        return this.hasErrors;
    }

    /**
     * Gets the name of the source
     *
     * @return the source name as a string
     */
    public String getSourceName() {
        return this.sourceName;
    }

    /**
     * Gets the instance of the lexer (this will be removed when there are multiple runtimes)
     * @return the lexer instance
     */
    public static Lexer getInstance() {
        return Lexer.instance == null ? new Lexer() : Lexer.instance;
    }
}