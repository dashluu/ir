package instructions;

public class ExitInstr extends Instruction {
    public ExitInstr() {
        super(InstrType.EXIT, null);
    }

    @Override
    public void accept(IInstrVisitor instrVisitor) {
        instrVisitor.visitExitInstr(this);
    }
}
