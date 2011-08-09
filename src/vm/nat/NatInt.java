package vm.nat;

import common.*;
import vm.*;

import java.util.HashMap;

public class NatInt extends ZObject {
    public static final NativeType TYPE;

    static {
        TYPE = new NatIntType();
    }

    public final int value;

    public NatInt(int value) {
        super(TYPE);
        this.value = value;
    }

    private static class NatIntType extends NativeType {
        private static final RawTypeDesc desc = new RawTypeDesc("core", "Int");

        private static final FullTypeDesc[] intOnly = new FullTypeDesc[] {new NormalFullTypeDesc(desc)};
        private static final FullTypeDesc[] objOnly = new FullTypeDesc[] {new NormalFullTypeDesc(new RawTypeDesc("core", "Object"))};

        NatIntType() {
            super(desc,
                new Method[] {
                    // Prefix
                    new NativeMethod(new RawMethodDesc("core", "Int", "+", 0, FullTypeDesc.none)) {
                        public void invoke(ZObject[] stack, int bp) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            stack[bp + 1] = new NatInt(+n);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", "-", 0, FullTypeDesc.none)) {
                        public void invoke(ZObject[] stack, int bp) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            stack[bp + 1] = new NatInt(-n);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", "~", 0, FullTypeDesc.none)) {
                        public void invoke(ZObject[] stack, int bp) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            stack[bp + 1] = new NatInt(~n);
                        }
                    },
                    // Infix
                    new NativeMethod(new RawMethodDesc("core", "Int", "+", 0, intOnly)) {
                        public void invoke(ZObject[] stack, int bp) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            int m = ((NatInt) stack[bp + 2]).value;
                            stack[bp + 1] = new NatInt(n + m);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", "-", 0, intOnly)) {
                        public void invoke(ZObject[] stack, int bp) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            int m = ((NatInt) stack[bp + 2]).value;
                            stack[bp + 1] = new NatInt(n - m);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", "*", 0, intOnly)) {
                        public void invoke(ZObject[] stack, int bp) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            int m = ((NatInt) stack[bp + 2]).value;
                            stack[bp + 1] = new NatInt(n * m);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", "/", 0, intOnly)) {
                        public void invoke(ZObject[] stack, int bp) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            int m = ((NatInt) stack[bp + 2]).value;
                            stack[bp + 1] = new NatInt(n / m);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", "%", 0, intOnly)) {
                        public void invoke(ZObject[] stack, int bp) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            int m = ((NatInt) stack[bp + 2]).value;
                            stack[bp + 1] = new NatInt(n % m);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", "&", 0, intOnly)) {
                        public void invoke(ZObject[] stack, int bp) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            int m = ((NatInt) stack[bp + 2]).value;
                            stack[bp + 1] = new NatInt(n & m);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", "|", 0, intOnly)) {
                        public void invoke(ZObject[] stack, int bp) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            int m = ((NatInt) stack[bp + 2]).value;
                            stack[bp + 1] = new NatInt(n | m);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", "^", 0, intOnly)) {
                        public void invoke(ZObject[] stack, int bp) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            int m = ((NatInt) stack[bp + 2]).value;
                            stack[bp + 1] = new NatInt(n ^ m);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", "<<", 0, intOnly)) {
                        public void invoke(ZObject[] stack, int bp) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            int m = ((NatInt) stack[bp + 2]).value;
                            stack[bp + 1] = new NatInt(n << m);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", ">>", 0, intOnly)) {
                        public void invoke(ZObject[] stack, int bp) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            int m = ((NatInt) stack[bp + 2]).value;
                            stack[bp + 1] = new NatInt(n >> m);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", "<=", 0, intOnly)) {
                        public void invoke(ZObject[] stack, int bp) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            int m = ((NatInt) stack[bp + 2]).value;
                            stack[bp + 1] = new NatBool(n <= m);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", ">=", 0, intOnly)) {
                        public void invoke(ZObject[] stack, int bp) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            int m = ((NatInt) stack[bp + 2]).value;
                            stack[bp + 1] = new NatBool(n >= m);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", "<", 0, intOnly)) {
                        public void invoke(ZObject[] stack, int bp) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            int m = ((NatInt) stack[bp + 2]).value;
                            stack[bp + 1] = new NatBool(n < m);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", ">", 0, intOnly)) {
                        public void invoke(ZObject[] stack, int bp) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            int m = ((NatInt) stack[bp + 2]).value;
                            stack[bp + 1] = new NatBool(n > m);
                        }
                    },
                    // Object methods
                    new NativeMethod(new RawMethodDesc("core", "Int", "==", 0, objOnly)) {
                        public void invoke(ZObject[] stack, int bp) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            try {
                                int m = ((NatInt) stack[bp + 2]).value;
                                stack[bp + 1] = new NatBool(n == m);
                            } catch (ClassCastException e) {
                                stack[bp + 1] = NatBool.FALSE;
                            }
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", "hashCode", 0, FullTypeDesc.none)) {
                        public void invoke(ZObject[] stack, int bp) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            stack[bp + 1] = new NatInt(n);
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Int", "toString", 0, FullTypeDesc.none)) {
                        public void invoke(ZObject[] stack, int bp) {
                            int n = ((NatInt) stack[bp + 1]).value;
                            String s = Integer.toString(n);
                            stack[bp + 1] = null; // FIXME: create String
                        }
                    },
                },
                new HashMap<RawMethodDesc, RawMethodDesc>() {{
                    put(new RawMethodDesc("core", "Object", "==", 0, objOnly),
                        new RawMethodDesc("core", "Int", "==", 0, objOnly));
                    put(new RawMethodDesc("core", "Object", "hashCode", 0, FullTypeDesc.none),
                        new RawMethodDesc("core", "Int", "hashCode", 0, FullTypeDesc.none));
                    put(new RawMethodDesc("core", "Object", "toString", 0, FullTypeDesc.none),
                        new RawMethodDesc("core", "Int", "toString", 0, FullTypeDesc.none));
                }},
                0);
        }

        public ZObject rawInstance() {
            return new NatInt(0);
        }
    }
}
