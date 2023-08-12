package instructions;

import refs.SymRef;

public class StoreInstr extends Instruction {
    private final SymRef destRef;

    public StoreInstr(long id, Opcode opcode, SymRef destRef) {
        super(id, InstrType.STORE, opcode);
        this.destRef = destRef;
    }

    public SymRef getDestRef() {
        return destRef;
    }

    @Override
    public void accept(IInstrVisitor instrVisitor) {
        instrVisitor.visitStoreInstr(this);
    }

    @Override
    public String toString() {
        return super.toString() + " " + destRef.getRefVal() + " (" + destRef.getId() + ")";
    }
}
