package instructions;

public class JmpInstr extends Instruction {
    private long targetId;

    public JmpInstr(Opcode opcode, long targetId) {
        super(InstrType.JMP, opcode);
        this.targetId = targetId;
    }

    public long getTargetId() {
        return targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    @Override
    public Instruction accept(IInstrVisitor instrVisitor) {
        return instrVisitor.visitJmpInstr(this);
    }

    @Override
    public String toString() {
        return super.toString() + " " + targetId;
    }
}
