package d.nat;

import common.*;

import d.*;
import d.ty.ConcreteType;
import d.ty.desc.*;

public class NativeMutableArray extends NativeObject {
    public static final NativeTypeDef TYPE;

    public BaseObject[] contents;

    public NativeMutableArray(BaseObject[] contents) {
        super(new ConcreteType(TYPE));
        this.contents = contents;
    }

    public NativeMutableArray() {
        this(null);
    }

    static {
        TYPE = new NativeTypeDef(
                RawType.coreMutableArray,
                new Variance[] {Variance.NONVARIANT},
                0, new String[0],

                // static methods
                new NativeMethodDef[] {
                },

                // instance methods
                new NativeMethodDef[] {
                        // Constructors
                        new NativeMethodDef(new RawMethod(false, RawType.coreMutableArray, "init", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                NativeMutableArray arr = (NativeMutableArray) stack[bp + 1];
                                arr.contents = new BaseObject[0];
                                stack[bp + 1] = VMUtils.getVoid();
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreMutableArray, "init", 0,
                                new TypeDesc[] {new ParameterizedTypeDesc(RawType.coreCollection,
                                        new TypeDesc[] {new TypeGenericTypeDesc(0)})})) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                NativeMutableArray arr = (NativeMutableArray) stack[bp + 1];
                                arr.contents = new BaseObject[0];
                                stack[bp + 1] = VMUtils.getVoid();
                            }
                        },

                        // get and set
                        new NativeMethodDef(new RawMethod(false, RawType.coreMutableArray, "get", 0, TypeDesc.coreIntOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                BaseObject[] arr = ((NativeMutableArray) stack[bp + 1]).contents;
                                int i = ((NativeInt) stack[bp + 2]).value;
                                stack[bp + 1] = arr[i];
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreMutableArray, "set", 0,
                                new TypeDesc[] {TypeDesc.coreInt, new TypeGenericTypeDesc(0)})) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                BaseObject[] arr = ((NativeMutableArray) stack[bp + 1]).contents;
                                int i = ((NativeInt) stack[bp + 2]).value;
                                BaseObject newVal = stack[bp + 3];
                                stack[bp + 1] = arr[i] = newVal;
                            }
                        },

                        // size
                        new NativeMethodDef(new RawMethod(false, RawType.coreMutableArray, "size", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                BaseObject[] arr = ((NativeMutableArray) stack[bp + 1]).contents;
                                stack[bp + 1] = new NativeInt(arr.length);
                            }
                        },
                })
        {
            @Override
            public BaseObject rawInstance(ConcreteType type) {
                return new NativeMutableArray();
            }
        };
    }
}
