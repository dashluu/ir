package passes;

import cfg.BasicBlock;
import instructions.Instruction;
import utils.IRContext;

import java.io.IOException;
import java.io.Writer;

public class IRDumper {
    public static void dumpCFG(IRContext context, Writer writer) throws IOException {
        int currInstr = 0;
        StringBuilder strBuff = new StringBuilder();

        for (BasicBlock block : context.getCfg()) {
            strBuff.append("block ").append(block.getId()).append(":").append(System.lineSeparator());
            for (Instruction instr : block) {
                strBuff.append(currInstr).append(": ").append(instr).append(System.lineSeparator());
                ++currInstr;
            }
            strBuff.append(System.lineSeparator());
        }

        writer.write(strBuff.toString());
    }

    public static void dumpInstrList(IRContext context, Writer writer) throws IOException {
        int currInstr = 0;
        StringBuilder strBuff = new StringBuilder();

        for (Instruction instr : context.getInstrList()) {
            strBuff.append(currInstr).append(": ").append(instr).append(System.lineSeparator());
            ++currInstr;
        }

        writer.write(strBuff.toString());
    }
}
