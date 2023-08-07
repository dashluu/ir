package instructions;

public interface IInstrVisitor {
    Instruction visitUnOpInstr(UnOpInstr unOpInstr);

    Instruction visitBinOpInstr(BinOpInstr binOpInstr);

    Instruction visitCallInstr(CallInstr callInstr);

    Instruction visitJmpInstr(JmpInstr jmpInstr);

    Instruction visitBreakInstr(BreakInstr breakInstr);

    Instruction visitContInstr(ContInstr contInstr);

    Instruction visitRetInstr(RetInstr retInstr);

    Instruction visitPushInstr(PushInstr pushInstr);

    Instruction visitLoadInstr(LoadInstr loadInstr);

    Instruction visitStoreInstr(StoreInstr storeInstr);

    Instruction visitExitInstr(ExitInstr exitInstr);
}
