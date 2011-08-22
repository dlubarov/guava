package comp;

public final class CodeTree {
    private final Object[] children;
    private int[] memCode = null;
    
    public CodeTree(Object... children) {
        this.children = children;
    }

    private int computeSize() {
        int res = 0;
        for (Object o : children)
            if (o instanceof Integer)
                ++res;
            else
                res += ((CodeTree) o).computeSize();
        return res;
    }

    private int flatten(int[] dest, int pos) {
        for (Object o : children)
            if (o instanceof Integer)
                dest[pos++] = (Integer) o;
            else
                pos = ((CodeTree) o).flatten(dest, pos);
        return pos;
    }

    public int[] getCode() {
        if (memCode != null)
            return memCode;
        int[] code = new int[computeSize()];
        int insnsProduced = flatten(code, 0);
        assert insnsProduced == code.length;
        return memCode = code;
    }
    
    public int size() {
        return getCode().length;
    }
}
