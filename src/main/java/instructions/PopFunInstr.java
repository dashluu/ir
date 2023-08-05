package instructions;

public class PopFunInstr extends Instruction {
    public PopFunInstr() {
        super(InstrType.POP_FUN, null);
    }

    @Override
    public Instruction accept(IInstrVisitor instrVisitor) {
        return instrVisitor.visitPopFunInstr(this);
    }
}
