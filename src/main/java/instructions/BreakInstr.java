package instructions;

public class BreakInstr extends Instruction {
    public BreakInstr(long id, Opcode opcode) {
        super(id, InstrType.BREAK, opcode);
    }

    @Override
    public void accept(IInstrVisitor instrVisitor) {
        instrVisitor.visitBreakInstr(this);
    }
}
