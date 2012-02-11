package a.exp;

public abstract class Expression {
    public static final Expression[] NONE = new Expression[0];

    public abstract b.exp.Expression refine();

    public static b.exp.Expression[] refineAll(Expression[] expressions) {
        b.exp.Expression[] result = new b.exp.Expression[expressions.length];
        for (int i = 0; i < result.length; ++i)
            result[i] = expressions[i].refine();
        return result;
    }
}
