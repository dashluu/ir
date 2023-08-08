package instructions;

public class ExitInstr extends Instruction {
    public ExitInstr(long id) {
        super(id, InstrType.EXIT, null);
    }

    @Override
    public Instruction accept(IInstrVisitor instrVisitor) {
        return instrVisitor.visitExitInstr(this);
    }
}
