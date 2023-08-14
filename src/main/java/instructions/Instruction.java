package instructions;

import cfg.BasicBlock;
import structs.IRStruct;

// A class whose instances represent IR instructions
public abstract class Instruction {
    protected final InstrType instrType;
    protected final Opcode opcode;
    // Label for the instruction(used when dumping in binary)
    protected long label;
    // The basic block that stores this instruction
    protected BasicBlock block;
    protected IRStruct container;
    protected Instruction nextInstr;
    protected Instruction prevInstr;

    public Instruction(InstrType instrType, Opcode opcode) {
        this.instrType = instrType;
        this.opcode = opcode;
    }

    public InstrType getInstrType() {
        return instrType;
    }

    public Opcode getOpcode() {
        return opcode;
    }

    public long getLabel() {
        return label;
    }

    public void setLabel(long label) {
        this.label = label;
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

    public abstract void accept(IInstrVisitor instrVisitor);

    @Override
    public String toString() {
        return opcode == null ? instrType.toString() : opcode.toString();
    }
}
