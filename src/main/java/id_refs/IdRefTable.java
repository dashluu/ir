package id_refs;

import java.util.HashMap;

public class IdRefTable {
    private final HashMap<String, Long> idRefMap = new HashMap<>();
    private final IdRefTable parent;
    public static final long END = -1;

    public IdRefTable(IdRefTable parent) {
        this.parent = parent;
    }

    public IdRefTable getParent() {
        return parent;
    }

    /**
     * Maps an identifier to its reference value.
     *
     * @param id  the identifier to be mapped from.
     * @param ref the reference value to be mapped to.
     */
    public void registerIdRef(String id, long ref) {
        idRefMap.put(id, ref);
    }

    /**
     * Gets the reference associated with an identifier in the current table only.
     *
     * @param id the input identifier.
     * @return an integer as the reference.
     */
    public long getLocalIdRef(String id) {
        Long ref = idRefMap.get(id);
        return ref == null ? END : ref;
    }

    /**
     * Gets the reference associated with an identifier by moving up the chain of tables.
     *
     * @param id the input identifier.
     * @return an integer as the reference.
     */
    public long getClosureIdRef(String id) {
        IdRefTable table = this;
        Long ref = null;
        while (table != null && ref == null) {
            ref = table.idRefMap.get(id);
            table = table.parent;
        }
        return ref == null ? END : ref;
    }
}
