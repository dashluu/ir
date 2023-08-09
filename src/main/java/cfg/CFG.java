package cfg;

import instructions.Instruction;

import java.io.IOException;
import java.io.Writer;
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

    /**
     * Outputs the CFG to a destination.
     *
     * @param writer the Writer object for outputting the CFG.
     * @throws IOException if there is an IO exception.
     */
    public void out(Writer writer) throws IOException {
        StringBuilder strBuff = new StringBuilder();

        for (BasicBlock block : blockList) {
            strBuff.append("block ").append(block.getId()).append(":").append(System.lineSeparator());
            for (Instruction instr : block) {
                strBuff.append(instr.getId()).append(": ").append(instr).append(System.lineSeparator());
            }
            strBuff.append(System.lineSeparator());
        }

        writer.write(strBuff.toString());
    }
}
