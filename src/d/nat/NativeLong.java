package d.nat;

import common.*;

import d.*;
import d.ty.ConcreteType;
import d.ty.desc.*;

public class NativeLong extends NativeObject {
    public static final NativeTypeDef TYPE;
    public static final ConcreteType CONCRETE_TYPE;

    public long value;

    public NativeLong(long value) {
        super(CONCRETE_TYPE);
        this.value = value;
    }

    public NativeLong() {
        this(0);
    }

    static {
        TYPE = new NativeTypeDef(
                RawType.coreLong,
                2, new String[0],

                // static methods
                new NativeMethodDef[] {
                },

                // instance methods
                new NativeMethodDef[] {
                        // Constructors
                        new NativeMethodDef(new RawMethod(false, RawType.coreLong, "init", 0, TypeDesc.coreIntOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                NativeLong n = (NativeLong) stack[bp + 1];
                                int m = ((NativeInt) stack[bp + 2]).value;
                                n.value = m;
                                stack[bp + 1] = God.objUnit;
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreLong, "init", 0, TypeDesc.coreByteOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                NativeLong n = (NativeLong) stack[bp + 1];
                                byte m = ((NativeByte) stack[bp + 2]).value;
                                n.value = m;
                                stack[bp + 1] = God.objUnit;
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreLong, "init", 0, TypeDesc.coreCharOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                NativeLong n = (NativeLong) stack[bp + 1];
                                char c = ((NativeChar) stack[bp + 2]).value;
                                n.value = c;
                                stack[bp + 1] = God.objUnit;
                            }
                        },

                        // Unary methods
                        new NativeMethodDef(new RawMethod(false, RawType.coreLong, "+", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                long n = ((NativeLong) stack[bp + 1]).value;
                                stack[bp + 1] = new NativeLong(+n);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreLong, "-", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                long n = ((NativeLong) stack[bp + 1]).value;
                                stack[bp + 1] = new NativeLong(-n);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreLong, "~", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                long n = ((NativeLong) stack[bp + 1]).value;
                                stack[bp + 1] = new NativeLong(~n);
                            }
                        },

                        // Binary methods
                        new NativeMethodDef(new RawMethod(false, RawType.coreLong, "+", 0, TypeDesc.coreLongOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                long n = ((NativeLong) stack[bp + 1]).value;
                                long m = ((NativeLong) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeLong(n + m);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreLong, "-", 0, TypeDesc.coreLongOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                long n = ((NativeLong) stack[bp + 1]).value;
                                long m = ((NativeLong) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeLong(n - m);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreLong, "*", 0, TypeDesc.coreLongOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                long n = ((NativeLong) stack[bp + 1]).value;
                                long m = ((NativeLong) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeLong(n * m);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreLong, "/", 0, TypeDesc.coreLongOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                long n = ((NativeLong) stack[bp + 1]).value;
                                long m = ((NativeLong) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeLong(n / m);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreLong, "%", 0, TypeDesc.coreLongOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                long n = ((NativeLong) stack[bp + 1]).value;
                                long m = ((NativeLong) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeLong(n % m);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreLong, "&", 0, TypeDesc.coreLongOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                long n = ((NativeLong) stack[bp + 1]).value;
                                long m = ((NativeLong) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeLong(n & m);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreLong, "|", 0, TypeDesc.coreLongOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                long n = ((NativeLong) stack[bp + 1]).value;
                                long m = ((NativeLong) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeLong(n | m);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreLong, "|", 0, TypeDesc.coreIntOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                long n = ((NativeLong) stack[bp + 1]).value;
                                int m = ((NativeInt) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeLong(n | m & 0xFFFFFFFFL);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreLong, "|", 0, TypeDesc.coreByteOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                long n = ((NativeLong) stack[bp + 1]).value;
                                byte b = ((NativeByte) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeLong(n | (b & 0xFF));
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreLong, "^", 0, TypeDesc.coreLongOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                long n = ((NativeLong) stack[bp + 1]).value;
                                long m = ((NativeLong) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeLong(n ^ m);
                            }
                        },

                        // comparisons
                        new NativeMethodDef(new RawMethod(false, RawType.coreLong, "compareTo", 0, TypeDesc.coreLongOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                long n = ((NativeLong) stack[bp + 1]).value;
                                long m = ((NativeLong) stack[bp + 2]).value;
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
                        new NativeMethodDef(new RawMethod(false, RawType.coreLong, "<", 0, TypeDesc.coreLongOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                long n = ((NativeLong) stack[bp + 1]).value;
                                long m = ((NativeLong) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeBool(n < m);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreLong, "<=", 0, TypeDesc.coreLongOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                long n = ((NativeLong) stack[bp + 1]).value;
                                long m = ((NativeLong) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeBool(n <= m);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreLong, ">", 0, TypeDesc.coreLongOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                long n = ((NativeLong) stack[bp + 1]).value;
                                long m = ((NativeLong) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeBool(n > m);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreLong, ">=", 0, TypeDesc.coreLongOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                long n = ((NativeLong) stack[bp + 1]).value;
                                long m = ((NativeLong) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeBool(n >= m);
                            }
                        },

                        // ==, hashCode, toString
                        new NativeMethodDef(new RawMethod(false, RawType.coreLong, "==", 0, TypeDesc.coreTopOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                long n = ((NativeLong) stack[bp + 1]).value;
                                BaseObject o = stack[bp + 2];
                                boolean result;
                                if (o instanceof NativeLong)
                                    result = n == ((NativeLong) o).value;
                                else
                                    result = false;
                                stack[bp + 1] = new NativeBool(result);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreLong, "hashCode", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                long n = ((NativeLong) stack[bp + 1]).value;
                                stack[bp + 1] = new NativeInt((int) n);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreLong, "toString", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                long n = ((NativeLong) stack[bp + 1]).value;
                                stack[bp + 1] = VMUtils.makeString(Long.toString(n));
                            }
                        },
                })
        {
            @Override
            public BaseObject rawInstance(ConcreteType type) {
                return new NativeLong();
            }
        };

        CONCRETE_TYPE = new ConcreteType(TYPE);

        // TODO: less fragile way?
        TYPE.staticFields[0] = new NativeLong(Long.MIN_VALUE);
        TYPE.staticFields[1] = new NativeLong(Long.MAX_VALUE);
    }
}
