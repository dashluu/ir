package instructions;

// Return instruction
public class RetInstr extends Instruction {
    public RetInstr() {
        super(InstrType.RET, null);
    }

    @Override
    public Instruction accept(IInstrVisitor instrVisitor) {
        return instrVisitor.visitRetInstr(this);
    }
}
