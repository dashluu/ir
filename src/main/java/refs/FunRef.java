package refs;

import structs.IRFunction;

public class FunRef extends SymRef {
    private final IRFunction function;

    public FunRef(String id, long refVal, IRFunction function) {
        super(id, SymRefType.FUNCTION, function.getRetDtype(), refVal);
        this.function = function;
    }

    public IRFunction getFunction() {
        return function;
    }
}
