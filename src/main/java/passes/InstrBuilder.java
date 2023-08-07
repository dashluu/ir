package passes;

import ast.*;
import id_refs.IdRefTable;
import instructions.*;
import literal_refs.LiteralRefTable;
import structs.IRFunction;
import structs.IRStruct;
import structs.IRStructType;
import toks.Tok;
import toks.TokType;
import types.TypeInfo;
import utils.IRContext;

import java.util.ArrayDeque;
import java.util.List;

public class InstrBuilder extends ASTPass {
    private final ArrayDeque<IdRefTable> idRefTableStack = new ArrayDeque<>();
    private List<Instruction> instrList;
    private long nextIdRef = 0, nextLitRef = 0;
    private final ArrayDeque<IRStruct> structStack = new ArrayDeque<>();

    /**
     * Traverses an AST and builds a list of instructions from an AST in the given context.
     *
     * @param root    the input AST root.
     * @param context the input IR context.
     */
    public void run(ASTNode root, IRContext context) {
        this.context = context;
        instrList = context.getInstrList();
        // Set up the first identifier reference table
        IdRefTable idRefTable = new IdRefTable(null);
        idRefTableStack.push(idRefTable);
        // Enter module
        IRStruct module = new IRStruct(currInstr(), -1, IRStructType.MODULE, null);
        structStack.push(module);
        // Traverse and process the AST
        root.accept(this);
        // Insert an exit instruction at the end
        Instruction instr = new ExitInstr();
        updateContainerAndAddInstr(instr);
        // Exit module
        structStack.pop();
        module.setEnd(currInstr());
        // Pop everything off the stack
        idRefTableStack.pop();
    }

    private long currInstr() {
        return instrList.size();
    }

    private void updateContainerAndAddInstr(Instruction instr) {
        IRStruct struct = structStack.peek();
        instr.setContainer(struct);
        instrList.add(instr);
    }

    private void storeVar(ASTNode idNode) {
        String destName = idNode.getTok().getVal();
        IdRefTable idRefTable = idRefTableStack.peek();
        assert idRefTable != null;
        long destRef = idRefTable.getClosureIdRef(destName);
        Instruction instr = new StoreInstr(Opcode.STORE_VAR, destName, destRef);
        updateContainerAndAddInstr(instr);
    }

    @Override
    public ASTNode visitId(ASTNode node) {
        String src = node.getTok().getVal();
        IdRefTable idRefTable = idRefTableStack.peek();
        assert idRefTable != null;
        long srcRef = idRefTable.getClosureIdRef(src);
        Instruction instr = new LoadInstr(Opcode.LOAD_VAR, src, srcRef);
        updateContainerAndAddInstr(instr);
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
        IdRefTable idRefTable = idRefTableStack.peek();
        assert idRefTable != null;
        idRefTable.registerIdRef(id, nextIdRef++);
        return paramDeclNode;
    }

    @Override
    public ASTNode visitVarDecl(ASTNode node) {
        VarDeclASTNode varDeclNode = (VarDeclASTNode) node;
        IdASTNode idNode = varDeclNode.getIdNode();
        String id = idNode.getTok().getVal();
        IdRefTable idRefTable = idRefTableStack.peek();
        assert idRefTable != null;
        idRefTable.registerIdRef(id, nextIdRef++);
        return varDeclNode;
    }

    @Override
    public ASTNode visitLiteral(ASTNode node) {
        String litVal = node.getTok().getVal();
        LiteralRefTable litRefTable = context.getLiteralRefTable();
        long litRef = litRefTable.getLiteralRef(litVal);
        if (litRef == LiteralRefTable.END) {
            // Create a new literal reference
            litRef = nextLitRef++;
            litRefTable.registerLiteralRef(litVal, litRef);
        }

        Instruction instr = new LoadInstr(Opcode.LOAD_LITERAL, litVal, litRef);
        updateContainerAndAddInstr(instr);
        return node;
    }

    @Override
    public ASTNode visitSimpleDtype(ASTNode node) {
        String src = node.getTok().getVal();
        long srcRef = context.getTypeRefTable().getTypeRef(src);
        Instruction instr = new LoadInstr(Opcode.LOAD_DTYPE, src, srcRef);
        updateContainerAndAddInstr(instr);
        return node;
    }

    @Override
    public ASTNode visitUnOp(ASTNode node) {
        ASTNode unOpNode = super.visitUnOp(node);
        Tok opTok = unOpNode.getTok();
        String opName = opTok.getVal();
        long opRef = opTok.getTokType().ordinal();
        Instruction instr = new UnOpInstr(opName, opRef);
        updateContainerAndAddInstr(instr);
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
            Instruction instr = new BinOpInstr(opName, opRef);
            updateContainerAndAddInstr(instr);
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
        IdRefTable idRefTable = idRefTableStack.peek();
        assert idRefTable != null;
        long calleeId = idRefTable.getClosureIdRef(calleeName);
        Instruction instr = new CallInstr(calleeName, calleeId);
        updateContainerAndAddInstr(instr);
        return funCallNode;
    }

    @Override
    public ASTNode visitFunDef(ASTNode node) {
        // Enter function
        IRStruct parent = structStack.peek();
        TypeInfo retDtype = node.getDtype();
        IRStruct function = new IRFunction(currInstr(), -1, parent, retDtype);
        structStack.push(function);

        // Add an instruction to prepare function
        FunDefASTNode funDefNode = (FunDefASTNode) node;
        IdASTNode idNode = funDefNode.getIdNode();
        String funId = idNode.getTok().getVal();
        long funRef = currInstr();
        Instruction instr = new PushFunInstr(funId, funRef);
        updateContainerAndAddInstr(instr);

        // Register function in the identifier reference table
        IdRefTable topIdRefTable = idRefTableStack.peek();
        assert topIdRefTable != null;
        topIdRefTable.registerIdRef(funId, funRef);
        IdRefTable newIdRefTable = new IdRefTable(topIdRefTable);
        // Visit the function signature and body
        idRefTableStack.push(newIdRefTable);
        FunSignASTNode funSignNode = (FunSignASTNode) funDefNode.getSignNode().accept(this);
        ScopeASTNode bodyNode = (ScopeASTNode) funDefNode.getBodyNode().accept(this);
        funDefNode.setSignNode(funSignNode);
        funDefNode.setBodyNode(bodyNode);
        idRefTableStack.pop();

        // Exit function
        structStack.pop();
        function.setEnd(currInstr());
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
        Instruction instr = new RetInstr();
        updateContainerAndAddInstr(instr);
        return retNode;
    }

    @Override
    public ASTNode visitBreak(ASTNode node) {
        Instruction instr = new BreakInstr(Opcode.BREAK_LOOP);
        updateContainerAndAddInstr(instr);
        return node;
    }

    @Override
    public ASTNode visitCont(ASTNode node) {
        Instruction instr = new ContInstr();
        updateContainerAndAddInstr(instr);
        return node;
    }

    @Override
    public ASTNode visitScope(ASTNode node) {
        IdRefTable topIdRefTable = idRefTableStack.peek();
        IdRefTable newIdRefTable = new IdRefTable(topIdRefTable);
        idRefTableStack.push(newIdRefTable);
        ASTNode scopeNode = super.visitScope(node);
        idRefTableStack.pop();
        return scopeNode;
    }

    @Override
    public ASTNode visitIfElse(ASTNode node) {
        // Enter if-else
        IRStruct parent = structStack.peek();
        IRStruct ifElseStmt = new IRStruct(currInstr(), -1, IRStructType.IF_ELSE, parent);
        structStack.push(ifElseStmt);

        node = super.visitIfElse(node);

        // Exit if-else
        structStack.pop();
        ifElseStmt.setEnd(currInstr());
        return node;
    }

    @Override
    public ASTNode visitIf(ASTNode node) {
        // Enter if-block
        IRStruct parent = structStack.peek();
        IRStruct ifStmt = new IRStruct(currInstr(), -1, IRStructType.IF, parent);
        structStack.push(ifStmt);

        // Visit the condition node
        IfASTNode ifNode = (IfASTNode) node;
        ASTNode condNode = ifNode.getCondNode().accept(this);
        ifNode.setCondNode(condNode);
        // Add a conditional jump before the body
        Instruction instr = new BreakInstr(Opcode.BREAK_IF_FALSE);
        updateContainerAndAddInstr(instr);
        // Visit the body node
        ScopeASTNode bodyNode = (ScopeASTNode) ifNode.getBodyNode().accept(this);
        ifNode.setBodyNode(bodyNode);
        // Insert an instruction to jump out of the if-else sequence
        instr = new BreakInstr(Opcode.BREAK_IF_ELSE);
        updateContainerAndAddInstr(instr);

        // Exit if-block
        structStack.pop();
        ifStmt.setEnd(currInstr());
        return ifNode;
    }

    @Override
    public ASTNode visitWhile(ASTNode node) {
        // Enter while-block
        IRStruct parent = structStack.peek();
        IRStruct loopStmt = new IRStruct(currInstr(), -1, IRStructType.LOOP, parent);
        structStack.push(loopStmt);

        // Visit the condition node
        WhileASTNode whileNode = (WhileASTNode) node;
        ASTNode condNode = whileNode.getCondNode().accept(this);
        whileNode.setCondNode(condNode);
        // Conditional jump if the condition isn't met
        Instruction instr = new BreakInstr(Opcode.BREAK_LOOP_FALSE);
        updateContainerAndAddInstr(instr);
        // Visit the body node
        ScopeASTNode bodyNode = (ScopeASTNode) whileNode.getBodyNode().accept(this);
        whileNode.setBodyNode(bodyNode);
        // Unconditional jump to the beginning of the while statement
        instr = new ContInstr();
        updateContainerAndAddInstr(instr);

        // Exit while-block
        structStack.pop();
        loopStmt.setEnd(currInstr());
        return whileNode;
    }
}
