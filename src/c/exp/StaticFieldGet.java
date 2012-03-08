package c.exp;

import c.*;
import c.ty.Type;
import common.RawType;
import d.Opcodes;

public class StaticFieldGet extends Expression {
    public final RawType owner;
    public final String fieldName;

    public StaticFieldGet(RawType owner, String fieldName) {
        this.owner = owner;
        this.fieldName = fieldName;
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
        return new CodeTree(Opcodes.GET_STATIC_FIELD, ownerIndex, fieldIndex);
    }

    @Override
    public String toString() {
        return String.format("%s.%s", owner, fieldName);
    }
}
