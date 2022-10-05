package me.hammer86gn.deimos.parser;

import me.hammer86gn.deimos.lexer.LexerToken;
import me.hammer86gn.deimos.lexer.LexerTokenType;
import me.hammer86gn.deimos.parser.nodes.AbstractParserNode;
import me.hammer86gn.deimos.parser.nodes.AssignVariableNode;
import me.hammer86gn.deimos.parser.nodes.ClosureNode;
import me.hammer86gn.deimos.parser.nodes.value.ValueSupplier;
import me.hammer86gn.deimos.parser.nodes.value.impl.BooleanValueSupplier;
import me.hammer86gn.deimos.parser.nodes.value.impl.ConditionalValueSupplier;
import me.hammer86gn.deimos.parser.nodes.value.impl.FloatValueSupplier;
import me.hammer86gn.deimos.parser.nodes.value.impl.IntegerValueSupplier;
import me.hammer86gn.deimos.parser.nodes.value.impl.OperationValueSupplier;
import me.hammer86gn.deimos.parser.nodes.value.impl.StringValueSupplier;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

public class Parser {

    private static Parser instance;

    private int index;
    private boolean finding;

    private LexerToken current;
    private LinkedList<LexerToken> lexerTokens;

    private boolean local;

    private ClosureNode rootClosure;
    private ClosureNode currentClosure;

    private AbstractParserNode currentNode;

    private Parser() {
        Parser.instance = this;
    }

    public void parse() {
        int tokenLen = this.lexerTokens.size();
        while (tokenLen < this.index) {
            if (this.finding) {
                this.findNode();
            } else {
                this.continueNode();
            }

            this.index += 1;
            this.current = this.lexerTokens.get(this.index);
        }
    }

    private void findNode() {
        LexerTokenType type = this.current.type();
        switch (type) {
            case IDENTIFIER -> {
                if (this.peek().type() == LexerTokenType.EQUAL) {
                    /* AssignVariable */
                    this.currentNode = new AssignVariableNode(this.currentClosure, this.local, this.current.context(), null);
                    this.index += 1;
                }
            }
        }
    }

    private void continueNode() {
        if (this.currentNode == null)
            return;

        if (this.currentNode instanceof AssignVariableNode) {
            this.continueAssignVariableNode();
        }
    }

    private void continueAssignVariableNode() {
        ValueSupplier supplier = this.createValueSupplier();
        ((AssignVariableNode) this.currentNode).setValue(supplier);

        this.finish();
    }


    private ValueSupplier createValueSupplier() {
        // TODO(Chloe): Conditional Value Suppliers e.g. a == 1; 2 <= 1.

        switch (this.current.type()) {
            case FLOAT, INT -> {
                LexerToken peekToken = this.peek();
                if (peekToken != null && peekToken.type().isOperation()) {
                    // OperationValue
                    return this.createOperationValueSupplier(0);
                } else if (peekToken != null && peekToken.type().isConditional()) {
                    // ConditionalValue
                    return this.createConditionalValueSupplier(0);
                } else  {
                    // NumberValue either float or int
                    if (this.current.type() == LexerTokenType.INT) {
                        return new IntegerValueSupplier(Long.parseLong(this.current.context()));
                    } else {
                        return new FloatValueSupplier(Double.parseDouble(this.current.context()));
                    }
                }
            }

            case STRING -> {
                String value = this.current.context().substring(1, this.current.context().length() - 1);

                LexerToken peekToken = this.peek();
                if (peekToken != null && peekToken.type() == LexerTokenType.CONCATENATE) {
                    value = this.concatString(value);
                } else if (peekToken != null && peekToken.type().isConditional()) {
                    // ConditionalValue
                    return this.createConditionalValueSupplier(0);
                }

                return new StringValueSupplier(value);
            }

            case TRUE, FALSE -> {
                boolean value = this.current.type() == LexerTokenType.TRUE;
                return new BooleanValueSupplier(value);
            }

        }


        return null;
    }

    private OperationValueSupplier createOperationValueSupplier(int a) {
        OperationValueSupplier rootOperation = new OperationValueSupplier(null, null, null);

        boolean endSearch = false;
        int increase = a;

        while (!endSearch) {
            LexerToken peekToken = this.lexerTokens.get(this.index + increase);
            if (peekToken == null || peekToken.type() == LexerTokenType.END_OF_LINE) {
                endSearch = true;
                break;
            }

            if (peekToken.type() == LexerTokenType.INT || peekToken.type() == LexerTokenType.FLOAT) {
                // add number
                if (rootOperation.getOperand() == null) {
                    if (peekToken.type() == LexerTokenType.INT) {
                        rootOperation.setOperand(new IntegerValueSupplier(Long.parseLong(peekToken.context())));
                    } else {
                        rootOperation.setOperand(new FloatValueSupplier(Double.parseDouble(peekToken.context())));
                    }
                } else {
                    LexerToken peekPeekToken = this.lexerTokens.get(this.index + increase + 1);
                    if (peekPeekToken != null && peekPeekToken.type().isOperation()) {
                        // extended operation
                        rootOperation.setOperand1(this.createOperationValueSupplier(increase));
                    } else {
                        // second number

                        if (peekToken.type() == LexerTokenType.INT) {
                            rootOperation.setOperand(new IntegerValueSupplier(Long.parseLong(peekToken.context())));
                        } else {
                            rootOperation.setOperand(new FloatValueSupplier(Double.parseDouble(peekToken.context())));
                        }
                    }

                }

            }

            if (peekToken.type().isOperation()) {
                if (rootOperation.getType() != null) {
                    // TODO(Chloe) Error
                }

                rootOperation.setType(this.peek().type());
            }

            increase += 1;
        }

        this.index += increase;
        return rootOperation;
    }

    private ConditionalValueSupplier createConditionalValueSupplier(int a) {
        ConditionalValueSupplier rootCondition = new ConditionalValueSupplier(null, null, null);

        boolean endSearch = false;
        int increase = a;

        while (!endSearch) {
            LexerToken peekToken = this.lexerTokens.get(this.index + increase);
            if (peekToken == null || peekToken.type() == LexerTokenType.END_OF_LINE) {
                endSearch = true;
                break;
            }

             //  TODO(Chloe): this

        }

        this.index += increase;
        return rootCondition;
    }

    private String concatString(String a) {
        StringBuilder sb = new StringBuilder(a);

        boolean endSearch = false;
        int increase = 0;

        while (!endSearch) {
            LexerToken peekToken = this.lexerTokens.get(this.index + increase);
            if (peekToken == null || peekToken.type() == LexerTokenType.END_OF_LINE) {
                endSearch = true;
                break;
            }
            if (peekToken.type() == LexerTokenType.STRING && this.lexerTokens.get(this.index + increase + 1).type() != LexerTokenType.CONCATENATE) {
                endSearch = true;
            }

            if (peekToken.type() == LexerTokenType.CONCATENATE) {
                // good
            } else if (peekToken.type() == LexerTokenType.STRING) {
                sb.append(peekToken.context().substring(1, peekToken.context().length() - 1));
            } else {
                // TODO(Chloe) Error
            }

            increase += 1;
        }

        this.index += increase;
        return sb.toString();
    }


    private @Nullable LexerToken checkout(int index) {
        if (this.lexerTokens.size() < index)
            return null;
        return this.lexerTokens.get(index);
    }

    private @Nullable LexerToken peek() {
        if (this.lexerTokens.size() < this.index + 1)
            return null;

        return this.lexerTokens.get(this.index + 1);
    }

    private void finish() {
        this.currentClosure.appendChild(this.currentNode.clone());
        this.currentNode = null;

        this.local = false;
        this.finding = true;
    }

}
