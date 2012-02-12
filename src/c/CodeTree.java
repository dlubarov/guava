package c;

import java.util.Arrays;

public final class CodeTree {
    private final Object[] children;
    private int[] memCode = null;
    private int memSize = -1;

    public CodeTree(Object... children) {
        for (Object child : children)
            assert child instanceof Integer || child instanceof CodeTree
                : "found " + child.getClass() + " in CodeTree";
        this.children = children;
    }

    public int getSize() {
        if (memSize >= 0)
            return memSize;
        int n = 0;
        for (Object child : children)
            if (child instanceof Integer)
                ++n;
            else
                n += ((CodeTree) child).getSize();
        return memSize = n;
    }

    private int flatten(int[] dest, int pos) {
        for (Object child : children)
            if (child instanceof Integer)
                dest[pos++] = (Integer) child;
            else
                pos = ((CodeTree) child).flatten(dest, pos);
        return pos;
    }

    public int[] getCode() {
        if (memCode != null)
            return memCode;
        int[] code = new int[getSize()];
        int n = flatten(code, 0);
        assert n == code.length;
        return memCode = code;
    }

    @Override
    public String toString() {
        return "CodeTree" + Arrays.toString(children);
    }
}
