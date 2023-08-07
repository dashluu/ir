package utils;

import cfg.CFG;
import instructions.BasicBlock;
import instructions.Instruction;
import literal_refs.LiteralRefTable;
import type_refs.TypeRefTable;

import java.util.ArrayList;
import java.util.List;

public class IRContext {
    private LiteralRefTable litRefTable;
    private TypeRefTable typeRefTable;
    private final List<Instruction> instrList = new ArrayList<>();
    private final List<BasicBlock> blockList = new ArrayList<>();
    private final CFG cfg = new CFG();

    private IRContext() {
    }

    /**
     * Creates an instance of IRContext and initializes it.
     *
     * @return an IRContext object.
     */
    public static IRContext createContext() {
        IRContext context = new IRContext();
        context.litRefTable = new LiteralRefTable();
        context.typeRefTable = TypeRefTable.createTable();
        return context;
    }

    public LiteralRefTable getLiteralRefTable() {
        return litRefTable;
    }

    public TypeRefTable getTypeRefTable() {
        return typeRefTable;
    }

    public List<Instruction> getInstrList() {
        return instrList;
    }

    public List<BasicBlock> getBasicBlockList() {
        return blockList;
    }

    public CFG getCfg() {
        return cfg;
    }
}
