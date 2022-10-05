package me.hammer86gn.deimos.parser.nodes.value.impl;

import me.hammer86gn.deimos.parser.nodes.value.ValueSupplier;

public class StringValueSupplier implements ValueSupplier {

    private final String value;

    public StringValueSupplier(String value) {
        this.value = value;
    }

    @Override
    public <U> U getValue() {
        return (U) this.value;
    }
}
