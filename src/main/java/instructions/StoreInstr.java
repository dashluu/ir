package instructions;

// Instruction used to store a value to a variable or a constant or a parameter
public class StoreInstr extends Instruction {
    private final String destName;
    private final long destRef;

    public StoreInstr(Opcode opcode, String destName, long destRef) {
        super(InstrType.STORE, opcode);
        this.destName = destName;
        this.destRef = destRef;
    }

    public String getDestName() {
        return destName;
    }

    public long getDestRef() {
        return destRef;
    }

    @Override
    public Instruction accept(IInstrVisitor instrVisitor) {
        return instrVisitor.visitStoreInstr(this);
    }

    @Override
    public String toString() {
        return super.toString() + " " + destRef + " (" + destName + ")";
    }
}
