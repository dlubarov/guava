package d;

import d.ty.ConcreteType;

public class NormalObject extends BaseObject {
    // TODO: Could perhaps eliminate this class, make BaseObject concrete, and use it
    // for non-native objects. Would reduce object construction overhead.
    public NormalObject(ConcreteType type) {
        super(type);
    }
}
