package a.stm;

public abstract class Statement {
    public abstract b.stm.Statement refine();

    public static b.stm.Statement[] refineAll(Statement[] statements) {
        b.stm.Statement[] result = new b.stm.Statement[statements.length];
        for (int i = 0; i < result.length; ++i)
            result[i] = statements[i].refine();
        return result;
    }
}
