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

    @Override
    public Type inferType(CodeContext ctx) {
        Type targetType = target.inferType(ctx);
        ParameterizedType[] concreteSuperTypes = targetType.getConcreteSupertypes(ctx.type, ctx.method);
        assert concreteSuperTypes.length > 0 : "list of concrete supertypes should at least contain core.Top";

        Set<Type> requiredTypes = new HashSet<Type>();
        for (ParameterizedType concreteSuperType : concreteSuperTypes) {
            try {
                TypeDef superTypeDef = ctx.project.resolve(concreteSuperType.rawType);
                FieldDef fieldDef = superTypeDef.getInstanceField(fieldName);
                Type[] genericArgs = concreteSuperType.asSupertype(fieldDef.owner, ctx).genericArgs;
                Type fieldType = fieldDef.type.withGenericArgs(genericArgs, null);
                requiredTypes.add(fieldType);
            } catch (NoSuchElementException e) {}
        }

        try {
            return TypeUtils.intersectionNoBottom(
                    requiredTypes.toArray(new Type[requiredTypes.size()]),
                    ctx.type, ctx.method);
        } catch (IllegalArgumentException e) {
            throw new NiftyException("what type should %s's %s field have?", targetType, fieldName);
        }
    }

    @Override
    public String toString() {
        return String.format("%s.%s", target, fieldName);
    }
}
