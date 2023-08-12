package cfg;

import instructions.Instruction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// A vector of sequential instructions without branching(except possibly for the last instruction)
public class BasicBlock implements Iterable<Instruction> {
    private class InstrIterator implements Iterator<Instruction> {
        protected Instruction currInstr;
        protected Instruction nextInstr = headInstr;

        @Override
        public boolean hasNext() {
            return nextInstr != null && nextInstr != tailInstr.getNextInstr();
        }

        @Override
        public Instruction next() {
            currInstr = nextInstr;
            nextInstr = nextInstr.getNextInstr();
            return currInstr;
        }
    }

    private final long id;
    // Previous block in the sequence != predecessor
    private BasicBlock prevBlock;
    // Next block in the sequence != successor
    private BasicBlock nextBlock;
    private Instruction headInstr;
    private Instruction tailInstr;
    // A list of successor edges(or outgoing edges)
    private final List<CFGEdge> succEdgeList = new ArrayList<>();
    // A list of predecessor edges(or ingoing edges)
    private final List<CFGEdge> predEdgeList = new ArrayList<>();

    public BasicBlock(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public BasicBlock getNextBasicBlock() {
        return nextBlock;
    }

    public void setNextBasicBlock(BasicBlock nextBlock) {
        this.nextBlock = nextBlock;
    }

    public BasicBlock getPrevBlock() {
        return prevBlock;
    }

    public void setPrevBlock(BasicBlock prevBlock) {
        this.prevBlock = prevBlock;
    }

    public Instruction getHeadInstr() {
        return headInstr;
    }

    public void setHeadInstr(Instruction headInstr) {
        this.headInstr = headInstr;
    }

    public Instruction getTailInstr() {
        return tailInstr;
    }

    public void setTailInstr(Instruction tailInstr) {
        this.tailInstr = tailInstr;
    }

    /**
     * Adds a new instruction to the block.
     *
     * @param instr the instruction to be added.
     */
    public void addInstr(Instruction instr) {
        instr.setBlock(this);
        if (headInstr == null) {
            headInstr = tailInstr = instr;
        } else {
            tailInstr = instr;
        }
    }

    /**
     * Checks if the basic block is empty.
     *
     * @return true if it is empty and false otherwise.
     */
    public boolean isEmpty() {
        return headInstr == null;
    }

    /**
     * Adds a new edge to the successor edge list.
     * Caution: this method should only be used by the CFG class and not called elsewhere.
     *
     * @param edge the successor edge to be added.
     */
    void addSuccEdge(CFGEdge edge) {
        succEdgeList.add(edge);
    }

    /**
     * Adds a new edge to the predecessor edge list.
     * Caution: this method should only be used by the CFG class and not called elsewhere.
     *
     * @param edge the predecessor edge to be added.
     */
    void addPredEdge(CFGEdge edge) {
        predEdgeList.add(edge);
    }

    @Override
    public Iterator<Instruction> iterator() {
        return new InstrIterator();
    }

    public Iterator<CFGEdge> succEdgeListIterator() {
        return succEdgeList.iterator();
    }

    public Iterator<CFGEdge> predEdgeListIterator() {
        return predEdgeList.iterator();
    }

    public void accept(ICFGVisitor cfgVisitor) {
        cfgVisitor.visitBasicBlock(this);
    }
}
