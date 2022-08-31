/*

 * This class is very messy, but it works for now and that's really my aim for the project at this point in time.
 *
 */
package me.hammer86gn.deimos.parser;

import me.hammer86gn.deimos.Deimos;
import me.hammer86gn.deimos.lexer.Lexer;
import me.hammer86gn.deimos.lexer.LexerToken;
import me.hammer86gn.deimos.lexer.LexerTokenType;
import me.hammer86gn.deimos.parser.node.AbstractNode;
import me.hammer86gn.deimos.parser.node.AssignVarNode;
import me.hammer86gn.deimos.parser.node.ClosureNode;
import me.hammer86gn.deimos.parser.node.OperationNode;
import me.hammer86gn.deimos.parser.node.util.BoolValueSupplier;
import me.hammer86gn.deimos.parser.node.util.FloatValueSupplier;
import me.hammer86gn.deimos.parser.node.util.IntegerValueSupplier;
import me.hammer86gn.deimos.parser.node.util.NilValueSupplier;
import me.hammer86gn.deimos.parser.node.util.OperationValueSupplier;
import me.hammer86gn.deimos.parser.node.util.StringValueSupplier;
import me.hammer86gn.deimos.parser.node.util.ValueSupplier;
import me.hammer86gn.deimos.parser.node.util.VariableValueSupplier;
import me.hammer86gn.deimos.util.Pair;

import java.util.LinkedList;

public class Parser {
    private static Parser instance;

    private int index = 0;
    private boolean initialized = false;

    private LexerToken current = null;
    private ClosureNode root = null;
    private boolean findingNode = false;

    private boolean local = false;
    private AbstractNode currentWorking = null;

    private String sourceName = "";
    private LinkedList<LexerToken> lexerTokens = null;

    private LinkedList<ParserError> parserErrors = null;

    private Parser() {
        Parser.instance = this;
    }

    public void init(Lexer lexer) {
        this.index = 0;
        this.initialized = true;

        this.current = null;
        this.root = new ClosureNode();
        this.findingNode = true;

        this.local = false;
        this.currentWorking = null;

        this.sourceName = lexer.getSourceName();
        this.lexerTokens = lexer.getTokens();

        this.parserErrors = new LinkedList<>();
    }

    public void parse() {
        int length = this.lexerTokens.size();
        while (this.index < length) {
            this.current = this.lexerTokens.get(this.index);

            if (this.findingNode) {
                this.findNode();
            } else {
               this.continueBridge();
            }

            this.index += 1;
        }
    }

    private void findNode() {
        LexerTokenType type = this.current.type();
        switch (type) {
            case LOCAL -> {
                this.local = true;
                if (this.peek().type() != LexerTokenType.IDENTIFIER && this.peek().type() != LexerTokenType.FUNCTION) {
                    this.parserErrors.add(new ParserError(this.current, "Undefined use of Local"));
                }
            }
            case IDENTIFIER -> {
                if (this.peek().type() == LexerTokenType.EQUAL) {
                    AssignVarNode node = new AssignVarNode(this.root);
                    node.setLocal(this.local);
                    node.setName(this.current.context());
                    node.setNode(new OperationNode());
                    this.currentWorking = node;
                    this.findingNode = false;
                }
            }
        }


    }

    private void continueBridge() {
        if (this.currentWorking instanceof AssignVarNode) {
            this.continueAssignVarNode();
        }
    }

    private void continueAssignVarNode() {
        AssignVarNode node = (AssignVarNode) this.currentWorking;
        OperationNode operation = node.getNode();

        this.createOperationNode(operation);

        node.setNode(operation);
        this.currentWorking = node;

       this.finish();
    }

    private void createOperationNode(OperationNode operation) {
        int braceIndex = -1;
        boolean finish = false;

        while (!finish) {
            ValueSupplier supplier = null;
            LexerToken braceToken = this.peek(this.index + braceIndex);

            if (operation.getType() != null) {
                if (this.peek(this.index + braceIndex + 1).type().isSymbol()) {
                    this.index += braceIndex;
                    OperationNode node = new OperationNode();
                    this.createOperationNode(node);
                    supplier = new OperationValueSupplier(node);

                    if (operation.getValue() == null) {
                        operation.setValue(supplier);

                        int increase = 1;
                        if (node.getValue() != null) {
                            increase += 1;
                        }
                        if (node.getValue1() != null) {
                            increase += 1;
                        }

                        if (this.peek(this.index + braceIndex + 1).type() == LexerTokenType.END_OF_LINE) {
                            finish = true;
                        }

                        braceIndex += increase;
                    } else {
                        operation.setValue1(supplier);
                        finish = true;
                    }
                }
            }

            if (!finish) {
                if (braceToken.type().isSymbol()) {
                    if (operation.getType() == null) {
                        operation.setType(OperationNode.OperationType.getTypeFromTokenType(braceToken.type()));
                    } else {
                        OperationNode node = new OperationNode();
                        this.createOperationNode(node);
                        supplier = new OperationValueSupplier(node);
                    }
                } else {
                    switch (braceToken.type()) {
                        case END_OF_LINE -> finish = true;

                        case INT -> supplier = new IntegerValueSupplier(Integer.parseInt(braceToken.context()));
                        case FLOAT -> supplier = new FloatValueSupplier(Integer.parseInt(braceToken.context()));
                        case STRING -> {
                            String content = braceToken.context();
                            content = content.substring(1, content.length() - 1);
                            supplier = new StringValueSupplier(content);
                        }
                        case TRUE, FALSE -> supplier = new BoolValueSupplier(braceToken.type().name().toLowerCase());
                        case NIL -> supplier = new NilValueSupplier();
                        case IDENTIFIER -> {
                            if (this.peek().type() == LexerTokenType.OPEN_PARENTH) {
                                // TODO(Chloe):
                                //  function call will be here once function calls get implemented into the lexer
                            } else {
                                supplier = new VariableValueSupplier(braceToken.context());
                            }
                        }
                    }
                }
            }

            if (operation.getValue() == null) {
                operation.setValue(supplier);
            } else {
                operation.setValue1(supplier);

                if (supplier != null) {
                    finish = true;
                }
            }

            if (this.peek(this.index + braceIndex + 1).type() == LexerTokenType.END_OF_LINE) {
                finish = true;
            }

            braceIndex += 1;
        }
        this.index += braceIndex;
    }

    private void finish() {
        if (Deimos.DEIMOS_ENABLE_DEBUG) {
            Deimos.getLogger().info("[PARSER] Finished Node: " + this.currentWorking.stringify());
        }

        this.root.appendChild(this.currentWorking);
        this.currentWorking = null;

        this.local = false;
        this.findingNode = true;
    }

    private LexerToken peek() {
        if (this.index + 1 >= this.lexerTokens.size()) {
            return new LexerToken(LexerTokenType.END_OF_LINE, new Pair<>(Long.MAX_VALUE - 1, Long.MAX_VALUE - 1), "");
        }
        return this.lexerTokens.get(this.index + 1);
    }

    private LexerToken peek(int a) {
        if (this.index + a >= this.lexerTokens.size()) {
            return new LexerToken(LexerTokenType.END_OF_LINE, new Pair<>(Long.MAX_VALUE - 1, Long.MAX_VALUE - 1), "");
        }
        return this.lexerTokens.get(this.index + a);
    }


    public static Parser getInstance() {
        return Parser.instance == null ? new Parser() : Parser.instance;
    }
}
