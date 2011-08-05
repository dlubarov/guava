package rst.exp;

import common.FullTypeDesc;
import common.NormalFullTypeDesc;
import rctx.CodeRCtx;

import static util.StringUtils.implode;

public class NewObject extends Expression {
    private final NormalFullTypeDesc type;
    private final Expression[] args;
    
    public NewObject(NormalFullTypeDesc type, Expression[] args) {
        this.type = type;
        this.args = args;
    }

    public FullTypeDesc inferType(CodeRCtx ctx) {
        return type;
    }

    public String toString() {
        return String.format("new %s(%s)", type, implode(", ", args));
    }
}
