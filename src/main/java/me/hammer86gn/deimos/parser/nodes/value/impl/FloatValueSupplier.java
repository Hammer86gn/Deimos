package me.hammer86gn.deimos.parser.nodes.value.impl;

import me.hammer86gn.deimos.parser.nodes.value.ValueSupplier;

public class FloatValueSupplier implements ValueSupplier {

    private final double value;

    public FloatValueSupplier(float value) {
        this.value = value;
    }

    public FloatValueSupplier(double value) {
        this.value = value;
    }

    @Override
    public <U> U getValue() {
        return (U) Double.valueOf(this.value);
    }
}
