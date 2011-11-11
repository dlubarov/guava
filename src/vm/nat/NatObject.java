package vm.nat;

import common.FullTypeDesc;
import common.NormalFullTypeDesc;
import common.RawMethodDesc;
import common.RawTypeDesc;
import vm.Method;
import vm.NativeMethod;
import vm.NativeType;
import vm.ZObject;

import java.util.HashMap;

public class NatObject extends ZObject {
    public static final NativeType TYPE;

    static {
        TYPE = new NatObjectType();
    }

    public NatObject() {
        super(TYPE);
    }

    private static class NatObjectType extends NativeType {
        private static final RawTypeDesc desc = new RawTypeDesc("core", "Int");

        private static final FullTypeDesc[] objOnly = new FullTypeDesc[] {new NormalFullTypeDesc(new RawTypeDesc("core", "Object"))};

        NatObjectType() {
            super(desc, new RawTypeDesc[0],
                new Method[] {
                    new NativeMethod(new RawMethodDesc("core", "Object", "==", 0, objOnly)) {
                        public void invoke(ZObject[] stack, int bp) {
                            ZObject obj1 = stack[bp + 1];
                            ZObject obj2 = stack[bp + 2];
                            stack[bp + 1] = new NatBool(obj1 == obj2);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Object", "hashCode", 0, FullTypeDesc.none)) {
                        public void invoke(ZObject[] stack, int bp) {
                            ZObject obj = stack[bp + 1];
                            int hash = obj.hashCode();
                            stack[bp + 1] = new NatInt(hash);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Object", "toString", 0, FullTypeDesc.none)) {
                        public void invoke(ZObject[] stack, int bp) {
                            ZObject obj = stack[bp + 1];
                            stack[bp + 1] = null; // FIXME: create String
                        }
                    },
                },
                new HashMap<RawMethodDesc, RawMethodDesc>(),
                0);
        }

        public ZObject rawInstance() {
            return new NatObject();
        }
    }
}
