package me.hammer86gn.deimos.parser.node.util;

public class NilValueSupplier implements ValueSupplier {

    @Override
    public <U> U getValue() {
        return null;
    }
}
