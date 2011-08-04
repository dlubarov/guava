package ast.stm;

import ast.*;
import ast.exp.*;
import ctx.CodeContext;

import static util.StringUtils.indent;

public class ForEachStm extends Statement {
    private final TypedVar elem;
    private final Expression source;
    private final Statement body;

    public ForEachStm(TypedVar elem, Expression source, Statement body) {
        this.elem = elem;
        this.source = source;
        this.body = body;
    }

    public RefineResult refine(CodeContext ctx) {
        // TODO: implement foreach
        throw new UnsupportedOperationException();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("for (").append(elem).append(" in ").append(source).append(")");
        if (body instanceof BlockStm)
            sb.append(' ').append(body);
        else
            sb.append('\n').append(indent(body));
        return sb.toString();
    }
}
