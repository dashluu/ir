package passes;

import cfg.CFG;
import cfg.CFGEdge;
import cfg.CFGNode;
import instructions.*;
import structs.IRStruct;
import utils.IRContext;

public class CFGBuilder implements IInstrVisitor {
    private IRContext context;
    private CFG cfg;
    private CFGNode currCFGNode;

    public CFG run(IRContext context) {
        this.context = context;
        cfg = context.getCfg();
        initCFGNodes();
        initCFGEdges();
        return cfg;
    }

    private void initCFGNodes() {
        CFGNode cfgNode;
        for (BasicBlock block : context.getBlockList()) {
            cfgNode = new CFGNode(block);
            block.setCfgNode(cfgNode);
            cfg.addNode(cfgNode);
        }
    }

    private void initCFGEdges() {
        for (CFGNode cfgNode : cfg) {
            currCFGNode = cfgNode;
            for (Instruction instr : cfgNode.getBlock()) {
                instr.accept(this);
            }
        }
    }

    private void createCFGEdge(long destId) {
        CFGNode dest = cfg.getNode(destId);
        CFGEdge edge = new CFGEdge(currCFGNode, dest);
        cfg.addEdge(edge);
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
        long calleeId = callInstr.getCalleeId();
        createCFGEdge(calleeId);
        return callInstr;
    }

    @Override
    public Instruction visitJmpInstr(JmpInstr jmpInstr) {
        long targetId = jmpInstr.getTargetId();
        createCFGEdge(targetId);
        return null;
    }

    @Override
    public Instruction visitBreakInstr(BreakInstr breakInstr) {
        // No break instructions detected in this pass
        return breakInstr;
    }

    @Override
    public Instruction visitContInstr(ContInstr contInstr) {
        // No continue instructions detected in this pass
        return contInstr;
    }

    @Override
    public Instruction visitRetInstr(RetInstr retInstr) {
        return null;
    }

    @Override
    public Instruction visitPushFunInstr(PushFunInstr pushFunInstr) {
        IRStruct function = pushFunInstr.getContainer();
        long end = function.getEnd();
        Instruction instr = context.getInstrList().get((int) end);
        BasicBlock block = instr.getBlock();
        long destId = block.getId();
        createCFGEdge(destId);
        return pushFunInstr;
    }

    @Override
    public Instruction visitPopFunInstr(PopFunInstr popFunInstr) {
        return popFunInstr;
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
    public Instruction visitExitInstr(ExitInstr exitInstr) {
        return exitInstr;
    }
}
