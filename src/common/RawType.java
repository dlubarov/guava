package common;

public class RawType {
    public static final RawType[] NONE = new RawType[0];

    public static final RawType
        coreTop = new RawType("core", "Top"),
        coreBottom = new RawType("core", "Bottom"),
        coreInt = new RawType("core", "Int"),
        coreChar = new RawType("core", "Char"),
        coreBool = new RawType("core", "Bool"),
        coreIterable = new RawType("core", "Iterable"),
        coreCollection = new RawType("core", "Collection"),
        coreSequence = new RawType("core", "Sequence"),
        coreSource = new RawType("core", "Source");

    public final String module, name;

    public RawType(String module, String name) {
        this.module = module;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        try {
            RawType that = (RawType) o;
            return module.equals(that.module) && name.equals(that.name);
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return module.hashCode() ^ name.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s.%s", module, name);
    }
}
