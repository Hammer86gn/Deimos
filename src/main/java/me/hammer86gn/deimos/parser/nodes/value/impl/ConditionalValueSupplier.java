package me.hammer86gn.deimos.parser.nodes.value.impl;

import me.hammer86gn.deimos.lexer.LexerTokenType;
import me.hammer86gn.deimos.parser.nodes.value.ValueSupplier;

public class ConditionalValueSupplier implements ValueSupplier {

    private final ValueSupplier value;
    private final ValueSupplier value1;
    private final LexerTokenType type;

    public ConditionalValueSupplier(ValueSupplier value, ValueSupplier value1, LexerTokenType type) {
        this.value = value;
        this.value1 = value1;
        this.type = type;

        if (!type.isConditional()) {
            throw new RuntimeException();
        }

    }

    @Override
    public <U> U getValue() {
        return (U) this;
    }
}
