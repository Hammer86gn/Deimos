package me.hammer86gn.deimos.parser.node.util;

public class IntegerValueSupplier implements ValueSupplier {

    private final int value;

    public IntegerValueSupplier(int value) {
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> U getValue() {
        return (U) Integer.valueOf(this.value);
    }
}
