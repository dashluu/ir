package cfg;

public class CFGEdge {
    private final BasicBlock src;
    private final BasicBlock dest;

    public CFGEdge(BasicBlock src, BasicBlock dest) {
        this.src = src;
        this.dest = dest;
    }

    public BasicBlock getSrc() {
        return src;
    }

    public BasicBlock getDest() {
        return dest;
    }

    @Override
    public int hashCode() {
        String hashStr = String.valueOf(src.getId()) + dest.getId();
        return hashStr.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CFGEdge edge)) {
            return false;
        }
        return src.equals(edge.src) && dest.equals(edge.dest);
    }
}
