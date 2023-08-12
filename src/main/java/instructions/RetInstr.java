package instructions;

public class RetInstr extends Instruction {
    public RetInstr(long id) {
        super(id, InstrType.RET, null);
    }

    @Override
    public void accept(IInstrVisitor instrVisitor) {
        instrVisitor.visitRetInstr(this);
    }
}
