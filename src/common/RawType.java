package common;

public class RawType {
    public static final RawType[] NONE = new RawType[0];

    public static final RawType
        coreTop = new RawType("core", "Top"),
        coreBottom = new RawType("core", "Bottom"),
        coreUnit = new RawType("core", "Unit"),
        coreInt = new RawType("core", "Int"),
        coreLong = new RawType("core", "Long"),
        coreByte = new RawType("core", "Byte"),
        coreChar = new RawType("core", "Char"),
        coreBool = new RawType("core", "Bool"),
        coreDouble = new RawType("core", "Double"),
        coreArray = new RawType("core", "Array"),
        coreEnumerable = new RawType("core", "Enumerable"),
        coreCollection = new RawType("core", "Collection"),
        coreSequence = new RawType("core", "Sequence"),
        coreSource = new RawType("core", "Source"),
        coreMaybe = new RawType("core", "Maybe"),
        coreString = new RawType("core", "String"),
        coreConsole = new RawType("core", "Console"),
        coreRelation = new RawType("core", "Relation");

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
