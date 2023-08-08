package refs;

import types.TypeInfo;

public class LiteralRef extends SymRef {
    public LiteralRef(String id, TypeInfo dtype, long refVal) {
        super(id, SymRefType.LITERAL, dtype, refVal);
    }
}
