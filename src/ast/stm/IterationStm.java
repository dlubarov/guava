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
        Type iteratorType = new Type("Iterator", new Type[] {var.type});
        String iteratorName = "@iterator_" + uniqueID;

        Type maybeType = new Type("Maybe", new Type[] {var.type});
        String maybeName = "@maybeElem_" + uniqueID;

        Statement equiv = new BlockStm(
                new LocalDefStm(
                        new TypedVar(iteratorType, iteratorName),
                        new Invocation(
                                new MemberAccess(iterable, "iterator"),
                                new Type[0], new Expression[0])),
                new LocalDefStm(
                        new TypedVar(maybeType, maybeName),
                        new Invocation(
                                new MemberAccess(new VarExp(iteratorName), "next"),
                                new Type[0], new Expression[0])),
                new WhileStm(
                        new Invocation(
                                new MemberAccess(new VarExp(maybeName), "isSomething"),
                                new Type[0], new Expression[0]),
                        new BlockStm(
                                new LocalDefStm(
                                        var,
                                        new Invocation(
                                                new MemberAccess(new VarExp(maybeName), "get"),
                                                new Type[0], new Expression[0])),
                                body,
                                new ExpStm(new AssignmentExp(
                                        new VarExp(maybeName),
                                        new Invocation(
                                                new MemberAccess(new VarExp(iteratorName), "next"),
                                                new Type[0], new Expression[0])))))
        );
        return equiv.refine(ctx);
    }

    public String toString() {
        return String.format("for (%s : %s)\n%s",
                var, iterable, body);
    }
}
