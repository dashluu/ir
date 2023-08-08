package refs;

import types.TypeInfo;

public class VarRef extends SymRef {
    public VarRef(String id, TypeInfo dtype, long refVal) {
        super(id, SymRefType.VAR, dtype, refVal);
    }
}
