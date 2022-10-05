package me.hammer86gn.deimos.parser.nodes;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractParserNode implements Cloneable {

    protected AbstractParserNode parent;
    protected List<AbstractParserNode> children = new ArrayList<>();

    public AbstractParserNode(AbstractParserNode parent) {
        this.parent = parent;
    }

    public AbstractParserNode() {
        this(null);
    }

    public void appendChild(AbstractParserNode node) {
        this.children.add(node);
    }

    @Override
    public String toString() {
        return "{}";
    }
    @Override
    public abstract AbstractParserNode clone();
}
