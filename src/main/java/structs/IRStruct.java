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

    public Instruction getTailInstr() {
        return tailInstr;
    }

    public IRStructType getStructType() {
        return structType;
    }

    public IRStruct getParent() {
        return parent;
    }

    /**
     * Adds an instruction to the structure.
     *
     * @param instr the instruction to be added.
     */
    public void addInstr(Instruction instr) {
        instr.setContainer(this);
        IRStruct struct = this;
        while (struct != null) {
            if (struct.headInstr == null) {
                struct.headInstr = struct.tailInstr = instr;
            } else {
                struct.tailInstr = instr;
            }
            struct = struct.parent;
        }
    }
}
