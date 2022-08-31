package me.hammer86gn.deimos.parser.node.util;

public class VariableValueSupplier implements ValueSupplier {

    private final String varname;

    public VariableValueSupplier(String varname) {
        this.varname = varname;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> U getValue() {
        return (U) this.varname;
    }
}
