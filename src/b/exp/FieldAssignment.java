package b.exp;

import b.*;

public class FieldAssignment extends Expression {
    public final Expression target;
    public final String fieldName;
    public final Expression value;

    public FieldAssignment(Expression target, String fieldName, Expression value) {
        this.target = target;
        this.fieldName = fieldName;
        this.value = value;
    }

    @Override
    public c.exp.Expression refine(TypeDef typeCtx, MethodDef methodCtx) {
        c.exp.Expression refinedValue = value.refine(typeCtx, methodCtx);
        if (target instanceof Variable) {
            String var = ((Variable) target).name;
            if (typeCtx.typeExists(var))
                return new c.exp.StaticFieldAssignment(typeCtx.qualifyType(var), fieldName, refinedValue);
        }
        return new c.exp.InstanceFieldAssignment(
                target.refine(typeCtx, methodCtx),
                fieldName,
                refinedValue);
    }

    @Override
    public String toString() {
        return String.format("%s.%s = %s", target, fieldName, value);
    }
}
