package me.hammer86gn.deimos.tests;

import me.hammer86gn.deimos.lexer.Lexer;
import me.hammer86gn.deimos.parser.Parser;
import org.junit.jupiter.api.Test;

// FIXME(Chloe): AssignVarNodes that are not local break :sob:
public class ParserTest {

    @Test
    public void basicParserVariableTest() {
        Lexer lexer = Lexer.getInstance();
        Parser parser = Parser.getInstance();

        lexer.init("Test", "local a = 'How are you'");
        lexer.lex();

        parser.init(lexer);
        parser.parse();
    }

    @Test
    public void basicParserNonLocalVariableTest() {
        Lexer lexer = Lexer.getInstance();
        Parser parser = Parser.getInstance();

        lexer.init("Test", "a = 1");
        lexer.lex();

        parser.init(lexer);
        parser.parse();
    }

    @Test
    public void basicParserVariableTest2() {
        Lexer lexer = Lexer.getInstance();
        Parser parser = Parser.getInstance();

        lexer.init("Test", "local a = 1 + 101");
        lexer.lex();

        parser.init(lexer);
        parser.parse();
    }


    @Test
    public void lessBasicParserVariableTest() {
        Lexer lexer = Lexer.getInstance();
        Parser parser = Parser.getInstance();

        lexer.init("Test", "local a = 2 + 3 * 4");
        lexer.lex();

        parser.init(lexer);
        parser.parse();
    }

    @Test
    public void lesslessBasicParserVariableTest() {
        Lexer lexer = Lexer.getInstance();
        Parser parser = Parser.getInstance();

        lexer.init("Test", "local a = 2 + 3 * 4 - 2");
        lexer.lex();

        parser.init(lexer);
        parser.parse();
    }


    @Test
    public void functionDeclareTest() {
        Lexer lexer = Lexer.getInstance();
        Parser parser = Parser.getInstance();

        lexer.init("Test", "function test_function() \nend");
        lexer.lex();

        parser.init(lexer);
        parser.parse();
    }

    @Test
    public void functionDeclareTestWithBody() {
        Lexer lexer = Lexer.getInstance();
        Parser parser = Parser.getInstance();

        lexer.init("Test", "function test_function()\nlocal a = 'b'\nend");
        lexer.lex();

        parser.init(lexer);
        parser.parse();
    }

    @Test
    public void functionCallTest() {
        Lexer lexer = Lexer.getInstance();
        Parser parser = Parser.getInstance();

        lexer.init("Test", "print('a')");
        lexer.lex();

        parser.init(lexer);
        parser.parse();
    }


}
