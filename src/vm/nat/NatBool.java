package vm.nat;

import common.FullTypeDesc;
import common.NormalFullTypeDesc;
import common.RawMethodDesc;
import common.RawTypeDesc;
import vm.Method;
import vm.NativeMethod;
import vm.NativeType;
import vm.ZObject;
import vm.ty.ConcreteType;

import java.util.HashMap;

public class NatBool extends ZObject {
    public static final NativeType TYPE;
    public static final NatBool TRUE, FALSE;

    static {
        TYPE = new NatBoolType();
        TRUE = new NatBool(true);
        FALSE = new NatBool(false);
    }

    public final boolean value;

    public NatBool(boolean value) {
        super(new ConcreteType(TYPE));
        this.value = value;
    }

    @SuppressWarnings("serial")
    private static class NatBoolType extends NativeType {
        private static final RawTypeDesc desc = new RawTypeDesc("core", "Bool");

        private static final FullTypeDesc[] boolOnly = new FullTypeDesc[] {new NormalFullTypeDesc(desc)};
        private static final FullTypeDesc[] objOnly = new FullTypeDesc[] {new NormalFullTypeDesc(new RawTypeDesc("core", "Object"))};

        NatBoolType() {
            super(desc,
                new Method[]{
                    // Prefix methods
                    new NativeMethod(new RawMethodDesc("core", "Bool", "!", 0, FullTypeDesc.none)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            boolean b = ((NatBool) stack[bp + 1]).value;
                            stack[bp + 1] = new NatBool(!b);
                        }
                    },

                    // Infix methods
                    new NativeMethod(new RawMethodDesc("core", "Bool", "&", 0, boolOnly)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            boolean a = ((NatBool) stack[bp + 1]).value;
                            boolean b = ((NatBool) stack[bp + 2]).value;
                            stack[bp + 1] = new NatBool(a & b);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Bool", "|", 0, boolOnly)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            boolean a = ((NatBool) stack[bp + 1]).value;
                            boolean b = ((NatBool) stack[bp + 2]).value;
                            stack[bp + 1] = new NatBool(a | b);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Bool", "^", 0, boolOnly)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            boolean a = ((NatBool) stack[bp + 1]).value;
                            boolean b = ((NatBool) stack[bp + 2]).value;
                            stack[bp + 1] = new NatBool(a ^ b);
                        }
                    },

                    // Object methods
                    new NativeMethod(new RawMethodDesc("core", "Bool", "==", 0, objOnly)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            boolean a = ((NatBool) stack[bp + 1]).value;
                            try {
                                boolean b = ((NatBool) stack[bp + 2]).value;
                                stack[bp + 1] = new NatBool(a == b);
                            } catch (ClassCastException e) {
                                stack[bp + 1] = NatBool.FALSE;
                            }
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Bool", "hashCode", 0, FullTypeDesc.none)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            boolean b = ((NatBool) stack[bp + 1]).value;
                            stack[bp + 1] = new NatInt(b? 1 : 0);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Bool", "toString", 0, FullTypeDesc.none)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            boolean b = ((NatBool) stack[bp + 1]).value;
                            String s = Boolean.toString(b);
                            stack[bp + 1] = null; // FIXME: create String
                            throw new RuntimeException("impl");
                        }
                    },
                },

                new HashMap<RawMethodDesc, RawMethodDesc>() {{
                    put(new RawMethodDesc("core", "Object", "==", 0, objOnly),
                        new RawMethodDesc("core", "Bool", "==", 0, objOnly));
                    put(new RawMethodDesc("core", "Object", "hashCode", 0, FullTypeDesc.none),
                        new RawMethodDesc("core", "Bool", "hashCode", 0, FullTypeDesc.none));
                    put(new RawMethodDesc("core", "Object", "toString", 0, FullTypeDesc.none),
                        new RawMethodDesc("core", "Bool", "toString", 0, FullTypeDesc.none));
                }},
                0);
        }

        public ZObject rawInstance(ConcreteType[] genericArgs) {
            assert genericArgs.length == 0;
            return new NatBool(false);
        }
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }
}
