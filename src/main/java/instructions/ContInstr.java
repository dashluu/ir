package instructions;

public class ContInstr extends Instruction {
    public ContInstr() {
        super(InstrType.CONT, null);
    }

    @Override
    public Instruction accept(IInstrVisitor instrVisitor) {
        return instrVisitor.visitContInstr(this);
    }
}
