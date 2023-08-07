package cfg;

import java.util.*;

// Control-flow graph
public class CFG implements Iterable<BasicBlock> {
    private final HashMap<Long, BasicBlock> blockMap = new HashMap<>();
    private final List<BasicBlock> blockList = new ArrayList<>();

    /**
     * Adds a new basic block to the graph.
     *
     * @param block the basic block to be added.
     */
    public void addBasicBlock(BasicBlock block) {
        blockMap.put(block.getId(), block);
        blockList.add(block);
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

    /**
     * Gets a basic block based on its identifier.
     *
     * @param id an integer as the identifier of a basic block.
     * @return a basic block.
     */
    public BasicBlock getBasicBlock(long id) {
        return blockMap.get(id);
    }

    @Override
    public Iterator<BasicBlock> iterator() {
        return blockList.iterator();
    }

    public ListIterator<BasicBlock> listIterator() {
        return blockList.listIterator();
    }

    public void accept(ICFGVisitor cfgVisitor) {
        cfgVisitor.visitCFG(this);
    }
}
