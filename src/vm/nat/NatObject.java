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

    public final int value;

    public NatObject(int value) {
        super(TYPE);
        this.value = value;
    }

    @SuppressWarnings("serial")
    private static class NatObjectType extends NativeType {
        private static final RawTypeDesc desc = new RawTypeDesc("core", "Int");

        private static final FullTypeDesc[] intOnly = new FullTypeDesc[] {new NormalFullTypeDesc(desc)};
        private static final FullTypeDesc[] objOnly = new FullTypeDesc[] {new NormalFullTypeDesc(new RawTypeDesc("core", "Object"))};

        NatObjectType() {
            super(desc, new RawTypeDesc[0],
                new Method[] {
                    new NativeMethod(new RawMethodDesc("core", "Int", "==", 0, objOnly)) {
                        public void invoke(ZObject[] stack, int bp) {
                            int n = ((NatObject) stack[bp + 1]).value;
                            try {
                                int m = ((NatObject) stack[bp + 2]).value;
                                stack[bp + 1] = new NatBool(n == m);
                            } catch (ClassCastException e) {
                                stack[bp + 1] = NatBool.FALSE;
                            }
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", "hashCode", 0, FullTypeDesc.none)) {
                        public void invoke(ZObject[] stack, int bp) {
                            int n = ((NatObject) stack[bp + 1]).value;
                            stack[bp + 1] = new NatObject(n);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", "toString", 0, FullTypeDesc.none)) {
                        public void invoke(ZObject[] stack, int bp) {
                            int n = ((NatObject) stack[bp + 1]).value;
                            String s = Integer.toString(n);
                            stack[bp + 1] = null; // FIXME: create String
                        }
                    },
                },
                new HashMap<RawMethodDesc, RawMethodDesc>(),
                0);
        }

        public ZObject rawInstance() {
            return new NatObject(0);
        }
    }
}
