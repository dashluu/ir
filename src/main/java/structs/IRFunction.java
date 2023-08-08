package structs;

import types.TypeInfo;

public class IRFunction extends IRStruct {
    private final TypeInfo retDtype;

    public IRFunction(TypeInfo retDtype, IRStruct parent) {
        super(IRStructType.FUNCTION, parent);
        this.retDtype = retDtype;
    }

    public TypeInfo getRetDtype() {
        return retDtype;
    }
}
