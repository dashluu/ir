package passes;

import instructions.*;
import structs.IRStruct;
import structs.IRStructType;

import java.util.List;
import java.util.ListIterator;

public class JmpTargetResolver implements IInstrVisitor {
    /**
     * Traverses a list of instructions and resolves any jump target.
     *
     * @param instrList the list of instructions.
     * @return the processed list of instructions.
     */
    public List<Instruction> run(List<Instruction> instrList) {
        ListIterator<Instruction> instrIter = instrList.listIterator();
        Instruction instr;

        while (instrIter.hasNext()) {
            instr = instrIter.next().accept(this);
            instrIter.set(instr);
        }

        return instrList;
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
        IRStruct container;

        switch (opcode) {
            case BREAK_LOOP -> {
                for (container = breakInstr.getContainer();
                     container.getStructType() != IRStructType.LOOP;
                     container = container.getParent())
                    ;
                targetId = container.getEnd();
                instr = new JmpInstr(Opcode.JMP, targetId);
            }
            case BREAK_IF_ELSE -> {
                for (container = breakInstr.getContainer();
                     container.getStructType() != IRStructType.IF_ELSE;
                     container = container.getParent())
                    ;
                targetId = container.getEnd();
                instr = new JmpInstr(Opcode.JMP_IF_FALSE, targetId);
            }
            case BREAK_LOOP_FALSE -> {
                for (container = breakInstr.getContainer();
                     container.getStructType() != IRStructType.LOOP;
                     container = container.getParent())
                    ;
                targetId = container.getEnd();
                instr = new JmpInstr(Opcode.JMP_IF_FALSE, targetId);
            }
            default -> {
                for (container = breakInstr.getContainer();
                     container.getStructType() != IRStructType.IF;
                     container = container.getParent())
                    ;
                targetId = container.getEnd();
                instr = new JmpInstr(Opcode.JMP_IF_FALSE, targetId);
            }
        }

        return instr;
    }

    @Override
    public Instruction visitContInstr(ContInstr contInstr) {
        IRStruct container;
        for (container = contInstr.getContainer();
             container.getStructType() != IRStructType.LOOP;
             container = container.getParent())
            ;
        long targetId = container.getStart();
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
    public Instruction visitPopFunInstr(PopFunInstr popFunInstr) {
        return popFunInstr;
    }

    @Override
    public Instruction visitExitInstr(ExitInstr exitInstr) {
        return exitInstr;
    }
}
