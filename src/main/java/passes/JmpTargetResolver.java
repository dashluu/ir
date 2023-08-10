package passes;

import cfg.BasicBlock;
import cfg.CFG;
import cfg.ICFGVisitor;
import instructions.*;
import structs.IRStruct;
import structs.IRStructType;
import utils.IRContext;

public class JmpTargetResolver implements ICFGVisitor {
    /**
     * Visits instructions in the given context's CFG and resolves jump targets.
     *
     * @param context the input IR context.
     */
    public void run(IRContext context) {
        CFG cfg = context.getCfg();
        cfg.accept(this);
    }

    @Override
    public void visitCFG(CFG cfg) {
        for (BasicBlock block : cfg) {
            block.accept(this);
        }
    }

    @Override
    public void visitBasicBlock(BasicBlock block) {
        IInstrListIterator instrListIter = block.instrListIterator();
        Instruction instr;
        while (instrListIter.hasNext()) {
            instr = instrListIter.next().accept(this);
            instrListIter.set(instr);
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
        long instrId = breakInstr.getId();
        Opcode opcode = breakInstr.getOpcode();
        Instruction instr, targetHeadInstr;
        IRStruct struct;

        switch (opcode) {
            case BREAK_LOOP -> {
                for (struct = breakInstr.getContainer();
                     struct.getStructType() != IRStructType.LOOP;
                     struct = struct.getParent())
                    ;
                targetHeadInstr = struct.getTailInstr().getNextInstr();
                instr = new JmpInstr(instrId, Opcode.JMP, targetHeadInstr);
            }
            case BREAK_IF_ELSE -> {
                for (struct = breakInstr.getContainer();
                     struct.getStructType() != IRStructType.IF_ELSE;
                     struct = struct.getParent())
                    ;
                targetHeadInstr = struct.getTailInstr().getNextInstr();
                instr = new JmpInstr(instrId, Opcode.JMP_IF_FALSE, targetHeadInstr);
            }
            case BREAK_LOOP_FALSE -> {
                for (struct = breakInstr.getContainer();
                     struct.getStructType() != IRStructType.LOOP;
                     struct = struct.getParent())
                    ;
                targetHeadInstr = struct.getTailInstr().getNextInstr();
                instr = new JmpInstr(instrId, Opcode.JMP_IF_FALSE, targetHeadInstr);
            }
            default -> {
                for (struct = breakInstr.getContainer();
                     struct.getStructType() != IRStructType.IF;
                     struct = struct.getParent())
                    ;
                targetHeadInstr = struct.getTailInstr().getNextInstr();
                instr = new JmpInstr(instrId, Opcode.JMP_IF_FALSE, targetHeadInstr);
            }
        }

        return instr;
    }

    @Override
    public Instruction visitContInstr(ContInstr contInstr) {
        IRStruct struct;
        for (struct = contInstr.getContainer();
             struct.getStructType() != IRStructType.LOOP;
             struct = struct.getParent())
            ;
        Instruction targetHeadInstr = struct.getHeadInstr();
        return new JmpInstr(contInstr.getId(), Opcode.JMP, targetHeadInstr);
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
    public Instruction visitExitInstr(ExitInstr exitInstr) {
        return exitInstr;
    }
}
