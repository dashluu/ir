package instructions;

public class BreakInstr extends Instruction {
    public BreakInstr(Opcode opcode) {
        super(InstrType.BREAK, opcode);
    }

    @Override
    public void accept(IInstrVisitor instrVisitor) {
        instrVisitor.visitBreakInstr(this);
    }
}
