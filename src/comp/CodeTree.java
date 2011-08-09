package comp;

public class CodeTree {
    private final Object[] children;
    
    public CodeTree(Object... children) {
        this.children = children;
    }

    private int size() {
        int res = 0;
        for (Object o : children)
            if (o instanceof Integer)
                ++res;
            else
                res += ((CodeTree) o).size();
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
        int[] code = new int[size()];
        int insnsProduced = flatten(code, 0);
        assert insnsProduced == code.length;
        return code;
    }
}
