package me.hammer86gn.deimos.parser.node;

import java.util.LinkedList;

public class ClosureNode extends AbstractNode {

    private LinkedList<AbstractNode> children = new LinkedList<>();

    public void appendChild(AbstractNode node) {
        this.children.add(node);
    }

    public boolean hasChildren() {
        return this.children.size() > 0;
    }

    public LinkedList<AbstractNode> getChildren() {
        return this.children;
    }

    @Override
    public String stringify() {
        StringBuilder sb = new StringBuilder();
        sb.append("name=closure, data=[");

        this.children.forEach(child -> {
            sb.append("{");
            sb.append(child.stringify());
            sb.append("}");
        });

        sb.append("]");

        return sb.toString();
    }
}
