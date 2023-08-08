package cfg;

import instructions.IInstrVisitor;

public interface ICFGVisitor extends IInstrVisitor {
    void visitCFG(CFG cfg);

    void visitBasicBlock(BasicBlock block);
}
