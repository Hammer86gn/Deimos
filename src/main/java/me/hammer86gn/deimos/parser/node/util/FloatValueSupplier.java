package me.hammer86gn.deimos.parser.node.util;

public class FloatValueSupplier implements ValueSupplier {

    private final float value;

    public FloatValueSupplier(float value) {
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> U getValue() {
        return (U) Float.valueOf(this.value);
    }
}
