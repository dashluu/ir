package passes;

import ast.*;
import utils.IRContext;

public abstract class ASTPass implements IASTVisitor {
    protected IRContext context;

    protected ASTNode visitList(ListASTNode listNode) {
        IASTNodeIterator nodeIterator = listNode.nodeIterator();
        ASTNode childNode;

        while (nodeIterator.hasNext()) {
            childNode = nodeIterator.next().accept(this);
            nodeIterator.set(childNode);
        }

        return listNode;
    }

    @Override
    public ASTNode visitId(ASTNode node) {
        return node;
    }

    @Override
    public ASTNode visitSimpleDtype(ASTNode node) {
        return node;
    }

    @Override
    public ASTNode visitLiteral(ASTNode node) {
        return node;
    }

    @Override
    public ASTNode visitVarDecl(ASTNode node) {
        VarDeclASTNode varDeclNode = (VarDeclASTNode) node;
        IdASTNode idNode = (IdASTNode) varDeclNode.getIdNode().accept(this);
        varDeclNode.setIdNode(idNode);
        DtypeASTNode dtypeNode = varDeclNode.getDtypeNode();
        if (dtypeNode != null) {
            dtypeNode = (DtypeASTNode) dtypeNode.accept(this);
            varDeclNode.setDtypeNode(dtypeNode);
        }
        return node;
    }

    @Override
    public ASTNode visitVarDef(ASTNode node) {
        VarDefASTNode varDefNode = (VarDefASTNode) node;
        ASTNode exprNode = varDefNode.getExprNode().accept(this);
        VarDeclASTNode varDeclNode = (VarDeclASTNode) varDefNode.getVarDeclNode().accept(this);
        varDefNode.setVarDeclNode(varDeclNode);
        varDefNode.setExprNode(exprNode);
        return node;
    }

    @Override
    public ASTNode visitParamDecl(ASTNode node) {
        ParamDeclASTNode paramDeclNode = (ParamDeclASTNode) node;
        IdASTNode idNode = (IdASTNode) paramDeclNode.getIdNode().accept(this);
        DtypeASTNode dtypeNode = (DtypeASTNode) paramDeclNode.getDtypeNode().accept(this);
        paramDeclNode.setIdNode(idNode);
        paramDeclNode.setDtypeNode(dtypeNode);
        return node;
    }

    @Override
    public ASTNode visitParamList(ASTNode node) {
        ParamListASTNode paramListNode = (ParamListASTNode) node;
        return visitList(paramListNode);
    }

    @Override
    public ASTNode visitUnOp(ASTNode node) {
        UnOpASTNode unOpNode = (UnOpASTNode) node;
        ASTNode child = unOpNode.getExprNode().accept(this);
        unOpNode.setExprNode(child);
        return node;
    }

    @Override
    public ASTNode visitBinOp(ASTNode node) {
        BinOpASTNode binOpNode = (BinOpASTNode) node;
        ASTNode left = binOpNode.getLeft().accept(this);
        ASTNode right = binOpNode.getRight().accept(this);
        binOpNode.setLeft(left);
        binOpNode.setRight(right);
        return node;
    }

    @Override
    public ASTNode visitFunCall(ASTNode node) {
        FunCallASTNode funCallNode = (FunCallASTNode) node;
        IdASTNode idNode = (IdASTNode) funCallNode.getIdNode().accept(this);
        ExprListASTNode argListNode = (ExprListASTNode) funCallNode.getArgListNode().accept(this);
        funCallNode.setIdNode(idNode);
        funCallNode.setArgListNode(argListNode);
        return node;
    }

    @Override
    public ASTNode visitFunDef(ASTNode node) {
        FunDefASTNode funDefNode = (FunDefASTNode) node;
        IdASTNode idNode = (IdASTNode) funDefNode.getIdNode().accept(this);
        FunSignASTNode funSignNode = (FunSignASTNode) funDefNode.getSignNode().accept(this);
        ScopeASTNode bodyNode = (ScopeASTNode) funDefNode.getBodyNode().accept(this);
        funDefNode.setIdNode(idNode);
        funDefNode.setSignNode(funSignNode);
        funDefNode.setBodyNode(bodyNode);
        return node;
    }

    @Override
    public ASTNode visitFunSign(ASTNode node) {
        FunSignASTNode funSignASTNode = (FunSignASTNode) node;
        ParamListASTNode paramListNode = (ParamListASTNode) funSignASTNode.getParamListNode().accept(this);
        funSignASTNode.setParamListNode(paramListNode);
        DtypeASTNode retDtypeNode = funSignASTNode.getRetDtypeNode();
        if (retDtypeNode != null) {
            retDtypeNode = (DtypeASTNode) retDtypeNode.accept(this);
            funSignASTNode.setRetDtypeNode(retDtypeNode);
        }
        return node;
    }

    @Override
    public ASTNode visitRet(ASTNode node) {
        RetASTNode retNode = (RetASTNode) node;
        ASTNode child = retNode.getExprNode();
        if (child != null) {
            child = child.accept(this);
            retNode.setExprNode(child);
        }
        return node;
    }

    @Override
    public ASTNode visitBreak(ASTNode node) {
        return node;
    }

    @Override
    public ASTNode visitCont(ASTNode node) {
        return node;
    }

    @Override
    public ASTNode visitScope(ASTNode node) {
        ScopeASTNode scopeNode = (ScopeASTNode) node;
        return visitList(scopeNode);
    }

    @Override
    public ASTNode visitIfElse(ASTNode node) {
        IfElseASTNode ifElseNode = (IfElseASTNode) node;
        return visitList(ifElseNode);
    }

    @Override
    public ASTNode visitIf(ASTNode node) {
        IfASTNode ifNode = (IfASTNode) node;
        ASTNode condNode = ifNode.getCondNode().accept(this);
        ScopeASTNode bodyNode = (ScopeASTNode) ifNode.getBodyNode().accept(this);
        ifNode.setCondNode(condNode);
        ifNode.setBodyNode(bodyNode);
        return node;
    }

    @Override
    public ASTNode visitElse(ASTNode node) {
        ElseASTNode elseNode = (ElseASTNode) node;
        ScopeASTNode bodyNode = (ScopeASTNode) elseNode.getBodyNode().accept(this);
        elseNode.setBodyNode(bodyNode);
        return node;
    }

    @Override
    public ASTNode visitWhile(ASTNode node) {
        WhileASTNode whileNode = (WhileASTNode) node;
        ASTNode condNode = whileNode.getCondNode().accept(this);
        ScopeASTNode bodyNode = (ScopeASTNode) whileNode.getBodyNode().accept(this);
        whileNode.setCondNode(condNode);
        whileNode.setBodyNode(bodyNode);
        return node;
    }

    @Override
    public ASTNode visitArrAccess(ASTNode node) {
        return node;
    }

    @Override
    public ASTNode visitArrLiteral(ASTNode node) {
        return node;
    }

    @Override
    public ASTNode visitExprList(ASTNode node) {
        ExprListASTNode exprListNode = (ExprListASTNode) node;
        return visitList(exprListNode);
    }
}
