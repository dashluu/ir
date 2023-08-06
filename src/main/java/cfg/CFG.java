package cfg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

// Control-flow graph
public class CFG implements Iterable<CFGNode> {
    private final HashMap<Long, CFGNode> nodeMap = new HashMap<>();
    private final List<CFGNode> nodeList = new ArrayList<>();

    /**
     * Adds a new node to the graph.
     *
     * @param node the node to be added.
     */
    public void addNode(CFGNode node) {
        nodeMap.put(node.getId(), node);
        nodeList.add(node);
    }

    /**
     * Adds an edge to the graph using delegation.
     *
     * @param edge the edge to be added.
     */
    public void addEdge(CFGEdge edge) {
        CFGNode src = edge.getSrc();
        CFGNode dest = edge.getDest();
        src.addSuccEdge(edge);
        dest.addPredEdge(edge);
    }

    /**
     * Gets a CFG node based on its identifier.
     *
     * @param id an integer as the identifier of a CFG node.
     * @return a CFG node.
     */
    public CFGNode getNode(long id) {
        return nodeMap.get(id);
    }

    @Override
    public Iterator<CFGNode> iterator() {
        return nodeList.iterator();
    }

    public void accept(ICFGVisitor cfgVisitor) {
        cfgVisitor.visitCFG(this);
    }
}
