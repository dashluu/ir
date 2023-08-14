package passes;

import cfg.BasicBlock;
import cfg.CFG;
import cfg.CFGEdge;
import instructions.*;
import structs.IRStructType;
import utils.IRContext;

import java.util.ArrayDeque;
import java.util.HashSet;

public class CFGBuilder {
    private CFG cfg;
    private HashSet<BasicBlock> reachableBlockSet;

    /**
     * Builds edges for the CFG and processes unreachable basic blocks.
     *
     * @param context the input IR context.
     */
    public void run(IRContext context) {
        cfg = context.getCfg();
        reachableBlockSet = new HashSet<>();
        buildEdges();

        for (BasicBlock reachableBlock : reachableBlockSet) {
            System.out.println(reachableBlock.getId());
        }
    }

    /**
     * Builds edges between basic blocks in the CFG and records reachable basic blocks.
     */
    private void buildEdges() {
        // Store the instruction to be executed
        ArrayDeque<Instruction> instrStack = new ArrayDeque<>();
        // Store the instruction to resume after function call
        ArrayDeque<Instruction> funInstrStack = new ArrayDeque<>();
        Opcode jmpOpcode;
        JmpInstr jmpInstr;
        CallInstr callInstr;
        Instruction nextInstr, targetInstr;
        BasicBlock srcBlock, destBlock;
        Instruction instr = cfg.getHeadBlock().getHeadInstr();
        instrStack.push(instr);

        while (!instrStack.isEmpty()) {
            instr = instrStack.pop();
            srcBlock = instr.getBlock();
            nextInstr = instr.getNextInstr();

            if (instr.getInstrType() != InstrType.JMP &&
                    instr.getInstrType() != InstrType.CALL &&
                    instr.getInstrType() != InstrType.RET) {
                // Exit instruction's next instruction is null so check first before proceeding
                if (nextInstr != null) {
                    destBlock = nextInstr.getBlock();
                    instrStack.push(nextInstr);
                    reachableBlockSet.add(destBlock);
                }
            } else if (instr.getInstrType() == InstrType.JMP) {
                jmpInstr = (JmpInstr) instr;
                targetInstr = jmpInstr.getTargetInstr();
                jmpOpcode = jmpInstr.getOpcode();
                destBlock = targetInstr.getBlock();
                cfg.addEdge(new CFGEdge(srcBlock, destBlock));
                if (!reachableBlockSet.contains(destBlock)) {
                    instrStack.push(targetInstr);
                    reachableBlockSet.add(destBlock);
                }

                if (jmpOpcode == Opcode.JMP_IF_FALSE || jmpOpcode == Opcode.JMP_LOOP_FALSE) {
                    destBlock = nextInstr.getBlock();
                    cfg.addEdge(new CFGEdge(srcBlock, destBlock));
                    instrStack.push(nextInstr);
                    reachableBlockSet.add(destBlock);
                }
            } else if (instr.getInstrType() == InstrType.CALL) {
                callInstr = (CallInstr) instr;
                targetInstr = callInstr.getCalleeInstr();
                destBlock = targetInstr.getBlock();
                cfg.addEdge(new CFGEdge(srcBlock, destBlock));
                if (!reachableBlockSet.contains(destBlock)) {
                    instrStack.push(targetInstr);
                    reachableBlockSet.add(destBlock);
                }

                destBlock = nextInstr.getBlock();
                cfg.addEdge(new CFGEdge(srcBlock, destBlock));
                instrStack.push(nextInstr);
                reachableBlockSet.add(destBlock);

                // Push the instruction to be resumed on the stack
                funInstrStack.push(nextInstr);
            } else {
                // The current instruction is a return instruction
                if (instr.getContainer().getStructType() == IRStructType.FUNCTION) {
                    targetInstr = funInstrStack.pop();
                } else {
                    targetInstr = funInstrStack.peek();
                }

                // We don't have to push the resume instruction on the instruction stack
                // It was pushed when we did the function call
                assert targetInstr != null;
                destBlock = targetInstr.getBlock();
                cfg.addEdge(new CFGEdge(srcBlock, destBlock));
            }
        }
    }
}
