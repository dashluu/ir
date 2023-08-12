package cfg;

import instructions.IInstrVisitor;
import instructions.Instruction;

public interface ICFGVisitor extends IInstrVisitor {
    default void visitCFG(CFG cfg) {
        for (BasicBlock block : cfg) {
            block.accept(this);
        }
    }

    default void visitBasicBlock(BasicBlock block) {
        for (Instruction instr : block) {
            instr.accept(this);
        }
    }
}
