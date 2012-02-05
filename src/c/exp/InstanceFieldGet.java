package c.exp;

import java.util.*;

import common.*;

import c.*;
import c.ty.*;

public class InstanceFieldGet extends Expression {
    public final Expression target;
    public final String fieldName;

    public InstanceFieldGet(Expression target, String fieldName) {
        this.target = target;
        this.fieldName = fieldName;
    }

    private FieldDef getField(CodeContext ctx) {
        Type targetType = target.inferType(ctx);
        Set<FieldDef> options = new HashSet<FieldDef>();
        for (ParameterizedType concreteSuper : targetType.getConcreteSupertypes(ctx.type, ctx.method))
            try {
                options.add(ctx.project.resolve(concreteSuper.rawType).getInstanceField(fieldName));
            } catch (NoSuchElementException e) {}
        if (options.isEmpty())
            throw new NiftyException("%s has no field named %s", target, fieldName);
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
    public String toString() {
        return String.format("%s.%s", target, fieldName);
    }
}
