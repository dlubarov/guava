package vm.nat;

import common.*;
import vm.*;
import vm.ty.ConcreteType;

public class NatObject extends ZObject {
    public static final NativeType TYPE;

    static {
        TYPE = new NatObjectType();
    }

    public NatObject() {
        super(new ConcreteType(TYPE));
    }

    private static class NatObjectType extends NativeType {
        private static final RawTypeDesc desc = new RawTypeDesc("core", "Object");

        private static final FullTypeDesc[] objOnly = new FullTypeDesc[] {new NormalFullTypeDesc(new RawTypeDesc("core", "Object"))};

        NatObjectType() {
            super(desc, new NormalFullTypeDesc[0],
                new Method[] {
                    new NativeMethod(new RawMethodDesc("core", "Object", "==", 0, objOnly)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            ZObject obj1 = stack[bp + 1];
                            ZObject obj2 = stack[bp + 2];
                            stack[bp + 1] = new NatBool(obj1 == obj2);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Object", "hashCode", 0, FullTypeDesc.NONE)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            ZObject obj = stack[bp + 1];
                            int hash = obj.hashCode();
                            stack[bp + 1] = new NatInt(hash);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Object", "toString", 0, FullTypeDesc.NONE)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            ZObject obj = stack[bp + 1];
                            String s = obj.type.toString() + "@" + System.identityHashCode(obj);
                            stack[bp + 1] = God.makeString(s);
                        }
                    },
                },
                0);
        }

        public ZObject rawInstance(ConcreteType[] genericArgs) {
            assert genericArgs.length == 0;
            return new NatObject();
        }
    }
}
