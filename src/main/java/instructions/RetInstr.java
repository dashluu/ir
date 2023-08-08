package instructions;

public class RetInstr extends Instruction {
    public RetInstr(long id) {
        super(id, InstrType.RET, null);
    }

    @Override
    public Instruction accept(IInstrVisitor instrVisitor) {
        return instrVisitor.visitRetInstr(this);
    }
}
