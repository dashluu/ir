package cfg;

import instructions.BasicBlock;

import java.util.ArrayList;
import java.util.List;

public class CFGNode {
    private final long id;
    private final BasicBlock block;
    // A list of successor edges(or outgoing edges)
    private final List<CFGEdge> succEdges = new ArrayList<>();
    // A list of predecessor edges(or ingoing edges)
    private final List<CFGEdge> predEdges = new ArrayList<>();

    public CFGNode(BasicBlock block) {
        id = block.getId();
        this.block = block;
    }

    public long getId() {
        return id;
    }

    public BasicBlock getBlock() {
        return block;
    }

    /**
     * Adds a new edge to the successor edge list.
     * Caution: this method should only be used by the CFG class and not called elsewhere.
     *
     * @param edge the successor edge to be added.
     */
    void addSuccEdge(CFGEdge edge) {
        succEdges.add(edge);
    }

    /**
     * Adds a new edge to the predecessor edge list.
     * Caution: this method should only be used by the CFG class and not called elsewhere.
     *
     * @param edge the predecessor edge to be added.
     */
    void addPredEdge(CFGEdge edge) {
        predEdges.add(edge);
    }

    public void accept(ICFGVisitor cfgVisitor) {
        cfgVisitor.visitCFGNode(this);
    }
}
