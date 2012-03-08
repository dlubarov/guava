package c.exp;

import c.*;
import c.ty.Type;
import common.*;
import d.Opcodes;

public class StaticFieldAssignment extends Expression {
    public final RawType owner;
    public final String fieldName;
    public final Expression value;

    public StaticFieldAssignment(RawType owner, String fieldName, Expression value) {
        this.owner = owner;
        this.fieldName = fieldName;
        this.value = value;
    }

    @Override
    public Type inferType(CodeContext ctx) {
        return Project.singleton.resolve(owner).getStaticField(fieldName).type;
    }

    @Override
    public CodeTree compile(Type requiredType, CodeContext ctx) {
        TypeDef ownerDef = Project.singleton.resolve(owner);
        int ownerIndex = ctx.method.getRawTypeTableIndex(owner);
        int fieldIndex = ownerDef.getStaticFieldIndex(fieldName);

        Type expectedType = ownerDef.getStaticField(fieldName).type;
        if (!value.hasType(expectedType, ctx))
            throw new NiftyException("'%s' does not conform to static field %s.%s's type %s.",
                    value, owner, fieldName, expectedType);

        return new CodeTree(
                value.compile(expectedType, ctx),
                Opcodes.DUP,
                Opcodes.PUT_STATIC_FIELD,
                ownerIndex, fieldIndex
        );
    }

    @Override
    public String toString() {
        return String.format("%s.%s = %s", owner, fieldName, value);
    }
}
