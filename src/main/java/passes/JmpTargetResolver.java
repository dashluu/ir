package passes;

import instructions.*;
import structs.IRStruct;
import structs.IRStructType;
import utils.IRContext;

import java.util.ListIterator;

public class JmpTargetResolver implements IInstrVisitor {
    /**
     * Traverses a list of instructions and resolves any jump target.
     *
     * @param context the input IR context.
     */
    public void run(IRContext context) {
        ListIterator<Instruction> instrIter = context.getInstrList().listIterator();
        Instruction instr;

        while (instrIter.hasNext()) {
            instr = instrIter.next().accept(this);
            instrIter.set(instr);
        }
    }

    @Override
    public Instruction visitUnOpInstr(UnOpInstr unOpInstr) {
        return unOpInstr;
    }

    @Override
    public Instruction visitBinOpInstr(BinOpInstr binOpInstr) {
        return binOpInstr;
    }

    @Override
    public Instruction visitCallInstr(CallInstr callInstr) {
        return callInstr;
    }

    @Override
    public Instruction visitJmpInstr(JmpInstr jmpInstr) {
        return jmpInstr;
    }

    @Override
    public Instruction visitBreakInstr(BreakInstr breakInstr) {
        Opcode opcode = breakInstr.getOpcode();
        long targetId;
        Instruction instr;
        IRStruct upStruct;

        switch (opcode) {
            case BREAK_LOOP -> {
                for (upStruct = breakInstr.getContainer();
                     upStruct.getStructType() != IRStructType.LOOP;
                     upStruct = upStruct.getParent())
                    ;
                targetId = upStruct.getEnd();
                instr = new JmpInstr(Opcode.JMP, targetId);
            }
            case BREAK_IF_ELSE -> {
                for (upStruct = breakInstr.getContainer();
                     upStruct.getStructType() != IRStructType.IF_ELSE;
                     upStruct = upStruct.getParent())
                    ;
                targetId = upStruct.getEnd();
                instr = new JmpInstr(Opcode.JMP_IF_FALSE, targetId);
            }
            case BREAK_LOOP_FALSE -> {
                for (upStruct = breakInstr.getContainer();
                     upStruct.getStructType() != IRStructType.LOOP;
                     upStruct = upStruct.getParent())
                    ;
                targetId = upStruct.getEnd();
                instr = new JmpInstr(Opcode.JMP_IF_FALSE, targetId);
            }
            default -> {
                for (upStruct = breakInstr.getContainer();
                     upStruct.getStructType() != IRStructType.IF;
                     upStruct = upStruct.getParent())
                    ;
                targetId = upStruct.getEnd();
                instr = new JmpInstr(Opcode.JMP_IF_FALSE, targetId);
            }
        }

        return instr;
    }

    @Override
    public Instruction visitContInstr(ContInstr contInstr) {
        IRStruct upStruct;
        for (upStruct = contInstr.getContainer();
             upStruct.getStructType() != IRStructType.LOOP;
             upStruct = upStruct.getParent())
            ;
        long targetId = upStruct.getStart();
        return new JmpInstr(Opcode.JMP, targetId);
    }

    @Override
    public Instruction visitLoadInstr(LoadInstr loadInstr) {
        return loadInstr;
    }

    @Override
    public Instruction visitStoreInstr(StoreInstr storeInstr) {
        return storeInstr;
    }

    @Override
    public Instruction visitRetInstr(RetInstr retInstr) {
        return retInstr;
    }

    @Override
    public Instruction visitPushFunInstr(PushFunInstr pushFunInstr) {
        return pushFunInstr;
    }

    @Override
    public Instruction visitExitInstr(ExitInstr exitInstr) {
        return exitInstr;
    }
}
