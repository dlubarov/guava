package d.nat;

import common.*;

import d.*;
import d.ty.ConcreteType;
import d.ty.desc.*;

public class NativeConsole extends NativeObject {
    public static final NativeTypeDef TYPE;

    private NativeConsole() {
        super(new ConcreteType(TYPE));
        assert false : "Console has no constructor, so it should never be instantiated.";
    }

    static {
        TYPE = new NativeTypeDef(
                RawType.coreConsole,
                0, new String[0],

                // static methods
                new NativeMethodDef[] {
                        new NativeMethodDef(new RawMethod(true, RawType.coreConsole, "outString", 0, TypeDesc.coreStringOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                BaseObject str = stack[bp + 1];
                                System.out.print(VMUtils.extractString(str));
                                stack[bp + 1] = God.objUnit;
                            }
                        },
                        new NativeMethodDef(new RawMethod(true, RawType.coreConsole, "errString", 0, TypeDesc.coreStringOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                BaseObject str = stack[bp + 1];
                                System.err.print(VMUtils.extractString(str));
                                stack[bp + 1] = God.objUnit;
                            }
                        },
                },

                // instance methods
                new NativeMethodDef[] {})
        {
            @Override
            public BaseObject rawInstance(ConcreteType type) {
                assert false : "Console should never be instantiated.";
                return null;
            }
        };
    }
}
