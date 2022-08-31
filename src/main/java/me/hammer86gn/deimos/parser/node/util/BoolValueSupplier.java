package me.hammer86gn.deimos.parser.node.util;


public class BoolValueSupplier implements ValueSupplier {

    private final Boolean b;

    public BoolValueSupplier(boolean b) {
        this.b = b;
    }

    public BoolValueSupplier(String b) {
        this.b = Boolean.parseBoolean(b);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> U getValue() {
        return (U) this.b;
    }
}
