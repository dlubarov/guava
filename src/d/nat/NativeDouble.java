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
        this(0);
    }

    static {
        TYPE = new NativeTypeDef(
                RawType.coreDouble,
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
                                double x = ((NativeDouble) stack[bp + 1]).value;
                                stack[bp + 1] = new NativeDouble(+x);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreDouble, "-", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                double x = ((NativeDouble) stack[bp + 1]).value;
                                stack[bp + 1] = new NativeDouble(-x);
                            }
                        },

                        // Binary methods
                        new NativeMethodDef(new RawMethod(false, RawType.coreDouble, "+", 0, TypeDesc.coreDoubleOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                double x = ((NativeDouble) stack[bp + 1]).value;
                                double y = ((NativeDouble) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeDouble(x + y);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreDouble, "-", 0, TypeDesc.coreDoubleOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                double x = ((NativeDouble) stack[bp + 1]).value;
                                double y = ((NativeDouble) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeDouble(x - y);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreDouble, "*", 0, TypeDesc.coreDoubleOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                double x = ((NativeDouble) stack[bp + 1]).value;
                                double y = ((NativeDouble) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeDouble(x * y);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreDouble, "/", 0, TypeDesc.coreDoubleOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                double x = ((NativeDouble) stack[bp + 1]).value;
                                double y = ((NativeDouble) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeDouble(x / y);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreDouble, "%", 0, TypeDesc.coreDoubleOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                double x = ((NativeDouble) stack[bp + 1]).value;
                                double y = ((NativeDouble) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeDouble(x % y);
                            }
                        },

                        // compareTo
                        new NativeMethodDef(new RawMethod(false, RawType.coreDouble, "compareTo", 0, TypeDesc.coreDoubleOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                double x = ((NativeDouble) stack[bp + 1]).value;
                                double y = ((NativeDouble) stack[bp + 2]).value;
                                BaseObject rel;
                                if (x < y)
                                    rel = VMUtils.getLT();
                                else if (x > y)
                                    rel = VMUtils.getGT();
                                else
                                    rel = VMUtils.getEQ();
                                stack[bp + 1] = rel;
                            }
                        },

                        // ==, hashCode, toString
                        new NativeMethodDef(new RawMethod(false, RawType.coreDouble, "==", 0, TypeDesc.coreTopOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                double x = ((NativeDouble) stack[bp + 1]).value;
                                BaseObject o = stack[bp + 2];
                                boolean result;
                                if (o instanceof NativeDouble)
                                    result = x == ((NativeDouble) o).value;
                                else
                                    result = false;
                                stack[bp + 1] = new NativeBool(result);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreDouble, "hashCode", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                double x = ((NativeDouble) stack[bp + 1]).value;
                                stack[bp + 1] = new NativeInt(new Double(x).hashCode());
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
