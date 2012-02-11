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
    public boolean hasType(Type type, CodeContext ctx) {
        // TODO: give type a getConcreteSubtypes method, check if any of them have types
        // Sequence[T] or Collection[T] and each element has type T. This will handle code like
        //   [M > Sequence[Int]]
        //   Unit foo[M]() {
        //     M s = {1, 2, 3};
        //   }
        search:
        for (ParameterizedType concreteType : type.getConcreteSupertypes(ctx.type, ctx.method)) {
            RawType rawType = concreteType.rawType;
            if (rawType.equals(RawType.coreSequence) ||
                rawType.equals(RawType.coreCollection)) {
                if (concreteType.genericArgs.length != 1)
                    throw new NiftyException("wrong number of generic args for %s", rawType);
                Type genericArg = concreteType.genericArgs[0];
                // Check if each element conforms to type genericArg
                for (Expression elem : elements)
                    if (!elem.hasType(genericArg, ctx))
                        continue search;
                return true;
            }
        }
        return super.hasType(type, ctx);
    }

    @Override
    public Type inferType(CodeContext ctx) {
        Type[] elementTypes = new Type[elements.length];
        for (int i = 0; i < elementTypes.length; ++i)
            elementTypes[i] = elements[i].inferType(ctx);
        Type elementType = TypeUtils.union(elementTypes);
        return new ParameterizedType(RawType.coreSequence, new Type[] {elementType});
    }

    @Override
    public CodeTree compile(CodeContext ctx) {
        return new CodeTree(
                Expression.compileAll(elements, ctx),
                Opcodes.CREATE_SEQ, elements.length
        );
    }

    @Override
    public String toString() {
        return '{' + implode(", ", elements) + '}';
    }
}
