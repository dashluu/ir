package structs;

public class IRStruct {
    private long start;
    private long end;
    private final IRStructType structType;
    private final IRStruct parent;

    public IRStruct(long start, long end, IRStructType structType, IRStruct parent) {
        this.start = start;
        this.end = end;
        this.structType = structType;
        this.parent = parent;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public IRStructType getStructType() {
        return structType;
    }

    public IRStruct getParent() {
        return parent;
    }
}
