package instructions;

public class ContInstr extends Instruction {
    public ContInstr() {
        super(InstrType.CONT, null);
    }

    @Override
    public void accept(IInstrVisitor instrVisitor) {
        instrVisitor.visitContInstr(this);
    }
}
