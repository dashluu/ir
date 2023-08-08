package refs;

import types.BoolType;
import types.FloatType;
import types.IntType;

import java.util.HashMap;

public class TypeRefTable {
    private final HashMap<String, TypeRef> typeRefMap = new HashMap<>();
    private byte nextRefVal = 0;

    /**
     * Creates an instance of TypeRefTable and initializes it.
     *
     * @return a TypeRefTable object.
     */
    public static TypeRefTable createTable() {
        TypeRefTable table = new TypeRefTable();
        table.registerTypeRef(new TypeRef(IntType.getInst(), table.nextRefVal++));
        table.registerTypeRef(new TypeRef(FloatType.getInst(), table.nextRefVal++));
        table.registerTypeRef(new TypeRef(BoolType.getInst(), table.nextRefVal++));
        return table;
    }

    /**
     * Adds a data type reference to the table.
     *
     * @param typeRef the data type reference to be added.
     */
    private void registerTypeRef(TypeRef typeRef) {
        typeRefMap.put(typeRef.getId(), typeRef);
    }

    /**
     * Gets a data type reference based on the data type identifier.
     *
     * @param id the data type identifier.
     * @return a data type reference if found and null if not.
     */
    public TypeRef getTypeRef(String id) {
        return typeRefMap.get(id);
    }
}
