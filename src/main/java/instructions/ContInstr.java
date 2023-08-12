package instructions;

public class ContInstr extends Instruction {
    public ContInstr(long id) {
        super(id, InstrType.CONT, null);
    }

    @Override
    public void accept(IInstrVisitor instrVisitor) {
        instrVisitor.visitContInstr(this);
    }
}
