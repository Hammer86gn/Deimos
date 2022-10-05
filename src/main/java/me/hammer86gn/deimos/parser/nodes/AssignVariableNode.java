package me.hammer86gn.deimos.parser.nodes;

import me.hammer86gn.deimos.parser.nodes.value.ValueSupplier;

import java.util.List;

public class AssignVariableNode extends AbstractParserNode {

    private boolean local;
    private String variableName;
    private ValueSupplier value;

    public AssignVariableNode(AbstractParserNode parent, boolean local, String variableName, ValueSupplier value) {
        super(parent);
        this.local = local;
        this.variableName = variableName;
        this.value = value;
    }

    public AssignVariableNode(AbstractParserNode parent, String variableName, ValueSupplier value) {
        this(parent, false, variableName, value);
    }

    public AssignVariableNode(AbstractParserNode parent) {
        this(parent, null, null);
    }

    public AssignVariableNode() {
        this(null);
    }

    AssignVariableNode(AbstractParserNode parent, List<AbstractParserNode> children, boolean local, String variableName, ValueSupplier supplier) {
        super.parent = parent;
        super.children = children;

        this.local = local;
        this.variableName = variableName;
        this.value = supplier;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public void setValue(ValueSupplier value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "AssignVariableNode{ local: %s, variableName: %s, value: %s }".formatted(this.local, this.variableName, this.value.toString());
    }

    @Override
    public AssignVariableNode clone() {
        AssignVariableNode clone = new AssignVariableNode(super.parent, super.children, this.local, this.variableName, this.value);
        return clone;
    }
}
