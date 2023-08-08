package instructions;

public class ContInstr extends Instruction {
    public ContInstr(long id) {
        super(id, InstrType.CONT, null);
    }

    @Override
    public Instruction accept(IInstrVisitor instrVisitor) {
        return instrVisitor.visitContInstr(this);
    }
}
