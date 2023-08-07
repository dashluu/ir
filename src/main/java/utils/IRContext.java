package utils;

import cfg.CFG;
import instructions.Instruction;
import literal_refs.LiteralRefTable;
import type_refs.TypeRefTable;

import java.util.ArrayList;
import java.util.List;

public class IRContext {
    private LiteralRefTable litRefTable;
    private TypeRefTable typeRefTable;
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

    public CFG getCfg() {
        return cfg;
    }
}
