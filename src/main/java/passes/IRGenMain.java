package passes;

import ast.ASTNode;
import exceptions.SyntaxErr;
import instructions.BasicBlock;
import instructions.Instruction;
import lexers.LexReader;
import parsers.module.ModuleParser;
import parsers.scope.ScopeType;
import parsers.utils.ParseContext;
import parsers.utils.ParseResult;
import parsers.scope.Scope;
import parsers.scope.ScopeStack;
import utils.IRContext;

import java.io.*;
import java.util.List;

public class IRGenMain {
    private static void dumpInstrList(Writer writer, List<Instruction> instrList) throws IOException {
        for (Instruction instr : instrList) {
            writer.write(instr.toString() + System.lineSeparator());
        }
    }

    private static void dumpBasicBlockList(Writer writer, List<BasicBlock> blockList) throws IOException {
        for (BasicBlock block : blockList) {
            writer.write(Long.toString(block.getId()) + ":" + System.lineSeparator());
            for (Instruction instr : block) {
                writer.write(instr.toString());
                writer.write(System.lineSeparator());
            }
        }
    }

    public static void main(String[] args) {
        String inFilename = args[0];
        String instrOutFilename = args[1];
        String blockOutFilename = args[2];
        Reader reader = null;
        Writer instrWriter = null;
        Writer blockWriter = null;

        try {
            reader = new BufferedReader(new FileReader(inFilename));
            instrWriter = new BufferedWriter(new FileWriter(instrOutFilename));
            blockWriter = new BufferedWriter(new FileWriter(blockOutFilename));
            LexReader lexReader = new LexReader(reader);
            ModuleParser moduleParser = new ModuleParser(lexReader);
            moduleParser.init();

            ParseContext parseContext = ParseContext.createContext();
            Scope globalScope = new Scope(ScopeType.MODULE, null);
            ScopeStack scopeStack = parseContext.getScopeStack();
            scopeStack.push(globalScope);
            ParseResult<ASTNode> moduleResult = moduleParser.parseModule(parseContext);
            scopeStack.pop();

            if (parseContext.hasErr()) {
                throw new SyntaxErr(parseContext.getErrMsg());
            }

            ASTNode moduleNode = moduleResult.getData();
            InstrBuilder instrBuilder = new InstrBuilder();
            JmpTargetResolver jmpTargetResolver = new JmpTargetResolver();
            BasicBlockBuilder basicBlockBuilder = new BasicBlockBuilder();
            IRContext irContext = IRContext.createContext();

            instrBuilder.run(moduleNode, irContext);
            List<Instruction> instrList = irContext.getInstrList();
            jmpTargetResolver.run(irContext);
            dumpInstrList(instrWriter, instrList);

            basicBlockBuilder.run(irContext);
            List<BasicBlock> blockList = irContext.getBasicBlockList();
            dumpBasicBlockList(blockWriter, blockList);
        } catch (SyntaxErr | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (instrWriter != null) {
                    instrWriter.close();
                }
                if (blockWriter != null) {
                    blockWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
