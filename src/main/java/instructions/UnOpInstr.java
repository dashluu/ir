package instructions;

// Unary operation instruction
public class UnOpInstr extends Instruction {
    private final String opName;
    private final long opRef;

    public UnOpInstr(long id, String opName, long opRef) {
        super(id, InstrType.UN_OP, null);
        this.opName = opName;
        this.opRef = opRef;
    }

    public String getOpName() {
        return opName;
    }

    public long getOpRef() {
        return opRef;
    }

    @Override
    public Instruction accept(IInstrVisitor instrVisitor) {
        return instrVisitor.visitUnOpInstr(this);
    }

    @Override
    public String toString() {
        return super.toString() + " " + opRef + " (" + opName + ")";
    }
}
