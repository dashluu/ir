package instructions;

// Opcode for instructions
public enum Opcode {
    STORE_VAR, LOAD_VAR, LOAD_LITERAL, LOAD_DTYPE,
    JMP_OVER_FUN, JMP_OUT_LOOP, JMP_TO_LOOP, JMP_LOOP_FALSE, JMP_IF_FALSE, JMP_OUT_IF_ELSE,
}
