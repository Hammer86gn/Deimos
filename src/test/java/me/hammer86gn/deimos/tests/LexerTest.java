package me.hammer86gn.deimos.tests;

import me.hammer86gn.deimos.lexer.Lexer;
import org.junit.jupiter.api.Test;

public class LexerTest {

    @Test
    public void errorTest() {
        Lexer.getInstance().init("Test", "'\\aHello String'");
        Lexer.getInstance().lex();
    }

    @Test
    public void generateLex() {
        Lexer.getInstance().init("Test", "local a = 'Hello'\nlocal b = 'World'");
        Lexer.getInstance().lex();
    }

    @Test
    public void lexerStressTest() {
        String code = "local function avocado()\nlocal a = 'hello'\nprint(a..'World')\nend";
        Lexer.getInstance().init("Test", code);
        Lexer.getInstance().lex();
    }

}
