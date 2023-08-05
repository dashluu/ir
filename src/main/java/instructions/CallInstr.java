package instructions;

// Instruction for calling function
public class CallInstr extends Instruction {
    private final String calleeName;
    private final long calleeId;

    public CallInstr(String calleeName, long calleeId) {
        super(InstrType.CALL, null);
        this.calleeName = calleeName;
        this.calleeId = calleeId;
    }

    public String getCalleeName() {
        return calleeName;
    }

    public long getCalleeId() {
        return calleeId;
    }

    @Override
    public Instruction accept(IInstrVisitor instrVisitor) {
        return instrVisitor.visitCallInstr(this);
    }

    @Override
    public String toString() {
        return super.toString() + " " + calleeId + " (" + calleeName + ")";
    }
}
