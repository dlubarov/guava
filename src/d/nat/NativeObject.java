package d.nat;

import common.*;

import d.*;
import d.ty.ConcreteType;
import d.ty.desc.TypeDesc;

public abstract class NativeObject extends BaseObject {
    public static final NativeTypeDef TOP_TYPE;

    // TODO: get rid of this class (except as a container for TOP_TYPE) and have NativeInt etc.
    // subclass BaseObject directly?

    protected NativeObject(ConcreteType type) {
        super(type);
    }

    static {
        TOP_TYPE = new NativeTypeDef(
                RawType.coreTop,
                0, new String[0],

                // static methods
                new NativeMethodDef[] {},

                // instance methods
                new NativeMethodDef[] {
                        new NativeMethodDef(new RawMethod(false, RawType.coreTop, "==", 0, TypeDesc.coreTopOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                BaseObject o1 = stack[bp + 1], o2 = stack[bp + 2];
                                stack[bp + 1] = new NativeBool(o1 == o2);
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreTop, "hashCode", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                BaseObject o = stack[bp + 1];
                                int hash = System.identityHashCode(o);
                                stack[bp + 1] = new NativeInt(hash);
                            }
                        },

                        new NativeMethodDef(new RawMethod(false, RawType.coreTop, "toString", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                BaseObject o = stack[bp + 1];
                                String s = o.type.toString() + "@" + System.identityHashCode(o);
                                stack[bp + 1] = VMUtils.makeString(s);
                            }
                        },

                        new NativeMethodDef(new RawMethod(false, RawType.coreTop, "instanceOf", 1, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                BaseObject o = stack[bp + 1];
                                stack[bp + 1] = new NativeBool(o.instanceOf(genericArgs[0]));
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreTop, "cast", 1, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                BaseObject o = stack[bp + 1];
                                if (!o.instanceOf(genericArgs[0]))
                                    throw new NiftyException(
                                            "Failed cast: %s is not a %s.",
                                            o, genericArgs[0]);
                            }
                        },
                })
        {
            @Override
            public BaseObject rawInstance(ConcreteType type) {
                return new NormalObject(type);
            }
        };
    }
}
