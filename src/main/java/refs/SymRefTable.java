package refs;

import java.util.HashMap;

public class SymRefTable {
    private final HashMap<String, SymRef> symRefMap = new HashMap<>();
    private final SymRefTable parent;

    public SymRefTable(SymRefTable parent) {
        this.parent = parent;
    }

    public SymRefTable getParent() {
        return parent;
    }

    /**
     * Adds a symbol reference to the table.
     *
     * @param symRef the symbol reference to be added.
     */
    public void registerSymRef(SymRef symRef) {
        symRefMap.put(symRef.getId(), symRef);
    }

    /**
     * Gets the symbol reference associated with an identifier in the current table only.
     *
     * @param id the input identifier.
     * @return a symbol reference if found and null if not.
     */
    public SymRef getLocalSymRef(String id) {
        return symRefMap.get(id);
    }

    /**
     * Gets the symbol reference associated with an identifier by moving up the chain of tables.
     *
     * @param id the input identifier.
     * @return a symbol reference if found and null if not.
     */
    public SymRef getClosureSymRef(String id) {
        SymRefTable table = this;
        SymRef symRef = null;
        while (table != null && symRef == null) {
            symRef = table.symRefMap.get(id);
            table = table.parent;
        }
        return symRef;
    }
}
