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
         *         Source[ElemType] iter = iterable.enumerator();
         *         Maybe[ElemType] elemName = iter.tryTake();
         *         while (!elemName.isEmpty()) {
         *             body
         *             elemName = iter.tryTake();
         *         }
         *     }
         */
        String iterId = VariableGenerator.randomId("enumerator");
        String maybeId = VariableGenerator.randomId("maybe");
        Variable iterVar = new Variable(iterId);
        Variable maybeVar = new Variable(maybeId);

        return new Block(
                new LocalDef(
                        new Type("Source", new Type[] {elemType}), iterId,
                        new Invocation(
                                new MemberAccess(iterable, "enumerator"),
                                Type.NONE, Expression.NONE
                        )
                ),
                new LocalDef(
                        new Type("Maybe", new Type[] {elemType}), maybeId,
                        new Invocation(
                                new MemberAccess(iterVar, "tryTake"),
                                Type.NONE, Expression.NONE
                        )
                ),
                new While(
                        new PrefixOperation(
                                "!",
                                new Invocation(
                                        new MemberAccess(maybeVar, "isEmpty"),
                                        Type.NONE, Expression.NONE
                                )
                        ),
                        new Block(
                                new LocalDef(
                                        elemType, elemName,
                                        new Invocation(
                                                new MemberAccess(maybeVar, "get"),
                                                Type.NONE, Expression.NONE
                                        )
                                ),
                                body,
                                new Evaluation(
                                        new Assignment(
                                                maybeVar,
                                                new Invocation(
                                                        new MemberAccess(iterVar, "tryTake"),
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
