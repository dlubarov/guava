package d;


public abstract class MethodDef {
    public final RawMethod desc;

    public MethodDef(RawMethod desc) {
        this.desc = desc;
        God.newMethod(this);
    }

    public abstract void link();
}
