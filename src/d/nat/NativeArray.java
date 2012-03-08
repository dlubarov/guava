package d.nat;

import java.util.*;

import common.*;

import d.*;
import d.ty.ConcreteType;
import d.ty.desc.*;

public class NativeArray extends NativeObject {
    public static final NativeTypeDef TYPE;

    public BaseObject[] contents;

    public NativeArray(ConcreteType type, BaseObject[] contents) {
        super(type);
        this.contents = contents;
    }

    public NativeArray(ConcreteType type) {
        this(type, null);
    }

    static {
        TYPE = new NativeTypeDef(
                RawType.coreArray,
                0, new String[0],

                // static methods
                new NativeMethodDef[] {
                },

                // instance methods
                new NativeMethodDef[] {
                        // empty array constructor
                        new NativeMethodDef(new RawMethod(false, RawType.coreArray, "init", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                NativeArray arr = (NativeArray) stack[bp + 1];
                                arr.contents = new BaseObject[0];
                                stack[bp + 1] = God.objUnit;
                            }
                        },

                        // constructor which copies elements from a given source
                        new NativeMethodDef(
                                new RawMethod(false, RawType.coreArray, "init", 0,
                                        new TypeDesc[] {
                                                new ParameterizedTypeDesc(
                                                        RawType.coreCollection,
                                                        new TypeDesc[] {new TypeGenericTypeDesc(0)})
                                        }
                                ),
                                RawType.NONE,
                                TypeDesc.NONE,
                                new RawMethod[] {
                                    RawMethod.coreEnumerable_enumerator,
                                    RawMethod.coreSource_tryTake,
                                    RawMethod.coreCollection_isEmpty,
                                    RawMethod.coreMaybe_get
                                }
                        ) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                MethodDef methEnumerator = methodTable[0];
                                MethodDef methTake = methodTable[1];
                                MethodDef methIsEmpty = methodTable[2];
                                MethodDef methGet = methodTable[3];

                                NativeArray arr = (NativeArray) stack[bp + 1];
                                BaseObject source = stack[bp + 2];

                                ConcreteMethodDef implEnumerator = source.type.rawType.virtualMethodTable.get(methEnumerator);
                                implEnumerator.invoke(stack, bp + 1, ConcreteType.NONE);
                                BaseObject enumerator = stack[bp + 2];

                                ConcreteMethodDef implTake = enumerator.type.rawType.virtualMethodTable.get(methTake);
                                List<BaseObject> contents = new ArrayList<BaseObject>();

                                for (;;) {
                                    stack[bp + 1] = enumerator;
                                    implTake.invoke(stack, bp, ConcreteType.NONE);
                                    BaseObject maybeNext = stack[bp + 1];

                                    ConcreteMethodDef implIsEmpty = maybeNext.type.rawType.virtualMethodTable.get(methIsEmpty);
                                    implIsEmpty.invoke(stack, bp, ConcreteType.NONE);
                                    boolean isEmpty = ((NativeBool) stack[bp + 1]).value;
                                    if (isEmpty)
                                        break;

                                    stack[bp + 1] = maybeNext;
                                    ConcreteMethodDef implGet = maybeNext.type.rawType.virtualMethodTable.get(methGet);
                                    implGet.invoke(stack, bp, ConcreteType.NONE);
                                    contents.add(stack[bp + 1]);
                                }

                                arr.contents = contents.toArray(new BaseObject[contents.size()]);
                                stack[bp + 1] = God.objUnit;
                            }
                        },

                        // get and set
                        new NativeMethodDef(new RawMethod(false, RawType.coreArray, "get", 0, TypeDesc.coreIntOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                BaseObject[] arr = ((NativeArray) stack[bp + 1]).contents;
                                int i = ((NativeInt) stack[bp + 2]).value;
                                stack[bp + 1] = arr[i];
                            }
                        },
                        new NativeMethodDef(new RawMethod(false, RawType.coreArray, "set", 0,
                                new TypeDesc[] {TypeDesc.coreInt, new TypeGenericTypeDesc(0)})) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                BaseObject[] arr = ((NativeArray) stack[bp + 1]).contents;
                                int i = ((NativeInt) stack[bp + 2]).value;
                                if (i >= arr.length)
                                    throw new NiftyException(
                                            "Array index %d is out of bounds (length=%d).",
                                            i, arr.length);
                                BaseObject newVal = stack[bp + 3];
                                stack[bp + 1] = arr[i] = newVal;
                            }
                        },

                        // repeat
                        new NativeMethodDef(new RawMethod(false, RawType.coreArray, "*", 0, TypeDesc.coreIntOnly)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                NativeArray arr = (NativeArray) stack[bp + 1];
                                BaseObject[] contents = arr.contents;
                                int l = contents.length, k = ((NativeInt) stack[bp + 2]).value;
                                BaseObject[] newContents = new BaseObject[l * k];
                                for (int i = 0; i < k; ++i)
                                    for (int j = 0; j < l; ++j)
                                        newContents[i * l + j] = contents[j];
                                stack[bp + 1] = new NativeArray(arr.type, newContents);
                            }
                        },

                        // size
                        new NativeMethodDef(new RawMethod(false, RawType.coreArray, "size", 0, TypeDesc.NONE)) {
                            @Override
                            public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
                                BaseObject[] arr = ((NativeArray) stack[bp + 1]).contents;
                                stack[bp + 1] = new NativeInt(arr.length);
                            }
                        },
                })
        {
            @Override
            public BaseObject rawInstance(ConcreteType type) {
                return new NativeArray(type);
            }
        };
    }
}
