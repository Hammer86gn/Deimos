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
import me.hammer86gn.deimos.parser.node.FunctionCallNode;
import me.hammer86gn.deimos.parser.node.FunctionDeclareNode;
import me.hammer86gn.deimos.parser.node.OperationNode;
import me.hammer86gn.deimos.parser.node.util.BoolValueSupplier;
import me.hammer86gn.deimos.parser.node.util.FloatValueSupplier;
import me.hammer86gn.deimos.parser.node.util.FunctionCallSupplier;
import me.hammer86gn.deimos.parser.node.util.IntegerValueSupplier;
import me.hammer86gn.deimos.parser.node.util.NilValueSupplier;
import me.hammer86gn.deimos.parser.node.util.OperationValueSupplier;
import me.hammer86gn.deimos.parser.node.util.StringValueSupplier;
import me.hammer86gn.deimos.parser.node.util.ValueSupplier;
import me.hammer86gn.deimos.parser.node.util.VariableValueSupplier;
import me.hammer86gn.deimos.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("DuplicatedCode")
public class Parser {
    private static Parser instance;

    private int index = 0;
    private boolean initialized = false;

    private int anonymized = 0;

    private LexerToken current = null;
    private ClosureNode root = null;
    private ClosureNode currentClosure = null;
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
        this.currentClosure = this.root;
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
                if (this.findNode()) {
                    return;
                }
            } else {
               this.continueBridge();
            }

            this.index += 1;
        }
    }

    @SuppressWarnings("ConstantConditions")
    private boolean findNode() {
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
                    AssignVarNode node = new AssignVarNode(this.currentClosure);
                    node.setLocal(this.local);
                    node.setName(this.current.context());
                    node.setNode(new OperationNode());
                    this.currentWorking = node;
                    this.findingNode = false;
                }
                if (this.peek().type() == LexerTokenType.OPEN_PARENTH) {
                    FunctionCallNode node = new FunctionCallNode(this.currentClosure);
                    node.setFunctionName(this.current.context());

                    this.currentWorking = node;
                    this.findingNode = false;
                }
            }
            case FUNCTION -> {
                FunctionDeclareNode node = new FunctionDeclareNode(this.currentClosure);
                node.setLocal(this.local);

                LexerToken peeked = this.peek();
                if (peeked.type() != LexerTokenType.IDENTIFIER || peeked.type() != LexerTokenType.OPEN_PARENTH) {
                    this.parserErrors.add(new ParserError(peeked, "Unexpected token following function declaration"));
                }

                node.setFunctionLabel(peeked.type() == LexerTokenType.IDENTIFIER ? peeked.context() : "anonymous$" + ++this.anonymized);
                this.index++;

                this.currentWorking = node;
                this.findingNode = false;
            }
            case END -> {
                return true;
            }
        }
        return false;
    }

    private void continueBridge() {
        if (this.currentWorking instanceof AssignVarNode) {
            this.continueAssignVarNode();
        }

        if (this.currentWorking instanceof FunctionDeclareNode) {
            this.continueFunctionDeclNode();
        }

        if (this.currentWorking instanceof FunctionCallNode) {
            this.createFunctionCallNode();
        }

    }

    private void continueFunctionDeclNode() {
        FunctionDeclareNode node = (FunctionDeclareNode) this.currentWorking;
        List<String> argNames = new ArrayList<>();
        ClosureNode funcClosure = new ClosureNode();

        if (this.current.type() == LexerTokenType.OPEN_PARENTH) {
            this.index++;

            while (this.peek(0).type() != LexerTokenType.CLSE_PARENTH) {
                if (this.current.type() == LexerTokenType.IDENTIFIER) {
                    argNames.add(this.peek(0).context());
                }
                this.index++;
            }

        }
        this.index++;
        node.setArgs(argNames.toArray(argNames.toArray(new String[0])));

        ClosureNode previousClosure = this.currentClosure;

        // new closure
        this.findingNode = true;
        this.currentClosure = funcClosure;

        this.parse();

        // fixing
        this.findingNode = false;
        this.currentClosure = previousClosure;
        this.currentWorking = node;

        node.setClosure(funcClosure);

        this.finish();
    }

    // NOTE(Chloe): Variable assign nodes mean there is already and equal sign
    private void continueAssignVarNode() {
        AssignVarNode node = (AssignVarNode) this.currentWorking;
        OperationNode operation = node.getNode();

        if (operation == null) {
            operation = new OperationNode();
        }

        this.createOperationNode(operation);

        node.setNode(operation);
        this.currentWorking = node;

       this.finish();
    }

    private void createOperationNode(OperationNode operationNode) {
        boolean finishedNode = false;
        int increase = 0;

        while(!finishedNode) {

            LexerToken token = this.peek(this.index + increase);
            if (token.type() == LexerTokenType.END_OF_LINE) {
                finishedNode = true;
            }

            if (token.type().isSymbol()) {

                if (operationNode.getValue1() != null) {
                    OperationNode depth = new OperationNode();
                    depth.setType(OperationNode.OperationType.getTypeFromTokenType(token.type()));
                    depth.setValue(operationNode.getValue1());
                    operationNode.setValue1(new OperationValueSupplier(depth));

                    finishedNode = true;
                } else {
                    operationNode.setType(OperationNode.OperationType.getTypeFromTokenType(token.type()));
                }

            } else {
                if (token.type() != LexerTokenType.END_OF_LINE) {
                    if (operationNode.getValue() == null) {
                        operationNode.setValue(this.createValueSupplierFromToken(token));
                    } else {
                        ValueSupplier supplier = this.createValueSupplierFromToken(token);
                        operationNode.setValue1(supplier);
                    }
                }
            }
            increase += 1;
        }

        if (operationNode.getType() == null) {
            operationNode.setType(OperationNode.OperationType.getTypeFromTokenType(this.peek(this.index + increase - 3).type()));
        }

        System.out.println("Finishing operation node: " + operationNode.stringify());

        this.index += increase;
    }

    private ValueSupplier createValueSupplierFromToken(LexerToken token) {
        ValueSupplier supplier = null;

        switch (token.type()) {
            case INT -> {
                supplier = new IntegerValueSupplier(Integer.parseInt(token.context()));
            }
            case FLOAT -> supplier = new FloatValueSupplier(Integer.parseInt(token.context()));
            case STRING -> {
                String content = token.context();
                content = content.substring(1, content.length() - 1);
                supplier = new StringValueSupplier(content);
            }
            case TRUE, FALSE -> supplier = new BoolValueSupplier(token.type().name().toLowerCase());
            case NIL -> supplier = new NilValueSupplier();
            case IDENTIFIER -> {
                if (this.peek().type() == LexerTokenType.OPEN_PARENTH) {
                    // TODO(Chloe)
                } else {
                    supplier = new VariableValueSupplier(token.context());
                }
            }
        }
        return supplier;
    }


    public void createFunctionCallNode() {
        FunctionCallNode node = (FunctionCallNode) this.currentWorking;

        if(this.current.type() == LexerTokenType.COMMA) {
            if (this.peek().type() == LexerTokenType.CLSE_PARENTH) {
                this.parserErrors.add(new ParserError(this.peek(), "Unexpected comma"));
            }
        }
        if (this.current.type() == LexerTokenType.CLSE_PARENTH) {
            this.finish();
        }

        ValueSupplier supplier = null;

        switch (this.current.type()) {
            case INT -> supplier = new IntegerValueSupplier(Integer.parseInt(this.current.context()));
            case FLOAT -> supplier = new FloatValueSupplier(Integer.parseInt(this.current.context()));
            case STRING -> {
                String content = this.current.context();
                content = content.substring(1, content.length() - 1);
                supplier = new StringValueSupplier(content);
            }
            case TRUE, FALSE -> supplier = new BoolValueSupplier(this.current.type().name().toLowerCase());
            case NIL -> supplier = new NilValueSupplier();
            case IDENTIFIER -> {
                if (this.peek().type() == LexerTokenType.OPEN_PARENTH) {
                    FunctionCallNode node1 = new FunctionCallNode(this.currentWorking);
                    node1.setFunctionName(this.current.context().substring(1, this.current.context().length() - 1));

                    supplier = this.createFunctionCallSupplier(node);
                } else {
                    supplier = new VariableValueSupplier(this.current.context());
                }
            }
        }

        node.addValue(supplier);
    }

    private FunctionCallSupplier createFunctionCallSupplier(FunctionCallNode node) {

        while(this.current.type() != LexerTokenType.CLSE_BRACE) {
            this.index += 1;
            this.current = this.lexerTokens.get(this.index);

            if (this.current.type() == LexerTokenType.COMMA) {
                if (this.peek().type() == LexerTokenType.CLSE_PARENTH) {
                    this.parserErrors.add(new ParserError(this.peek(), "Unexpected comma"));
                }
            }

            ValueSupplier supplier = null;

            switch (this.current.type()) {
                case INT -> supplier = new IntegerValueSupplier(Integer.parseInt(this.current.context()));
                case FLOAT -> supplier = new FloatValueSupplier(Integer.parseInt(this.current.context()));
                case STRING -> {
                    String content = this.current.context();
                    content = content.substring(1, content.length() - 1);
                    supplier = new StringValueSupplier(content);
                }
                case TRUE, FALSE -> supplier = new BoolValueSupplier(this.current.type().name().toLowerCase());
                case NIL -> supplier = new NilValueSupplier();
                case IDENTIFIER -> {
                    if (this.peek().type() == LexerTokenType.OPEN_PARENTH) {
                        FunctionCallNode node1 = new FunctionCallNode(this.currentWorking);
                        node1.setFunctionName(this.current.context().substring(1, this.current.context().length() - 1));

                        supplier = this.createFunctionCallSupplier(node);
                    } else {
                        supplier = new VariableValueSupplier(this.current.context());
                    }
                }
            }

            node.addValue(supplier);
        }

        return new FunctionCallSupplier(node);
    }

    private void finish() {
        if (Deimos.DEIMOS_ENABLE_DEBUG) {
            Deimos.getLogger().info("[PARSER] Finished Node: " + this.currentWorking.stringify());
        }

        this.currentClosure.appendChild(this.currentWorking);
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
