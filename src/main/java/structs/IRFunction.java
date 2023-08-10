package structs;

import types.TypeInfo;

public class IRFunction extends IRStruct {
    private final String id;
    private final TypeInfo retDtype;

    public IRFunction(String id, TypeInfo retDtype, IRStruct parent) {
        super(IRStructType.FUNCTION, parent);
        this.id = id;
        this.retDtype = retDtype;
    }

    public String getId() {
        return id;
    }

    public TypeInfo getRetDtype() {
        return retDtype;
    }
}
