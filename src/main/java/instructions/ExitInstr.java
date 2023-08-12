package instructions;

public class ExitInstr extends Instruction {
    public ExitInstr(long id) {
        super(id, InstrType.EXIT, null);
    }

    @Override
    public void accept(IInstrVisitor instrVisitor) {
        instrVisitor.visitExitInstr(this);
    }
}
