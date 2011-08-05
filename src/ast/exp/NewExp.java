package ast.exp;

import ast.Type;
import common.NormalFullTypeDesc;
import ctx.CodeContext;
import rst.exp.NewObject;

import static util.StringUtils.implode;

public class NewExp extends Expression {
    private final Type type;
    private final Expression[] args;

    public NewExp(Type type, Expression[] args) {
        this.type = type;
        this.args = args;
    }

    public rst.exp.Expression refine(CodeContext ctx) {
        NormalFullTypeDesc typeNormal;
        try {
            typeNormal = (NormalFullTypeDesc) ctx.resolveFull(type);
        } catch (ClassCastException e) {
            throw new RuntimeException(String.format("cannot instantiate %s", type));
        }
        
        rst.exp.Expression[] argsRef = new rst.exp.Expression[args.length];
        for (int i = 0; i < argsRef.length; ++i)
            argsRef[i] = args[i].refine(ctx);

        return new NewObject(typeNormal, argsRef);
    }

    public String toString() {
        return String.format("new %s(%s)", type, implode(", ", args));
    }
}
