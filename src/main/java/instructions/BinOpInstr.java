package instructions;

// Binary operation instruction
public class BinOpInstr extends Instruction {
    private final String opName;
    private final long opRef;

    public BinOpInstr(String opName, long opRef) {
        super(InstrType.BIN_OP, null);
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
        return instrVisitor.visitBinOpInstr(this);
    }

    @Override
    public String toString() {
        return super.toString() + " " + opRef + " (" + opName + ")";
    }
}
