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

    private Expression proxy() {
        if (operator.equals("="))
            return new Assignment(left, right);

        Expression target, argument;
        if (operator.equals("+:")) {
            target = right;
            argument = left;
        } else {
            target = left;
            argument = right;
        }

        return new Invocation(
                new MemberAccess(target, operator),
                Type.NONE,
                new Expression[] {argument});
    }

    @Override
    public b.exp.Expression refine() {
        return proxy().refine();
    }

    @Override
    public String toString() {
        boolean parens = operator.endsWith("=");
        String format = parens ? "(%s %s %s)" : "%s %s %s";
        return String.format(format, left, operator, right);
    }
}
