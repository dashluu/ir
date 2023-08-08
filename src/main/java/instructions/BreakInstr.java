package instructions;

public class BreakInstr extends Instruction {
    public BreakInstr(long id, Opcode opcode) {
        super(id, InstrType.BREAK, opcode);
    }

    @Override
    public Instruction accept(IInstrVisitor instrVisitor) {
        return instrVisitor.visitBreakInstr(this);
    }
}
