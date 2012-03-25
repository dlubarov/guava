package a.stm;

import static util.StringUtils.indent;

import a.Type;
import a.exp.*;
import a.exp.lit.LiteralInt;
import common.VariableGenerator;

public class Repeat extends Statement {
    public final Expression count;
    public final Statement body;

    public Repeat(Expression count, Statement body) {
        this.count = count;
        this.body = body;
    }

    @Override
    public b.stm.Statement refine() {
        String counterId = VariableGenerator.randomId("rep");
        Variable counterVar = new Variable(counterId);
        return new Block(
                new LocalDef(new Type("Int"), counterId, count),
                new While(
                        new InfixOperation(counterVar, ">", new LiteralInt(0)),
                        new Block(
                                body,
                                new Evaluation(new Assignment(
                                        counterVar,
                                        new InfixOperation(counterVar, "-", new LiteralInt(1))
                                ))
                        )
                )
        ).refine();
    }

    @Override
    public String toString() {
        if (body instanceof Block)
            return String.format("repeat (%s) %s", count, body);
        return String.format("repeat (%s)\n%s", count, indent(body));
    }
}
