package instructions;

// Unary operation instruction
public class UnOpInstr extends Instruction {
    private final String opName;
    private final long opRef;

    public UnOpInstr(String opName, long opRef) {
        super(InstrType.UN_OP, null);
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
    public void accept(IInstrVisitor instrVisitor) {
        instrVisitor.visitUnOpInstr(this);
    }

    @Override
    public String toString() {
        return super.toString() + " " + opRef + " (" + opName + ")";
    }
}
