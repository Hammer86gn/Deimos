package me.hammer86gn.deimos.parser.node;

import org.jetbrains.annotations.Nullable;

public abstract class AbstractNode {

    private @Nullable AbstractNode parent;

    public AbstractNode(AbstractNode parent) {
        this.parent = parent;
    }

    public AbstractNode() {
        this(null);
    }

    public void setParent(AbstractNode parent) {
        this.parent = parent;
    }

    public AbstractNode getParent() {
        return this.parent;
    }

    public abstract String stringify();

}
