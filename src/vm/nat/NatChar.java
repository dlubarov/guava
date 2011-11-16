package vm.nat;

import common.*;
import vm.*;
import vm.ty.ConcreteType;

public class NatChar extends ZObject {
    public static final NativeType TYPE;

    static {
        TYPE = new NatCharType();
    }

    public char value;

    public NatChar(char value) {
        super(new ConcreteType(TYPE));
        this.value = value;
    }

    private static class NatCharType extends NativeType {
        private static final RawTypeDesc desc = new RawTypeDesc("core", "Char");

        private static final FullTypeDesc[] objOnly = new FullTypeDesc[] {new NormalFullTypeDesc(new RawTypeDesc("core", "Object"))};
        private static final FullTypeDesc[] intOnly = new FullTypeDesc[] {new NormalFullTypeDesc(new RawTypeDesc("core", "Int"))};

        NatCharType() {
            super(desc,
                new Method[]{
                    // Constructors
                    new NativeMethod(new RawMethodDesc("core", "Char", "init", 0, intOnly)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            NatChar c = (NatChar) stack[bp + 1];
                            int i = ((NatInt) stack[bp + 2]).value;
                            // TODO: return unit
                            c.value = (char) i;
                        }
                    },
                    // Char conversion methods
                    new NativeMethod(new RawMethodDesc("core", "Char", "toLower", 0, FullTypeDesc.NONE)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            char c = ((NatChar) stack[bp + 1]).value;
                            stack[bp + 1] = new NatChar(Character.toLowerCase(c));
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Char", "toUpper", 0, FullTypeDesc.NONE)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            char c = ((NatChar) stack[bp + 1]).value;
                            stack[bp + 1] = new NatChar(Character.toUpperCase(c));
                        }
                    },

                    // Category check methods
                    new NativeMethod(new RawMethodDesc("core", "Char", "isLetter", 0, FullTypeDesc.NONE)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            char c = ((NatChar) stack[bp + 1]).value;
                            stack[bp + 1] = new NatBool(Character.isLetter(c));
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Char", "isDigit", 0, FullTypeDesc.NONE)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            char c = ((NatChar) stack[bp + 1]).value;
                            stack[bp + 1] = new NatBool(Character.isDigit(c));
                        }
                    },

                    // Object methods
                    new NativeMethod(new RawMethodDesc("core", "Char", "==", 0, objOnly)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            char a = ((NatChar) stack[bp + 1]).value;
                            try {
                                char b = ((NatChar) stack[bp + 2]).value;
                                stack[bp + 1] = new NatBool(a == b);
                            } catch (ClassCastException e) {
                                stack[bp + 1] = NatBool.FALSE;
                            }
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Char", "hashCode", 0, FullTypeDesc.NONE)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            char c = ((NatChar) stack[bp + 1]).value;
                            stack[bp + 1] = new NatInt(new Character(c).hashCode());
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Char", "toString", 0, FullTypeDesc.NONE)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            char c = ((NatChar) stack[bp + 1]).value;
                            String s = Character.toString(c);
                            stack[bp + 1] = God.makeString(s);
                        }
                    },
                },
                0);
        }

        public ZObject rawInstance(ConcreteType[] genericArgs) {
            assert genericArgs.length == 0;
            return new NatChar('\0');
        }
    }
}
