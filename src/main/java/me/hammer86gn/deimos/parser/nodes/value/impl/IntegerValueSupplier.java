package me.hammer86gn.deimos.parser.nodes.value.impl;

import me.hammer86gn.deimos.parser.nodes.value.ValueSupplier;

public class IntegerValueSupplier implements ValueSupplier {

    private final long value;

    public IntegerValueSupplier(int value) {
        this.value = value;
    }

    public IntegerValueSupplier(long value) {
        this.value = value;
    }

    @Override
    public <U> U getValue() {
        return (U) Long.valueOf(this.value);
    }
}
