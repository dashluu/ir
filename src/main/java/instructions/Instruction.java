package instructions;

import cfg.BasicBlock;
import structs.IRStruct;

// A class whose instances represent IR instructions
public abstract class Instruction {
    protected final long id;
    protected final InstrType instrType;
    protected final Opcode opcode;
    // The basic block that stores this instruction
    protected BasicBlock block;
    protected IRStruct container;
    protected Instruction nextInstr;
    protected Instruction prevInstr;

    public Instruction(long id, InstrType instrType, Opcode opcode) {
        this.id = id;
        this.instrType = instrType;
        this.opcode = opcode;
    }

    public long getId() {
        return id;
    }

    public InstrType getInstrType() {
        return instrType;
    }

    public Opcode getOpcode() {
        return opcode;
    }

    public BasicBlock getBlock() {
        return block;
    }

    public void setBlock(BasicBlock block) {
        this.block = block;
    }

    public IRStruct getContainer() {
        return container;
    }

    public void setContainer(IRStruct container) {
        this.container = container;
    }

    public Instruction getNextInstr() {
        return nextInstr;
    }

    public void setNextInstr(Instruction nextInstr) {
        this.nextInstr = nextInstr;
    }

    public Instruction getPrevInstr() {
        return prevInstr;
    }

    public void setPrevInstr(Instruction prevInstr) {
        this.prevInstr = prevInstr;
    }

    /**
     * Replaces the given instruction with the caller instruction.
     *
     * @param instr the instruction to be replaced.
     */
    public void replaceInstr(Instruction instr) {
        // Update the instruction's basic block
        block = instr.getBlock();
        if (instr == block.getHeadInstr()) {
            block.setHeadInstr(this);
        }
        if (instr == block.getTailInstr()) {
            block.setTailInstr(this);
        }

        // Update the instruction's container
        container = instr.getContainer();
        if (instr == container.getHeadInstr()) {
            container.setHeadInstr(instr);
        }
        if (instr == container.getTailInstr()) {
            container.setTailInstr(instr);
        }

        // Update the previous and next instruction
        prevInstr = instr.getPrevInstr();
        nextInstr = instr.getNextInstr();
        if (prevInstr != null) {
            prevInstr.setNextInstr(this);
        }
        if (nextInstr != null) {
            nextInstr.setPrevInstr(this);
        }
    }

    public abstract void accept(IInstrVisitor instrVisitor);

    @Override
    public String toString() {
        return opcode == null ? instrType.toString() : opcode.toString();
    }
}
