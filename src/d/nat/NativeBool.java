package d.nat;

import common.*;

import d.*;
import d.ty.ConcreteType;
import d.ty.desc.*;

public class NativeBool extends NativeObject {
    public static final NativeBool TRUE = new NativeBool(true), FALSE = new NativeBool(false);

    public static final NativeTypeDef TYPE;

    public final boolean value;

    public NativeBool(boolean value) {
        super(new ConcreteType(TYPE));
        this.value = value;
    }

    public NativeBool() {
        this(false);
    }

    static {
        TYPE = new NativeTypeDef(
                RawType.coreBool,
                Variance.NONE,
                0, 0,

                // static methods
                new NativeMethodDef[] {
                },

                // instance methods
                new NativeMethodDef[] {
                        // Unary
                        new NativeMethodDef(new RawMethod(false, RawType.coreBool, "!", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                boolean a = ((NativeBool) stack[bp + 1]).value;
                                stack[bp + 1] = new NativeBool(!a);
                            }
                        },

                        // Binary
                        new NativeMethodDef(new RawMethod(false, RawType.coreBool, "&", 0, TypeDesc.coreBoolOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                boolean a = ((NativeBool) stack[bp + 1]).value;
                                boolean b = ((NativeBool) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeBool(a & b);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreBool, "|", 0, TypeDesc.coreBoolOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                boolean a = ((NativeBool) stack[bp + 1]).value;
                                boolean b = ((NativeBool) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeBool(a | b);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreBool, "^", 0, TypeDesc.coreBoolOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                boolean a = ((NativeBool) stack[bp + 1]).value;
                                boolean b = ((NativeBool) stack[bp + 2]).value;
                                stack[bp + 1] = new NativeBool(a ^ b);
                            }
                        },

                        // ==, hashCode, toString
                        new NativeMethodDef(new RawMethod(false, RawType.coreBool, "==", 0, TypeDesc.coreTopOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                boolean c = ((NativeBool) stack[bp + 1]).value;
                                BaseObject o = stack[bp + 2];
                                boolean result;
                                if (o instanceof NativeBool)
                                    result = c == ((NativeBool) o).value;
                                else
                                    result = false;
                                stack[bp + 1] = new NativeBool(result);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreBool, "hashCode", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                boolean b = ((NativeBool) stack[bp + 1]).value;
                                stack[bp + 1] = new NativeInt(b ? 1 : 0);
                            }
                        },
                })
        {
            @Override
            public BaseObject rawInstance(ConcreteType type) {
                return new NativeBool();
            }
        };
    }
}
