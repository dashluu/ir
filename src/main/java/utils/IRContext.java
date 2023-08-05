package utils;

import literal_refs.LiteralRefTable;
import type_refs.TypeRefTable;

public class IRContext {
    private LiteralRefTable litRefTable;
    private TypeRefTable typeRefTable;

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
}
