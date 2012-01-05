package a.exp;

public abstract class Expression {
    public static final Expression[] NONE = new Expression[0];

    public abstract b.exp.Expression refine();
}
