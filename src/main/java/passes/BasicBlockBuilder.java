package passes;

import cfg.BasicBlock;
import cfg.CFG;
import instructions.*;
import utils.IRContext;

public class BasicBlockBuilder implements IInstrVisitor {
    private BasicBlock block;
    private CFG cfg;
    private long currBlock = 0;

    /**
     * Traverses a list of instructions and constructs a list of basic blocks for the given context's CFG.
     *
     * @param context the input IR context.
     */
    public void run(IRContext context) {
        cfg = context.getCfg();
        block = new BasicBlock(currBlock++);
        cfg.addBasicBlock(block);
        for (Instruction instr : context.getInstrList()) {
            instr.accept(this);
        }
    }

    private void createBlock() {
        if (!block.isEmpty()) {
            block = new BasicBlock(currBlock++);
            cfg.addBasicBlock(block);
        }
    }

    private void addInstrToBlock(Instruction instr) {
        block.addInstr(instr);
        instr.setBlock(block);
    }

    @Override
    public Instruction visitUnOpInstr(UnOpInstr unOpInstr) {
        addInstrToBlock(unOpInstr);
        return unOpInstr;
    }

    @Override
    public Instruction visitBinOpInstr(BinOpInstr binOpInstr) {
        addInstrToBlock(binOpInstr);
        return binOpInstr;
    }

    @Override
    public Instruction visitCallInstr(CallInstr callInstr) {
        addInstrToBlock(callInstr);
        return callInstr;
    }

    @Override
    public Instruction visitJmpInstr(JmpInstr jmpInstr) {
        addInstrToBlock(jmpInstr);
        createBlock();
        return jmpInstr;
    }

    @Override
    public Instruction visitBreakInstr(BreakInstr breakInstr) {
        // Break instructions will not be encountered in this pass
        addInstrToBlock(breakInstr);
        return breakInstr;
    }

    @Override
    public Instruction visitContInstr(ContInstr contInstr) {
        // Continue instructions will not be encountered in this pass
        addInstrToBlock(contInstr);
        return contInstr;
    }

    @Override
    public Instruction visitRetInstr(RetInstr retInstr) {
        addInstrToBlock(retInstr);
        createBlock();
        return retInstr;
    }

    @Override
    public Instruction visitPushFunInstr(PushFunInstr pushFunInstr) {
        createBlock();
        addInstrToBlock(pushFunInstr);
        return pushFunInstr;
    }

    @Override
    public Instruction visitLoadInstr(LoadInstr loadInstr) {
        addInstrToBlock(loadInstr);
        return loadInstr;
    }

    @Override
    public Instruction visitStoreInstr(StoreInstr storeInstr) {
        addInstrToBlock(storeInstr);
        return storeInstr;
    }

    @Override
    public Instruction visitExitInstr(ExitInstr exitInstr) {
        addInstrToBlock(exitInstr);
        return exitInstr;
    }
}
