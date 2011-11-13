package vm.nat;

import java.util.HashMap;

import common.*;

import vm.*;

public class InternalIO extends ZObject {
    public static final NativeType TYPE;

    static {
        TYPE = new InternalIOType();
    }
    
    public InternalIO() {
        super(TYPE);
    }
    
    private static class InternalIOType extends NativeType {
        private static final RawTypeDesc desc = new RawTypeDesc("core", "InternalIO");

        InternalIOType() {
            super(desc,
                    new Method[] {
                        new NativeMethod(
                                new RawMethodDesc(desc, "stdout", 0, null, true),
                                new RawTypeDesc[] {new RawTypeDesc("core", "String")},
                                new RawMethodDesc[] {}) {
                            public void invoke(ZObject[] stack, int bp) {
                                NormalObject s = (NormalObject) stack[bp + 1];
                                NatMutableArray arr = (NatMutableArray) s.fields[0];
                                throw new RuntimeException("FIXME: finish implementing");
                            }}
                    },
                    new HashMap<RawMethodDesc, RawMethodDesc>(),
                    0);
        }

        public ZObject rawInstance() {
            throw new RuntimeException("InternalIO should never be instantiated");
        }
    }
}
