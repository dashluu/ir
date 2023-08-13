package instructions;

// Instruction for calling function
public class CallInstr extends Instruction {
    private final String calleeName;
    private Instruction calleeInstr;

    public CallInstr(String calleeName, Instruction calleeInstr) {
        super(InstrType.CALL, null);
        this.calleeName = calleeName;
        this.calleeInstr = calleeInstr;
    }

    public String getCalleeName() {
        return calleeName;
    }

    public Instruction getCalleeInstr() {
        return calleeInstr;
    }

    public void setCalleeInstr(Instruction calleeInstr) {
        this.calleeInstr = calleeInstr;
    }

    @Override
    public void accept(IInstrVisitor instrVisitor) {
        instrVisitor.visitCallInstr(this);
    }

    @Override
    public String toString() {
        return super.toString() + " " + calleeInstr.getLabel() + " (" + calleeName + ")";
    }
}
