package instructions;

// Binary operation instruction
public class BinOpInstr extends Instruction {
    private final String opName;
    private final long opRef;

    public BinOpInstr(long id, String opName, long opRef) {
        super(id, InstrType.BIN_OP, null);
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
        instrVisitor.visitBinOpInstr(this);
    }

    @Override
    public String toString() {
        return super.toString() + " " + opRef + " (" + opName + ")";
    }
}
