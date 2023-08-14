package passes;

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
        Opcode opcode = jmpInstr.getOpcode();
        IRStruct struct;

        switch (opcode) {
            case JMP_OVER_FUN -> {
                for (struct = jmpInstr.getNextInstr().getContainer();
                     struct.getStructType() != IRStructType.FUNCTION;
                     struct = struct.getParent())
                    ;
                Instruction targetInstr = struct.getTailInstr().getNextInstr();
                jmpInstr.setTargetInstr(targetInstr);
            }
            case JMP_TO_LOOP -> {
                for (struct = jmpInstr.getContainer();
                     struct.getStructType() != IRStructType.LOOP;
                     struct = struct.getParent())
                    ;
                Instruction targetInstr = struct.getHeadInstr();
                jmpInstr.setTargetInstr(targetInstr);
            }
            case JMP_OUT_LOOP, JMP_LOOP_FALSE -> {
                for (struct = jmpInstr.getContainer();
                     struct.getStructType() != IRStructType.LOOP;
                     struct = struct.getParent())
                    ;
                Instruction targetInstr = struct.getTailInstr().getNextInstr();
                jmpInstr.setTargetInstr(targetInstr);
            }
            case JMP_OUT_IF_ELSE -> {
                for (struct = jmpInstr.getContainer();
                     struct.getStructType() != IRStructType.IF_ELSE;
                     struct = struct.getParent())
                    ;
                Instruction targetInstr = struct.getTailInstr().getNextInstr();
                jmpInstr.setTargetInstr(targetInstr);
            }
            default -> {
                // JMP_IF_FALSE
                for (struct = jmpInstr.getContainer();
                     struct.getStructType() != IRStructType.IF;
                     struct = struct.getParent())
                    ;
                Instruction targetInstr = struct.getTailInstr().getNextInstr();
                jmpInstr.setTargetInstr(targetInstr);
            }
        }
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
