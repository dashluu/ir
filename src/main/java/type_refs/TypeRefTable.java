package type_refs;

import types.BoolType;
import types.FloatType;
import types.IntType;

import java.util.HashMap;

public class TypeRefTable {
    private final HashMap<String, Byte> typeRefMap = new HashMap<>();
    private byte nextDtypeRef = 1;

    /**
     * Creates an instance of TypeRefTable and initializes it.
     *
     * @return an TypeRefTable object.
     */
    public static TypeRefTable createTable() {
        TypeRefTable table = new TypeRefTable();
        table.typeRefMap.put(IntType.ID, table.nextDtypeRef++);
        table.typeRefMap.put(FloatType.ID, table.nextDtypeRef++);
        table.typeRefMap.put(BoolType.ID, table.nextDtypeRef++);
        return table;
    }

    /**
     * Gets a data type reference based on the data type identifier.
     *
     * @param id the data type identifier.
     * @return an integer as the associated data type reference.
     */
    public byte getTypeRef(String id) {
        return typeRefMap.get(id);
    }
}
