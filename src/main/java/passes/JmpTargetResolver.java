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
        for (Instruction instr : block) {
            instr.accept(this);
        }
    }

    @Override
    public void visitUnOpInstr(UnOpInstr unOpInstr) {
    }

    @Override
    public void visitBinOpInstr(BinOpInstr binOpInstr) {
    }

    @Override
    public void visitCallInstr(CallInstr callInstr) {
    }

    @Override
    public void visitJmpInstr(JmpInstr jmpInstr) {
    }

    @Override
    public void visitBreakInstr(BreakInstr breakInstr) {
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

        instr.replaceInstr(breakInstr);
    }

    @Override
    public void visitContInstr(ContInstr contInstr) {
        IRStruct struct;
        for (struct = contInstr.getContainer();
             struct.getStructType() != IRStructType.LOOP;
             struct = struct.getParent())
            ;
        Instruction targetHeadInstr = struct.getHeadInstr();
        Instruction instr = new JmpInstr(contInstr.getId(), Opcode.JMP, targetHeadInstr);
        instr.replaceInstr(contInstr);
    }

    @Override
    public void visitLoadInstr(LoadInstr loadInstr) {
    }

    @Override
    public void visitStoreInstr(StoreInstr storeInstr) {
    }

    @Override
    public void visitRetInstr(RetInstr retInstr) {
    }

    @Override
    public void visitExitInstr(ExitInstr exitInstr) {
    }
}
