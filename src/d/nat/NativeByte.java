package d.nat;

import common.*;

import d.*;
import d.ty.ConcreteType;
import d.ty.desc.*;

public class NativeByte extends NativeObject {
    public static final NativeTypeDef TYPE;
    public static final ConcreteType CONCRETE_TYPE;

    public byte value;

    public NativeByte(byte value) {
        super(CONCRETE_TYPE);
        this.value = value;
    }

    public NativeByte() {
        this((byte) 0);
    }

    static {
        // TODO: performance difference between e.g. byte.+=(int) and casting the sum?

        TYPE = new NativeTypeDef(
                RawType.coreByte,
                2, new String[0],

                // static methods
                new NativeMethodDef[] {
                },

                // instance methods
                new NativeMethodDef[] {
                        // Constructors
                        new NativeMethodDef(new RawMethod(false, RawType.coreByte, "init", 0, TypeDesc.coreIntOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                NativeByte n = (NativeByte) stack[bp + 1];
                                int m = ((NativeInt) stack[bp + 2]).value;
                                n.value = (byte) m;
                                stack[bp + 1] = God.objUnit;
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreByte, "init", 0, TypeDesc.coreLongOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                NativeByte n = (NativeByte) stack[bp + 1];
                                long m = ((NativeLong) stack[bp + 2]).value;
                                n.value = (byte) m;
                                stack[bp + 1] = God.objUnit;
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreByte, "init", 0, TypeDesc.coreCharOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                NativeByte n = (NativeByte) stack[bp + 1];
                                char c = ((NativeChar) stack[bp + 2]).value;
                                n.value = (byte) c;
                                stack[bp + 1] = God.objUnit;
                            }
                        },

                        // Unary methods
                        new NativeMethodDef(new RawMethod(false, RawType.coreByte, "+", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                byte n = ((NativeByte) stack[bp + 1]).value;
                                stack[bp + 1] = new NativeByte(n);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreByte, "-", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                byte n = ((NativeByte) stack[bp + 1]).value;
                                stack[bp + 1] = new NativeByte((byte) -n);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreByte, "~", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                byte n = ((NativeByte) stack[bp + 1]).value;
                                stack[bp + 1] = new NativeByte((byte) ~n);
                            }
                        },

                        // Binary methods
                        new NativeMethodDef(new RawMethod(false, RawType.coreByte, "+", 0, TypeDesc.coreByteOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                byte n = ((NativeByte) stack[bp + 1]).value;
                                byte m = ((NativeByte) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeByte((byte) (n + m));
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreByte, "-", 0, TypeDesc.coreByteOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                byte n = ((NativeByte) stack[bp + 1]).value;
                                byte m = ((NativeByte) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeByte((byte) (n - m));
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreByte, "*", 0, TypeDesc.coreByteOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                byte n = ((NativeByte) stack[bp + 1]).value;
                                byte m = ((NativeByte) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeByte((byte) (n * m));
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreByte, "/", 0, TypeDesc.coreByteOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                byte n = ((NativeByte) stack[bp + 1]).value;
                                byte m = ((NativeByte) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeByte((byte) (n / m));
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreByte, "%", 0, TypeDesc.coreByteOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                byte n = ((NativeByte) stack[bp + 1]).value;
                                byte m = ((NativeByte) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeByte((byte) (n % m));
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreByte, "&", 0, TypeDesc.coreByteOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                byte n = ((NativeByte) stack[bp + 1]).value;
                                byte m = ((NativeByte) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeByte((byte) (n & m));
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreByte, "|", 0, TypeDesc.coreByteOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                byte n = ((NativeByte) stack[bp + 1]).value;
                                byte m = ((NativeByte) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeByte((byte) (n | m));
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreByte, "^", 0, TypeDesc.coreByteOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                byte n = ((NativeByte) stack[bp + 1]).value;
                                byte m = ((NativeByte) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeByte((byte) (n ^ m));
                            }
                        },

                        // compareTo
                        new NativeMethodDef(new RawMethod(false, RawType.coreByte, "compareTo", 0, TypeDesc.coreByteOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                int n = ((NativeByte) stack[bp + 1]).value;
                                int m = ((NativeByte) stack[bp + 2]).value;
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

                        // ==, hashCode, toString
                        new NativeMethodDef(new RawMethod(false, RawType.coreByte, "==", 0, TypeDesc.coreTopOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                byte n = ((NativeByte) stack[bp + 1]).value;
                                BaseObject o = stack[bp + 2];
                                boolean result;
                                if (o instanceof NativeByte)
                                    result = n == ((NativeByte) o).value;
                                else
                                    result = false;
                                stack[bp + 1] = new NativeBool(result);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreByte, "hashCode", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                byte n = ((NativeByte) stack[bp + 1]).value;
                                stack[bp + 1] = new NativeInt(n);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreByte, "toString", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                byte n = ((NativeByte) stack[bp + 1]).value;
                                stack[bp + 1] = VMUtils.makeString(Byte.toString(n));
                            }
                        },
                })
        {
            @Override
            public BaseObject rawInstance(ConcreteType type) {
                return new NativeByte();
            }
        };

        CONCRETE_TYPE = new ConcreteType(TYPE);

        // TODO: less fragile way?
        TYPE.staticFields[0] = new NativeByte(Byte.MIN_VALUE);
        TYPE.staticFields[1] = new NativeByte(Byte.MAX_VALUE);
    }
}
