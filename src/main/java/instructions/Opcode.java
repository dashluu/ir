package instructions;

// Opcode for instructions
public enum Opcode {
    STORE_VAR, LOAD_VAR, LOAD_LITERAL, LOAD_DTYPE,
    JMP_IF_FALSE, JMP,
    PUSH_FUN, PUSH_LOOP,
    BREAK_LOOP, BREAK_LOOP_FALSE, BREAK_IF_ELSE, BREAK_IF_FALSE
}
