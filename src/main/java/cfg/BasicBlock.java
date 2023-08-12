package cfg;

import instructions.IInstrListIterator;
import instructions.Instruction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// A vector of sequential instructions without branching(except possibly for the last instruction)
public class BasicBlock implements Iterable<Instruction> {
    private class InstrListIterator implements IInstrListIterator {
        private int i = 0;

        @Override
        public boolean hasNext() {
            return i < instrList.size();
        }

        @Override
        public Instruction next() {
            return instrList.get(i++);
        }

        @Override
        public void set(Instruction instr) {
            // Update the current instruction
            Instruction currInstr = instrList.get(i - 1);
            instrList.set(i - 1, instr);
            // Update the previous and next instruction
            Instruction prevInstr = currInstr.getPrevInstr();
            Instruction nextInstr = currInstr.getNextInstr();
            instr.setPrevInstr(prevInstr);
            instr.setNextInstr(nextInstr);
            if (prevInstr != null) {
                prevInstr.setNextInstr(instr);
            }
            if (nextInstr != null) {
                nextInstr.setPrevInstr(instr);
            }
        }
    }

    private final long id;
    // Previous block in the sequence != predecessor
    private BasicBlock prevBlock;
    // Next block in the sequence != successor
    private BasicBlock nextBlock;
    private final List<Instruction> instrList = new ArrayList<>();
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

    /**
     * Adds a new instruction to the block.
     *
     * @param instr the instruction to be added.
     */
    public void addInstr(Instruction instr) {
        instrList.add(instr);
        instr.setBlock(this);
    }

    /**
     * Checks if the basic block is empty.
     *
     * @return true if it is empty and false otherwise.
     */
    public boolean isEmpty() {
        return instrList.isEmpty();
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
        return instrList.iterator();
    }

    public IInstrListIterator instrListIterator() {
        return new InstrListIterator();
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
