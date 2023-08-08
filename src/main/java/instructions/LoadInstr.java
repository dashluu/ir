package instructions;

import refs.SymRef;

public class LoadInstr extends Instruction {
    private final SymRef srcRef;

    public LoadInstr(Opcode opcode, SymRef srcRef) {
        super(InstrType.LOAD, opcode);
        this.srcRef = srcRef;
    }

    public SymRef getSrcRef() {
        return srcRef;
    }

    @Override
    public Instruction accept(IInstrVisitor instrVisitor) {
        return instrVisitor.visitLoadInstr(this);
    }

    @Override
    public String toString() {
        return super.toString() + " " + srcRef.getRefVal() + " (" + srcRef.getId() + ")";
    }
}
