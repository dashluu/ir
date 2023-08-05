package instructions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

// A vector of sequential instructions without branching(except possibly for the last instruction)
// Uses the iterator pattern
public class BasicBlock implements Iterable<Instruction> {
    private long id;
    private final List<Instruction> instrList = new ArrayList<>();

    public BasicBlock(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    @Override
    public Iterator<Instruction> iterator() {
        return instrList.iterator();
    }

    public ListIterator<Instruction> listIterator() {
        return instrList.listIterator();
    }
}
