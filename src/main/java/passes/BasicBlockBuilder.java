package passes;

import instructions.*;

import java.util.ArrayList;
import java.util.List;

public class BasicBlockBuilder implements IInstrVisitor {
    private BasicBlock block;
    private List<BasicBlock> blockList;
    private long currInstr = 1;

    /**
     * Traverses a list of instructions and constructs a list of basic blocks.
     *
     * @param instrList the list of instructions.
     * @return the constructed list of basic blocks.
     */
    public List<BasicBlock> run(List<Instruction> instrList) {
        blockList = new ArrayList<>();
        block = new BasicBlock(currInstr);
        blockList.add(block);
        for (Instruction instr : instrList) {
            instr.accept(this);
        }
        return blockList;
    }

    private void createBasicBlock() {
        if (!block.isEmpty()) {
            block = new BasicBlock(currInstr);
            blockList.add(block);
        }
    }

    private void addInstr(Instruction instr) {
        block.addInstr(instr);
        currInstr++;
    }

    @Override
    public Instruction visitUnOpInstr(UnOpInstr unOpInstr) {
        addInstr(unOpInstr);
        return unOpInstr;
    }

    @Override
    public Instruction visitBinOpInstr(BinOpInstr binOpInstr) {
        addInstr(binOpInstr);
        return binOpInstr;
    }

    @Override
    public Instruction visitCallInstr(CallInstr callInstr) {
        addInstr(callInstr);
        return callInstr;
    }

    @Override
    public Instruction visitJmpInstr(JmpInstr jmpInstr) {
        addInstr(jmpInstr);
        createBasicBlock();
        return jmpInstr;
    }

    @Override
    public Instruction visitBreakInstr(BreakInstr breakInstr) {
        // Break instructions will not be encountered in this pass
        addInstr(breakInstr);
        return breakInstr;
    }

    @Override
    public Instruction visitContInstr(ContInstr contInstr) {
        // Continue instructions will not be encountered in this pass
        addInstr(contInstr);
        return contInstr;
    }

    @Override
    public Instruction visitRetInstr(RetInstr retInstr) {
        addInstr(retInstr);
        createBasicBlock();
        return retInstr;
    }

    @Override
    public Instruction visitPushFunInstr(PushFunInstr pushFunInstr) {
        createBasicBlock();
        addInstr(pushFunInstr);
        return pushFunInstr;
    }

    @Override
    public Instruction visitPopFunInstr(PopFunInstr popFunInstr) {
        addInstr(popFunInstr);
        createBasicBlock();
        return popFunInstr;
    }

    @Override
    public Instruction visitLoadInstr(LoadInstr loadInstr) {
        addInstr(loadInstr);
        return loadInstr;
    }

    @Override
    public Instruction visitStoreInstr(StoreInstr storeInstr) {
        addInstr(storeInstr);
        return storeInstr;
    }

    @Override
    public Instruction visitExitInstr(ExitInstr exitInstr) {
        addInstr(exitInstr);
        return exitInstr;
    }
}
