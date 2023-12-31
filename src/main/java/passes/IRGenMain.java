package passes;

import ast.ASTNode;
import cfg.CFG;
import exceptions.SyntaxErr;
import lex.LexReader;
import parse.module.ModuleParser;
import parse.scope.ScopeType;
import parse.utils.ParseContext;
import parse.utils.ParseResult;
import parse.scope.Scope;
import parse.scope.ScopeStack;
import utils.IRContext;

import java.io.*;

public class IRGenMain {
    public static void main(String[] args) {
        String inFilename = args[0];
        String cfgOutFilename = args[1];

        try (Reader reader = new BufferedReader(new FileReader(inFilename));
             Writer cfgWriter = new BufferedWriter(new FileWriter(cfgOutFilename))) {
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
            CFG cfg = irContext.getCfg();
            cfg.out(cfgWriter);
        } catch (SyntaxErr | IOException e) {
            e.printStackTrace();
        }
    }
}
