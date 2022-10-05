package me.hammer86gn.deimos.parser.nodes.value.impl;

import me.hammer86gn.deimos.lexer.LexerTokenType;
import me.hammer86gn.deimos.parser.nodes.value.ValueSupplier;

public class OperationValueSupplier implements ValueSupplier {

    private ValueSupplier operand;
    private ValueSupplier operand1;
    private LexerTokenType type;

    public OperationValueSupplier(ValueSupplier operand, ValueSupplier operand1, LexerTokenType type) {
        this.operand = operand;
        this.operand1 = operand1;
        this.type = type;

        if (!this.type.isOperation()) {
            throw new RuntimeException();
        }

    }

    public void setOperand(ValueSupplier operand) {
        this.operand = operand;
    }

    public void setOperand1(ValueSupplier operand1) {
        this.operand1 = operand1;
    }

    public void setType(LexerTokenType type) {
        this.type = type;

        if (!this.type.isOperation()) {
            throw new RuntimeException();
        }
    }

    public LexerTokenType getType() {
        return this.type;
    }

    public ValueSupplier getOperand() {
        return this.operand;
    }

    public ValueSupplier getOperand1() {
        return this.operand1;
    }

    @Override
    public <U> U getValue() {
        return (U) this;
    }

    @Override
    public String toString() {
        return "OperationValueSupplier{ operand: %s, operand1: %s, type: %s }".formatted(this.operand.toString(), this.operand1.toString(), this.type.name());
    }
}
