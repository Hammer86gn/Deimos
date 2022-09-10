package me.hammer86gn.deimos.parser.node;

public class FunctionDeclareNode extends AbstractNode {

    private boolean local;

    private String functionLabel;

    private String[] argNames;
    private long argCount;

    private ClosureNode closure;

    public FunctionDeclareNode(AbstractNode parent, String functionLabel, boolean local, String[] argNames, ClosureNode closure) {
        super(parent);
        this.functionLabel = functionLabel;
        this.local = local;
        this.argNames = argNames;
        this.argCount = argNames.length;
        this.closure = closure;
    }

    public FunctionDeclareNode(String functionLabel, boolean local, String[] argNames, ClosureNode closure) {
        this(null, functionLabel, local, argNames, closure);
    }

    public FunctionDeclareNode(AbstractNode parent) {
        super(parent);
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public void setFunctionLabel(String functionLabel) {
        this.functionLabel = functionLabel;
    }

    public void setClosure(ClosureNode closure) {
        this.closure = closure;
    }

    public void setArgs(String[] argNames) {
        this.argNames = argNames;
        this.argCount = argNames.length;
    }

    public void appendArg(String arg) {
        String[] newArgs = new String[this.argNames.length + 1];
        System.arraycopy(this.argNames, 0, newArgs, 0, this.argNames.length);
        newArgs[this.argNames.length + 1] = arg;
        this.argCount = newArgs.length;
        this.argNames = newArgs;

    }

    public ClosureNode getClosure() {
        return this.closure;
    }

    public long getArgCount() {
        return this.argCount;
    }

    public String getFunctionLabel() {
        return this.functionLabel;
    }

    public String[] getArgNames() {
        return this.argNames;
    }

    @Override
    public String stringify() {
        StringBuilder sb = new StringBuilder("name=FUNC_DECL")
                .append("type=")
                .append(this.local)
                .append("label=")
                .append(this.functionLabel)
                .append("args={");

        for (String argName :this.argNames){
            sb.append(argName).append(", ");
        }
        sb.append("}, ")
                .append("arg_count=")
                .append(this.argCount)
                .append(", Closure=")
                .append(this.closure.stringify());

        return sb.toString();
    }
}
