package instructions;

public interface IInstrIterator {
    boolean hasNext();

    Instruction next();

    void set(Instruction instr);
}
