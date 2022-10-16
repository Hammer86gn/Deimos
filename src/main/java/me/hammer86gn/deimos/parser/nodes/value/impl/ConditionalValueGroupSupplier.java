package me.hammer86gn.deimos.parser.nodes.value.impl;

import me.hammer86gn.deimos.lexer.LexerTokenType;
import me.hammer86gn.deimos.parser.nodes.value.ValueSupplier;

public class ConditionalValueGroupSupplier extends ConditionalValueSupplier {

    public ConditionalValueGroupSupplier(ConditionalValueSupplier value, ConditionalValueSupplier value1, LexerTokenType type) {
        super(value, value1, type);

        if (type != LexerTokenType.AND && type != LexerTokenType.OR && type != null) {
            throw new RuntimeException();
        }

    }



}
