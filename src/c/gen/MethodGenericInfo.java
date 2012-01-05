package c.gen;

import c.ty.Type;

public class MethodGenericInfo extends GenericInfo {
    public MethodGenericInfo(int index, Type[] subOf, Type[] supOf) {
        super(index, subOf, supOf);
    }

    @Override
    public String toString() {
        return 'M' + super.toString();
    }
}
