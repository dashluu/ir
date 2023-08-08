package instructions;

import refs.SymRef;

public class StoreInstr extends Instruction {
    private final SymRef destRef;

    public StoreInstr(Opcode opcode, SymRef destRef) {
        super(InstrType.STORE, opcode);
        this.destRef = destRef;
    }

    public SymRef getDestRef() {
        return destRef;
    }

    @Override
    public Instruction accept(IInstrVisitor instrVisitor) {
        return instrVisitor.visitStoreInstr(this);
    }

    @Override
    public String toString() {
        return super.toString() + " " + destRef.getRefVal() + " (" + destRef.getId() + ")";
    }
}
