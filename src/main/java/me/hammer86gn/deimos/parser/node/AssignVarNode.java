package me.hammer86gn.deimos.parser.node;

public class AssignVarNode extends AbstractNode {

    private String name;
    private boolean local;
    private OperationNode node;

    public AssignVarNode(AbstractNode parent, String name, boolean local, OperationNode node) {
        super(parent);
        this.name = name;
        this.local = local;
        this.node = node;
    }

    public AssignVarNode(AbstractNode parent) {
        this(parent, null, false, null);
    }

    public AssignVarNode() {
        this(null);
    }

    @Override
    public String stringify() {
        return "name=ASSIGN_VAR, var=%s, local=%s, operation={%s}".formatted(this.name, this.local, this.node.stringify());
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNode(OperationNode node) {
        this.node = node;
    }

    public String getName() {
        return this.name;
    }

    public OperationNode getNode() {
        return this.node;
    }

    public boolean isLocal() {
        return this.local;
    }
}
