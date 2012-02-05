package d.nat;

import common.*;

import d.*;
import d.ty.ConcreteType;
import d.ty.desc.*;

public class NativeChar extends NativeObject {
    public final static NativeTypeDef TYPE;

    public final char value;

    public NativeChar(char value) {
        super(new ConcreteType(TYPE));
        this.value = value;
    }

    static {
        TYPE = new NativeTypeDef(
                RawType.coreChar,
                Variance.NONE,

                // instance methods
                new NativeMethodDef[] {
                        new NativeMethodDef(new RawMethod(false, RawType.coreChar, "equals", 0, TypeDesc.coreTopOnly)) {
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
                },

                // static methods
                new NativeMethodDef[] {
                });
    }
}
