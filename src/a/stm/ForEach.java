package a.stm;

import static util.StringUtils.indent;

import common.VariableGenerator;

import a.Type;
import a.exp.*;

public class ForEach extends Statement {
    public final Type elemType;
    public final String elemName;
    public final Expression iterable;
    public final Statement body;

    public ForEach(Type elemType, String elemName, Expression iterable, Statement body) {
        this.elemType = elemType;
        this.elemName = elemName;
        this.iterable = iterable;
        this.body = body;
    }

    @Override
    public b.stm.Statement refine() {
        /*
         * A foreach loop like
         *     for (ElemType elemName : iterable)
         *         body
         * is sugar for (roughly)
         *     {
         *         Source[ElemType] iter = iterable.iterator();
         *         ElemType elemName = iter.take();
         *         while (elemName.nonEmpty()) {
         *             body
         *             elemName = iter.take();
         *         }
         *     }
         */
        String iterId = VariableGenerator.randomId("iter");
        Variable iterVar = new Variable(iterId);
        Variable elemVar = new Variable(elemName);

        return new Block(
                new LocalDef(
                        new Type("Source", new Type[] {elemType}), iterId,
                        new Invocation(
                                new MemberAccess(iterable, "iterator"),
                                Type.NONE, Expression.NONE
                        )
                ),
                new LocalDef(
                        elemType, elemName,
                        new Invocation(
                                new MemberAccess(iterVar, "take"),
                                Type.NONE, Expression.NONE
                        )
                ),
                new While(
                        new Invocation(
                                new MemberAccess(elemVar, "nonEmpty"),
                                Type.NONE, Expression.NONE
                        ),
                        new Block(
                                body,
                                new Evaluation(
                                        new Assignment(
                                                elemVar,
                                                new Invocation(
                                                        new MemberAccess(iterVar, "take"),
                                                        Type.NONE, Expression.NONE)
                                        )
                                )
                        )
                )
        ).refine();
    }

    @Override
    public String toString() {
        String header = String.format("foreach (%s %s in %s)",
                elemType, elemName, iterable);
        if (body instanceof Block)
            return header + " " + body;
        return header + "\n" + indent(body);
    }
}
