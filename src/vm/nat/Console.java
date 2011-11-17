package vm.nat;

import common.*;

import vm.*;
import vm.ty.ConcreteType;

public class Console extends ZObject {
    public static final NativeType TYPE;

    static {
        TYPE = new ConsoleType();
    }

    public Console() {
        super(new ConcreteType(TYPE));
    }

    private static class ConsoleType extends NativeType {
        private static final RawTypeDesc desc = new RawTypeDesc("core", "Console");
        private static final RawTypeDesc descStr = new RawTypeDesc("core", "String");

        ConsoleType() {
            super(desc,
                    new Method[] {
                        new NativeMethod(
                                new RawMethodDesc(desc, "outString", 0, new FullTypeDesc[] {new NormalFullTypeDesc(descStr)}, true),
                                new RawTypeDesc[] {descStr},
                                new RawMethodDesc[] {}) {
                            public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                                NormalObject s = (NormalObject) stack[bp + 1];
                                NatMutableArray arr = (NatMutableArray) s.fields[0];
                                for (ZObject o : arr.elems) {
                                    char c = ((NatChar) o).value;
                                    System.out.print(c);
                                }
                                // TODO: return void (likewise for other native methods?)
                            }
                        },
                        new NativeMethod(
                                new RawMethodDesc(desc, "errString", 0, new FullTypeDesc[] {new NormalFullTypeDesc(descStr)}, true),
                                new RawTypeDesc[] {descStr},
                                new RawMethodDesc[] {}) {
                            public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                                NormalObject s = (NormalObject) stack[bp + 1];
                                NatMutableArray arr = (NatMutableArray) s.fields[0];
                                for (ZObject o : arr.elems) {
                                    char c = ((NatChar) o).value;
                                    System.err.print(c);
                                }
                                // TODO: return void (likewise for other native methods?)
                            }
                        },
                    },
                    0);
        }

        public ZObject rawInstance(ConcreteType[] genericArgs) {
            throw new RuntimeException("Console should never be instantiated");
        }
    }
}
