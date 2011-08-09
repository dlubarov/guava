package vm.nat;

import common.*;
import vm.Method;
import vm.NativeMethod;
import vm.NativeType;
import vm.TObject;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;

public class NatMutableArray extends TObject {
    public static final NativeType TYPE;

    static {
        TYPE = new NatMutableArrayType();
    }

    private final TObject[] elems;

    public NatMutableArray() {
        super(TYPE);
        elems = null;
    }

    private static class NatMutableArrayType extends NativeType {
        private static final RawTypeDesc desc = new RawTypeDesc("core", "MutableArray");
        
        private static final FullTypeDesc[] objOnly = new FullTypeDesc[] {new NormalFullTypeDesc(new RawTypeDesc("core", "Object"))};
        
        NatMutableArrayType() {
            super(desc,
                new Method[] {
                    // Get, set
                    new NativeMethod(new RawMethodDesc(
                            "core", "MutableArray", "get", 0,
                            new FullTypeDesc[] {new NormalFullTypeDesc(new RawTypeDesc("core", "Int"))})) {
                        public void invoke(TObject[] stack, int bp) {
                            TObject[] arr = ((NatMutableArray) stack[bp + 1]).elems;
                            int i = ((NatInt) stack[bp + 2]).value;
                            stack[bp + 1] = arr[i];
                        }
                    },
                    new NativeMethod(new RawMethodDesc(
                            "core", "MutableArray", "set", 0,
                            new FullTypeDesc[] {new NormalFullTypeDesc(new RawTypeDesc("core", "Int")),
                                                new TypeGenericFullTypeDesc(new RawTypeDesc("core", "MutableArray"), 0)})) {
                        public void invoke(TObject[] stack, int bp) {
                            TObject[] arr = ((NatMutableArray) stack[bp + 1]).elems;
                            int i = ((NatInt) stack[bp + 2]).value;
                            TObject val = stack[bp + 3];
                            stack[bp + 1] = arr[i] = val;
                        }
                    },

                    // Object methods
                    new NativeMethod(new RawMethodDesc("core", "MutableArray", "==", 0, objOnly)) {
                        public void invoke(TObject[] stack, int bp) {
                            TObject[] a = ((NatMutableArray) stack[bp + 1]).elems;
                            try {
                                TObject[] b = ((NatMutableArray) stack[bp + 2]).elems;
                                // FIXME: do sequence comparison for ==
                                stack[bp + 1] = new NatBool(a == b);
                            } catch (ClassCastException e) {
                                stack[bp + 1] = NatBool.FALSE;
                            }
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "MutableArray", "hashCode", 0, FullTypeDesc.none),
                            new RawMethodDesc("core", "Object", "hashCode", 0, FullTypeDesc.none)) {
                        public void invoke(TObject[] stack, int bp) {
                            TObject[] arr = ((NatMutableArray) stack[bp + 1]).elems;
                            int[] codes = new int[arr.length];
                            for (int i = 0; i < codes.length; ++i) {
                                methodTable[i].invoke(stack, bp);
                                codes[i] = ((NatInt) stack[bp + 1]).value;
                            }
                            stack[bp + 1] = new NatInt(Arrays.hashCode(codes));
                        }
                    },
                    new NativeMethod(new RawMethodDesc("core", "MutableArray", "toString", 0, FullTypeDesc.none),
                            new RawMethodDesc("core", "Object", "toString", 0, FullTypeDesc.none)) {
                        public void invoke(TObject[] stack, int bp) {
                            TObject[] arr = ((NatMutableArray) stack[bp + 1]).elems;
                            String s = "an array"; // TODO: proper toString
                            stack[bp + 1] = null; // FIXME: create String
                        }
                    },
                },

                new HashMap<RawMethodDesc, RawMethodDesc>() {{
                    put(new RawMethodDesc("core", "Object", "==", 0, objOnly),
                        new RawMethodDesc("core", "MutableArray", "==", 0, objOnly));
                    put(new RawMethodDesc("core", "Object", "hashCode", 0, FullTypeDesc.none),
                        new RawMethodDesc("core", "MutableArray", "hashCode", 0, FullTypeDesc.none));
                    put(new RawMethodDesc("core", "Object", "toString", 0, FullTypeDesc.none),
                        new RawMethodDesc("core", "MutableArray", "toString", 0, FullTypeDesc.none));
                }});
        }
        
        public TObject rawInstance() {
            return new NatMutableArray();
        }
    }
}
