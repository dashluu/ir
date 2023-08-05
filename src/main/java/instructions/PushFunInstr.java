package instructions;

public class PushFunInstr extends Instruction {
    private final String funName;
    private final long funRef;

    public PushFunInstr(String funName, long funRef) {
        super(InstrType.PUSH_FUN, null);
        this.funName = funName;
        this.funRef = funRef;
    }

    public String getFunName() {
        return funName;
    }

    public long getFunRef() {
        return funRef;
    }

    @Override
    public Instruction accept(IInstrVisitor instrVisitor) {
        return instrVisitor.visitPushFunInstr(this);
    }

    @Override
    public String toString() {
        return super.toString() + " " + funRef + " (" + funName + ")";
    }
}
