package refs;

import java.util.HashMap;

public class LiteralRefTable {
    private final HashMap<String, LiteralRef> litRefMap = new HashMap<>();

    /**
     * Gets a literal reference based on its value.
     *
     * @param litVal the literal value.
     * @return a literal reference if found and null if not.
     */
    public LiteralRef getLiteralRef(String litVal) {
        return litRefMap.get(litVal);
    }

    /**
     * Adds a literal reference to the table.
     *
     * @param litRef the literal reference to be added.
     */
    public void registerLiteralRef(LiteralRef litRef) {
        litRefMap.put(litRef.getId(), litRef);
    }
}
