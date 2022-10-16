package me.hammer86gn.deimos.parser.nodes.value.impl;

import me.hammer86gn.deimos.lexer.LexerTokenType;
import me.hammer86gn.deimos.parser.nodes.value.ValueSupplier;

public class ConditionalValueSupplier implements ValueSupplier {

    private ValueSupplier value;
    private ValueSupplier value1;
    private LexerTokenType type;

    public ConditionalValueSupplier(ValueSupplier value, ValueSupplier value1, LexerTokenType type) {
        this.value = value;
        this.value1 = value1;
        this.type = type;

        if (!type.isConditional()) {
            throw new RuntimeException();
        }

    }

    public void setType(LexerTokenType type) {
        this.type = type;
    }

    public void setValue(ValueSupplier value) {
        this.value = value;
    }

    public void setValue1(ValueSupplier value1) {
        this.value1 = value1;
    }

    public ValueSupplier getValue0() {
        return this.value;
    }

    public LexerTokenType getType() {
        return this.type;
    }

    public ValueSupplier getValue1() {
        return this.value1;
    }

    @Override
    public <U> U getValue() {
        return (U) this;
    }
}
