package me.hammer86gn.deimos.parser.node.util;

import me.hammer86gn.deimos.parser.node.OperationNode;

public class OperationValueSupplier implements ValueSupplier {

    private OperationNode value;

    public OperationValueSupplier(OperationNode value) {
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> U getValue() {
        return (U) this.value;
    }
}
