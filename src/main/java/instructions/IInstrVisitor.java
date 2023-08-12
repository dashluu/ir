package instructions;

public interface IInstrVisitor {
    void visitUnOpInstr(UnOpInstr unOpInstr);

    void visitBinOpInstr(BinOpInstr binOpInstr);

    void visitCallInstr(CallInstr callInstr);

    void visitJmpInstr(JmpInstr jmpInstr);

    void visitBreakInstr(BreakInstr breakInstr);

    void visitContInstr(ContInstr contInstr);

    void visitRetInstr(RetInstr retInstr);

    void visitLoadInstr(LoadInstr loadInstr);

    void visitStoreInstr(StoreInstr storeInstr);

    void visitExitInstr(ExitInstr exitInstr);
}
