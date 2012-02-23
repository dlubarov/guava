package d.nat;

import common.*;

import d.*;
import d.ty.ConcreteType;
import d.ty.desc.*;

public class NativeInt extends NativeObject {
    public static final NativeTypeDef TYPE;

    public int value;

    public NativeInt(int value) {
        super(new ConcreteType(TYPE));
        this.value = value;
    }

    public NativeInt() {
        this(0);
    }

    static {
        TYPE = new NativeTypeDef(
                RawType.coreInt,
                2, new String[0],

                // static methods
                new NativeMethodDef[] {
                },

                // instance methods
                new NativeMethodDef[] {
                        // Constructors
                        new NativeMethodDef(new RawMethod(false, RawType.coreInt, "init", 0, TypeDesc.coreCharOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                NativeInt n = (NativeInt) stack[bp + 1];
                                char c = ((NativeChar) stack[bp + 2]).value;
                                n.value = c;
                                stack[bp + 1] = VMUtils.getUnit();
                            }
                        },

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

                        // compareTo
                        new NativeMethodDef(new RawMethod(false, RawType.coreInt, "compareTo", 0, TypeDesc.coreIntOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                int n = ((NativeInt) stack[bp + 1]).value;
                                int m = ((NativeInt) stack[bp + 2]).value;
                                BaseObject rel;
                                if (n < m)
                                    rel = VMUtils.getLT();
                                else if (n > m)
                                    rel = VMUtils.getGT();
                                else
                                    rel = VMUtils.getEQ();
                                stack[bp + 1] = rel;
                            }
                        },

                        // ==, hashCode, toString
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
                        new NativeMethodDef(new RawMethod(false, RawType.coreInt, "toString", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                int n = ((NativeInt) stack[bp + 1]).value;
                                stack[bp + 1] = VMUtils.makeString(Integer.toString(n));
                            }
                        },
                })
        {
            @Override
            public BaseObject rawInstance(ConcreteType type) {
                return new NativeInt();
            }
        };

        // TODO: less fragile way?
        TYPE.staticFields[0] = new NativeInt(Integer.MIN_VALUE);
        TYPE.staticFields[1] = new NativeInt(Integer.MAX_VALUE);
    }
}
