package me.hammer86gn.deimos.parser.node.util;

public class StringValueSupplier implements ValueSupplier {

    private final String value;

    public StringValueSupplier(String value) {
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> U getValue() {
        return (U) this.value;
    }
}
