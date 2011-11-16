package vm.nat;

import common.*;
import vm.*;
import vm.ty.ConcreteType;

import java.util.*;

public class NatMutableArray extends ZObject {
    public static final NativeType TYPE;

    static {
        TYPE = new NatMutableArrayType();
    }

    public ZObject[] elems;

    public NatMutableArray(ConcreteType[] genericArgs, ZObject[] contents) {
        super(new ConcreteType(TYPE, genericArgs));
        elems = contents;
    }

    public NatMutableArray(ConcreteType[] genericArgs) {
        this(genericArgs, null);
    }

    @SuppressWarnings("serial")
    private static class NatMutableArrayType extends NativeType {
        private static final RawTypeDesc desc = new RawTypeDesc("core", "MutableArray");

        private static final FullTypeDesc[] objOnly = new FullTypeDesc[] {new NormalFullTypeDesc(new RawTypeDesc("core", "Object"))};
        private static final RawTypeDesc descColl = new RawTypeDesc("core", "Collection");

        NatMutableArrayType() {
            super(desc, new RawTypeDesc[] {new RawTypeDesc("core", "Array")},
                new Method[] {
                    // Constructor
                    new NativeMethod(
                            new RawMethodDesc("core", "MutableArray", "init", 0,
                                    new FullTypeDesc[] {new NormalFullTypeDesc(descColl, new FullTypeDesc[] {new TypeGenericFullTypeDesc(desc, 0)})}),
                            new RawTypeDesc[0],
                            new RawMethodDesc[] {
                                new RawMethodDesc("core", "Collection", "iterator", 0, FullTypeDesc.NONE),
                                new RawMethodDesc("core", "Iterator", "next", 0, FullTypeDesc.NONE),
                                new RawMethodDesc("core", "Maybe", "isSomething", 0, FullTypeDesc.NONE),
                                new RawMethodDesc("core", "Maybe", "get", 0, FullTypeDesc.NONE),
                            }) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            NatMutableArray arr = (NatMutableArray) stack[bp + 1];
                            ZObject source = stack[bp + 2];
                            Method meth = methodTable[0]; // Collection.iterator
                            meth = source.type.rawType.vtable.get(meth);
                            meth.invoke(stack, bp + 1, ConcreteType.NONE);
                            ZObject iter = stack[bp + 2];

                            meth = methodTable[1]; // Iterator.next
                            meth = iter.type.rawType.vtable.get(meth);
                            List<ZObject> buffer = new ArrayList<ZObject>();
                            for (;;) {
                                stack[bp + 1] = iter;
                                meth.invoke(stack, bp, ConcreteType.NONE);
                                ZObject maybeNext = stack[bp + 1];
                                maybeNext.type.rawType.vtable.get(methodTable[2]).invoke(stack, bp, ConcreteType.NONE);
                                boolean isSomething = ((NatBool) stack[bp + 1]).value;
                                if (isSomething) {
                                    stack[bp + 1] = maybeNext;
                                    maybeNext.type.rawType.vtable.get(methodTable[3]).invoke(stack, bp, ConcreteType.NONE);
                                    buffer.add(stack[bp + 1]);
                                } else
                                    break;
                            }

                            arr.elems = buffer.toArray(new ZObject[buffer.size()]);
                        }
                    },

                    // Get, set
                    new NativeMethod(new RawMethodDesc(
                            "core", "MutableArray", "get", 0,
                            new FullTypeDesc[] {new NormalFullTypeDesc(new RawTypeDesc("core", "Int"))})) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            ZObject[] arr = ((NatMutableArray) stack[bp + 1]).elems;
                            int i = ((NatInt) stack[bp + 2]).value;
                            stack[bp + 1] = arr[i];
                        }
                    },
                    new NativeMethod(new RawMethodDesc(
                            "core", "MutableArray", "set", 0,
                            new FullTypeDesc[] {new NormalFullTypeDesc(new RawTypeDesc("core", "Int")),
                                                new TypeGenericFullTypeDesc(new RawTypeDesc("core", "MutableArray"), 0)})) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            ZObject[] arr = ((NatMutableArray) stack[bp + 1]).elems;
                            int i = ((NatInt) stack[bp + 2]).value;
                            ZObject val = stack[bp + 3];
                            stack[bp + 1] = arr[i] = val;
                        }
                    },

                    // Size
                    new NativeMethod(new RawMethodDesc("core", "MutableArray", "size", 0, FullTypeDesc.NONE)) {
                        public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
                            ZObject[] arr = ((NatMutableArray) stack[bp + 1]).elems;
                            stack[bp + 1] = new NatInt(arr.length);
                        }
                    },

                    // Object methods
//                    new NativeMethod(new RawMethodDesc("core", "MutableArray", "==", 0, objOnly)) {
//                        public void invoke(ZObject[] stack, int bp) {
//                            ZObject[] a = ((NatMutableArray) stack[bp + 1]).elems;
//                            try {
//                                ZObject[] b = ((NatMutableArray) stack[bp + 2]).elems;
//                                // FIXME: do sequence comparison for ==
//                                stack[bp + 1] = new NatBool(a == b);
//                            } catch (ClassCastException e) {
//                                stack[bp + 1] = NatBool.FALSE;
//                            }
//                        }
//                    },
//                    new NativeMethod(new RawMethodDesc("core", "MutableArray", "hashCode", 0, FullTypeDesc.none),
//                            new RawMethodDesc("core", "Object", "hashCode", 0, FullTypeDesc.none)) {
//                        public void invoke(ZObject[] stack, int bp) {
//                            ZObject[] arr = ((NatMutableArray) stack[bp + 1]).elems;
//                            int[] codes = new int[arr.length];
//                            for (int i = 0; i < codes.length; ++i) {
//                                methodTable[i].invoke(stack, bp);
//                                codes[i] = ((NatInt) stack[bp + 1]).value;
//                            }
//                            stack[bp + 1] = new NatInt(Arrays.hashCode(codes));
//                        }
//                    },
//                    new NativeMethod(new RawMethodDesc("core", "MutableArray", "toString", 0, FullTypeDesc.none),
//                            new RawMethodDesc("core", "Object", "toString", 0, FullTypeDesc.none)) {
//                        public void invoke(ZObject[] stack, int bp) {
//                            ZObject[] arr = ((NatMutableArray) stack[bp + 1]).elems;
//                            String s = "an array"; // TODO: proper toString
//                            stack[bp + 1] = null; // FIXME: create String
//                            throw new RuntimeException("impl");
//                        }
//                    },
                },

                new HashMap<RawMethodDesc, RawMethodDesc>() {{
//                    put(new RawMethodDesc("core", "Object", "==", 0, objOnly),
//                        new RawMethodDesc("core", "MutableArray", "==", 0, objOnly));
//                    put(new RawMethodDesc("core", "Object", "hashCode", 0, FullTypeDesc.none),
//                        new RawMethodDesc("core", "MutableArray", "hashCode", 0, FullTypeDesc.none));
//                    put(new RawMethodDesc("core", "Object", "toString", 0, FullTypeDesc.none),
//                        new RawMethodDesc("core", "MutableArray", "toString", 0, FullTypeDesc.none));
                    put(new RawMethodDesc("core", "Collection", "size", 0, FullTypeDesc.NONE),
                        new RawMethodDesc("core", "MutableArray", "size", 0, FullTypeDesc.NONE));
                    put(new RawMethodDesc("core", "Iterable", "iterator", 0, FullTypeDesc.NONE),
                        new RawMethodDesc("core", "Array", "iterator", 0, FullTypeDesc.NONE));
                }},

                0);
        }

        public ZObject rawInstance(ConcreteType[] genericArgs) {
            assert genericArgs.length == 1;
            return new NatMutableArray(genericArgs);
        }
    }
}
