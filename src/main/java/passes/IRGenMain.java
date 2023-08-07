package passes;

import ast.ASTNode;
import exceptions.SyntaxErr;
import lexers.LexReader;
import parsers.module.ModuleParser;
import parsers.scope.ScopeType;
import parsers.utils.ParseContext;
import parsers.utils.ParseResult;
import parsers.scope.Scope;
import parsers.scope.ScopeStack;
import utils.IRContext;

import java.io.*;

public class IRGenMain {
    public static void main(String[] args) {
        String inFilename = args[0];
        String instrOutFilename = args[1];
        String cfgOutFilename = args[2];
        Reader reader = null;
        Writer instrWriter = null;
        Writer cfgWriter = null;

        try {
            reader = new BufferedReader(new FileReader(inFilename));
            instrWriter = new BufferedWriter(new FileWriter(instrOutFilename));
            cfgWriter = new BufferedWriter(new FileWriter(cfgOutFilename));
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
            BasicBlockBuilder blockBuilder = new BasicBlockBuilder();
            JmpTargetResolver jmpTargetResolver = new JmpTargetResolver();
            IRContext irContext = IRContext.createContext();

            blockBuilder.run(moduleNode, irContext);
            jmpTargetResolver.run(irContext);
            IRDumper.dumpCFG(irContext, cfgWriter);
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
                if (cfgWriter != null) {
                    cfgWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
