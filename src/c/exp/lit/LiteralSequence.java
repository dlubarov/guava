package c.exp.lit;

import static util.StringUtils.*;

import common.*;

import c.*;
import c.exp.Expression;
import c.ty.*;
import d.Opcodes;

public class LiteralSequence extends Expression {
    public final Expression[] elements;

    public LiteralSequence(Expression[] elements) {
        this.elements = elements;
    }

    // If type is an Array, Collection, etc., get its element type.
    private static Type getElemType(Type type, CodeContext ctx) {
        try {
            return type.asSupertype(RawType.coreEnumerable, ctx).genericArgs[0];
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private Type inferElemType(CodeContext ctx) {
        Type[] elementTypes = new Type[elements.length];
        for (int i = 0; i < elementTypes.length; ++i)
            elementTypes[i] = elements[i].inferType(ctx);
        return TypeUtils.union(elementTypes);
    }

    @Override
    public ParameterizedType inferType(CodeContext ctx) {
        return new ParameterizedType(RawType.coreArray, new Type[] {inferElemType(ctx)});
    }

    @Override
    public boolean hasType(Type type, CodeContext ctx) {
        Type elemType = getElemType(type, ctx);
        if (elemType != null) {
            Type myType = new ParameterizedType(RawType.coreArray, new Type[] {elemType});
            if (!myType.isSubtype(type, ctx))
                return false; // type is something like List[elemType]
            for (Expression elem : elements)
                if (!elem.hasType(elemType, ctx))
                    return false;
            return true;
        }
        return super.hasType(type, ctx);
    }

    @Override
    public CodeTree compile(Type requiredType, CodeContext ctx) {
        Type elementType = getElemType(requiredType, ctx);
        if (elementType == null)
            elementType = inferElemType(ctx);
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
