package me.hammer86gn.deimos.parser.nodes;

import java.util.List;

public class ClosureNode extends AbstractParserNode {

    ClosureNode(AbstractParserNode parent, List<AbstractParserNode> children) {
        this.parent = parent;
        this.children = children;
    }

    @Override
    public AbstractParserNode clone() {
        return new ClosureNode(this.parent, this.children);
    }
}
