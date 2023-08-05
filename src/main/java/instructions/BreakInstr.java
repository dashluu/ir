package instructions;

public class BreakInstr extends Instruction {
    public BreakInstr(Opcode opcode) {
        super(InstrType.BREAK, opcode);
    }

    @Override
    public Instruction accept(IInstrVisitor instrVisitor) {
        return instrVisitor.visitBreakInstr(this);
    }
}
