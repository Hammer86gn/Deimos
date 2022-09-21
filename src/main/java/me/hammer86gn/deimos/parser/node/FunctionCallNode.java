package me.hammer86gn.deimos.parser.node;

import me.hammer86gn.deimos.parser.node.util.ValueSupplier;

import java.util.ArrayList;
import java.util.List;

public class FunctionCallNode extends AbstractNode {

    private String functionName;
    private List<ValueSupplier> valueSuppliers;

    public FunctionCallNode(AbstractNode parent, String functionName, List<ValueSupplier> valueSuppliers) {
        super(parent);
        this.functionName = functionName;
        this.valueSuppliers = valueSuppliers;
    }

    public FunctionCallNode(AbstractNode parent, String functionName) {
        super(parent);
        this.functionName = functionName;
        this.valueSuppliers = new ArrayList<>();
    }

    public FunctionCallNode(AbstractNode parent) {
        super(parent);
        this.valueSuppliers = new ArrayList<>();
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public void addValue(ValueSupplier supplier) {
        this.valueSuppliers.add(supplier);
    }


    @Override
    public String stringify() {
        StringBuilder sb = new StringBuilder();
        for (ValueSupplier valueSupplier : this.valueSuppliers) {
            sb.append(valueSupplier.getValue().toString());
        }

        return "name=FUNC_CALL, function_name=%s, args=%s".formatted(this.functionName, sb.toString());
    }
}
