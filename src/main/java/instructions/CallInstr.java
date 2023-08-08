package instructions;

// Instruction for calling function
public class CallInstr extends Instruction {
    private final String calleeName;
    private Instruction calleeHeadInstr;

    public CallInstr(long id, String calleeName, Instruction calleeHeadInstr) {
        super(id, InstrType.CALL, null);
        this.calleeName = calleeName;
        this.calleeHeadInstr = calleeHeadInstr;
    }

    public String getCalleeName() {
        return calleeName;
    }

    public Instruction getCalleeHeadInstr() {
        return calleeHeadInstr;
    }

    public void setCalleeHeadInstr(Instruction calleeHeadInstr) {
        this.calleeHeadInstr = calleeHeadInstr;
    }

    @Override
    public Instruction accept(IInstrVisitor instrVisitor) {
        return instrVisitor.visitCallInstr(this);
    }

    @Override
    public String toString() {
        return super.toString() + " " + calleeHeadInstr.getId() + " (" + calleeName + ")";
    }
}
