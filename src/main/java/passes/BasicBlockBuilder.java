package passes;

import instructions.*;
import utils.IRContext;

import java.util.List;

public class BasicBlockBuilder implements IInstrVisitor {
    private BasicBlock block;
    private List<BasicBlock> blockList;
    private long currInstr = 1;

    /**
     * Traverses a list of instructions and constructs a list of basic blocks.
     *
     * @param context the input IR context.
     */
    public void run(IRContext context) {
        blockList = context.getBlockList();
        block = new BasicBlock(currInstr);
        blockList.add(block);
        for (Instruction instr : context.getInstrList()) {
            instr.accept(this);
        }
    }

    private void createBasicBlock() {
        if (!block.isEmpty()) {
            block = new BasicBlock(currInstr);
            blockList.add(block);
        }
    }

    private void addInstrToBasicBlock(Instruction instr) {
        block.addInstr(instr);
        instr.setBlock(block);
        currInstr++;
    }

    @Override
    public Instruction visitUnOpInstr(UnOpInstr unOpInstr) {
        addInstrToBasicBlock(unOpInstr);
        return unOpInstr;
    }

    @Override
    public Instruction visitBinOpInstr(BinOpInstr binOpInstr) {
        addInstrToBasicBlock(binOpInstr);
        return binOpInstr;
    }

    @Override
    public Instruction visitCallInstr(CallInstr callInstr) {
        addInstrToBasicBlock(callInstr);
        return callInstr;
    }

    @Override
    public Instruction visitJmpInstr(JmpInstr jmpInstr) {
        addInstrToBasicBlock(jmpInstr);
        createBasicBlock();
        return jmpInstr;
    }

    @Override
    public Instruction visitBreakInstr(BreakInstr breakInstr) {
        // Break instructions will not be encountered in this pass
        addInstrToBasicBlock(breakInstr);
        return breakInstr;
    }

    @Override
    public Instruction visitContInstr(ContInstr contInstr) {
        // Continue instructions will not be encountered in this pass
        addInstrToBasicBlock(contInstr);
        return contInstr;
    }

    @Override
    public Instruction visitRetInstr(RetInstr retInstr) {
        addInstrToBasicBlock(retInstr);
        createBasicBlock();
        return retInstr;
    }

    @Override
    public Instruction visitPushFunInstr(PushFunInstr pushFunInstr) {
        createBasicBlock();
        addInstrToBasicBlock(pushFunInstr);
        return pushFunInstr;
    }

    @Override
    public Instruction visitPopFunInstr(PopFunInstr popFunInstr) {
        addInstrToBasicBlock(popFunInstr);
        createBasicBlock();
        return popFunInstr;
    }

    @Override
    public Instruction visitLoadInstr(LoadInstr loadInstr) {
        addInstrToBasicBlock(loadInstr);
        return loadInstr;
    }

    @Override
    public Instruction visitStoreInstr(StoreInstr storeInstr) {
        addInstrToBasicBlock(storeInstr);
        return storeInstr;
    }

    @Override
    public Instruction visitExitInstr(ExitInstr exitInstr) {
        addInstrToBasicBlock(exitInstr);
        return exitInstr;
    }
}
