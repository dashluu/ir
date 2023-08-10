package instructions;

public interface IInstrListIterator {
    boolean hasNext();

    Instruction next();

    void set(Instruction instr);
}
