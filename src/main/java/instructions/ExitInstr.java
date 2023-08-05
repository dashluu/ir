package instructions;

public class ExitInstr extends Instruction {
    public ExitInstr() {
        super(InstrType.EXIT, null);
    }

    @Override
    public Instruction accept(IInstrVisitor instrVisitor) {
        return instrVisitor.visitExitInstr(this);
    }
}
