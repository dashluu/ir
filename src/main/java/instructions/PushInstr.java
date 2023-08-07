package instructions;

public class PushInstr extends Instruction {
    public PushInstr(Opcode opcode) {
        super(InstrType.PUSH, opcode);
    }

    @Override
    public Instruction accept(IInstrVisitor instrVisitor) {
        return instrVisitor.visitPushInstr(this);
    }
}
