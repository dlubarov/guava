package b.exp;

import b.*;

public class MemberAccess extends Expression {
    public final Expression target;
    public final String memberName;

    public MemberAccess(Expression target, String memberName) {
        this.target = target;
        this.memberName = memberName;
    }

    @Override
    public c.exp.Expression refine(TypeDef typeCtx, MethodDef methodCtx) {
        if (target instanceof Variable) {
            String var = ((Variable) target).name;
            if (typeCtx.typeExists(var))
                return new c.exp.StaticFieldGet(typeCtx.qualifyType(var), memberName);
        }
        return new c.exp.InstanceFieldGet(target.refine(typeCtx, methodCtx), memberName);
    }

    @Override
    public String toString() {
        return target + "." + memberName;
    }
}
