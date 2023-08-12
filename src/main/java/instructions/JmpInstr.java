package instructions;

public class JmpInstr extends Instruction {
    private Instruction targetHeadInstr;

    public JmpInstr(long id, Opcode opcode, Instruction targetHeadInstr) {
        super(id, InstrType.JMP, opcode);
        this.targetHeadInstr = targetHeadInstr;
    }

    public Instruction getTargetHeadInstr() {
        return targetHeadInstr;
    }

    public void setTargetHeadInstr(Instruction targetHeadInstr) {
        this.targetHeadInstr = targetHeadInstr;
    }

    @Override
    public void accept(IInstrVisitor instrVisitor) {
        instrVisitor.visitJmpInstr(this);
    }

    @Override
    public String toString() {
        return super.toString() + " " + targetHeadInstr.getId();
    }
}
