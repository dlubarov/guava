package ast.exp;

import ctx.CodeContext;
import rst.exp.ClassFieldGet;
import rst.exp.InstanceFieldGet;

public class MemberAccess extends Expression {
    public final Expression target;
    public final String memberName;

    public MemberAccess(Expression target, String memberName) {
        this.target = target;
        this.memberName = memberName;
    }

    public rst.exp.Expression refine(CodeContext ctx) {
        if (target instanceof VarExp) {
            VarExp var = (VarExp) target;
            if (ctx.hasRawType(var.id))
                return new ClassFieldGet(ctx.resolveRaw(var.id), memberName);
        }
        return new InstanceFieldGet(target.refine(ctx), memberName);
    }

    public String toString() {
        return String.format("%s.%s", target, memberName);
    }
}
