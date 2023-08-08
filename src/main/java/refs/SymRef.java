package refs;

import types.TypeInfo;

public class SymRef {
    protected final String id;
    protected final SymRefType symRefType;
    protected final TypeInfo dtype;
    protected final long refVal;

    public SymRef(String id, SymRefType symRefType, TypeInfo dtype, long refVal) {
        this.id = id;
        this.symRefType = symRefType;
        this.dtype = dtype;
        this.refVal = refVal;
    }

    public String getId() {
        return id;
    }

    public SymRefType getSymRefType() {
        return symRefType;
    }

    public TypeInfo getDtype() {
        return dtype;
    }

    public long getRefVal() {
        return refVal;
    }
}
