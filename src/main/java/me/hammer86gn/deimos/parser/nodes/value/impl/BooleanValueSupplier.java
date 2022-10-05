package me.hammer86gn.deimos.parser.nodes.value.impl;

import me.hammer86gn.deimos.parser.nodes.value.ValueSupplier;

public class BooleanValueSupplier implements ValueSupplier {

    private final boolean value;

    public BooleanValueSupplier(boolean value) {
        this.value = value;
    }

    @Override
    public <U> U getValue() {
        return (U) Boolean.valueOf(this.value);
    }
}
