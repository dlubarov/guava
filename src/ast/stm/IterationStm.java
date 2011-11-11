package ast.stm;

import ast.*;
import ast.exp.*;
import ctx.CodeContext;

public class IterationStm extends Statement {
    private static int counter = 0;
    private final TypedVar var;
    private final Expression iterable;
    private final Statement body;
    private final int uniqueID;

    public IterationStm(TypedVar var, Expression iterable, Statement body) {
        this.var = var;
        this.iterable = iterable;
        this.body = body;
        uniqueID = counter++;
    }

    public RefineResult refine(CodeContext ctx) {
        // FIXME: implement foreach
        if (1+1==2) return new BlockStm().refine(ctx);
        
        Type iteratorType = new Type("Iterator", new Type[] {var.type});
        String iterableName = "@iterable_" + uniqueID;
        
        Statement equiv = new BlockStm(
                new LocalDefStm(
                        new TypedVar(iteratorType, iterableName),
                        new Invocation(
                                new MemberAccess(iterable, "iterator"),
                                new Type[0],
                                new Expression[0])),
                new WhileStm(
                        new Invocation(
                                new MemberAccess(new VarExp(iterableName), "hasNext"),
                                new Type[0],
                                new Expression[0]),
                        new BlockStm(
                                new LocalDefStm(
                                        var,
                                        new Invocation(
                                                new MemberAccess(new VarExp(iterableName), "next"),
                                                new Type[0],
                                                new Expression[0])),
                                body))
        );
        return equiv.refine(ctx);
    }

    public String toString() {
        return String.format("for (%s : %s)\n%s",
                var, iterable, body);
    }
}
