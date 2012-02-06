package a.exp;

import a.Type;

public class PrefixOperation extends Expression {
    public final String operator;
    public final Expression target;

    public PrefixOperation(String operator, Expression target) {
        this.operator = operator;
        this.target = target;
    }

    @Override
    public b.exp.Expression refine() {
        return new Invocation(
                new MemberAccess(target, operator),
                Type.NONE, Expression.NONE
        ).refine();
    }

    @Override
    public String toString() {
        return String.format("%s%s", operator, target);
    }
}
