package instructions;

public class JmpInstr extends Instruction {
    private Instruction targetInstr;

    public JmpInstr(long id, Opcode opcode, Instruction targetInstr) {
        super(id, InstrType.JMP, opcode);
        this.targetInstr = targetInstr;
    }

    public Instruction getTargetInstr() {
        return targetInstr;
    }

    public void setTargetInstr(Instruction targetInstr) {
        this.targetInstr = targetInstr;
    }

    @Override
    public void accept(IInstrVisitor instrVisitor) {
        instrVisitor.visitJmpInstr(this);
    }

    @Override
    public String toString() {
        return super.toString() + " " + targetInstr.getId();
    }
}
