package d;


public abstract class MethodDef {
    public final RawMethod desc;

    public MethodDef(RawMethod desc) {
        this.desc = desc;
    }

    public abstract void link();
}
