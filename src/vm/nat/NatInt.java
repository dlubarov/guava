package vm.nat;

import common.*;
import vm.*;
import vm.ty.ConcreteType;

public class NatInt extends ZObject {
    public static final NativeType TYPE;

    static {
        TYPE = new NatIntType();
    }

    public int value;

    public NatInt(int value) {
        super(new ConcreteType(TYPE));
        this.value = value;
    }

    private static class NatIntType extends NativeType {
        private static final RawTypeDesc desc = new RawTypeDesc("core", "Int");

        private static final FullTypeDesc[] intOnly = new FullTypeDesc[] {new NormalFullTypeDesc(desc)};
        private static final FullTypeDesc[] charOnly = new FullTypeDesc[] {new NormalFullTypeDesc(new RawTypeDesc("core", "Char"))};
        private static final FullTypeDesc[] objOnly = new FullTypeDesc[] {new NormalFullTypeDesc(new RawTypeDesc("core", "Object"))};

        NatIntType() {
            super(desc,
                new Method[] {
                    // Constructors
                    new NativeMethod(new RawMethodDesc("core", "Int", "init", 0, charOnly)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            NatInt i = (NatInt) stack[bp + 1];
                            char c = ((NatChar) stack[bp + 2]).value;
                            i.value = c;
                            // TODO: return unit
                        }
                    },
                    // Prefix
                    new NativeMethod(new RawMethodDesc("core", "Int", "+", 0, FullTypeDesc.NONE)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            stack[bp + 1] = new NatInt(+n);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", "-", 0, FullTypeDesc.NONE)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            stack[bp + 1] = new NatInt(-n);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", "~", 0, FullTypeDesc.NONE)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            stack[bp + 1] = new NatInt(~n);
                        }
                    },
                    // Infix
                    new NativeMethod(new RawMethodDesc("core", "Int", "+", 0, intOnly)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            int m = ((NatInt) stack[bp + 2]).value;
                            stack[bp + 1] = new NatInt(n + m);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", "-", 0, intOnly)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            int m = ((NatInt) stack[bp + 2]).value;
                            stack[bp + 1] = new NatInt(n - m);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", "*", 0, intOnly)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            int m = ((NatInt) stack[bp + 2]).value;
                            stack[bp + 1] = new NatInt(n * m);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", "/", 0, intOnly)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            int m = ((NatInt) stack[bp + 2]).value;
                            stack[bp + 1] = new NatInt(n / m);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", "%", 0, intOnly)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            int m = ((NatInt) stack[bp + 2]).value;
                            stack[bp + 1] = new NatInt(n % m);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", "&", 0, intOnly)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            int m = ((NatInt) stack[bp + 2]).value;
                            stack[bp + 1] = new NatInt(n & m);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", "|", 0, intOnly)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            int m = ((NatInt) stack[bp + 2]).value;
                            stack[bp + 1] = new NatInt(n | m);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", "^", 0, intOnly)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            int m = ((NatInt) stack[bp + 2]).value;
                            stack[bp + 1] = new NatInt(n ^ m);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", "<<", 0, intOnly)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            int m = ((NatInt) stack[bp + 2]).value;
                            stack[bp + 1] = new NatInt(n << m);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", ">>", 0, intOnly)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            int m = ((NatInt) stack[bp + 2]).value;
                            stack[bp + 1] = new NatInt(n >> m);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", "<=", 0, intOnly)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            int m = ((NatInt) stack[bp + 2]).value;
                            stack[bp + 1] = new NatBool(n <= m);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", ">=", 0, intOnly)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            int m = ((NatInt) stack[bp + 2]).value;
                            stack[bp + 1] = new NatBool(n >= m);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", "<", 0, intOnly)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            int m = ((NatInt) stack[bp + 2]).value;
                            stack[bp + 1] = new NatBool(n < m);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", ">", 0, intOnly)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            int m = ((NatInt) stack[bp + 2]).value;
                            stack[bp + 1] = new NatBool(n > m);
                        }
                    },
                    // Object methods
                    new NativeMethod(new RawMethodDesc("core", "Int", "==", 0, objOnly)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            try {
                                int m = ((NatInt) stack[bp + 2]).value;
                                stack[bp + 1] = new NatBool(n == m);
                            } catch (ClassCastException e) {
                                stack[bp + 1] = NatBool.FALSE;
                            }
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", "hashCode", 0, FullTypeDesc.NONE)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            stack[bp + 1] = new NatInt(n);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", "toString", 0, FullTypeDesc.NONE)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            String s = Integer.toString(n);
                            stack[bp + 1] = God.makeString(s);
                        }
                    },
                },
                0);
        }

        public ZObject rawInstance(ConcreteType[] genericArgs) {
            assert genericArgs.length == 0;
            return new NatInt(0);
        }
    }
}
