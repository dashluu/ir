package structs;

import types.TypeInfo;

public class IRFunction extends IRStruct {
    private final TypeInfo retDtype;

    public IRFunction(long start, long end, IRStruct parent, TypeInfo retDtype) {
        super(start, end, IRStructType.FUNCTION, parent);
        this.retDtype = retDtype;
    }

    public TypeInfo getRetDtype() {
        return retDtype;
    }
}
