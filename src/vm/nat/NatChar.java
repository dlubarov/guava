package vm.nat;

import common.FullTypeDesc;
import common.NormalFullTypeDesc;
import common.RawMethodDesc;
import common.RawTypeDesc;
import vm.Method;
import vm.NativeMethod;
import vm.NativeType;
import vm.ZObject;

import java.util.HashMap;

public class NatChar extends ZObject {
    public static final NativeType TYPE;

    static {
        TYPE = new NatCharType();
    }

    public char value;

    public NatChar(char value) {
        super(TYPE);
        this.value = value;
    }

    @SuppressWarnings("serial")
    private static class NatCharType extends NativeType {
        private static final RawTypeDesc desc = new RawTypeDesc("core", "Char");

        private static final FullTypeDesc[] objOnly = new FullTypeDesc[] {new NormalFullTypeDesc(new RawTypeDesc("core", "Object"))};
        private static final FullTypeDesc[] intOnly = new FullTypeDesc[] {new NormalFullTypeDesc(new RawTypeDesc("core", "Int"))};

        NatCharType() {
            super(desc,
                new Method[]{
                    // Constructors
                    new NativeMethod(new RawMethodDesc("core", "Char", "init", 0, intOnly)) {
                        public void invoke(ZObject[] stack, int bp) {
                            NatChar c = (NatChar) stack[bp + 1];
                            int i = ((NatInt) stack[bp + 2]).value;
                            // TODO: return unit
                            c.value = (char) i;
                        }
                    },
                    // Char conversion methods
                    new NativeMethod(new RawMethodDesc("core", "Char", "toLower", 0, FullTypeDesc.none)) {
                        public void invoke(ZObject[] stack, int bp) {
                            char c = ((NatChar) stack[bp + 1]).value;
                            stack[bp + 1] = new NatChar(Character.toLowerCase(c));
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Char", "toUpper", 0, FullTypeDesc.none)) {
                        public void invoke(ZObject[] stack, int bp) {
                            char c = ((NatChar) stack[bp + 1]).value;
                            stack[bp + 1] = new NatChar(Character.toUpperCase(c));
                        }
                    },

                    // Category check methods
                    new NativeMethod(new RawMethodDesc("core", "Char", "isLetter", 0, FullTypeDesc.none)) {
                        public void invoke(ZObject[] stack, int bp) {
                            char c = ((NatChar) stack[bp + 1]).value;
                            stack[bp + 1] = new NatBool(Character.isLetter(c));
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Char", "isDigit", 0, FullTypeDesc.none)) {
                        public void invoke(ZObject[] stack, int bp) {
                            char c = ((NatChar) stack[bp + 1]).value;
                            stack[bp + 1] = new NatBool(Character.isDigit(c));
                        }
                    },

                    // Object methods
                    new NativeMethod(new RawMethodDesc("core", "Char", "==", 0, objOnly)) {
                        public void invoke(ZObject[] stack, int bp) {
                            char a = ((NatChar) stack[bp + 1]).value;
                            try {
                                char b = ((NatChar) stack[bp + 2]).value;
                                stack[bp + 1] = new NatBool(a == b);
                            } catch (ClassCastException e) {
                                stack[bp + 1] = NatBool.FALSE;
                            }
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Char", "hashCode", 0, FullTypeDesc.none)) {
                        public void invoke(ZObject[] stack, int bp) {
                            char c = ((NatChar) stack[bp + 1]).value;
                            stack[bp + 1] = new NatInt(new Character(c).hashCode());
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "Char", "toString", 0, FullTypeDesc.none)) {
                        public void invoke(ZObject[] stack, int bp) {
                            char c = ((NatChar) stack[bp + 1]).value;
                            String s = Character.toString(c);
                            stack[bp + 1] = null; // FIXME: create String
                        }
                    },
                },
                new HashMap<RawMethodDesc, RawMethodDesc>() {{
                    put(new RawMethodDesc("core", "Object", "==", 0, objOnly),
                        new RawMethodDesc("core", "Char", "==", 0, objOnly));
                    put(new RawMethodDesc("core", "Object", "hashCode", 0, FullTypeDesc.none),
                        new RawMethodDesc("core", "Char", "hashCode", 0, FullTypeDesc.none));
                    put(new RawMethodDesc("core", "Object", "toString", 0, FullTypeDesc.none),
                        new RawMethodDesc("core", "Char", "toString", 0, FullTypeDesc.none));
                }},
                0);
        }

        public ZObject rawInstance() {
            return new NatChar('\0');
        }
    }
}
