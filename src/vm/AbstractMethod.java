package vm;

import common.*;

import vm.ty.*;

public class AbstractMethod extends Method {

    public AbstractMethod(RawMethodDesc desc) {
        super(desc, null, null, null);
    }

    public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
        throw new RuntimeException("abstract method was invoked");
    }
}
