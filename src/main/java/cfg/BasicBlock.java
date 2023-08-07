package cfg;

import instructions.Instruction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

// A vector of sequential instructions without branching(except possibly for the last instruction)
// Uses the iterator pattern
public class BasicBlock implements Iterable<Instruction> {
    private final long id;
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

    /**
     * Adds a new instruction to the block.
     *
     * @param instr the instruction to be added.
     */
    public void addInstr(Instruction instr) {
        instrList.add(instr);
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

    /**
     * Removes an edge from an edge list(either the successor or predecessor edge list).
     *
     * @param edge     the edge to be removed.
     * @param edgeList the edge list that used to contain the removed edge.
     */
    private void removeEdge(CFGEdge edge, List<CFGEdge> edgeList) {
        int i;
        long destId = edge.getDest().getId();
        for (i = 0; i < edgeList.size() && edgeList.get(i).getDest().getId() != destId; ++i) ;
        if (i < edgeList.size()) {
            edgeList.remove(i);
        }
    }

    /**
     * Removes an edge from the successor edge list.
     * Caution: this method should only be used by the CFG class and not called elsewhere.
     *
     * @param succEdge the successor edge to be removed.
     */
    void removeSuccEdge(CFGEdge succEdge) {
        removeEdge(succEdge, succEdgeList);
    }

    /**
     * Removes an edge from the predecessor edge list.
     * Caution: this method should only be used by the CFG class and not called elsewhere.
     *
     * @param predEdge the predecessor edge to be removed.
     */
    void removePredEdge(CFGEdge predEdge) {
        removeEdge(predEdge, predEdgeList);
    }

    @Override
    public Iterator<Instruction> iterator() {
        return instrList.iterator();
    }

    public ListIterator<Instruction> listIterator() {
        return instrList.listIterator();
    }

    public BasicBlock accept(ICFGVisitor cfgVisitor) {
        return cfgVisitor.visitBasicBlock(this);
    }
}
