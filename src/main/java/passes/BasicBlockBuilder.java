package passes;

import ast.*;
import cfg.BasicBlock;
import cfg.CFG;
import refs.*;
import instructions.*;
import structs.IRFunction;
import structs.IRStruct;
import structs.IRStructType;
import toks.Tok;
import toks.TokType;
import types.TypeInfo;
import utils.IRContext;

import java.util.ArrayDeque;

public class BasicBlockBuilder extends ASTPass {
    private final ArrayDeque<SymRefTable> symRefTableStack = new ArrayDeque<>();
    private long nextSymRefVal = 0, nextLitRefVal = 0;
    private final ArrayDeque<IRStruct> structStack = new ArrayDeque<>();
    private BasicBlock block;
    private CFG cfg;
    private long blockId = 0;
    private long instrId = 0;
    private Instruction headInstr;
    private Instruction tailInstr;

    /**
     * Traverses an AST and constructs basic blocks for the CFG in the given context.
     *
     * @param root    the input AST root.
     * @param context the input IR context.
     */
    public void run(ASTNode root, IRContext context) {
        this.context = context;
        cfg = context.getCfg();
        // Set up the first block
        block = new BasicBlock(blockId++);
        cfg.addBasicBlock(block);
        // Set up the first identifier reference table
        SymRefTable symRefTable = new SymRefTable(null);
        symRefTableStack.push(symRefTable);
        // Enter module
        IRStruct module = new IRStruct(IRStructType.MODULE, null);
        structStack.push(module);
        // Traverse and process the AST
        root.accept(this);
        // Insert an exit instruction at the end
        Instruction instr = new ExitInstr(instrId++);
        updateAndAddInstr(instr);
        // Exit module
        structStack.pop();
        // Pop everything off the stack
        symRefTableStack.pop();
    }

    private void createBlock() {
        if (!block.isEmpty()) {
            block = new BasicBlock(blockId++);
            cfg.addBasicBlock(block);
        }
    }

    private void updateAndAddInstr(Instruction instr) {
        // Link the current block to the instruction
        block.addInstr(instr);
        instr.setBlock(block);

        // Update the head and tail instruction of the current structure hierarchy
        IRStruct upStruct = structStack.peek();
        instr.setContainer(upStruct);
        while (upStruct != null) {
            if (upStruct.getHeadInstr() == null) {
                upStruct.setHeadInstr(instr);
            } else {
                upStruct.setTailInstr(instr);
            }
            upStruct = upStruct.getParent();
        }

        // Update the linked list of instructions
        if (headInstr == null) {
            headInstr = tailInstr = instr;
        } else {
            tailInstr.setNextInstr(instr);
            instr.setPrevInstr(tailInstr);
            tailInstr = instr;
        }
    }

    private void storeVar(ASTNode idNode) {
        String destName = idNode.getTok().getVal();
        SymRefTable symRefTable = symRefTableStack.peek();
        assert symRefTable != null;
        VarRef destRef = (VarRef) symRefTable.getClosureSymRef(destName);
        Instruction instr = new StoreInstr(instrId++, Opcode.STORE_VAR, destRef);
        updateAndAddInstr(instr);
    }

    @Override
    public ASTNode visitId(ASTNode node) {
        String src = node.getTok().getVal();
        SymRefTable symRefTable = symRefTableStack.peek();
        assert symRefTable != null;
        VarRef srcRef = (VarRef) symRefTable.getClosureSymRef(src);
        Instruction instr = new LoadInstr(instrId++, Opcode.LOAD_VAR, srcRef);
        updateAndAddInstr(instr);
        return node;
    }

    @Override
    public ASTNode visitVarDef(ASTNode node) {
        VarDefASTNode varDefNode = (VarDefASTNode) super.visitVarDef(node);
        IdASTNode idNode = varDefNode.getVarDeclNode().getIdNode();
        storeVar(idNode);
        return varDefNode;
    }

    @Override
    public ASTNode visitParamDecl(ASTNode node) {
        ParamDeclASTNode paramDeclNode = (ParamDeclASTNode) node;
        IdASTNode idNode = paramDeclNode.getIdNode();
        String id = idNode.getTok().getVal();
        TypeInfo dtype = idNode.getDtype();
        SymRefTable symRefTable = symRefTableStack.peek();
        assert symRefTable != null;
        VarRef varRef = new VarRef(id, dtype, nextSymRefVal++);
        symRefTable.registerSymRef(varRef);
        return paramDeclNode;
    }

    @Override
    public ASTNode visitVarDecl(ASTNode node) {
        VarDeclASTNode varDeclNode = (VarDeclASTNode) node;
        IdASTNode idNode = varDeclNode.getIdNode();
        String id = idNode.getTok().getVal();
        TypeInfo dtype = idNode.getDtype();
        SymRefTable symRefTable = symRefTableStack.peek();
        assert symRefTable != null;
        VarRef varRef = new VarRef(id, dtype, nextSymRefVal++);
        symRefTable.registerSymRef(varRef);
        return varDeclNode;
    }

    @Override
    public ASTNode visitLiteral(ASTNode node) {
        String litVal = node.getTok().getVal();
        TypeInfo dtype = node.getDtype();
        LiteralRefTable litRefTable = context.getLiteralRefTable();
        LiteralRef litRef = litRefTable.getLiteralRef(litVal);
        if (litRef == null) {
            // Create a new literal reference
            litRef = new LiteralRef(litVal, dtype, nextLitRefVal++);
            litRefTable.registerLiteralRef(litRef);
        }

        Instruction instr = new LoadInstr(instrId++, Opcode.LOAD_LITERAL, litRef);
        updateAndAddInstr(instr);
        return node;
    }

    @Override
    public ASTNode visitSimpleDtype(ASTNode node) {
        String src = node.getTok().getVal();
        TypeRef srcRef = context.getTypeRefTable().getTypeRef(src);
        Instruction instr = new LoadInstr(instrId++, Opcode.LOAD_DTYPE, srcRef);
        updateAndAddInstr(instr);
        return node;
    }

    @Override
    public ASTNode visitUnOp(ASTNode node) {
        ASTNode unOpNode = super.visitUnOp(node);
        Tok opTok = unOpNode.getTok();
        String opName = opTok.getVal();
        long opRef = opTok.getTokType().ordinal();
        Instruction instr = new UnOpInstr(instrId++, opName, opRef);
        updateAndAddInstr(instr);
        return unOpNode;
    }

    @Override
    public ASTNode visitBinOp(ASTNode node) {
        BinOpASTNode binOpNode = (BinOpASTNode) node;
        ASTNode left = binOpNode.getLeft();
        Tok opTok = binOpNode.getTok();
        TokType opId = opTok.getTokType();

        if (opId == TokType.ASSIGNMENT && left.getNodeType() == ASTNodeType.ID) {
            ASTNode right = binOpNode.getRight().accept(this);
            binOpNode.setRight(right);
            storeVar(left);
        } else {
            binOpNode = (BinOpASTNode) super.visitBinOp(binOpNode);
            String opName = opTok.getVal();
            long opRef = opId.ordinal();
            Instruction instr = new BinOpInstr(instrId++, opName, opRef);
            updateAndAddInstr(instr);
        }

        return binOpNode;
    }

    @Override
    public ASTNode visitFunCall(ASTNode node) {
        FunCallASTNode funCallNode = (FunCallASTNode) node;
        ExprListASTNode argListNode = (ExprListASTNode) funCallNode.getArgListNode().accept(this);
        funCallNode.setArgListNode(argListNode);
        IdASTNode idNode = funCallNode.getIdNode();
        String calleeName = idNode.getTok().getVal();
        SymRefTable symRefTable = symRefTableStack.peek();
        assert symRefTable != null;
        FunRef funRef = (FunRef) symRefTable.getClosureSymRef(calleeName);
        Instruction instr = new CallInstr(instrId++, calleeName, funRef.getFunction().getHeadInstr());
        updateAndAddInstr(instr);
        return funCallNode;
    }

    @Override
    public ASTNode visitFunDef(ASTNode node) {
        // Enter function
        IRStruct parent = structStack.peek();
        FunDefASTNode funDefNode = (FunDefASTNode) node;
        TypeInfo retDtype = funDefNode.getDtype();
        IRFunction function = new IRFunction(retDtype, parent);
        structStack.push(function);
        // Create a new basic block
        createBlock();

        // Register function in the identifier reference table
        IdASTNode idNode = funDefNode.getIdNode();
        String funId = idNode.getTok().getVal();
        SymRefTable topSymRefTable = symRefTableStack.peek();
        assert topSymRefTable != null;
        FunRef funRef = new FunRef(funId, function, nextSymRefVal++);
        topSymRefTable.registerSymRef(funRef);
        SymRefTable newSymRefTable = new SymRefTable(topSymRefTable);
        // Visit the function signature and body
        symRefTableStack.push(newSymRefTable);
        FunSignASTNode funSignNode = (FunSignASTNode) funDefNode.getSignNode().accept(this);
        ScopeASTNode bodyNode = (ScopeASTNode) funDefNode.getBodyNode().accept(this);
        funDefNode.setSignNode(funSignNode);
        funDefNode.setBodyNode(bodyNode);
        symRefTableStack.pop();

        // Create a new basic block
        createBlock();
        // Exit function
        structStack.pop();
        return node;
    }

    @Override
    public ASTNode visitFunSign(ASTNode node) {
        // Only visit the parameter list without visiting the return data type node
        FunSignASTNode funSignASTNode = (FunSignASTNode) node;
        ParamListASTNode paramListNode = (ParamListASTNode) funSignASTNode.getParamListNode().accept(this);
        funSignASTNode.setParamListNode(paramListNode);
        return node;
    }

    @Override
    public ASTNode visitRet(ASTNode node) {
        ASTNode retNode = super.visitRet(node);
        Instruction instr = new RetInstr(instrId++);
        updateAndAddInstr(instr);
        createBlock();
        return retNode;
    }

    @Override
    public ASTNode visitBreak(ASTNode node) {
        Instruction instr = new BreakInstr(instrId++, Opcode.BREAK_LOOP);
        updateAndAddInstr(instr);
        createBlock();
        return node;
    }

    @Override
    public ASTNode visitCont(ASTNode node) {
        Instruction instr = new ContInstr(instrId++);
        updateAndAddInstr(instr);
        createBlock();
        return node;
    }

    @Override
    public ASTNode visitScope(ASTNode node) {
        SymRefTable topSymRefTable = symRefTableStack.peek();
        SymRefTable newSymRefTable = new SymRefTable(topSymRefTable);
        symRefTableStack.push(newSymRefTable);
        ASTNode scopeNode = super.visitScope(node);
        symRefTableStack.pop();
        return scopeNode;
    }

    @Override
    public ASTNode visitIfElse(ASTNode node) {
        // Enter if-else
        IRStruct parent = structStack.peek();
        IRStruct ifElseStmt = new IRStruct(IRStructType.IF_ELSE, parent);
        structStack.push(ifElseStmt);
        // Create a new basic block
        createBlock();

        node = super.visitIfElse(node);

        // Create a new basic block
        createBlock();
        // Exit if-else
        structStack.pop();
        return node;
    }

    @Override
    public ASTNode visitIf(ASTNode node) {
        // Enter if-block
        IRStruct parent = structStack.peek();
        IRStruct ifStmt = new IRStruct(IRStructType.IF, parent);
        structStack.push(ifStmt);
        // Create a new basic block
        createBlock();

        // Visit the condition node
        IfASTNode ifNode = (IfASTNode) node;
        ASTNode condNode = ifNode.getCondNode().accept(this);
        ifNode.setCondNode(condNode);
        // Add a conditional jump before the body
        Instruction instr = new BreakInstr(instrId++, Opcode.BREAK_IF_FALSE);
        updateAndAddInstr(instr);
        // Visit the body node
        ScopeASTNode bodyNode = (ScopeASTNode) ifNode.getBodyNode().accept(this);
        ifNode.setBodyNode(bodyNode);
        // Insert an instruction to jump out of the if-else sequence
        instr = new BreakInstr(instrId++, Opcode.BREAK_IF_ELSE);
        updateAndAddInstr(instr);

        // Create a new basic block
        createBlock();
        // Exit if-block
        structStack.pop();
        return ifNode;
    }

    @Override
    public ASTNode visitWhile(ASTNode node) {
        // Enter while-block
        IRStruct parent = structStack.peek();
        IRStruct loopStmt = new IRStruct(IRStructType.LOOP, parent);
        structStack.push(loopStmt);
        // Create a new basic block
        createBlock();

        // Visit the condition node
        WhileASTNode whileNode = (WhileASTNode) node;
        ASTNode condNode = whileNode.getCondNode().accept(this);
        whileNode.setCondNode(condNode);
        // Conditional jump if the condition isn't met
        Instruction instr = new BreakInstr(instrId++, Opcode.BREAK_LOOP_FALSE);
        updateAndAddInstr(instr);
        // Visit the body node
        ScopeASTNode bodyNode = (ScopeASTNode) whileNode.getBodyNode().accept(this);
        whileNode.setBodyNode(bodyNode);
        // Unconditional jump to the beginning of the while statement
        instr = new ContInstr(instrId++);
        updateAndAddInstr(instr);

        // Create a new basic block
        createBlock();
        // Exit while-block
        structStack.pop();
        return whileNode;
    }
}
