package a.exp;

import a.Type;

public class InfixOperation extends Expression {
    public final Expression left;
    public final String operator;
    public final Expression right;

    public InfixOperation(Expression left, String operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    private boolean rightAssociative() {
        return operator.equals(":");
    }

    @Override
    public b.exp.Expression refine() {
        Expression target, arg;
        if (rightAssociative()) {
            target = right;
            arg = left;
        } else {
            target = left;
            arg = right;
        }
        return new Invocation(
                new MemberAccess(target, operator),
                Type.NONE, new Expression[] {arg}
        ).refine();
    }

    @Override
    public String toString() {
        return String.format("(%s %s %s)", left, operator, right);
    }
}
