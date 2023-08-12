package instructions;

import refs.SymRef;

public class LoadInstr extends Instruction {
    private final SymRef srcRef;

    public LoadInstr(long id, Opcode opcode, SymRef srcRef) {
        super(id, InstrType.LOAD, opcode);
        this.srcRef = srcRef;
    }

    public SymRef getSrcRef() {
        return srcRef;
    }

    @Override
    public void accept(IInstrVisitor instrVisitor) {
        instrVisitor.visitLoadInstr(this);
    }

    @Override
    public String toString() {
        return super.toString() + " " + srcRef.getRefVal() + " (" + srcRef.getId() + ")";
    }
}
