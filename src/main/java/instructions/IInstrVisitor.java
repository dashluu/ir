package instructions;

public interface IInstrVisitor {
    Instruction visitUnOpInstr(UnOpInstr unOpInstr);

    Instruction visitBinOpInstr(BinOpInstr binOpInstr);

    Instruction visitCallInstr(CallInstr callInstr);

    Instruction visitJmpInstr(JmpInstr jmpInstr);

    Instruction visitBreakInstr(BreakInstr breakInstr);

    Instruction visitContInstr(ContInstr contInstr);

    Instruction visitRetInstr(RetInstr retInstr);

    Instruction visitPushFunInstr(PushFunInstr pushFunInstr);

    Instruction visitPopFunInstr(PopFunInstr popFunInstr);

    Instruction visitLoadInstr(LoadInstr loadInstr);

    Instruction visitStoreInstr(StoreInstr storeInstr);

    Instruction visitExitInstr(ExitInstr exitInstr);
}
