package refs;

import types.TypeInfo;

public class TypeRef extends SymRef {
    public TypeRef(TypeInfo dtype, long refVal) {
        super(dtype.getId(), SymRefType.DTYPE, dtype, refVal);
    }
}
