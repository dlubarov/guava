package d.nat;

import common.*;

import d.*;
import d.ty.ConcreteType;
import d.ty.desc.*;

public class NativeChar extends NativeObject {
    public static final NativeTypeDef TYPE;

    public char value;

    public NativeChar(char value) {
        super(new ConcreteType(TYPE));
        this.value = value;
    }

    public NativeChar() {
        this('\0');
    }

    static {
        TYPE = new NativeTypeDef(
                RawType.coreChar,
                0, new String[0],

                // static methods
                new NativeMethodDef[] {
                },

                // instance methods
                new NativeMethodDef[] {
                        // Constructors
                        new NativeMethodDef(new RawMethod(false, RawType.coreChar, "init", 0, TypeDesc.coreIntOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                NativeChar c = (NativeChar) stack[bp + 1];
                                int ord = ((NativeInt) stack[bp + 2]).value;
                                c.value = (char) ord;
                                stack[bp + 1] = VMUtils.getUnit();
                            }
                        },

                        // compareTo
                        new NativeMethodDef(new RawMethod(false, RawType.coreChar, "compareTo", 0, TypeDesc.coreCharOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                char a = ((NativeChar) stack[bp + 1]).value;
                                char b = ((NativeChar) stack[bp + 2]).value;
                                BaseObject rel;
                                if (a < b)
                                    rel = VMUtils.getLT();
                                else if (a > b)
                                    rel = VMUtils.getGT();
                                else
                                    rel = VMUtils.getEQ();
                                stack[bp + 1] = rel;
                            }
                        },

                        // ==, hashCode, toString
                        new NativeMethodDef(new RawMethod(false, RawType.coreChar, "==", 0, TypeDesc.coreTopOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                char c = ((NativeChar) stack[bp + 1]).value;
                                BaseObject o = stack[bp + 2];
                                boolean result;
                                if (o instanceof NativeChar)
                                    result = c == ((NativeChar) o).value;
                                else
                                    result = false;
                                stack[bp + 1] = new NativeBool(result);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreChar, "hashCode", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                char c = ((NativeChar) stack[bp + 1]).value;
                                stack[bp + 1] = new NativeInt(c);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreChar, "toString", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                char c = ((NativeChar) stack[bp + 1]).value;
                                stack[bp + 1] = VMUtils.makeString(Character.toString(c));
                            }
                        },
                }) {
            @Override
            public BaseObject rawInstance(ConcreteType type) {
                return new NativeChar();
            }
        };
    }
}
