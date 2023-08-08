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
        Instruction instr, targetHeadInstr;
        IRStruct upStruct;

        switch (opcode) {
            case BREAK_LOOP -> {
                for (upStruct = breakInstr.getContainer();
                     upStruct.getStructType() != IRStructType.LOOP;
                     upStruct = upStruct.getParent())
                    ;
                targetHeadInstr = upStruct.getTailInstr().getNextInstr();
                instr = new JmpInstr(Opcode.JMP, targetHeadInstr);
            }
            case BREAK_IF_ELSE -> {
                for (upStruct = breakInstr.getContainer();
                     upStruct.getStructType() != IRStructType.IF_ELSE;
                     upStruct = upStruct.getParent())
                    ;
                targetHeadInstr = upStruct.getTailInstr().getNextInstr();
                instr = new JmpInstr(Opcode.JMP_IF_FALSE, targetHeadInstr);
            }
            case BREAK_LOOP_FALSE -> {
                for (upStruct = breakInstr.getContainer();
                     upStruct.getStructType() != IRStructType.LOOP;
                     upStruct = upStruct.getParent())
                    ;
                targetHeadInstr = upStruct.getTailInstr().getNextInstr();
                instr = new JmpInstr(Opcode.JMP_IF_FALSE, targetHeadInstr);
            }
            default -> {
                for (upStruct = breakInstr.getContainer();
                     upStruct.getStructType() != IRStructType.IF;
                     upStruct = upStruct.getParent())
                    ;
                targetHeadInstr = upStruct.getTailInstr().getNextInstr();
                instr = new JmpInstr(Opcode.JMP_IF_FALSE, targetHeadInstr);
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
        Instruction targetHeadInstr = upStruct.getHeadInstr();
        return new JmpInstr(Opcode.JMP, targetHeadInstr);
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
