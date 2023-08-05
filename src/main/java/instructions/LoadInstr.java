package instructions;

// Instruction used to load the value of a variable or a constant or a parameter
public class LoadInstr extends Instruction {
    // A source can be a variable, a constant, or a parameter
    private final String srcName;
    private final long srcRef;

    public LoadInstr(Opcode opcode, String srcName, long srcRef) {
        super(InstrType.LOAD, opcode);
        this.srcName = srcName;
        this.srcRef = srcRef;
    }

    public String getSrcName() {
        return srcName;
    }

    public long getSrcRef() {
        return srcRef;
    }

    @Override
    public Instruction accept(IInstrVisitor instrVisitor) {
        return instrVisitor.visitLoadInstr(this);
    }

    @Override
    public String toString() {
        return super.toString() + " " + srcRef + " (" + srcName + ")";
    }
}
