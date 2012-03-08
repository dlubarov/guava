package c.exp;

import java.util.*;

import common.*;

import c.*;
import c.ty.*;
import d.Opcodes;

public class InstanceFieldGet extends Expression {
    public final Expression target;
    public final String fieldName;

    public InstanceFieldGet(Expression target, String fieldName) {
        this.target = target;
        this.fieldName = fieldName;
    }

    FieldDef getField(CodeContext ctx) {
        Type targetType = target.inferType(ctx);
        Set<FieldDef> options = new HashSet<FieldDef>();
        for (ParameterizedType concreteSuper : targetType.getConcreteSupertypes(ctx.type, ctx.method))
            try {
                TypeDef superDef = Project.singleton.resolve(concreteSuper.rawType);
                options.add(superDef.getInstanceField(fieldName));
            } catch (NoSuchElementException e) {}
        if (options.isEmpty())
            throw new NoSuchElementException(String.format(
                    "%s has no field '%s'. Its type is %s.",
                    target, fieldName, targetType));
        if (options.size() > 1)
            throw new NiftyException("%s inherits multiple fields named %s", target, fieldName);
        return options.iterator().next();
    }

    @Override
    public Type inferType(CodeContext ctx) {
        FieldDef field = getField(ctx);
        ParameterizedType targetAsFieldOwner = target.inferType(ctx).asSupertype(field.owner, ctx);
        return field.type.withGenericArgs(targetAsFieldOwner.genericArgs, null);
    }

    @Override
    public CodeTree compile(Type requiredType, CodeContext ctx) {
        getField(ctx); // Ensure that the field exists.
        int fieldNameIndex = ctx.method.getStringTableIndex(fieldName);
        return new CodeTree(
                target.compile(ctx),
                Opcodes.GET_INSTANCE_FIELD, fieldNameIndex);
    }

    @Override
    public String toString() {
        return String.format("%s.%s", target, fieldName);
    }
}
