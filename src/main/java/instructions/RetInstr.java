package instructions;

public class RetInstr extends Instruction {
    public RetInstr() {
        super(InstrType.RET, null);
    }

    @Override
    public void accept(IInstrVisitor instrVisitor) {
        instrVisitor.visitRetInstr(this);
    }
}
