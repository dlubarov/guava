package d.nat;

import common.*;

import d.*;
import d.ty.ConcreteType;
import d.ty.desc.*;

public class NativeDouble extends NativeObject {
    public static final NativeTypeDef TYPE;

    public double value;

    public NativeDouble(double value) {
        super(new ConcreteType(TYPE));
        this.value = value;
    }

    public NativeDouble() {
        this(0e1d);
    }

    static {
        TYPE = new NativeTypeDef(
                RawType.coreDouble,
                Variance.NONE,
                0, new String[0],

                // static methods
                new NativeMethodDef[] {
                },

                // instance methods
                new NativeMethodDef[] {
                        // Unary methods
                        new NativeMethodDef(new RawMethod(false, RawType.coreDouble, "+", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                double n = ((NativeDouble) stack[bp + 1]).value;
                                stack[bp + 1] = new NativeDouble(+n);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreDouble, "-", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                double n = ((NativeDouble) stack[bp + 1]).value;
                                stack[bp + 1] = new NativeDouble(-n);
                            }
                        },

                        // Binary methods
                        new NativeMethodDef(new RawMethod(false, RawType.coreDouble, "+", 0, TypeDesc.coreDoubleOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                double n = ((NativeDouble) stack[bp + 1]).value;
                                double m = ((NativeDouble) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeDouble(n + m);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreDouble, "-", 0, TypeDesc.coreDoubleOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                double n = ((NativeDouble) stack[bp + 1]).value;
                                double m = ((NativeDouble) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeDouble(n - m);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreDouble, "*", 0, TypeDesc.coreDoubleOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                double n = ((NativeDouble) stack[bp + 1]).value;
                                double m = ((NativeDouble) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeDouble(n * m);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreDouble, "/", 0, TypeDesc.coreDoubleOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                double n = ((NativeDouble) stack[bp + 1]).value;
                                double m = ((NativeDouble) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeDouble(n / m);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreDouble, "%", 0, TypeDesc.coreDoubleOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                double n = ((NativeDouble) stack[bp + 1]).value;
                                double m = ((NativeDouble) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeDouble(n % m);
                            }
                        },

                        // Comparisons
                        new NativeMethodDef(new RawMethod(false, RawType.coreDouble, "<", 0, TypeDesc.coreDoubleOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                double n = ((NativeDouble) stack[bp + 1]).value;
                                double m = ((NativeDouble) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeBool(n < m);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreDouble, ">", 0, TypeDesc.coreDoubleOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                double n = ((NativeDouble) stack[bp + 1]).value;
                                double m = ((NativeDouble) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeBool(n > m);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreDouble, "<=", 0, TypeDesc.coreDoubleOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                double n = ((NativeDouble) stack[bp + 1]).value;
                                double m = ((NativeDouble) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeBool(n <= m);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreDouble, ">=", 0, TypeDesc.coreDoubleOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                double n = ((NativeDouble) stack[bp + 1]).value;
                                double m = ((NativeDouble) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeBool(n >= m);
                            }
                        },

                        // ==, hashCode
                        new NativeMethodDef(new RawMethod(false, RawType.coreDouble, "==", 0, TypeDesc.coreTopOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                double n = ((NativeDouble) stack[bp + 1]).value;
                                BaseObject o = stack[bp + 2];
                                boolean result;
                                if (o instanceof NativeDouble)
                                    result = n == ((NativeDouble) o).value;
                                else
                                    result = false;
                                stack[bp + 1] = new NativeBool(result);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreDouble, "hashCode", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                // We don't need to do anything, since n.hashCode() = n. :-)
                                // And Int is immutable, so we don't need to clone it.
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreDouble, "toString", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                double x = ((NativeDouble) stack[bp + 1]).value;
                                stack[bp + 1] = VMUtils.makeString(Double.toString(x));
                            }
                        },
                })
        {
            @Override
            public BaseObject rawInstance(ConcreteType type) {
                return new NativeDouble();
            }
        };
    }
}
