package c.stm;

import c.CodeContext;

public abstract class Statement {
    public abstract CompilationResult compile(CodeContext ctx);

    @Override
    public abstract String toString();
}
