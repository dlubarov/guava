package b;

import common.FieldVisibility;

public class FieldDef {
    public final FieldVisibility visibility;
    public final boolean isStatic;
    public final Type type;
    public final String name;

    public FieldDef(FieldVisibility visibility, boolean isStatic, Type type, String name) {
        this.visibility = visibility;
        this.isStatic = isStatic;
        this.type = type;
        this.name = name;
    }

    public c.FieldDef refine(TypeDef typeCtx) {
        return new c.FieldDef(typeCtx.desc, visibility, isStatic, type.refine(typeCtx, null), name);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (isStatic)
            sb.append("static ");
        sb.append(type).append(' ').append(name).append(';');
        return sb.toString();
    }
}
