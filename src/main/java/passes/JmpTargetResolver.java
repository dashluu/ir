package passes;

import cfg.BasicBlock;
import cfg.CFG;
import cfg.ICFGVisitor;
import instructions.*;
import structs.IRStruct;
import structs.IRStructType;
import utils.IRContext;

import java.util.ListIterator;

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
        ListIterator<BasicBlock> blockIter = cfg.listIterator();
        BasicBlock block;

        while (blockIter.hasNext()) {
            block = blockIter.next().accept(this);
            if (block == null) {
                blockIter.remove();
            } else {
                blockIter.set(block);
            }
        }
    }

    @Override
    public BasicBlock visitBasicBlock(BasicBlock block) {
        ListIterator<Instruction> instrIter = block.listIterator();
        Instruction instr;

        while (instrIter.hasNext()) {
            instr = instrIter.next().accept(this);
            if (instr == null) {
                instrIter.remove();
            } else {
                instrIter.set(instr);
            }
        }

        // Remove basic block if it is empty
        return block.isEmpty() ? null : block;
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
    public Instruction visitPushInstr(PushInstr pushInstr) {
        IRStruct upStruct;
        for (upStruct = pushInstr.getContainer(); upStruct != null; upStruct = upStruct.getParent()) {
            upStruct.setEnd(upStruct.getEnd() - 1);
        }
        // Remove push instructions
        return null;
    }

    @Override
    public Instruction visitExitInstr(ExitInstr exitInstr) {
        return exitInstr;
    }
}
