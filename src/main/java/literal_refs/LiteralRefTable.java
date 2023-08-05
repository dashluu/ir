package literal_refs;

import java.util.HashMap;

public class LiteralRefTable {
    private final HashMap<String, Long> litRefMap = new HashMap<>();
    public static final long END = -1;

    /**
     * Gets a literal reference based on its value.
     *
     * @param val the literal value.
     * @return an integer as the literal reference.
     */
    public long getLiteralRef(String val) {
        Long ref = litRefMap.get(val);
        return ref == null ? END : ref;
    }

    /**
     * Maps a literal value to a literal reference.
     *
     * @param val the literal value.
     * @param ref the literal reference.
     */
    public void registerLiteralRef(String val, long ref) {
        litRefMap.put(val, ref);
    }
}
