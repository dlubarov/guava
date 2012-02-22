package c.exp;

import static util.StringUtils.*;

import common.*;

import c.*;
import c.ty.*;
import d.Opcodes;

public class LiteralSequence extends Expression {
    public final Expression[] elements;

    public LiteralSequence(Expression[] elements) {
        this.elements = elements;
    }

    @Override
    public ParameterizedType inferType(CodeContext ctx) {
        Type[] elementTypes = new Type[elements.length];
        for (int i = 0; i < elementTypes.length; ++i)
            elementTypes[i] = elements[i].inferType(ctx);
        Type elementType = TypeUtils.union(elementTypes);
        return new ParameterizedType(RawType.coreSequence, new Type[] {elementType});
    }

    @Override
    public CodeTree compile(CodeContext ctx) {
        Type elementType = inferType(ctx).genericArgs[0];
        return new CodeTree(
                Expression.compileAll(elements, ctx),
                Opcodes.CREATE_SEQ,
                ctx.method.getFullTypeTableIndex(elementType),
                elements.length
        );
    }

    @Override
    public String toString() {
        return '{' + implode(", ", elements) + '}';
    }
}
