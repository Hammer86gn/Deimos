package me.hammer86gn.deimos.parser.node;

import me.hammer86gn.deimos.parser.node.util.ValueSupplier;

public class OperationNode extends AbstractNode {

    private OperationType type;
    private ValueSupplier value;
    private ValueSupplier value1;

    // IDEA(Chloe), if I instead use a linked list for values that could maybe make this less cluttered
    public OperationNode(AbstractNode node, OperationType type, ValueSupplier valueSupplier, ValueSupplier valueSupplier1) {
        super(node);
        this.type = type;
        this.value = valueSupplier;
        this.value1 = valueSupplier1;
    }

    public OperationNode() {
        this(null, null, null, null);
    }

    @Override
    public String stringify() {
        return "name=Operation, type=%s, value=%s, value1=%s".formatted(this.type.name(), this.value.getValue(), this.value1 == null ? "none" : this.value1.getValue());
    }

    @Override
    public String toString() {
        return this.stringify();
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    public void setValue(ValueSupplier value) {
        this.value = value;
    }

    public void setValue1(ValueSupplier value) {
        this.value1 = value;
    }

    public OperationType getType() {
        return this.type;
    }

    public ValueSupplier getValue() {
        return this.value;
    }

    public ValueSupplier getValue1() {
        return this.value1;
    }

    public enum OperationType {
        EQUAL,

        ADD,
        ADD_EQUAL,

        SUBTRACT,
        SUBTRACT_EQUAL,

        MULTIPLY,
        MULTIPLY_EQUAL,

        DIVIDE,
        DIVIDE_EQUAL,

        MODULO,
        POWER,
        LENGTH,
        LOGICAL_AND,
        LOGICAL_NOT,
        LOGICAL_OR,
        SHIFT_LEFT,
        SHIFT_RIGHT,
        FLOOR,
        ;

        public static OperationType getTypeFromTokenType(me.hammer86gn.deimos.lexer.LexerTokenType lexerTokenType) {
            if (!lexerTokenType.isSymbol())
                return null;


            for (OperationType type : OperationType.values()) {
                if (type.name().equalsIgnoreCase(lexerTokenType.name())) {
                    return type;
                }
            }
            return null;
        }
    }

}
