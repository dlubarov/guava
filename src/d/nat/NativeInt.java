package d.nat;

import common.*;

import d.*;
import d.ty.ConcreteType;
import d.ty.desc.*;

public class NativeInt extends NativeObject {
    public final static NativeTypeDef TYPE;

    public final int value;

    public NativeInt(int value) {
        super(new ConcreteType(TYPE));
        this.value = value;
    }

    static {
        TYPE = new NativeTypeDef(
                RawType.coreInt,
                Variance.NONE,

                // instance methods
                new NativeMethodDef[] {
                        // Unary methods
                        new NativeMethodDef(new RawMethod(false, RawType.coreInt, "+", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                int n = ((NativeInt) stack[bp + 1]).value;
                                stack[bp + 1] = new NativeInt(+n);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreInt, "-", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                int n = ((NativeInt) stack[bp + 1]).value;
                                stack[bp + 1] = new NativeInt(-n);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreInt, "~", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                int n = ((NativeInt) stack[bp + 1]).value;
                                stack[bp + 1] = new NativeInt(~n);
                            }
                        },

                        // Binary methods
                        new NativeMethodDef(new RawMethod(false, RawType.coreInt, "+", 0, TypeDesc.coreIntOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                int n = ((NativeInt) stack[bp + 1]).value;
                                int m = ((NativeInt) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeInt(n + m);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreInt, "-", 0, TypeDesc.coreIntOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                int n = ((NativeInt) stack[bp + 1]).value;
                                int m = ((NativeInt) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeInt(n - m);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreInt, "*", 0, TypeDesc.coreIntOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                int n = ((NativeInt) stack[bp + 1]).value;
                                int m = ((NativeInt) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeInt(n * m);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreInt, "/", 0, TypeDesc.coreIntOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                int n = ((NativeInt) stack[bp + 1]).value;
                                int m = ((NativeInt) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeInt(n / m);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreInt, "%", 0, TypeDesc.coreIntOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                int n = ((NativeInt) stack[bp + 1]).value;
                                int m = ((NativeInt) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeInt(n % m);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreInt, "&", 0, TypeDesc.coreIntOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                int n = ((NativeInt) stack[bp + 1]).value;
                                int m = ((NativeInt) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeInt(n & m);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreInt, "|", 0, TypeDesc.coreIntOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                int n = ((NativeInt) stack[bp + 1]).value;
                                int m = ((NativeInt) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeInt(n | m);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreInt, "^", 0, TypeDesc.coreIntOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                int n = ((NativeInt) stack[bp + 1]).value;
                                int m = ((NativeInt) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeInt(n ^ m);
                            }
                        },

                        // Comparisons
                        new NativeMethodDef(new RawMethod(false, RawType.coreInt, "<", 0, TypeDesc.coreIntOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                int n = ((NativeInt) stack[bp + 1]).value;
                                int m = ((NativeInt) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeBool(n < m);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreInt, ">", 0, TypeDesc.coreIntOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                int n = ((NativeInt) stack[bp + 1]).value;
                                int m = ((NativeInt) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeBool(n > m);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreInt, "<=", 0, TypeDesc.coreIntOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                int n = ((NativeInt) stack[bp + 1]).value;
                                int m = ((NativeInt) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeBool(n <= m);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreInt, ">=", 0, TypeDesc.coreIntOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                int n = ((NativeInt) stack[bp + 1]).value;
                                int m = ((NativeInt) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeBool(n >= m);
                            }
                        },

                        // equals, hashCode
                        new NativeMethodDef(new RawMethod(false, RawType.coreInt, "==", 0, TypeDesc.coreTopOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                int n = ((NativeInt) stack[bp + 1]).value;
                                BaseObject o = stack[bp + 2];
                                boolean result;
                                if (o instanceof NativeInt)
                                    result = n == ((NativeInt) o).value;
                                else
                                    result = false;
                                stack[bp + 1] = new NativeBool(result);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreInt, "hashCode", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                // We don't need to do anything, since n.hashCode() = n. :-)
                                // And Int is immutable, so we don't need to clone it.
                            }
                        },
                },

                // static methods
                new NativeMethodDef[] {
                });
    }
}
