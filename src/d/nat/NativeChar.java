package d.nat;

import common.*;

import d.*;
import d.ty.ConcreteType;
import d.ty.desc.*;

public class NativeChar extends NativeObject {
    public static final NativeTypeDef TYPE;
    public static final ConcreteType CONCRETE_TYPE;

    public char value;

    public NativeChar(char value) {
        super(CONCRETE_TYPE);
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
                                stack[bp + 1] = God.objUnit;
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreChar, "init", 0, TypeDesc.coreByteOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                NativeChar c = (NativeChar) stack[bp + 1];
                                byte ord = ((NativeByte) stack[bp + 2]).value;
                                c.value = (char) ord;
                                stack[bp + 1] = God.objUnit;
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
                                    rel = God.objLT;
                                else if (a > b)
                                    rel = God.objGT;
                                else
                                    rel = God.objEQ;
                                stack[bp + 1] = rel;
                            }
                        },

                        // toUpperCase, toLowerCase
                        new NativeMethodDef(new RawMethod(false, RawType.coreChar, "toUpperCase", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                char c = ((NativeChar) stack[bp + 1]).value;
                                stack[bp + 1] = new NativeChar(Character.toUpperCase(c));
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreChar, "toLowerCase", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                char c = ((NativeChar) stack[bp + 1]).value;
                                stack[bp + 1] = new NativeChar(Character.toLowerCase(c));
                            }
                        },

                        // isLetter, isDigit
                        new NativeMethodDef(new RawMethod(false, RawType.coreChar, "isLetter", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                char c = ((NativeChar) stack[bp + 1]).value;
                                stack[bp + 1] = new NativeBool(Character.isLetter(c));
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreChar, "isDigit", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                char c = ((NativeChar) stack[bp + 1]).value;
                                stack[bp + 1] = new NativeBool(Character.isDigit(c));
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
                })
        {
            @Override
            public BaseObject rawInstance(ConcreteType type) {
                return new NativeChar();
            }
        };

        CONCRETE_TYPE = new ConcreteType(TYPE);
    }
}
