package b;

import common.FieldVisibility;

public class FieldDef {
    public final FieldVisibility visibility;
    public final boolean isStatic, isReadOnly;
    public final Type type;
    public final String name;

    public FieldDef(FieldVisibility visibility, boolean isStatic, boolean isReadOnly, Type type, String name) {
        this.visibility = visibility;
        this.isStatic = isStatic;
        this.isReadOnly = isReadOnly;
        this.type = type;
        this.name = name;
    }

    public c.FieldDef refine(TypeDef typeCtx) {
        return new c.FieldDef(typeCtx.desc, visibility, isStatic, isReadOnly, type.refine(typeCtx, null), name);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(visibility);
        if (isStatic)
            sb.append("static ");
        if (isReadOnly)
            sb.append("readonly ");
        sb.append(type).append(' ').append(name).append(';');
        return sb.toString();
    }
}
