package c;

import common.*;

import c.ty.Type;

public class FieldDef {
    public final RawType owner;
    public final FieldVisibility visibility;
    public final boolean isStatic;
    public final Type type;
    public final String name;

    public FieldDef(RawType owner, FieldVisibility visibility, boolean isStatic, Type type, String name) {
        this.owner = owner;
        this.visibility = visibility;
        this.isStatic = isStatic;
        this.type = type;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        try {
            FieldDef that = (FieldDef) o;
            return owner.equals(that.owner)
                    && isStatic == that.isStatic
                    && type.equals(that.type)
                    && name.equals(that.name);
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return owner.hashCode() ^ name.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s %s;", type, name);
    }
}
