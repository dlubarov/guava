package d.nat;

import common.*;

import d.*;
import d.ty.ConcreteType;
import d.ty.desc.*;

public class NativeInt extends NativeObject {
    public static final NativeTypeDef TYPE;
    public static final ConcreteType CONCRETE_TYPE;

    public int value;

    public NativeInt(int value) {
        super(CONCRETE_TYPE);
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
                        new NativeMethodDef(new RawMethod(false, RawType.coreInt, "init", 0, TypeDesc.coreIntOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                NativeInt n = (NativeInt) stack[bp + 1];
                                long l = ((NativeLong) stack[bp + 2]).value;
                                n.value = (int) l;
                                stack[bp + 1] = God.objUnit;
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreInt, "init", 0, TypeDesc.coreByteOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                NativeInt n = (NativeInt) stack[bp + 1];
                                byte b = ((NativeByte) stack[bp + 2]).value;
                                n.value = b;
                                stack[bp + 1] = God.objUnit;
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreInt, "init", 0, TypeDesc.coreCharOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                NativeInt n = (NativeInt) stack[bp + 1];
                                char c = ((NativeChar) stack[bp + 2]).value;
                                n.value = c;
                                stack[bp + 1] = God.objUnit;
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
                        new NativeMethodDef(new RawMethod(false, RawType.coreInt, "|", 0, TypeDesc.coreLongOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                int n = ((NativeInt) stack[bp + 1]).value;
                                long l = ((NativeLong) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeLong(n & 0xFFFFFFFFL | l);
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
                        new NativeMethodDef(new RawMethod(false, RawType.coreInt, "|", 0, TypeDesc.coreByteOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                int n = ((NativeInt) stack[bp + 1]).value;
                                byte b = ((NativeByte) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeInt(n | b & 0xFF);
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

                        // comparisons
                        new NativeMethodDef(new RawMethod(false, RawType.coreInt, "compareTo", 0, TypeDesc.coreIntOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                int n = ((NativeInt) stack[bp + 1]).value;
                                int m = ((NativeInt) stack[bp + 2]).value;
                                BaseObject rel;
                                if (n < m)
                                    rel = God.objLT;
                                else if (n > m)
                                    rel = God.objGT;
                                else
                                    rel = God.objEQ;
                                stack[bp + 1] = rel;
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreInt, "<", 0, TypeDesc.coreIntOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                int n = ((NativeInt) stack[bp + 1]).value;
                                int m = ((NativeInt) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeBool(n < m);
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
                        new NativeMethodDef(new RawMethod(false, RawType.coreInt, ">", 0, TypeDesc.coreIntOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                int n = ((NativeInt) stack[bp + 1]).value;
                                int m = ((NativeInt) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeBool(n > m);
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

        CONCRETE_TYPE = new ConcreteType(TYPE);

        // TODO: less fragile way?
        TYPE.staticFields[0] = new NativeInt(Integer.MIN_VALUE);
        TYPE.staticFields[1] = new NativeInt(Integer.MAX_VALUE);
    }
}
