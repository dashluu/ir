package cfg;

import instructions.Instruction;

import java.io.IOException;
import java.io.Writer;
import java.util.*;

// Control-flow graph
public class CFG implements Iterable<BasicBlock> {
    private class BasicBlockIterator implements Iterator<BasicBlock> {
        private BasicBlock nextBlock = headBlock;

        @Override
        public boolean hasNext() {
            return nextBlock != null;
        }

        @Override
        public BasicBlock next() {
            BasicBlock currBlock = nextBlock;
            nextBlock = nextBlock.getNextBasicBlock();
            return currBlock;
        }
    }

    private BasicBlock headBlock;
    private BasicBlock tailBlock;

    public BasicBlock getHeadBlock() {
        return headBlock;
    }

    public void setHeadBlock(BasicBlock headBlock) {
        this.headBlock = headBlock;
    }

    public BasicBlock getTailBlock() {
        return tailBlock;
    }

    public void setTailBlock(BasicBlock tailBlock) {
        this.tailBlock = tailBlock;
    }

    /**
     * Adds a new basic block to the graph.
     *
     * @param block the basic block to be added.
     */
    public void addBasicBlock(BasicBlock block) {
        if (headBlock == null) {
            headBlock = tailBlock = block;
        } else {
            tailBlock.setNextBasicBlock(block);
            block.setPrevBasicBlock(tailBlock);
            tailBlock = block;
        }
    }

    /**
     * Adds an edge to the graph using delegation.
     *
     * @param edge the edge to be added.
     */
    public void addEdge(CFGEdge edge) {
        BasicBlock src = edge.getSrc();
        BasicBlock dest = edge.getDest();
        src.addSuccEdge(edge);
        dest.addPredEdge(edge);
    }

    @Override
    public Iterator<BasicBlock> iterator() {
        return new BasicBlockIterator();
    }

    public void accept(ICFGVisitor cfgVisitor) {
        cfgVisitor.visitCFG(this);
    }

    /**
     * Outputs the CFG to a destination.
     *
     * @param writer the Writer object for outputting the CFG.
     * @throws IOException if there is an IO exception.
     */
    public void out(Writer writer) throws IOException {
        StringBuilder strBuff = new StringBuilder();

        for (BasicBlock block : this) {
            strBuff.append("block ").append(block.getId()).append(":").append(System.lineSeparator());
            for (Instruction instr : block) {
                strBuff.append(instr.getLabel()).append(": ").append(instr).append(System.lineSeparator());
            }
            strBuff.append(System.lineSeparator());
        }

        writer.write(strBuff.toString());
    }
}
