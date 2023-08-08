package structs;

import instructions.Instruction;

public class IRStruct {
    private Instruction headInstr;
    private Instruction tailInstr;
    private final IRStructType structType;
    private final IRStruct parent;

    public IRStruct(IRStructType structType, IRStruct parent) {
        this.structType = structType;
        this.parent = parent;
    }

    public Instruction getHeadInstr() {
        return headInstr;
    }

    public void setHeadInstr(Instruction headInstr) {
        this.headInstr = headInstr;
    }

    public Instruction getTailInstr() {
        return tailInstr;
    }

    public void setTailInstr(Instruction tailInstr) {
        this.tailInstr = tailInstr;
    }

    public IRStructType getStructType() {
        return structType;
    }

    public IRStruct getParent() {
        return parent;
    }
}
